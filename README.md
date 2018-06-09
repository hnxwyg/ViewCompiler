## APPMonitor使用文档 
### 一、集成方法
1、在http://source.skyworth.com:3000/MoneyApps/AF_APPMonitor下载MonitorSDK.aar和MonitorSDK-no-op.aar文件并放在主工程的libs目录下。

2、在主工程的build.gradle的android层级里加入:
```
repositories {
	flatDir {
		dirs 'libs'
	}
}
```
3、在主工程的build.gradle的dependencies中加入：
```
debugCompile(name: 'MonitorSDK', ext: 'aar')
releaseCompile(name: 'MonitorSDK-no-op', ext: 'aar')
```
### 二、使用方法
1、启动监控，在工程Application的onCreate中初始化Monitor，具体如下：
```
	@Override
    public void onCreate() {
        super.onCreate();
        //判断是主进程才加入监控
        if (DdmHandleAppName.getAppName().equals(getPackageName())){
            AppMonitor.getInstance().start(this);
        }
    }
```
2、查看监控，在浏览器（最好是chrome）中输入：192.168.1.100:23456（ip是电视机的ip，端口23456是固定的），然后就可以在浏览器中查看当前App的相关信息。
3、具体信息分析：
1）首先是当前机器及应用的基本信息，具体如下：
Android版本:23|应用名：影视-教育|名包：com.tianci.movieplatform|总内存：3.50 GB|CPU核数：4|CPU频率：1.1GHZ

2）CPU信息，可以监控系统CPU和当前应用的CPU。

3）内存信息，可以监控当前App的内存占用情况，并可以进行内存分析，显示出当前进程中的图片以及Activity的内存泄露情况。

4）最大帧率：如果什么操作也不做，最大帧率就是60，如果没有达到60，可能主线程在做除UI外的其它事情，可以通过下面主线程监控进行排查。如果要观察应用整体UI性能，建议进行正常的操作，这里可以看到在操作时最大帧率的波动以及平均最大帧率，如果平均最大帧率在30以下就说明整个操作过程比较卡顿。

5）主线程监控，Android主线程的所有操作都是通过Handler放到消息循环中的，那么主线程就是在执行一个一个的消息，这里就是统计了耗时超过30ms的消息，比如：

the msg >>>>> Dispatching to Handler (com.coocaa.homepage.vast.BaseHomeActivity$UIHandler) {57d5d1c} null: 5 cost 115ms

以上打印，表明是com.coocaa.homepage.vast.BaseHomeActivity$UIHandler向主线程中抛的消息，msg.what为5，callback为null，耗时115ms。

the msg >>>>> Dispatching to Handler (android.view.Choreographer\$FrameHandler) {738fcc7} android.view.Choreographer$FrameDisplayEventReceiver@93272f4: 0 cost 127ms
这条打印表明是绘制过程耗时，也就是measure,layout,draw三个过程 ，这种打印如果非常多，那么就需要优化布局。

