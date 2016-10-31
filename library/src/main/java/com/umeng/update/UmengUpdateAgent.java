//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.update;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.umeng.update.net.j;

import java.io.File;
import java.io.IOException;

import u.upd.l;
import u.upd.n;

public class UmengUpdateAgent {
    private static UmengUpdateListener a = null;
    private static UmengDialogButtonListener b = null;
    private static UmengDownloadListener c = null;
    private static Context d;
    private static c e = new c();
    private static Handler f;
    private static boolean g = false;
    static{
        setChannel("");
    }

    public UmengUpdateAgent() {
    }

    public static void setDefault() {
        setDeltaUpdate(true);
        setUpdateAutoPopup(true);
        setUpdateOnlyWifi(true);
        setRichNotification(true);
        setDialogListener((UmengDialogButtonListener) null);
        setDownloadListener((UmengDownloadListener) null);
        setUpdateListener((UmengUpdateListener) null);
        setAppkey((String) null);
        setChannel((String) null);
        setUpdateUIStyle(0);
    }

    public static void setUpdateFromPushMessage(boolean var0) {
        g = var0;
    }

    public static boolean getUpdateFromPushMessage() {
        return g;
    }

    public static void setUpdateCheckConfig(boolean var0) {
        UpdateConfig.setUpdateCheck(var0);
    }

    public static void setUpdateOnlyWifi(boolean var0) {
        UpdateConfig.setUpdateOnlyWifi(var0);
    }

    public static void setUpdateAutoPopup(boolean var0) {
        UpdateConfig.setUpdateAutoPopup(var0);
    }

    public static void setUpdateUIStyle(int var0) {
        UpdateConfig.setStyle(var0);
    }

    public static void setDeltaUpdate(boolean var0) {
        UpdateConfig.setDeltaUpdate(var0);
    }

    public static void setAppkey(String var0) {
        UpdateConfig.setAppkey(var0);
    }

    public static void setSlotId(String var0) {
        UpdateConfig.setSlotId(var0);
    }

    public static void setChannel(String var0) {
        UpdateConfig.setChannel(var0);
    }

    public static void setRichNotification(boolean var0) {
        UpdateConfig.setRichNotification(var0);
    }

    public static void setUpdateListener(UmengUpdateListener var0) {
        a = var0;
    }

    public static void setDialogListener(UmengDialogButtonListener var0) {
        b = var0;
    }

    public static void setDownloadListener(UmengDownloadListener var0) {
        c = var0;
    }

    private static void b(int var0, UpdateResponse var1) {
        Message var2 = new Message();
        var2.what = var0;
        var2.obj = var1;
        f.sendMessage(var2);
    }

    public static void silentUpdate(Context var0) {
        UpdateConfig.setUpdateForce(false);
        UpdateConfig.setSilentDownload(true);
        b(var0.getApplicationContext());
    }

    public static void forceUpdate(Context var0) {
        UpdateConfig.setUpdateForce(true);
        UpdateConfig.setSilentDownload(false);
        b(var0.getApplicationContext());
    }

    public static void update(Context var0) {
        UpdateConfig.setUpdateForce(false);
        UpdateConfig.setSilentDownload(false);
        b(var0.getApplicationContext());
    }

    /**
     * @deprecated
     */
    public static void update(Context var0, String var1, String var2) {
        UpdateConfig.setAppkey(var1);
        UpdateConfig.setChannel(var2);
        update(var0);
    }

