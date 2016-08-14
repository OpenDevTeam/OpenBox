# Android开发过程中遇到的哪些坑 #

这篇文章是本人对在开发过程中踩坑经历的一次总结；分为系统API的坑、使用不当导致的坑、开源项目中的坑等几个方面，知识面有限，认知难免会有偏颇，如发现有问题还请指正。

## 系统API的坑 ##

- **Android library中的资源ID在R.java中不是final类型：**

&emsp;&emsp;**问题现象：**在library中使用switch语句区分不同的资源ID时，IDE会报错；

&emsp;&emsp;**原因分析：**这个问题在[Android Studio Project Site](http://tools.android.com/tips/non-constant-fields)有提及，在ADT14及以上的版本中，library所对应的R.java中所有ID不再是final类型，所以不能将ID作为switch语句中的case分支属性值。这个问题和IDE无关，在Eclipse和AS中都存在。

&emsp;&emsp;**解决方案：**如果涉及到区分多个ID的情况（比如监听回调事件、初始化通过xml给自定义View设置的属性值等）应该使用if...else if...else代替switch语句；

- **同一个程序内的多个进程之间使用SharedPreferences不安全：**

&emsp;&emsp;**问题现象：**在同一个程序内使用多进程时，在不同进程间使用SharedPreferences操作数据会导致SF中的数据随机丢失的情况（获取到的值为空）；

&emsp;&emsp;**原因分析：**虽然API中提供了Context.MODE_MULTI_PROCESS模式打开SF文件，但在[官方文档](https://developer.android.com/reference/android/content/SharedPreferences.html)中已有说明：“currently this class does not support use across multiple processes”，因为SF在多进程间操作是不安全的，并行操作时会导致写冲突。

&emsp;&emsp;**解决方案：**Github上有个开源项目[Tray](https://github.com/grandcentrix/tray)，专门针对在多进程中使用SF不安全的问题提供的解决方案。

- **Typeface初始化自定义字体慢：**

&emsp;&emsp;**问题现象：**在使用自定义字体的页面，进入慢；

&emsp;&emsp;**原因分析：**使用Typeface初始化字体很耗时，至少需要100ms（不同文件耗时不一样）以上的时间。

&emsp;&emsp;**解决方案：**如果在Activity的onCreate方法中初始化Typeface，会导致进入Activity慢，出现黑屏/白屏现象，所以应该尽量在非UI线程中做自定义字体的初始化操作。

- **Activity在没有完全显示/已退出的情况下显示PopupWindow异常：**

&emsp;&emsp;**问题现象：**进入Activity界面直接报错，log异常显示为："Unable to add window -- token null is not valid"；

&emsp;&emsp;**原因分析：**原因是在Activity的onCreate方法中直接显示了PopupWindow导致，PopupWindow的显示是依附在某一个View上面的（showAtLocation方法第一个参数为需要依附的view），在Activity没有完全显示时，PopupWindow无法依附在该View上，如果在此时显示PopupWindow会导致上面的异常，同样在退出Activity后也不能正常显示PopupWindow。

&emsp;&emsp;**解决方案：**在开发过程中需要考虑通过异步显示PopupWindow，避免PoupWindow显示报异常的问题。

- **Activity的onDestory方法调用时机不确定：**

&emsp;&emsp;**问题现象：**连续进入、退出某一个Activity，会出现Activity Crash掉的现象；

&emsp;&emsp;**原因分析：**在Activity的onCreate做的初始化操作（打开文件），在onDestory做的销毁操作（关闭文件）；退出Activity后onDestory并没有立即调用，再次快速进入该Activity时，该Activity是另外一个实例，并且首先调用了新Activity的onCreate方法之后再才调用上个Activity实例的onDestory方法，导致文件刚被打开就关闭了，在程序使用数据时Crash掉；

&emsp;&emsp;**解决方案：**准确来讲只要是系统方法，调用时机都不确定。对于这种问题只能尽量不要在Activity的系统回调方法中做资源初始化和释放的操作，比如涉及到IO操作的情况，在使用的时候才打开，使用完后立即关闭；

- **透明主题导致Activity生命周期回调的变化：**

&emsp;&emsp;**问题现象：**从当前Activity跳转到其它Activity时，当前Activity的onStop方法并没有调用；

&emsp;&emsp;**原因分析：**给当前Activity设置为透明主题导致，通过添加打印跟踪发现，从该Activity跳转到其它Activity时，该Activity的onStop方法不会执行；

&emsp;&emsp;**解决方案：**谨慎使用透明主题，如果必须要为Activity设置为透明主题，不要在onStop方法中做任何操作，因为该方法并不会被调用。透明主题存在很多问题，比如在设置为透明主题的界面按Home按键时，会存在界面刷不干净的情况。

- **不要通过Bundle传递很大块的数据：**

&emsp;&emsp;**问题现象：**从目录界面跳转到内容显示界面，出现随机崩溃的现象，报的异常是：TransactionTooLargeException；

&emsp;&emsp;**原因分析：**跟踪发现如果通过Bundle传递太大块（>1M）的数据会在程序运行时报TransactionTooLargeException异常，具体原因可以上[官网](https://developer.android.com/reference/android/os/TransactionTooLargeException.html)查看，大致意思是传递的序列化数据不能超过1M，否则会报TransactionTooLargeException异常；之所以随机是因为每次传递的数据大小不一样。

&emsp;&emsp;**解决方案：**如果你在不同组件之间传递的数据太大，甚至超过了1M，为了提高效率和程序的稳定性，建议通过持久化的方式传递数据，即在传递方写文件，在接收方去读取这个文件；

- **不要在Application类中缓存数据：**

&emsp;&emsp;**问题现象：**程序从后台切换到前台，直接崩了；

&emsp;&emsp;**原因分析：**程序在后台时，为了给正在运行的程序提供更多可使用的内存，Application中的数据可能会被清理掉，如果在Application中缓存了数据，并且在程序重新回到前台时没有做好恢复工作，程序会出现不可预见的情况（比如数据错乱、崩溃等），具体可以参照这篇文章[Don't Store Data in the Application Object](http://www.developerphil.com/dont-store-data-in-the-application-object/)；

&emsp;&emsp;**解决方案：**不要在Application中缓存数据。

- **使用AsyncTask无法避开的坑：**

&emsp;&emsp;**问题现象：**使用AsyncTask异步执行的任务并没有立即执行；

&emsp;&emsp;**原因分析：**AsyncTask这个类的实现可谓一波三折，方案修改了好几个版本，初次引入这个类时，所有的Task是放在一个独立的后台线程中执行的，也就是如果有多个Task同时被调用也是顺序执行的；从1.6开始，改为通过线程池可以支持并行执行多个Task；但从3.0开始，又改回只有一个独立的后台线程执行所有Task，主要是为了避免多个Task并行执行导致的程序错误，但为了让AsyncTask能够支持多个Task并行执行，从3.0起，增加了executeOnExecutor方法，调用者自行实现线程池可以达到并行多个Task的效果。

&emsp;&emsp;**解决方案：**如果在某个地方需要同时执行多个异步任务，强烈建议使用线程池；

- **数据库升级中的坑：**

&emsp;&emsp;**问题现象：**在数据库的某个表中增加/修改了某个字段后，程序在运行时崩溃掉了；或者在增加字段时修改了数据库的版本号，但程序升级后，原来的数据丢失了；

&emsp;&emsp;**原因分析：**SQlite数据库升级时需要修改OpenHelper中的版本号，并且数据库升级会删掉原来数据库中的数据，需要手动将原数据库中的数据拷贝到高版本的数据库中；

&emsp;&emsp;**解决方案：**做好数据库升级的恢复工作，避免出现崩溃、数据丢失的情况。

- **程序在未启动的情况下，静态注册的广播无法收到消息：**

&emsp;&emsp;**问题现象：**程序添加了对开机广播的监听，但无法接收到；

&emsp;&emsp;**原因分析：**这个问题只有在程序安装但没有启动时才会出现，只要程序启动过一次后就不会有这个问题。并且只有在Android 3.1及以上的版本才会出现，具体原因是：从Android3.1开始，新安装的程序会被置于"stopped"状态，并且只有在至少手动启动这个程序一次后该程序才会改变状态，能够正常接收到指定的广播消息。Android这样做的目的是防止广播无意或者不必要地开启未启动的APP后台服务。也就是说在Android3.1及以上的版本，程序在未启动的情况下通过应用自身完成一些操作是不可能的，但Android提供了一种借助其它应用发送指定Flag广播的方式，达到应用在未启动的情况下仍然能够收到消息的效果。从Android 3.1开始，系统给Intent定义了两个新的Flag，分别为FLAG_INCLUDE_STOPPED_PACKAGES（表示包含未启动的App）和FLAG_EXCLUDE_STOPPED_PACKAGES（表示不包含未启动的App），用来控制Intent是否要对处于停止状态的App起作用。

&emsp;&emsp;**解决方案：**只能借助其它应用给自己发送带FLAG_INCLUDE_STOPPED_PACKAGES标志的广播才能实现在程序未启动的情况下接收到广播；

- **android:windowBackground导致的过渡绘制问题：**

&emsp;&emsp;**问题现象：**界面的布局已无法进一步优化，但仍然存在过渡绘制的问题；

&emsp;&emsp;**原因分析：**window存在默认的背景，会增加过渡绘制的可能。Activity是依附在Window上的，如果给Activity设置了背景，并且没有去掉window的背景，很容易导致过渡绘制；这里还有一个坑，有的应用为了避免程序冷启动时出现黑屏/白屏的问题，在主题中给window设置了背景，并且在Activity的布局中给Activity也设置了背景，这会导致当前界面存在两个背景，占用了双倍的内存，并且还会有过渡绘制的问题。程序启动黑屏应该去优化性能问题，而不是采用给window设置背景的方式；

&emsp;&emsp;**解决方案：**可以通过给Activity自定义主题，在主题中去掉window的默认背景，即：<item name="android:windowBackground">@null</item>；

- **类的finalize方法调用时机不确定：**

&emsp;&emsp;**问题现象：**程序随机崩溃；

&emsp;&emsp;**原因分析：**多个地方用到了同一个类，该类用于对数据的IO操作，打开文件后并没有立即关闭，也没有释放资源的public方法，主要通过类的finalize方法关闭文件，释放资源；

&emsp;&emsp;**解决方案：**finalize方法的调用时机是不确定的，不要指望通过该方法释放与类相关的资源，避免出现随机的bug；

- **Fragment isAdded：**

&emsp;&emsp;**问题现象：**程序随机崩溃；

&emsp;&emsp;**原因分析：**跟踪异常log发现，是因为Fragment没有完全显示或者已经离开Fragment的情况下，导致的异常，这类异常的主要原因是：使用Fragment时，通过异步操作（比如回调、非UI线程等）更新Fragment的状态，但此时Fragment没有完全显示或者已经离开Fragment；

&emsp;&emsp;**解决方案：**在调用Fragment的方法之前，强烈建议调用isAdded方法判断Fragment是否依附在Activity上，避免出现异常。

- **Fragment hide、show被调用时，生命周期不会回调：**

&emsp;&emsp;**问题现象：**同一界面不同Fragment之间切换时，并没有触发一些动态效果，比如播报音频、显示切换动画等；

&emsp;&emsp;**原因分析：**Fragment hide、show被调用时，系统并不会调用Fragment的生命周期回调；

&emsp;&emsp;**解决方案：**不同Fragment之间切换时，主动调用各个Fragment的生命周期回调；

## 使用不当造成的坑 ##

- **9图不要用tinypng压缩：**

&emsp;&emsp;**问题现象：**使用压缩工具压缩9图后，显示变形；

&emsp;&emsp;**原因分析：**9图除了图片信息外，还存储一些Android在显示9图过程中需要用到的必要信息，通过压缩工具压缩图片会改变文件的信息，9图被压缩后程序能显示，但显示的效果无法达到预期，因为拉伸信息丢失了。

&emsp;&emsp;**解决方案：**9图文件本身就不大，没必要压缩；

- **同一设备上，相同程序的图片放在不同drawable文件夹下，占用内存不一样：**

&emsp;&emsp;**问题现象：**程序刚启动就占用了很高的内存；

&emsp;&emsp;**原因分析：**图片放置位置不合理导致的，程序在不同的设备中运行时，会根据设备的分辨率和屏幕密度去从与之分辨率匹配的资源文件夹中取图片，如果没有对应分辨率的文件夹，则从相近分辨率的文件夹中取，但图片会被拉伸到当前设备屏幕的宽高，所以会存在图片被放大或者缩小的问题，导致占用内存会随之变化，具体可以查看这篇博客[关于Android中图片大小、内存占用与drawable文件夹关系的研究与分析](http://blog.csdn.net/zhaokaiqiang1992/article/details/49787117)；

&emsp;&emsp;**解决方案：**为了减少UI的工作量，并且减少APK的内存占用的方法是让UI出一套高分辨率版本的图片，放在hdpi文件夹下。

- **Adapter ViewHolder缓存导致显示错乱的坑：**

&emsp;&emsp;**问题现象：**ListView每一项在滑动的过程中内容显示错乱；

&emsp;&emsp;**原因分析：**在Adapter的getView方法中通过position更新每一项的内容时，对于根据判断条件给每一项设置属性的情况，每个判断条件下都需要给每一项的每个属性赋值，否则在滑动ListView或GridView时会导致内容错乱；

&emsp;&emsp;**解决方案：**在getView方法里面，给每一项都要设置对应的属性，比如给每一项的头像设置图片，如果某一项没有头像，不能不设置，应该设置为透明，否则会错乱。

- **Toast连续显示时长时间不消失：**

&emsp;&emsp;**问题现象：**多个Toast同时显示时，Toast一直显示不消失，退出程序了仍然显示；

&emsp;&emsp;**原因分析：**看Toast的源码可以发现，同时显示多个toast时是排队显示的，所以才会出现同时显示多个Toast时很久都不消失的情况；

&emsp;&emsp;**解决方案：**这属于体验问题，很多应用都存在。建议定义一个全局的Toast对象，这样可以避免连续显示Toast时不能取消上一次Toast消息的情况（如果你有连续弹出Toast的情况，避免使用Toast.makeText）；

- **build.gradle中的versionName和versionCode：**

&emsp;&emsp;**问题现象：**从Eclipse转到AS的项目，在机器上运行时报版本比之前APK版本低的错误；

&emsp;&emsp;**原因分析：**从Eclipse转到AS的过程中，如果你是通过AS直接新创建的一个工程，注意模板会在build.gradle中给程序设置默认versionName和versionCode为1，如果AndroidManifest.xml中的versionCode、versionName比build.gradle中的更高，会导致因为版本问题安装不上的情况（报INSTALL_FAILED_VERSION_DOWNGRADE错误）；

&emsp;&emsp;**解决方案：**只在build.gradle中设置版本名和版本号；

- **AS中依赖包的动态更新：**

&emsp;&emsp;**问题现象：**依赖包频繁更新，因为AS编译有缓存，每次更新都需要修改依赖包的版本号，特别麻烦，特别是依赖关系比较复杂的情况下；

&emsp;&emsp;**解决方案：**在AS中，如果你想动态同步一个依赖包的更新，可以在依赖包的最后面写上“+”，比如：compile 'com.android.support:appcompat-v7:23.0.+' ，但这种方法需要谨慎使用，否则会因为依赖包的变动导致你的项目不稳定：[Don't use dynamic versions for your dependencies](https://link.zhihu.com/?target=http%3A//blog.danlew.net/2015/09/09/dont-use-dynamic-versions-for-your-dependencies/)

- **AS中同一个工程module太多导致编译慢：**

&emsp;&emsp;**问题现象：**编译一个工程要好几分钟，特别是clean的时候，经常10分钟以上；

&emsp;&emsp;**原因分析：**其实这个很好理解，每个module中都有一个build.gradle，编译的时候，每个module的build.gradle中的task都需要执行，所以编译时间会很长。

&emsp;&emsp;**解决方案：**要解决这个问题很简单，将不经常变动的module打包成aar，主工程依赖aar而不是module，这样避免了每次都需要重新编译module的情况。

- **频繁的GC操作导致程序卡顿：**

&emsp;&emsp;**问题现象：**通过AS Monitor观察应用运行过程中的内存抖动厉害，通过GPU呈现模式观察每一帧的曲线差别很大，整体感受程序运行时不流畅；

&emsp;&emsp;**原因分析：**在2.3之前GC操作是不能并发进行的，也就是系统正在进行GC程序就只能阻塞住等待GC结束，在2.3之后GC操作改成了并发的方式进行，GC过程中不会影响程序的正常运行，但在GC操作的开始和结束还是会短暂阻塞一段时间，所以频繁的GC会导致使用应用的过程中卡顿。

&emsp;&emsp;**解决方案：**为了应用在使用过程中更流畅，需要尽量减少触发GC操作，这涉及到性能优化，对于静态代码的分析，AS已经很强大了，可以使用Android Studio的Analyze→Inspect Code...进行分析；

- **TextView 的setText方法，如果传入一个数字会直接当作字符串资源ID处理：**

&emsp;&emsp;**问题现象：**程序运行时报“NotFoundException”异常；

&emsp;&emsp;**原因分析：**TextView.setText(int value)的传值有问题，在xml文件中没有找到id对应的字符串；

&emsp;&emsp;**解决方案：**给TextView设置文本的时候一定要转成String或者Charsequence类型，避免TextView将setText中的参数当做字符串资源ID处理，去加载字符串资源，因为字符串在xml文件中不存在导致程序运行时崩溃。

- **通过反射访问方法和字段的效率大不一样：**

&emsp;&emsp;**问题现象：**程序运行卡、慢；

&emsp;&emsp;**原因分析：**在一个循环中使用到了反射，并且是调用的反射方法，改成反射字段后，卡、慢的现象得到明显的改善；

&emsp;&emsp;**解决方案：**通过反射修改或者获取类中的某个属性时，强烈建议使用访问字段的方式，不要使用访问方法的方式，这两者之间的效率相差很大，亲测访问方法是访问字段耗时的1.5倍，具体情况和类的复杂度有关。

- **.nomedia文件的使用：**

&emsp;&emsp;**问题现象：**程序中的缓存文件在相册、音乐播放器中显示；

&emsp;&emsp;**原因分析：**相册、音乐播放器等多媒体应用是读取媒体库中的数据，而程序的缓存文件被缓存到了媒体数据库中；

&emsp;&emsp;**解决方案：**如果你希望自己应用生成的数据不被媒体库扫描到，应该在生成数据的文件夹下创建一个名为".nomedia"的隐藏文件，避免出现一些无意义的文件也被媒体库扫描到的情况，比如APP的缓存图片在相册中显示、宣传视频在视频播放器中显示、音效在音乐播放器中显示等。

- **循环动画：**

&emsp;&emsp;**问题现象：**在不待机的情况下，长时间处于一个界面时，手机发烫；

&emsp;&emsp;**原因分析：**界面中存在循环动画，CPU、GPU一直在工作；

&emsp;&emsp;**解决方案：**循环动画会导致界面一直在刷新，CPU、GPU持续工作，会有功耗问题，建议拒掉这种视觉呈现效果。

- **谨慎使用aaptOptions.cruncherEnabled = false;aaptOptions.useNewCruncher = false;**

&emsp;&emsp;**问题现象：**编译生成的APK文件特别大，超过了正常的大小；

&emsp;&emsp;**原因分析：**解压APK发现，主要是图片资源导致，将APK中的res文件夹和源码下的res文件夹对比，发现多了很多图片文件；跟踪原因发现最新的buildtools对资源文件的检测很严格，对于Eclipse转AS的项目，很多时候都是因为图片问题导致在AS上编译不过，比如将jpg强转为png在AS上就编译不过，在项目中可以在build.gradle中加上这两句：aaptOptions.cruncherEnabled = false;aaptOptions.useNewCruncher = false，屏蔽掉aapt对图片的严格检测。但需要谨慎使用这两个属性，否则可能会导致编译生成的APK特别大（解压生成后的APK发现，对于有问题的图片，每个drawable文件夹下都会拷贝一份）；

&emsp;&emsp;**解决方案：**去掉属性设置，解决编译问题。

- **谨慎使用对话框主题：@android:style/Theme.Dialog**

&emsp;&emsp;**问题现象：**点击打开一个对话框样式的界面，界面中什么内容也没有，内存瞬间上涨了30M左右（1920*1080 xxhdpi的真机）；

&emsp;&emsp;**原因分析：**采用排除法发现根本原因是该对话框为Activity，主题设置成了对话框样式，如果将主题改为普通Activity的主题，内存的上涨很正常；

&emsp;&emsp;**解决方案：**使用PopupWindow或者Dialog代替对话框样式的Activity。

## 开源项目中的坑 ##

- **FancyCoverFlow：** 这个控件在API高于16的设备中，滑动的过程中会强制刷新一遍，导致切换和初始化的时候都很卡，当时觉得这个效果挺好，后来用上之后这个控件成了性能瓶颈；

![](http://ww4.sinaimg.cn/large/6d17e381gw1f4q8dvd8l1j20fh04hwfp.jpg)

- **Fresco：** 这个控件用起来特别爽，唯一的缺陷的相比于相同功能的其它开源项目（Glide、Picasso），体积过大；

- **ActiveAndroid：** 这个轻量级的数据库框架也挺好用，但缺陷是初始化耗时，可以看一下这篇文章：[在Android中使用反射到底有多慢？](http://blog.nimbledroid.com/2016/02/23/slow-Android-reflection-zh.html)

- **JXL：** 一个读写Excel文件的开源库，用起来很方便，但有个问题：文件大小超过5M直接挂掉；

- **JPinyin：** 汉字转拼音的一个工具库，APK加密后这个库不能正常使用，后来查出是因为项目中数据的问题，加密后数据的内容变化了，最后只能自己改造，将数据按照我们自己的方式处理。

# 特别说明 #

&emsp;&emsp;在工作过程中肯定会遇到很多问题，虽然网络发达，但亲力亲为去解决问题会让自己对各个知识点的理解更深刻，工作经验就是一个一个坑填过来的，上面的总结只是冰山一角，强烈推荐看一看[StackOverflow](http://stackoverflow.com/)和[Android Issue Tracker](https://code.google.com/p/android/issues/list?can=2&q=&sort=-stars&colspec=ID%20Status%20Priority%20Owner%20Summary%20Stars%20Reporter%20Opened)上关于android标签的热点问题，里面都是开发过程中可能会遇到的问题，非常值得一读。



