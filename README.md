Android Wear Demos
==========
该项目为Android Wear的Demos，使用Gradle构建(推荐使用Android Studio，目前最新版为beta 0.8)  
[API文档](https://developer.android.com/shareables/training/wearable-support-docs.zip).

## 遗留问题
1.  CardScrollView不能滚动问题
2.  BoxInsetLayout由于手上没有圆形的Android Wear，测试不了
3.  未整理Demo，由于为了赶时间，代码很乱
4.  WearableListView.Item、SimpleAnimatorListener用法未知

## 依赖
    - Oracle JDK 8
    - Android SDK，包括：
        -Android 4.4W (API 20) SDK Platform
        -Android 4.4.2 （API 19）SDK Platform
        -Android SDK Build-tools 20.0.0

## 项目架构
    ————WearDemos
        |项目
        |
        |————ApplicationDemos
        |运行于手机，手机版本最低要求4.3，主要为与Android Wear交互示例
        |
        |————WearDemos
        |运行于android Wear，主要为Android Wear与Android 手机交互以及support.werable包的控件使用示例
        
## 唠叨
部分api使用方法官方samples有示例，暂不列出