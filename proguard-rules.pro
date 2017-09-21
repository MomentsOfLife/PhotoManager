-optimizationpasses 5                   # 指定代码的压缩级别
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses        # 是否混淆第三方jar
-dontpreverify                          # 混淆时是否做预校验
-dontoptimize
-ignorewarning                          # 忽略警告，避免打包时某些警告出现
-verbose                                # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    # 混淆时所采用的算法
-dontshrink                             # 不删除未引用的资源(类，方法等)

-libraryjars  <java.home>/lib/rt.jar	# Java运行时
#常规的代码混淆规则

-keep public class com.bihe0832.photo.Main {
	public static void main(java.lang.String[]);
}

-keep class mediautil.**{*;}
-keep class com.adobe.xmp.**{*;}
-keep class com.drew.**{*;}