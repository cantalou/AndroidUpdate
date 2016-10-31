package com.cantalou.android.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.cantalou.android.update.AppUpdateConfig;
import com.cantalou.android.update.AppUpdateManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void update(View v) {
        AppUpdateManager manager = AppUpdateManager.getInstance();
        AppUpdateConfig conf = new AppUpdateConfig("http://192.168.57.252:8080/updateJson.txt", true, true);
        manager.update(this, conf);
    }
}
