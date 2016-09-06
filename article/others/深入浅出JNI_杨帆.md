## 深入浅出JNI ## 

**JNI** 是一种用来使得java代码和c代码之间互相调用的技术。那么我们会问，java代码为什么要调用c代码呢？出现的场景很多，比如：效率问题，众所周知java是高级语言，java程序一般都会依赖各种库，java语句的执行时间一般比c语句要慢，因此在对效率要求严格的情况下可以通过jni用c代码来提高效率。另一个比较有技巧性的使用场景是解除最大内存的限制，大家都知道android中任意一个应用程序可以使用的虚拟机内存是受限制的，比如40M，如何解除这个限制呢？可以通过jni在c代码中申请内存，因为c代码中申请的内存是存在于native线程的，因此不占用虚拟机内存，从而避开了这个限制。 java语言虽然效率比c低，但是一般不影响性能，而且库多，实现一个功能比纯c方便得多，因此，如果在c环境想调用java已经实现好的功能，也可以通过jni来直接调用。



### 实例一：java调用c ###

**第一步**：新建一个jni类：com.mycompany.jni.MyAuthToken，并书写java的native接口方法和其它代码。如下：
<pre><code>
package com.mycompany.jni;

/**
 * @author gansc23
 */
public class MyAuthToken
{
    /**
     * 生成鉴权token
     * 
     * @param imei 设备的IMEI号
     * @param imsi sim卡的IMESI号
     * @param utc  格林威治时间
     * @param agent 设备代理。如 "moto mb525"
     * @param random 随机数
     * @return
     */
    public static native String getToken(
            String imei, 
            String imsi, 
            String agent, 
            long timestamp, 
            int random );


</code></pre>

**第二步**：命令行进入<project>\src\com\mycompany\jni  目录，运行命令 javac MyAuthToken.java，将生成的 MyAuthToken.class 文件剪切到 <project>\bin\com\mycompany\jni 目录下，覆盖之前的文件。

**第三步**：命令行进入<project>\bin 目录，运行命令 javah -jni com.mycompany.jni.MyAuthToken，将生成的 <project>\bin\com_mycompany_jni_MyAuthToken.h 文件拷贝到 <project>\jni\ 目录。

**第四步**：在<project>\jni\ 目录中实现方法和其它的c/c++文件。假设实现文件的清单如下：

com_mycompany_MyAuthToken.h (自动生成的，不用修改)

com_mycompany_MyAuthToken.c

aaa.h

aaa.c

bbb.h

bbb.c


**第五步**：在项目的 <project>\jni 目录中新建一个名为 Android.mk的make文件，编写内容如下：

<pre><code>
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := authtokenmodule
LOCAL_SRC_FILES := aaa.c bbb.c \
　　com_mycompany_MyAuthToken.c

#LOCAL_PRELINK_MODULE := false

# for logging
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)


</code></pre>


