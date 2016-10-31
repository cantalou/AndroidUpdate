package com.cantalou.android.update;

import org.json.JSONObject;

public class AppUpdateConfig {

    /**
     * 执行检测的时候是否显示相应的加载提示框
     */
    private boolean isShowLoadingDialog = true;

    /**
     * 是否强制检测,忽略本地设置或者缓存
     */
    private boolean isForceCheck;

    /**
     * 更新包渠道
     */
    private String channel;

    /**
     * 更新接口url地址
     */
    private String url;

    /**
     * 自定义访问接口
     */
    private UpdateDataGetter updateDataGetter;

    /**
     * @param url                 更新接口地址
     * @param isShowLoadingDialog 是否显示加载框
     * @param isForceCheck        是否强制检测, 忽略"不再提示更新"选项
     */
    public AppUpdateConfig(String url, boolean isShowLoadingDialog, boolean isForceCheck) {
        this(url, isShowLoadingDialog, isForceCheck, "", null);
    }


    /**
     * @param url                 服务器接口地址
     * @param isShowLoadingDialog 是否显示加载框
     * @param isForceCheck        是否强制检测, 忽略"不再提示更新"选项
     * @param channel             渠道名称
     * @param updateDataGetter    接口访问器 {@link com.cantalou.android.update.AppUpdateConfig.UpdateDataGetter}
     */
    public AppUpdateConfig(String url, boolean isShowLoadingDialog, boolean isForceCheck, String channel, UpdateDataGetter updateDataGetter) {
        this.url = url;
        this.isShowLoadingDialog = isShowLoadingDialog;
        this.isForceCheck = isForceCheck;
        this.channel = channel;
        this.updateDataGetter = updateDataGetter;
    }

    public boolean isShowLoadingDialog() {
        return isShowLoadingDialog;
    }

    public boolean isForceCheck() {
        return isForceCheck;
    }

    public String getChannel() {
        return channel;
    }

    public String getUrl() {
        return url;
    }

    public UpdateDataGetter getUpdateDataGetter() {
        return updateDataGetter;
    }

    /**
     * 自定义的接口访问器以实现自定义的 接口鉴权, HTTPS等访问要求
     */
    public static interface UpdateDataGetter {
        public JSONObject get();
    }

}
