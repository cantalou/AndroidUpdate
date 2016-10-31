package com.cantalou.android.update;

public interface AppUpdateListener {
    /**
     * 监测到新版本
     *
     * @param newCode 版本号
     * @param newName 名称
     */
    public void onNewVersionFound(int newCode, int newName);

    /**
     * @param click 点击类型 <br>
     * @Description: 点击按钮
     * @Title: UserOperationListener.java
     * @date 2015年3月4日 上午10:00:46
     * @author LinZhiWei
     */
    public void onUserOperationClick(int click);
}