    private static void b(final Context var0) {
        try {
            if (var0 == null) {
                u.upd.b.b("update", "unexpected null context in update");
                return;
            }

            f = new Handler(var0.getMainLooper()) {
                public void handleMessage(Message var1) {
                    super.handleMessage(var1);
                    if (var1.what == 0 && UpdateConfig.isUpdateAutoPopup()) {
                        UmengUpdateAgent.b(UmengUpdateAgent.d, (UpdateResponse) var1.obj, UpdateConfig.getStyle());
                    }

                    UmengUpdateAgent.b(var0, var1);
                    UmengUpdateAgent.d = null;
                    if (UmengUpdateAgent.a != null) {
                        UmengUpdateAgent.a.onUpdateReturned(var1.what, (UpdateResponse) var1.obj);
                    }

                }
            };
            c(var0);
            if (!u.upd.a.k(var0)) {
                if (UpdateConfig.isSilentDownload()) {
                    b(2, (UpdateResponse) null);
                    return;
                }

                if (UpdateConfig.isUpdateOnlyWifi() && !UpdateConfig.isUpdateForce()) {
                    b(2, (UpdateResponse) null);
                    return;
                }
            }

            if (e.a()) {
                b(4, (UpdateResponse) null);
                u.upd.b.a("update", "Is updating now.");
                f.post(new Runnable() {
                    public void run() {
                        Toast.makeText(var0, l.b(var0), Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }

            d = var0;
            (new Thread(new UmengUpdateAgent.a(var0.getApplicationContext()))).start();
        } catch (Exception var2) {
            u.upd.b.b("update", "Exception occurred in Mobclick.update(). ", var2);
        }

    }

    private static void b(Context var0, UpdateResponse var1, int var2) {
        try {
            if (isIgnore(var0, var1)) {
                return;
            }

            File var3 = downloadedFile(var0, var1);
            boolean var4 = var3 != null;
            if (!var4 && UpdateConfig.isSilentDownload()) {
                startDownload(var0, var1);
            } else {
                switch (var2) {
                    case 0:
                        e.a(var0, var1, var4, var3);
                        break;
                    case 1:
                        NotificationManager var5 = (NotificationManager) var0.getSystemService(Context.NOTIFICATION_SERVICE);
                        var5.notify(0, e.b(var0, var1, var4, var3).a());
                }
            }
        } catch (Exception var6) {
            u.upd.b.b("update", "Fail to create update dialog box.", var6);
        }

    }

    public static void showUpdateDialog(Context var0, UpdateResponse var1) {
        b(var0, var1, 0);
    }

    public static void showUpdateNotification(Context var0, UpdateResponse var1) {
        b(var0, var1, 1);
    }

    public static File downloadedFile(Context var0, UpdateResponse var1) {
        String var2 = var1.new_md5 + ".apk";

        try {
            File var3 = j.a("/apk", var0, new boolean[1]);
            var3 = new File(var3, var2);
            return var3.exists() && var1.new_md5.equalsIgnoreCase(n.a(var3)) ? var3 : null;
        } catch (IOException var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static boolean isIgnore(Context var0, UpdateResponse var1) {
        return var1.new_md5 != null && var1.new_md5.equalsIgnoreCase(UpdateConfig.getIgnoreMd5(var0)) && !UpdateConfig.isUpdateForce();
    }

    public static void ignoreUpdate(Context var0, UpdateResponse var1) {
        UpdateConfig.saveIgnoreMd5(var0, var1.new_md5);
    }

    static void a(int var0, Context var1, UpdateResponse var2, File var3) {
        switch (var0) {
            case 5:
                a(var1, var2, var3);
                break;
            case 7:
                ignoreUpdate(var1, var2);
        }

        if (b != null) {
            b.onClick(var0);
        }

    }

    private static void a(Context var0, UpdateResponse var1, File var2) {
        if (var2 == null) {
            startDownload(var0, var1);
        } else {
            startInstall(var0, var2);
        }

    }

    public static void startInstall(Context var0, File var1) {
        Intent var2 = new Intent("android.intent.action.VIEW");
        var2.addFlags(268435456);
        var2.setDataAndType(Uri.fromFile(var1), "application/vnd.android.package-archive");
        var0.startActivity(var2);
    }

    public static void startDownload(Context var0, UpdateResponse var1) {
        if (var1.delta && UpdateConfig.isDeltaUpdate()) {
            e.a(var0, var1.origin, var1.new_md5, var1.path, var1.patch_md5, c);
            e.b();
        } else {
            e.a(var0, var1.path, var1.new_md5, (String) null, (String) null, c);
            e.c();
        }

    }

    private static boolean c(final Context var0) {
        if (!UpdateConfig.isUpdateCheck()) {
            return true;
        } else {
            try {
                Class var1 = Class.forName(var0.getPackageName() + ".BuildConfig");
                boolean var2 = var1.getField("DEBUG").getBoolean((Object) null);
                if (!var2) {
                    return true;
                }
            } catch (Exception var10) {
                return true;
            }

            boolean var11 = false;

            try {
                if (UpdateConfig.getAppkey(var0) == null) {
                    f.post(new Runnable() {
                        public void run() {
                            Toast.makeText(var0, "Please set umeng appkey!", Toast.LENGTH_LONG).show();
                        }
                    });
                    return false;
                }

                PackageInfo var12 = var0.getPackageManager().getPackageInfo(var0.getPackageName(), 4101);
                int var3;
                if (var12.activities != null) {
                    for (var3 = 0; var3 < var12.activities.length; ++var3) {
                        if ("com.umeng.update.UpdateDialogActivity".equals(var12.activities[var3].name)) {
                            var11 = true;
                        }
                    }
                }

                if (!var11) {
                    f.post(new Runnable() {
                        public void run() {
                            Toast.makeText(var0, "Please add Activity in AndroidManifest!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return false;
                }

                var11 = false;
                if (var12.services != null) {
                    for (var3 = 0; var3 < var12.services.length; ++var3) {
                        if ("com.umeng.update.net.DownloadingService".equals(var12.services[var3].name)) {
                            var11 = true;
                        }
                    }
                }

                if (!var11) {
                    f.post(new Runnable() {
                        public void run() {
                            Toast.makeText(var0, "Please add Service in AndroidManifest!", Toast.LENGTH_LONG).show();
                        }
                    });
                    return false;
                }

                var11 = false;
                boolean var13 = false;
                boolean var4 = false;
                boolean var5 = false;
                if (var12.requestedPermissions != null) {
                    for (int var6 = 0; var6 < var12.requestedPermissions.length; ++var6) {
                        if ("android.permission.WRITE_EXTERNAL_STORAGE".equals(var12.requestedPermissions[var6])) {
                            var13 = true;
                        } else if ("android.permission.ACCESS_NETWORK_STATE".equals(var12.requestedPermissions[var6])) {
                            var4 = true;
                        } else if ("android.permission.INTERNET".equals(var12.requestedPermissions[var6])) {
                            var5 = true;
                        }
                    }
                }

                var11 = var13 && var4 && var5;
                if (!var11) {
                    f.post(new Runnable() {
                        public void run() {
                            Toast.makeText(var0, "Please add Permission in AndroidManifest!", Toast.LENGTH_LONG).show();
                        }
                    });
                    return false;
                }

                var11 = false;

                try {
                    Class var14 = Class.forName(var0.getPackageName() + ".R$string");
                    int var7 = var14.getField("UMUpdateCheck").getInt((Object) null);
                    if ("2.6.0.20150211".equals(var0.getString(var7))) {
                        var11 = true;
                    }

                    if (var11) {
                        return true;
                    }
                } catch (Exception var8) {
                    ;
                }

                f.post(new Runnable() {
                    public void run() {
                        Toast.makeText(var0, "Please copy all resources (res/) from SDK to your project!", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception var9) {
                ;
            }

            return var11;
        }
    }

    private static void b(Context var0, Message var1) {
    }

    static class a implements Runnable {
        Context a;

        public a(Context var1) {
            this.a = var1;
        }

        public void run() {
            try {
                UpdateResponse var1 = (new com.umeng.update.b(this.a)).a();
                if (var1 == null) {
                    UmengUpdateAgent.b(3, (UpdateResponse) null);
                } else if (!var1.hasUpdate) {
                    UmengUpdateAgent.b(1, var1);
                } else {
                    UmengUpdateAgent.b(0, var1);
                }
            } catch (Exception var2) {
                UmengUpdateAgent.b(1, (UpdateResponse) null);
                u.upd.b.a("update", "request update error", var2);
            } catch (Error var3) {
                u.upd.b.a("update", "request update error" + var3.getMessage());
            }

        }
    }
}
