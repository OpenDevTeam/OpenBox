

## 前言

&emsp;&emsp;之前我在微信群里面有说到，随着Android开发越来越成熟，关于Android方面的技术文章越来越多，作为开发者不缺资源，缺乏的是系统的知识和指导，对于学生和上班族来讲，更缺乏筛选信息的时间。从某个角度来讲，精心筛选整理网络上的优质文章，如果需要学习某方面的知识只需要看这一篇文章就够，那意义会比写一篇纯技术文章会有价值得多。

&emsp;&emsp;出于能够更便捷、高效获取优质资源的角度考虑，写一系列专题的想法就诞生了，结合自己阅读过的技术文章和实际工作经验，将平时收集的优质技术文章按照某个方面整理成一系列的专题，比如APK瘦身、插件化、程序架构、性能优化、自定义view、增量升级、移动开发各种技术解决方案等。这是我目前能够想到的除了写书之外最值得做的一件事情了。

&emsp;&emsp;这个系列从APK瘦身专题开始，后面会不定期推出其它专题，发不过的专题也会不断更新。

## APK瘦身的价值

&emsp;&emsp;APK瘦身严格来讲不算是对应用性能的优化，应该算是对程序体验的优化。APK瘦身的价值主要有几点：

- 省流量：特别是在4G网络下，更多的移动流量表示需要花费更多的钱（别指望着用户一个月1、2G的流量专门给你腾出几十M来用于下载安装包）；

- 给用户一个好印象，试想用户在安装你的程序需要很长的时间时，不仅会影响到他的心情，更是在浪费他的生命；

- 如果你的APK是预置到设备中，更大的APK表示需要占用更多的存储空间，也会增加烧录的时间。

## 从哪些方面入手

&emsp;&emsp;一个APK实际上就是一个压缩文件，解压后可以看到通常包含如下几种类型的文件或文件夹：

- classes.dex源码；

- 编译生成的二进制资源文件resources.arsc；

- res资源文件夹；

- assets文件夹；

- lib库文件夹；

- AndroidManifest.xml清单文件；

- 依赖关系配置文件project.properties；

- 代码混淆配置文件proguard.cfg；

- 签名信息文件META-INF等。

&emsp;&emsp;除了AndroidManifest.xml、proguard.cfg、project.properties、META-INF这些本身很小没有必要做进一步压缩的文件外，其它文件或者文件夹都可以进一步优化，从而减小APK的体积。下面分别就每一个文件或者文件夹该如何减小其大小做介绍：

- **classes.dex源码：**

1.代码混淆可以减小该文件的大小，因为混淆后的代码将较长的文件名、实例、变量、方法名等等做了简化，从而实现字节长度上的优化，但代码混淆会存在一些问题，比如比较耗时间，因为需要找到不能做混淆的代码并在配置文件中注明，其次是调试起来不太方便；

2.删掉没有用到的代码，可以借助Android Studio→Inspect Code...对工程做静态代码检查，删掉无用的代码；

- **resources.arsc：**

1.这个文件主要涉及到资源的ID这些，优化的空间不大，可以借助Android Studio→Inspect Code...删掉不必要的资源ID；

- **res资源文件夹：**这是APK瘦身过程中优化的大头，一个APK里面最占用空间的就是多媒体资源，图片、音频、视频主要放在res和assets文件夹下；

1.通过Android Studio→Inspect Code...对工程做静态代码检查，删掉没有用到的资源；

2.一个APK尽量只用一套图片，从内存占用和适配的角度考虑，这一套图建议放在xhdpi文件夹下；

3.使用tinypng等图片压缩工具对图片进行压缩；

4.如果对图片压缩的质量不满意，可以考虑使用其它图片格式，比如不带alpha值的jpg图片、同等质量下文件更小的webP图片格式；

5.借助微信提供的资源文件混淆工具对资源文件做混淆，进一步压缩资源文件所占用的空间；

6.如果raw文件夹下有音频文件，尽量不要使用无损的音频格式，比如wav。可以考虑相比于mp3同等质量但文件更小的opus音频格式；

7.能不用图片的就不用图片（用代码实现），如果要用图片则优先使用9图；

8.考虑引进VectorDrawable和svg。

- **assets文件夹：**assets文件夹相比于res文件夹，还有可能放字体文件、预置数据和web页面等

1.使用文中提供的字体压缩工具对字体文件进行压缩；

2.如果有web页面，可以考虑使用7zip压缩工具对该文件夹进行压缩，在正式使用的时候解压；

