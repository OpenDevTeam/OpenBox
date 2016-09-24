#[第6期]Android技术资源每周精选


##摘要
>本周推出微信开发资源汇总、MVP重构实战经验、RXJava、DataBinding等前沿知识；还包括TextView的细节知识，IconFont攻略以及Android系统安全的知识。

#View

#1 [Android IconFont全攻略](http://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650821086&idx=1&sn=080bc2cfaa04947b974bf6273bbd7259&chksm=80b78540b7c00c56fd15fb8027b4d2730523c32c2099970cc49f9ddc03d235c907dc59e1695c&scene=1&srcid=0918uOZgcVm8Kk2IPJj4ZHto#rd) 

作者：**张鸿洋**   分享者： 张明云

> 1、如何方便的使用一些素材库
> 2、字体库大学的忧虑
> 3、如何方便的使用Iconfont

#2 [ViewRootImpl & ViewGroup & View 触摸事件派发机制源码分析](http://www.jianshu.com/p/670baed75f8b?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=weixin) 

作者：**Nvsleep**   分享者： Evsleep

> 1、Activity顶层窗口接受屏幕触摸事件的准备以及对输入事件到来时候的预处理;

> 2、ViewGroup的事件派发机制dispatchTouchEvent()分析;

> 3、View自身的事件派发机制dispatchTouchEvent()分析;

> 4、View自身onTouchEvent()方法分析;

#3 [StyleTextView——细节至上的TextView](http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650237088&idx=1&sn=208fdcb2f3f9947e682de1bdfac95527&scene=1&srcid=0915kkeutfqy4d4CXO6hHCSE#rd) 

作者：**siyehua**   分享者： means

> 1、 TextView控件绘制文字，上下会留有一定空白原因；

> 2、使用 canvas.drawText()绘制文字时，baseLine 该如何确定；

> 3、使用 canvas.drawText() 绘制文字时，如何让文字上下没有留白。

#经验之谈

#1 [从MVC到MVP,记我的两次项目重构实战经历](http://mp.weixin.qq.com/s?__biz=MzIwNjQ1NzQxNA==&mid=2247483871&idx=1&sn=2ee9837a6d94bbb83ef966cd26bddec7&scene=1&srcid=0919KEtDO7rbbeH2bDYuvfcy#rd) 

作者：**井方哥**   分享者： 张明云

> 1、MVC和MVP对比

> 2、MVP最简模型

> 3、重构之路

> 4、重构之后：作者个人体会

#2 [推送，从入门到放弃](http://mp.weixin.qq.com/s?__biz=MzAxNzMxNzk5OQ==&mid=2649484726&idx=1&sn=7bcd8c2c9265be6a49b9e9f7fe4a95ad&chksm=83f824b6b48fada09d01bbd7ff09adb2ede6fbc4857d8be7114dab7f9c3f45137b7c6e008c2b&scene=1&srcid=0919nhO8TS5c36snSnr32XuZ#rd) 

作者：**徐宜生**   分享者： 徐宜生

> 1、推送的介绍

> 2、推送方案

> 3、第三方推送注意事项

> 4、推送原理

#3 [Android WebView：性能优化不得不说的事](http://mp.weixin.qq.com/s?__biz=MzI3MDE0NzYwNA==&mid=2651433887&idx=1&sn=49ef97de2710409531c4118819a2f4d7&chksm=f12882e4c65f0bf28f89077a11df980d8e08882b24738812688b20cfb3751d52dda7d83ea43a&scene=1&srcid=0918K1MGEAUWuIETEv3BgQ8O#rd) 

作者：**motalks**   分享者： 刘兵

> 本文作者从Android中使用WebView的角度来总结了Android中的WebView性能优化的常见方法：

> 1、页面加载速度优化

> 2、选择合适的WebView缓存

> 3、常用资源预加载

> 4、常用js本地化及延迟加载

#4 [[Android技术专题]动画那点小秘密](http://mp.weixin.qq.com/s?__biz=MzIwNjQ1NzQxNA==&mid=2247483850&idx=1&sn=92533e833b506820f6729cbb565b35c7&scene=1&srcid=0912HN6s7TLdiZxEwiPhBqiO#rd) 

作者：**张明云**   分享者： 井方哥

> 1、实现动画效果的几种方式

> 2、使用动画过程中遇到过哪些坑

> 3、动画优质开源项目推荐

> 4、参考资料

#前沿技术

#1 [微信小程序开发资源](https://github.com/justjavac/awesome-wechat-weapp) 

作者：**justjavac**   分享者： 张明云

> 1、官方文档
> 2、新闻报道
> 3、工具破解
> 4、分析
> 5、讨论
> 6、教程
> 7、文章
> 8、代码