**Android.mk：**是Android提供的一种makefile文件，用来指定诸如编译生成so库名、引用的头文件目录、需要编译的.c/.cpp文件和.a静态库文件等。要掌握jni，就必须熟练掌握Android.mk的语法规范。参考资料见：[Android.mk详解](http://blog.csdn.net/ly131420/article/details/9619269) 

上面这段配置中：

**LOCAL_PATH：=$(call my-dir)**

Android.mk文件必须以LOCAL_PATH变量开始，它用于在树中定位文件。在这个例子中，宏功能'my-dir'是由build system提供的，用于返回当前目录路径（包括Android.mk文件本身）

**include $(CLEAR_VARS)**

CLEAR_VARS变量是由build system提供的，并且指明了一个GNU makefile文件，这个功能会清理掉所有以LOCAL_开头的内容（例如LOCAL_MODULE,LOCAL_SRC_FILES,LOCAL_STATIC_LIBRARIES等），除了LOCAL_PATH，这句话是必须的，因为如果所有的变量都是全局变量的话，所有的可控的编译文件都需要在一个单独的GNU中被解析并执行

**LOCAL_MODULE :=authtokenmodule**

LOCAL_MODULE变量必须被定义，用来区分Android.mk中的每一个模块。文件名必须是唯一的，不能有空格。注意，这里编译器会为你自动加上一些前缀和后缀，来保证文件是一致的，比如：这里表明一个动态连接库模块被命名为"authtokenmodule"，但是最后会生成为"libauthtokenmodule.so"文件。
重要提示：如果你将你的模块命名为'libfoo'，编译系统将不会将前缀'lib'加上去，并且也会生成libfoo.so文件。

**LOCAL_SRC_FILES := aaa.c bbb.c  com_mycompany_MyAuthToken.c**

LOCAL_SRC_FILES变量被需包括一个C和C++源文件的列表，这些会编译并聚合到一个模块中。
注意：这里并不需要你列出头文件和被包含的文件，因为编译系统会自动为你计算相关的属性，源代码中的列表会直接传递给编译器。

**#LOCAL_PRELINK_MODULE := false**

'#'开头的语句表示注释

**LOCAL_LDLIBS    := -llog**

当额外的链接标志列表被用于在编译你的模块时，通过用"-l"前缀的特定系统库传递名字是很有用的。该语句告诉你生成一个在加载时链接到/system/lib/liblog.so的模块。

**include $(BUILD_SHARED_LIBRARY)**

在编译脚本中收集所有以LOCAL_开头的信息并且决定从列出的源代码中编译一个目标共享库。注意，你必须定义了LOCAL_MODULE和LOCAL_SRC_FILES变量。该语句表示会生成一个名字叫做libauthtokenmodule.so的共享库文件，这个文件可以在java代码里通过System.loadLibrary("authtokenmodule");
来依赖，一般是写在类的静态代码块里，比如：

<pre><code>
static {
	// 链接到so库
	System.loadLibrary("authtokenmodule");
}


</code></pre>

依赖之后，运行时jni就使用在.so里找到native方法的原型，从而进行调用。

**第六步：**在项目的 \jni 目录中新建一个名为 Application.mk的make文件，编写内容如下：

<pre><code>
APP_ABI := armeabi-v7a
APP_CPPFLAGS := -frtti -DCC_ENABLE_BOX2D_INTEGRATION=1 -DCOCOS2D_DEBUG=1



</code></pre>

**Application.mk：**目的是描述在你的应用程序中所需要的模块(即静态库或动态库)。Application.mk文件通常被放置在 $PROJECT/jni/Application.mk下，$PROJECT指的是您的项目。详解请参考：[Application.mk详解](http://www.cnblogs.com/yaozhongxiao/archive/2012/03/06/2381586.html) 

上面这段配置中：

**APP_ABI := armeabi-v7a**

默认情况下，NDK的编译系统根据 "armeabi" ABI生成机器代码。可以使用APP_ABI 来选择一个不同的ABI。比如：为了在ARMv7的设备上支持硬件FPU指令。可以使用  APP_ABI := armeabi-v7a或者为了支持IA-32指令集，可以使用APP_ABI := x86或者为了同时支持这三种，可以使用       APP_ABI := armeabi armeabi-v7a x86

**APP_CPPFLAGS := -frtti -DCC_ENABLE_BOX2D_INTEGRATION=1 -DCOCOS2D_DEBUG=1**

 一个C编译器开关集合，在编译任意模块的任意C或C++源代码时传递。它可以用于改变一个给定的应用程序需要依赖的模块的构建，而不是修改它自身的Android.mk文件


编译时有以下几种方式：

**1，**使用ant编译，可以将ndk-build放在ant的配置文件（build.xml）里，这样每次ant debug 都会把c文件编译一遍重新生成.so，进一步生成apk。

**2，**使用ant编译，可以将ndk-build从ant的配置文件（build.xml）里去掉，而在外面先执行ndk-build单独生成.so，然后再执行ant debug生成apk。

**3，**使用gradle编译，同ant，可以将ndk的编译步骤放在gradle编译脚本里，也可以是在外面。

这里建议放在外面编译.so。因为这样不用每一次都编译c文件了，节约了时间。

关于android studio或者eclipse中如何配置jni编译，请自行上网查阅资料，也可以参考下面链接：
[Android Studio使用新的Gradle构建工具配置NDK环境](http://blog.csdn.net/sbsujjbcy/article/details/48469569)、[手把手教你通过Eclipse工程配置调用JNI完全攻略](http://blog.csdn.net/binyao02123202/article/details/18075747)

### 实例二：c调用java ###
c调用java要简单得多，不用新建文件，只要指定需要调用java的哪一个类，类中的哪一个方法就行了。大家都知道java里方法是可以重载的，因此不光要指定名字，而且要指定参数和返回值，这样才能真正确定一个方法，获取方法实例后，传入指定参数就ok了，下面以cocos2dx中的调用为例，代码如下：
<pre><code>
void setGuajianUpdateNum(int num)
{
#if(CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
	JniMethodInfo info;
	bool ret = JniHelper::getStaticMethodInfo(info,"com/xtc/petsystem/cocosstatic/NativeFunctionForCocos",
		"setGuajianUpdateNum","(I)V");
	if(ret)
	{
		info.env->CallStaticVoidMethod(info.classID,info.methodID,num);
	}
	else
	{
		;
	}
#else
#endif
}


</code></pre>

注释宏的意思表示在android环境才编译，win32环境不编译。上面例子是调用了com.xtc.petsystem.cocosstatic.NativeFunctionForCocos这个类里的一个静态方法void setGuajianUpdateNum(int )。"(I)V"这个表达式是jni类型的写法，网上有规则，按照规则来就行了。jni类型说明请参考文档：
[JNI学习积累之二 ---- 数据类型映射、域描述符说明](http://blog.csdn.net/qinjuning/article/details/7599796)