3.尽量不要在APK中打包预置数据，做到程序和数据分离，如果是不得不，可以考虑用7zip压缩工具对该文件进行压缩，在程序运行时解压；

- **lib库文件夹：**

1.只提供对主流架构的支持，比如arm，对于mips和x86架构可以考虑不支持，这样可以大大减小APK的体积；

## 需要用到的工具

- [图片压缩神器tinypng](https://tinypng.com/)

- [图片格式转换工具iSparta](http://isparta.github.io/)

- Android Studio→Inspect Code...

- [Android应用增量更新开源项目](https://github.com/cundong/SmartAppUpdates)

- [Android资源混淆工具](https://github.com/shwenzhang/AndResGuard)

- [主流开源项目的混淆规则列表](https://github.com/krschultz/android-proguard-snippets)

- [字体资源文件压缩神器FontZip](https://github.com/forJrking/FontZip)

- [统计APK文件中class、method、field、string数量](https://github.com/zxlie/apkcal)

- [AndroidUn7zip解压库](https://github.com/hzy3774/AndroidUn7zip)

## 值得阅读的文章

- [Android APP终极瘦身指南](http://jayfeng.com/2016/03/01/Android-APP%E7%BB%88%E6%9E%81%E7%98%A6%E8%BA%AB%E6%8C%87%E5%8D%97/)

- [Putting Your APKs on Diet](http://cyrilmottier.com/2014/08/26/putting-your-apks-on-diet/)

- [Facebook工程师是如何改进他们Android客户端的](http://greenrobot.me/devnews/facebook-engineer-improve-android-app/)

- [Shrink Your Code and Resources](https://developer.android.com/studio/build/shrink-code.html)

- [安装包立减1M--微信Android资源混淆打包工具](http://mp.weixin.qq.com/s?__biz=MzA3NTYzODYzMg==&amp;mid=214472913&amp;idx=1&amp;sn=92b54b5fcd9bbab6513e46d92095a07f&amp;scene=1&amp;srcid=0427eTI2x0dnk2EsFnysnjZI#rd)

- [美团Android资源混淆保护实践](http://tech.meituan.com/mt-android-resource-obfuscation.html)

- [APK瘦身实践](http://jayfeng.com/2015/12/29/APK%E7%98%A6%E8%BA%AB%E5%AE%9E%E8%B7%B5/)

- [让你的APK瘦成一道闪电](http://www.cnblogs.com/tianzhijiexian/p/4505312.html)

- [关于APK瘦身值得分享的一些经验](https://zhuanlan.zhihu.com/p/20006066)

- [Android APK安装包瘦身](http://hukai.me/android-tips-for-reduce-apk-size/)

- [WebP 探寻之路](http://isux.tencent.com/introduction-of-webp.html)

- [SmallerAPK, Part 1: Anatomy of an APK](https://medium.com/google-developers/smallerapk-part-1-anatomy-of-an-apk-da83c25e7003#.n98i0fzcx)

- [SmallerAPK, Part 2: Minifying code](https://medium.com/google-developers/smallerapk-part-2-minifying-code-554560d2ed40#.16iaoje8i)

- [SmallerAPK, Part 3: Removing unused resources](https://medium.com/google-developers/smallerapk-part-3-removing-unused-resources-1511f9e3f761#.88cm68g9p)

- [SmallerAPK, Part 4: Multi-APK through ABI and density splits](https://medium.com/@wkalicinski/smallerapk-part-4-multi-apk-through-abi-and-density-splits-477083989006#.dpzn5nk2f)

- [SmallerAPK, Part 5: Multi-APK through product flavors](https://medium.com/@wkalicinski/smallerapk-part-5-multi-apk-through-product-flavors-e069759f19cd#.f28004jq6)

- [SmallerAPK, Part 6: Image optimization, Zopfli & WebP](https://medium.com/@wkalicinski/smallerapk-part-6-image-optimization-zopfli-webp-4c462955647d#.23kdd9u1n)

- [SmallerAPK, Part 7: Image optimization, Shape and VectorDrawables](https://medium.com/@wkalicinski/smallerapk-part-7-image-optimization-shape-and-vectordrawables-ed6be3dca3f#.pb4174mt1)

- [SmallerAPK, Part 8: Native libraries, open from APK](https://medium.com/@wkalicinski/smallerapk-part-8-native-libraries-open-from-apk-fc22713861ff#.oc5i5wojm)

## 忠告

&emsp;&emsp;APK瘦身要有一定的度，如果对某一方面做大小优化需要很长的时间，并且效果不大，可以考虑不做。