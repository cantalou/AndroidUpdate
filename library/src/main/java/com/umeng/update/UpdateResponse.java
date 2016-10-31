//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.update;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

import u.upd.c;
import u.upd.i;
import u.upd.n;

public class UpdateResponse extends i implements Serializable {

    private static final long a = -7768683594079202710L;
    public boolean hasUpdate ;
    public String hasUpdateStr ;
    public String updateLog = null;
    public String version = null;
    public String path;
    public String origin;
    public String proto_ver;
    public String new_md5;
    public String size;
    public String target_size;
    public boolean delta = false;
    public String patch_md5;
    public boolean display_ads;

    public int forceVersion;
    public int status;

    public UpdateResponse(JSONObject var1) {
        super(var1);
        this.a(var1);
    }

    private void a(JSONObject var1) {
        try {
            hasUpdateStr = var1.optString("update");
            this.hasUpdate = "Yes".equalsIgnoreCase(hasUpdateStr);
            if(this.hasUpdate) {
                this.updateLog = var1.getString("update_log");
                this.version = var1.getString("version");
                this.path = var1.getString("path");
                this.target_size = var1.optString("target_size");
                this.new_md5 = var1.optString("new_md5");
                this.delta = var1.optBoolean("delta");
                this.display_ads = var1.optBoolean("display_ads", false);
                if(this.delta) {
                    this.patch_md5 = var1.optString("patch_md5");
                    this.size = var1.optString("size");
                    this.origin = var1.optString("origin");
                }
                this.forceVersion = var1.optInt("forceVersion");
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public String a(Context var1, boolean var2) {
        String var3 = var1.getString(c.a(var1).f("UMNewVersion"));
        String var4 = var1.getString(c.a(var1).f("UMTargetSize"));
        String var5 = var1.getString(c.a(var1).f("UMUpdateSize"));
        String var6 = var1.getString(c.a(var1).f("UMUpdateContent"));
        String var7 = var1.getString(c.a(var1).f("UMDialog_InstallAPK"));
        if(var2) {
            return String.format("%s %s\n%s\n\n%s\n%s\n", new Object[]{var3, this.version, var7, var6, this.updateLog});
        } else {
            String var8;
            if(this.delta) {
                var8 = String.format("\n%s %s", new Object[]{var5, n.c(this.size)});
            } else {
                var8 = "";
            }

            return String.format("%s %s\n%s %s%s\n\n%s\n%s\n", new Object[]{var3, this.version, var4, n.c(this.target_size), var8, var6, this.updateLog});
        }
    }
}