6）IO监控，如果为红色表明是主线程，具体如下：
```
/system/vendor/TianciVersion read 3 times and write 0 times cost 2ms in main thread//具体io操作说明
stackTrace://读取io栈
dalvik.system.VMStack.getThreadStackTrace(Native Method)
java.lang.Thread.getStackTrace(Thread.java:580)
com.coocaa.monitor.io.MyPosix.invoke(MyPosix.java:51)
java.lang.reflect.Proxy.invoke(Proxy.java:393)
$Proxy0.open(Unknown Source)
libcore.io.BlockGuardOs.open(BlockGuardOs.java:186)
libcore.io.IoBridge.open(IoBridge.java:438)
java.io.FileInputStream.(FileInputStream.java:76)
java.io.FileReader.(FileReader.java:42)
com.tianci.webservice.framework.CommonHeader.readFileByLines(CommonHeader.java:248)
com.tianci.webservice.framework.CommonHeader.getCoocaaVer(CommonHeader.java:211)
com.tianci.webservice.framework.CommonHeader.(CommonHeader.java:80)
com.tianci.webservice.framework.CommonHeader.getInstance(CommonHeader.java:58)
com.coocaa.homepage.vast.http.HomeHeaderGenerator.generator(HomeHeaderGenerator.java:26)
com.coocaa.homepage.vast.HomePageApp.init(HomePageApp.java:95)
com.tianci.movieplatform.application.MyApplication.onCreate(MyApplication.java:186)
com.example.test.TestApplication.onCreate(TestApplication.java:23)
android.app.Instrumentation.callApplicationOnCreate(Instrumentation.java:1013)
android.app.ActivityThread.handleBindApplication(ActivityThread.java:4707)
android.app.ActivityThread.-wrap1(ActivityThread.java)
android.app.ActivityThread$H.handleMessage(ActivityThread.java:1405)
android.os.Handler.dispatchMessage(Handler.java:102)
android.os.Looper.loop(Looper.java:148)
android.app.ActivityThread.main(ActivityThread.java:5417)
java.lang.reflect.Method.invoke(Native Method)
com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:731)
com.android.internal.os.ZygoteInit.main(ZygoteInit.java:621)
```
7）绘制监控，主要监控绘制时间超过16ms的情况，具体如下：
```
dispatch Draw:com.android.internal.policy.PhoneWindow$DecorView@95830807 cost 19ms total level 10
draw level 0 class is com.android.internal.policy.PhoneWindow$DecorView cost 18ms
draw level 1 class is android.widget.LinearLayout cost 18ms
draw level 2 class is android.widget.FrameLayout cost 18ms
draw level 3 class is com.coocaa.homepage.vast.ui.HomePageLayout cost 18ms
draw level 4 class is com.coocaa.homepage.vast.ui.navigate.NavigateLayout cost 15ms
draw level 5 class is com.tianci.plugins.homepage.theme.view.BaseNaviLayout cost 15ms
draw level 6 class is com.tianci.plugins.homepage.theme.view.subnavi.BaseSubNaviLayout cost 2ms
draw level 7 class is com.skyworth.ui.api.ScrollView cost 2ms
draw level 8 class is android.widget.LinearLayout cost 2ms
draw level 9 class is com.tianci.plugins.homepage.theme.view.subnavi.BaseSubNaviItemLayout cost 1ms
draw level 9 class is com.tianci.plugins.homepage.theme.view.subnavi.BaseSubNaviItemLayout cost 0ms
draw level 9 class is com.tianci.plugins.homepage.theme.view.subnavi.BaseSubNaviItemLayout cost 0ms
draw level 9 class is com.tianci.plugins.homepage.theme.view.subnavi.BaseSubNaviItemLayout cost 0ms
draw level 9 class is com.tianci.plugins.homepage.theme.view.subnavi.BaseSubNaviItemLayout cost 0ms
draw level 9 class is com.tianci.plugins.homepage.theme.view.subnavi.BaseSubNaviItemLayout cost 0ms
draw level 9 class is com.tianci.plugins.homepage.theme.view.subnavi.BaseSubNaviItemLayout cost 1ms
draw level 9 class is com.tianci.plugins.homepage.theme.view.subnavi.BaseSubNaviItemLayout cost 0ms
draw level 9 class is com.tianci.plugins.homepage.theme.view.subnavi.BaseSubNaviItemLayout cost 0ms
draw level 6 class is com.skyworth.ui.api.ScrollView cost 13ms
draw level 7 class is android.widget.LinearLayout cost 13ms
draw level 8 class is com.tianci.plugins.homepage.theme.view.NormalNaviItemView cost 13ms
draw level 8 class is com.tianci.plugins.homepage.theme.view.NormalNaviItemView cost 0ms
draw level 4 class is android.widget.FrameLayout cost 2ms
draw level 5 class is com.coocaa.homepage.vast.ui.major.MajorPluginLayout cost 2ms
draw level 6 class is com.coocaa.homepage.vast.ui.major.PluginViewContainerLayout cost 1ms
draw level 7 class is com.coocaa.homepage.pluginsdk.SubMajorLoading cost 1ms
draw level 6 class is com.coocaa.homepage.vast.ui.major.PluginViewContainerLayout cost 1ms
draw level 7 class is android.support.v7.widget.ExpanderLayout cost 1ms
draw level 8 class is com.coocaa.operate6_0.view.panel.PanelLayout cost 1ms
draw level 8 class is com.coocaa.operate6_0.view.panel.PanelLayout cost 0ms
```
从绘制情况可以看出整个View的层级以及每层绘制所花费的时间，这样就可以有针对性的优化层级或者View的绘制过程。
