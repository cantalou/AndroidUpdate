//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.update;

import android.content.Context;

import com.cantalou.android.update.AppUpdateConfig;
import com.cantalou.android.update.AppUpdateManager;

import u.upd.g;

public class b extends g {

    Context a;
    private String[] b;
    private static final String c = b.class.getName();

    public static String url;

    public b(Context var1) {
        this.a = var1;
    }

    public boolean shouldCompressData() {
        return false;
    }

    public UpdateResponse a() {
        AppUpdateConfig config = AppUpdateManager.getInstance().getConfig();
        if (config == null) {
            return null;
        }
        d var1 = new d(this.a);
        var1.setBaseUrl(config.getUrl());

        UpdateResponse var2;
        AppUpdateConfig.UpdateDataGetter getter = config.getUpdateDataGetter();
        if (getter != null) {
            var2 = new UpdateResponse(getter.get());
        } else {
            var2 = (UpdateResponse) this.execute(var1, UpdateResponse.class);
        }

        return var2;
    }
}
