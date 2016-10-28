package com.umeng.update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cantalou.android.util.Log;

import java.io.File;

import static u.upd.c.a;

public class UpdateDialogActivity extends Activity implements UmengDownloadListener {

    UpdateResponse updateResponse;

    File file = null;

    int clickValue = 6;

    ProgressDialog dialog;

    ProgressBar progressBar;

    private long mLastBackTime;

    private TextView progressTv;

    protected void onCreate(Bundle var1) {
        super.onCreate(var1);
        requestWindowFeature(1);
        setContentView(a(this).d("umeng_update_dialog"));

        UmengUpdateAgent.setDownloadListener(this);
        Bundle bundle = getIntent().getExtras();
        updateResponse = (UpdateResponse) bundle.getSerializable("response");

        findViewById(getId("umeng_update_wifi_indicator")).setVisibility(u.upd.a.k(this) ? View.GONE : View.VISIBLE);

        boolean forceCheck = getIntent().getExtras().getBoolean("force");
        if (forceCheck) {
            findViewById(getId("umeng_update_id_check")).setVisibility(View.GONE);
        }

        final boolean forceUpdate = isForceUpdate(updateResponse);
        String filePath = bundle.getString("file");
        if (filePath != null) {
            file = new File(filePath);
        }

        View v = findViewById(getId("umeng_update_id_ok"));
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickValue = 5;
                if (forceUpdate) {
                    if (file != null) {
                        UmengUpdateAgent.a(5, UpdateDialogActivity.this, updateResponse, file);
                    } else {
                        UmengUpdateAgent.startDownload(UpdateDialogActivity.this, updateResponse);
                        dialog = new ProgressDialog(UpdateDialogActivity.this);
                        dialog.setMax(100);
                        dialog.show();
                        dialog.setContentView(u.upd.c.a(UpdateDialogActivity.this).d("umeng_update_downloading_dialog"));
                        progressBar = (ProgressBar) dialog.findViewById(getId("umeng_update_download_progress_bar"));
                        progressTv = (TextView) dialog.findViewById(getId("umeng_update_download_progress_value"));
                    }
                } else {
                    finish();
                }
            }
        });


        v = findViewById(getId("umeng_update_id_cancel"));
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickValue = 6;
                finish();
            }
        });
        if (forceUpdate) {
            v.setVisibility(View.GONE);
        }

        v = findViewById(getId("umeng_update_id_close"));
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickValue = 6;
                finish();
            }
        });
        if (forceUpdate) {
            v.setVisibility(View.GONE);
        }

        ((CheckBox) findViewById(getId("umeng_update_id_check"))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton var1, boolean var2) {
                clickValue = var2 ? 7 : 6;
            }
        });


        String var18 = this.updateResponse.a(this, filePath != null);
        TextView var15 = (TextView) findViewById(getId("umeng_update_content"));
        var15.requestFocus();
        var15.setText(var18);
    }

    private int getId(String name) {
        return u.upd.c.a(this).b(name);
    }

    protected void onDestroy() {
        super.onDestroy();
        UmengUpdateAgent.a(clickValue, this, this.updateResponse, file);
    }

    public boolean isForceUpdate(UpdateResponse updateResponse) {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.versionCode < updateResponse.forceVersion;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(e);
            return false;
        }
    }

    private void showBackToast() {
        long currentTime = System.currentTimeMillis();
        if (mLastBackTime != 0 && (currentTime - mLastBackTime) < 3000) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, u.upd.c.a(this).f("UMExit"), Toast.LENGTH_SHORT).show();
            mLastBackTime = currentTime;
        }
    }

    @Override
    public void onBackPressed() {
        if (isForceUpdate(updateResponse)) {
            showBackToast();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void OnDownloadStart() {
    }

    @Override
    public void OnDownloadUpdate(int i) {
        if (progressBar != null) {
            progressBar.setProgress(i);
        }
        if (progressTv != null) {
            progressTv.setText(i + "%");
        }
    }

    @Override
    public void OnDownloadEnd(int i, String s) {
        dialog.dismiss();
        UmengUpdateAgent.a(5, this, this.updateResponse, file);
    }
}

