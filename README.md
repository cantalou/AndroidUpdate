# Android 自动更新组件  
 该项目是从友盟更新组件2.6.3改造而来以实现APP应用内更新功能(友盟已停止更新服务，旧的友盟自动更新组件已经不能使用)。

--
#### 修改内容
1. 增加更新接口地址参数： 接口地址通过调用者传入  
2. 增加获取更新数据获取接口： 方便自定义的方式访问服务器接口如：访问鉴权，HTTPS等  
3. 增加强制更新功能： 在没有升级到新版本前将停留在更新界面  
4. 禁用增量更新功能： APP增量更新需要服务端根据新旧APK实现diff功能产生差异Patch包


--
#### 集成
1. 添加依赖
``` 
  compile 'com.cantalou：android-update:1.0.0'
```
--

#### 接入方法
1. [官方文档](http://dev.umeng.com/auto-update/android-doc/quick-start)
2. 使用封装过的方式
```
   AppUpdateConfig conf = new AppUpdateConfig("服务器接口地址", true, true);
   AppUpdateManager manager = AppUpdateManager.getInstance();
   manager.update(this, conf);
```
3. 如果想直接使用友盟原有的方式, 需要先调用
```
   AppUpdateManager.setUpdateUrl("服务器接口地址");
```

#### 注意事项
1. 如果没有接入友盟统计功能的APP, 需要手动调用```UmengUpdateAgent.setChannel("渠道")```设置渠道
2. 对于已接入非2.6.3版本的友盟更新但又不想更换的,可以在APP启动的使用添加下面代码
```
   try {
       Field f = com.umeng.update.b.class.getDeclaredField("b");
       f.setAccessible(true);
       f.set(null,new String[]{"服务器接口地址"});   
   } catch (Exception e) {
       Log.e(e);
   }
```
3. 服务端接口返回数据要求
```
  {
       "update": "Yes", //是否有更新 有更新:"Yes", 无更新:"No"
       "version": "1.6.0",//新版本版本号
       "path": "http://文件服务器地址/app-debug.apk",//apk文件下载地址
       "origin": "",
       "update_log": "1.6.0 500 ot",//更新日志用于更新界面显示
       "proto_ver": "1.4",
       "delta": false, //是否增量更新
       "new_md5": "252e2bef51aa24218ea701c9989a6e7a", //apk文件md5
       "size": "4141388",//文件大小,单位字节
       "patch_md5": "",//增量更新补丁包md5
       "target_size": "4141388",
       "display_ads": false,
       "forceVersion":10 //强制更新最低版本, 对于所有小于forceVersion版本号的都会进行强制更新
  }
```