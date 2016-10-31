package com.cantalou.android.update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.cantalou.android.util.Log;
import com.cantalou.android.util.NetworkUtils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;


public class AppUpdateManager implements UmengUpdateListener {

    private static class InstanceHolder {
        static final AppUpdateManager INSTANCE = new AppUpdateManager();
    }

    private Activity mActivity;

    /**
     * 加载提示框
     */
    private ProgressDialog mDialog;

    /**
     * 是否已取消
     */
    private volatile boolean isCancel = false;

    private AppUpdateConfig mConfig;

    private UpdateResponse mUpdateResponse;

    private AppUpdateManager() {
    }

    public static AppUpdateManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void update(Activity activity, AppUpdateConfig config) {

        if (activity == null) {
            return;
        }

        if (mUpdateResponse != null) {
            File f = UmengUpdateAgent.downloadedFile(activity, mUpdateResponse);
            if (f != null) {
                f.delete();
            }
        }


        this.mConfig = config;
        this.mActivity = activity;

        if (!NetworkUtils.isNetworkAvailable(activity)) {
            if (config.isShowLoadingDialog()) {
                Toast.makeText(activity, R.string.UMBreak_Network, Toast.LENGTH_SHORT).show();
            } else {
                Log.v("后台检测更新,无网络直接返回");
            }
            return;
        }

        isCancel = false;

        try {
            // 如果已有任务在下载,则直接按上次获取的参数弹出提示框
            if (isUmengDownloading()) {
                Log.v("已有任务在下载, 直接按上次获取的参数弹出提示框");
                onUpdateReturned(mUpdateResponse.status, mUpdateResponse);
                return;
            }

            if (config.isShowLoadingDialog()) {
                mDialog = new ProgressDialog(activity);
                mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        AppUpdateManager.this.mDialog = null;
                    }
                });
                mDialog.show();
                mDialog.setContentView(R.layout.umeng_update_loading_dialog);
            }

            // 获取版本信息
            UmengUpdateAgent.setAppkey("");
            UmengUpdateAgent.setUpdateOnlyWifi(false);
            UmengUpdateAgent.setUpdateAutoPopup(false);
            UmengUpdateAgent.setUpdateListener(this);
            UmengUpdateAgent.setRichNotification(true);
            UmengUpdateAgent.setDeltaUpdate(false);
            UmengUpdateAgent.setChannel("");
            UpdateConfig.setSilentDownload(true);

            if (config.isForceCheck()) {
                UmengUpdateAgent.forceUpdate(activity);
            } else {
                UmengUpdateAgent.update(activity);
            }
        } catch (Exception e) {
            Log.w("error:{}", e);
        }
    }

    @Override
    public void onUpdateReturned(final int updateStatus, UpdateResponse updateResponse) {

        if (updateResponse == null) {
            updateResponse = new UpdateResponse(new JSONObject());
            updateResponse.hasUpdate = false;
        }
        updateResponse.status = updateStatus;
        mUpdateResponse = updateResponse;

        if (isCancel) {
            Log.v("更新已取消,直接返回");
            return;
        }

        if (mConfig.isShowLoadingDialog() && !isForceUpdate(updateResponse) && (mDialog == null || !mDialog.isShowing())) {
            Log.v("更新数据返回,但用户取消数据加载等待,不显示更新提示");
            return;
        }

        if (mConfig.isShowLoadingDialog()) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        }

        switch (updateStatus) {
            case UpdateStatus.Yes: {
                UmengUpdateAgent.showUpdateDialog(mActivity, updateResponse);
                break;
            }
            case UpdateStatus.No: {
                if (mConfig.isShowLoadingDialog()) {
                    Toast.makeText(mActivity, R.string.UMDialogVersion, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case UpdateStatus.Timeout: {
                if (mConfig.isShowLoadingDialog()) {
                    if (NetworkUtils.isNetworkAvailable(mActivity)) {
                        Toast.makeText(mActivity, R.string.umeng_common_network_timeout_alert, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mActivity, R.string.umeng_common_network_break_alert, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }

    public boolean isForceUpdate(UpdateResponse updateResponse) {
        try {
            PackageInfo pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
            return pi.versionCode < updateResponse.forceVersion;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(e);
            return false;
        }
    }

    public boolean isUmengDownloading() {
        try {
            for (Field f : UmengUpdateAgent.class.getDeclaredFields()) {
                if ("e".equals(f.getName())) {
                    f.setAccessible(true);
                    com.umeng.update.c e = (com.umeng.update.c) f.get(null);
                    return e.a();
                }
            }

        } catch (Exception e) {
            Log.e(e);
        }
        return false;
    }


    public void cancelUpdate() {
        isCancel = true;
    }

    public AppUpdateConfig getConfig() {
        return mConfig;
    }
}
