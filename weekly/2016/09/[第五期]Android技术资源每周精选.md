#[第5期]Android技术资源每周精选


##摘要
>本周推出RecyclerView、歌词显示控件、SQLite、三大热修复方案等相关经典文章，以及今年几个值得研究的开源项目（项目使用了MVP、RxJava 、 Retrofit、okhttp等热门技术）。

#View

###1 [进击的RecyclerView——LRecyclerView](http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650237006&idx=1&sn=3275f10db7eafd92b204206c6f3d869c&scene=1&srcid=0907XXiIglauxPreIgkA5gd4#rd) 

作者：**一叶飘舟**   分享者： means

> 又一个RecyclerView项目，具备左右两侧菜单、侧滑、MultiType子条目显示、长按拖拽、滑动删除等功能，节省开发时间。

 `RecyclerView` 
###2 [自定义LyricView实现歌词显示控件](http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650236950&idx=1&sn=d51e6420df7f7533bc81cfe98541c3c0&scene=0#rd) 

作者：**码农小阿飞**   分享者： 张明云

> 文中从歌词文件的格式入手，首先写了一个解析歌词文件的程序，然后开始自定义LyricView实现类似当前很多播放器那样的实现当前播放高亮显示、歌词回弹效果、歌词淡入淡出效果以及滑动歌词快速播放等功能，最后作者给出了自己的demo。

`歌词解析` `自定义View` `音乐播放器` 

###3 [GitHub - Ramotion/folding-cell-android](https://github.com/Ramotion/folding-cell-android) 

作者：**Ramotion**   分享者： Sobin

> 一个非常酷炫的折叠动画效果，遵循material design设计，接近2000个star！

`动画`
#深度解析
###1 [SQLite 中的各种限制](http://mp.weixin.qq.com/s?__biz=MjM5NzM0MjcyMQ==&mid=2650067376&idx=1&sn=c951a33e73fc2ba17c24bff14d4987ae&scene=1&srcid=0906JfOogISMCmY8AI96dkcp#rd) 

作者：**开源中国**   分享者： 张明云

> 干货，介绍了SQLite中的一些限制如SQL语句的最大长度、一次连接操作中最大的表数量等等，并提供了相应的解决办法。

`SQLite`
###2 [[Android：用GSON 五招之内搞定任何JSON数组]](http://mp.weixin.qq.com/s?__biz=MzA3NzMxODEyMQ==&mid=2666453375&idx=1&sn=18e6cd1de54be7cd3adb8dfb2aab93dc&scene=1&srcid=0904uEnRDhoC6ctTz3BVNDm8#rd) 

作者：**Android技术之家**   分享者： 张明云

> 本文简洁明快的讲解了Json数组中不同形式的解析方法，是在充分了解了GSON解析json的基础上进行的讲解，不懂的也没有关系，作者开头就给出了一篇介绍性文章，从另一篇中看完就可以继续研究本文了。

`Json` `Gson`
###3[老司机带你快速掌握JNI](http://mp.weixin.qq.com/s?__biz=MzIwNjQ1NzQxNA==&mid=2247483845&idx=1&sn=c746314478bf40bbe4df21e8438ef6eb&scene=4#wechat_redirect)

作者：**杨帆**   分享者： 井方哥

> 本文通过实例java调c,c调java两个实例，概要分析JNU的使用方法。

`JNI`
###4 [微信二维码登录原理](http://zhuanlan.zhihu.com/p/22325152) 

作者：**Evelyn Shen**   分享者： 井方哥

> 用案例展示了微信是如何通过扫二维码登录的，作者讲解生动，简单易懂。

`微信二维码`
#前沿技术

###1 [Android热修复技术选型——三大流派解析](http://mp.weixin.qq.com/s?__biz=MzA3ODg4MDk0Ng==&mid=2651112709&idx=1&sn=09f82f5e2e8e287904391dbc4b1f83f6&scene=1&srcid=0908PYLhpCyp8k8IS8QXKKSV#rd) 

作者：**所为**   分享者： 王东尧

> 通过介绍QQ空间补丁、Tinker以及基于AndFix的阿里百川HotFix技术的原理分析和横向比较，帮助开发者更深入了解热修复方案。





###2 [Android一整套图片解决方案](http://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650820998&idx=1&sn=c9670674dcfb71a24521e898776f234e&scene=1&srcid=09069kqHN4pIvvn1m07ALCeh#rd) 

作者：**hss01248**   分享者： 水牛

> 分析了几种常用图片的特点，从图片选择到裁剪，再到压缩、上传到七牛，客户端显示、gif处理、效果处理，完整地展示了一套图片解决方案。




###3 [Android 7.0多窗口支持](http://www.jianshu.com/p/efcb08140ab0?utm_campaign=maleskine&utm_content=note&utm_medium=reader_share&utm_source=weixin) 

作者：**MuMuXuan**   分享者： MuMu轩

> 介绍了Android 7.0 如何切换到多窗口模式、多窗口生命周期、如何禁用多窗口模式，适合想体验新功能的同学阅读。

#经验之谈

###1 [【Dev Club 话题讨论】程序员的成长离不开哪些软技能？](http://mp.weixin.qq.com/s?__biz=MzA3NTYzODYzMg==&mid=2653577777&idx=2&sn=a43f6305ca6977bcc4377ad1c4f51728&scene=1&srcid=0908zJ12D9fe7JeeC4WL53SI#rd) 

作者：**Dev Club**   分享者： 井方哥

> 分享程序员在编程以外应该具备的技能，包括沟通、时间管理、写作、探究新技术、锻炼身体等等。对丰富程序员的业余生活很有帮助。
###2 [代码可读性提升指南](http://mp.weixin.qq.com/s?__biz=MzAxMzk5MDA5OA==&mid=2247483749&idx=1&sn=3035015ce4f6f9ff394f81f3d38ee3ff&scene=1&srcid=0906g2zMmizvP3GjxDkOPaOQ#rd) 

作者：**极光 - hevin**   分享者： 孙生光

> 作者介绍了怎样提升代码可读性，怎样是更好的代码风格，如何重新组织代码。适合编程团队协作入门者阅读。

#经典推荐
###1[一个Android程序员憋不住要分享的微信公众号](http://mp.weixin.qq.com/s?__biz=MzIwNjQ1NzQxNA==&mid=2247483841&idx=1&sn=4d103e343f4cb84cb35c52253ef76019&scene=4#wechat_redirect)

作者：**张明云**   分享者： 井方哥
> Android老司机每日必看的一些Android技术公众号，纯二维码。

`微信公众号` 
###2 [2016最值得学习的项目课程，干货最多](http://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650821003&idx=1&sn=289fd1253f8067ef311fad9f8718df13&scene=1&srcid=090758njLSdTPmbaLRGuQGum#rd) 

作者：**菜鸟窝**   分享者： 张明云

> 纯干货，涵盖了从商城、新闻到团购、微博等众多常见的安卓APP完整源码，涉及到MVP、OkHttp、EventBus、热更新等技术，还包含React-Native入门到实战。

 `开源项目`
###3 [2016年最值得学习的五大开源项目](http://www.jianshu.com/p/8180cc105f01) 

作者：**maat红飞**   分享者： 张明云

> 作者分别介绍了具体的这五个开源项目中用到了那些比较值得学习的东西，如架构层上面结构，图片加载用Picasso，网络请求用RxJava & Retrofit＋okhttp等等很多，而且最后都给出github地址，便于让读者找到资料进行学习

 `开源项目`