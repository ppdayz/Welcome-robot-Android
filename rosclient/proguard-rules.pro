# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in G:\SDKS\Android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:


#保证是独立的jar,没有任何项目引用,如果不写就会认为我们所有的代码是无用的,从而把所有的代码压缩掉,导出一个空的jar
-dontshrink
-keepattributes Signature #忽略泛型
-keepattributes Exceptions,InnerClasses #忽略异常和内部类
-optimizations !class/unboxing/enum
#这样是keep所有类的所有静态变量。
-keepclassmembers class ** {
    static <fields>;
}

-keep class com.csjbot.rosclient.RosClientAgent {*;}
-keep class com.csjbot.rosclient.listener.EventListener {*;}
-keep class com.csjbot.rosclient.listener.ClientEvent {*;}

#忽略警告
-dontwarn java.lang.**
-dontwarn java.io.**
-dontwarn java.nio.**
-dontwarn java.net.**
-dontwarn java.util.**
