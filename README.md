<<<<<<< HEAD
# AndroidUpdate
=======
# Android 自动更新组件  
 该项目是从友盟更新组件2.6.3改造而来，由于友盟已停止更新服务，旧的友盟自动更新组件已经不能使用。

--
#### 修改内容
1. 增加更新接口地址参数： 接口地址通过调用者传入  
2. 增加获取更新数据获取接口： 方便自定义的方式访问服务器接口如：访问鉴权，HTTPS等  
3. 增加强制更新功能： 在没有升级到新版本前将停留在更新界面  
4. 禁用增量更新功能： APP增量更新需要服务端根据新旧APK实现diff功能产生差异Patch包


--
####集成方式
1. 添加依赖
``` 
  compile 'com.cantalou：android-update:1.0.0'
```
--

####使用方法
1. [官方文档](http://dev.umeng.com/auto-update/android-doc/quick-start) UMENG_APPKEY 可任意非空值
2. 使用封装过的方式
```
```
>>>>>>> update