#2 [拆轮子系列：拆 RxJava](http://sc.qq.com/fx/u?r=mcjpenA) 

作者：**piasy**   分享者： 寂小桦

> 1、事件流源头（observable）怎么发出数据
> 
> 2、响应者（subscriber）怎么收到数据
> 
> 3、怎么对事件流进行操作（operator/transformer）以及整个过程的调（scheduler）
> 
> 4、backpressure

> 5、hook

> 6、测试

#3 [Data Binding Formatter Plugin - 知乎专栏·「loli.xing.moe」](http://zhuanlan.zhihu.com/p/22427306) 

作者：**loli.xing.moe**   分享者： joker

> 这是一个Android Data Binding的实体类的插件工具，目前已经在JetBrans Plugin中体提供下载，并介绍了制作过程的原理等，值得一看。

#4 [GitHub - lzyzsd/JsBridge: android java and javascript bridge, inspired by wechat webview jsbridge](https://github.com/lzyzsd/JsBridge) 

作者：**lzyzsd**   分享者： Rex

> 本篇为github项目推荐，JsBridge就如他的名字一样是Android和网页的桥梁，可以使安卓的开发效率和程序移植性更高，Android中更多的使用h5，所以大家也有必要多多了解一下。

#工具

#1 [【互联快谈】Android你应该知道的调试神器----adb](http://m.pstatp.com/group/6329108662216343810/?iid=5315415024&app=news_article&wxshare_count=1&tt_from=weixin&utm_source=weixin&utm_medium=toutiao_android&utm_campaign=client_share) 

作者：**互联快谈**   分享者： 冯先生

> 1、效果

> 2、介绍

> 3、使用

#2 [AndroidStudio 从基本到高级使用技巧 - DiyCode](http://sc.qq.com/fx/u?r=HLC8bxA) 

作者：**chenping**   分享者： 寂小桦

> 1、一些基础的使用技巧

> 2、进阶使用技巧

> 3、代码模板

> 4、插件

#Android系统安全知识

#1 [Android 6.0 运行时权限管理最佳实践](http://m.blog.csdn.net/article/details?id=52503533) 

作者：**yanzhenjie1003**   分享者： 张明云

> 本篇作者结合自身实践经验，对6.0运行时权限做了全面而详细的介绍与解答，希望可以帮助大家迅速深入的理解、掌握6.0新特性的权限管理。

#2 [Android内存泄漏的简单检查与分析方法](http://mp.weixin.qq.com/s?__biz=MzAxMzYyNDkyNA==&mid=2651332518&idx=1&sn=bcc31ed271efbdc7784c2b18bd046d33&scene=1&srcid=0908vrWOHzhOfspBV5CIdIA4#rd) 

作者：**黄希**   分享者： 育禧

> 一、什么是内存泄漏？

> 二、发现内存泄漏

> 三、分析内存泄漏（DDMS dump + MAT分析）

#3 [Android系统16ms之内能做些什么](http://mp.weixin.qq.com/s?__biz=MzIwNjQ1NzQxNA==&mid=2247483857&idx=1&sn=c74288a11914e5a4560d32e62f043d4a&scene=1&srcid=0914nAGPgMcsWJAWratujNhH#rd) 

作者：**朱志立**   分享者： 井方哥

> 1、16ms指的是什么？

> 2、从xml到display

> 3、关于VSYNC

> 4、双缓冲机制

> 5、三倍缓冲机制

> 6、如何优化16ms问题

> 7、参考文章

#4 [Android Framework 如何学习，如何从应用深入到 Framework？ - weishu 的回答](http://www.zhihu.com/question/46486807/answer/122513260) 

作者：**weishu**   分享者： 袁凯

> 本篇为知乎热门话题热门答案的推荐，有很高的学习价值。

#老僧长谈
 
#1 [有哪些值得关注的技术博客（Linux篇） - 知乎专栏·「学习编程」](http://zhuanlan.zhihu.com/p/22407435) 

作者：**路人甲 **   分享者： 董大为

> 1、前端技术博客推荐

> 2、Java技术博客推荐

> 3、Python技术博客推荐

> 4、Linux技术博客推荐

> 5、PHP技术博客推荐
 
#2 [Android 九年，我们需要学什么？](http://mp.weixin.qq.com/s?__biz=MjM5MjAwODM4MA==&mid=2650687242&idx=1&sn=a4ea8c1e5780eb2494b59cfa51b3c7c2&chksm=bea636d989d1bfcf9c751ba5fdbf55c980ac0d6c4d16c55cbfc467a3fb6d6bee86e229db7e2f&scene=1&srcid=0913wpiPrVMbt0mN3bra4Ksr#rd) 

作者：**CSDN移动**   分享者： 董大为



