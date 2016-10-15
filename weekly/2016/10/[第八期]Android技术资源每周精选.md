#[第8期]Android技术资源每周精选


##摘要
>本周推出自定义View、Android开发框架和增量更新、神器Vysor的实现原理以及技术学习方法和相关感悟。

#View

###1 [Android仿微信通讯录：悬停头部分组列表](http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650237310&idx=1&sn=5a97e0598d72eecda69a08ca40bed670&chksm=88639811bf1411075f33b840c2bd3c3f5443e644d3aecaa9a59c3290deb63a7deddcfd0cd8ff&mpshare=1&scene=1&srcid=1011Lj2laQaIVmUBpg77ZpX1#wechat_redirect) 

作者：**郭霖**   分享者： 郑郑

> 1、使用ItemDecoration

> 2、ItemDecoratuon方法介绍

> 3、一些ItemDecoration补充

###2 [Android 事件分发机制详解](http://www.diycode.cc/topics/352) 

作者：**sloop**   分享者： 寂小桦

> 1、常见事件

> 2、事件分发、拦截与消费

> 3、VIewGroup 的事件分发流程又是如何的呢？

> 4、核心要点

> 4、总结

###3 [Attr、Style和Theme详解](http://www.jianshu.com/p/dd79220b47dd?utm_campaign=maleskine&utm_content=note&utm_medium=reader_share&utm_source=weixin) 

作者：**CPPAlien**   分享者： 宗恒

> 1、Attr的定义

> 2、Style的使用

> 3、Theme的使用

> 4、Attr的获得方法

> 5、总结

###4 [Android自定义控件--仿安全卫士中的一键加速](http://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650821214&idx=1&sn=d0c120c9dc21669c7acac9c250991e3c&chksm=80b786c0b7c00fd62c7be336df91885614e0184b153350602bcd1ff39618d6d80cc7a6999e20&mpshare=1&scene=1&srcid=10145QHCusL06gHaO9Tx0JEH#wechat_redirect) 

作者：**鸿洋**   分享者： 张浩

> 1、行动由来

> 2、设计剖析

> 3、核心代码

#开发框架

###1 [OkGo，一个专注于让网络请求更简单的框架，与RxJava完美结合，比Retrofit更简单易用](http://www.jianshu.com/p/6aa5cb272514) 

作者：**廖子尧**   分享者： HockGod

> 1、OkGo的优势

> 2、用法

> 3、自定义Convert使用


###2 [Android路由框架设计与实现](http://www.sixwolf.net/blog/2016/03/23/Android%E8%B7%AF%E7%94%B1%E6%A1%86%E6%9E%B6%E8%AE%BE%E8%AE%A1/) 

作者：**beautifulSoup**   分享者： 胖胖

> 1、背景

> 2、框架整体设计

> 3、路由格式

> 4、路由表的初始化

> 5、总结 

###3 [浅析ButterKnife](http://mp.weixin.qq.com/s?__biz=MzI1NjEwMTM4OA==&mid=2651232205&idx=1&sn=6c24e6eef2b18f253284b9dd92ec7efb&chksm=f1d9eaaec6ae63b82fd84f72c66d3759c693f164ff578da5dde45d367f168aea0038bc3cc8e8&mpshare=1&scene=1&srcid=1014X59cuALAaBbyi38oqTqi#wechat_redirect) 

作者：**QQ音乐技术团队**   分享者： 育禧

> 1、使用依赖注入框架

> 2、初探ButterKnife

> 3、JavaPoet生成代码

> 4、Java Annotation Tool

> 5、执行注解处理器

> 6、总结

#Android热更新与补丁

###1 [QFix探索之路——手Q热补丁轻量级方案](http://mp.weixin.qq.com/s?__biz=MzA3NTYzODYzMg==&mid=2653577964&idx=1&sn=bac5c8883b7aaaf7d7d9ea227f200412&chksm=84b3b0ebb3c439fd56a502a27e1adc18f600b875718e537191ef109e2d18dae1c52e5e36f2d9&mpshare=1&scene=1&srcid=1013bDiBm3IqclasgZlnmJ0g#wechat_redirect) 

作者：**叶哲恺**   分享者： 咫尺星尘

> 1、热补丁方案及手Q上的使用

> 2、性能无法提升，需要改变

> 3、重新分析”unexpected DEX”异常

> 4、......

###2 [Android 增量更新完全解析 是增量不是热修复](http://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650821209&idx=1&sn=6821835111ce0ab4452866efaf4d78f2&chksm=80b786c7b7c00fd19663782b9ac51dea1c881456a3933e6848aa7d24aec0eb56066f504022f9&mpshare=1&scene=1&srcid=1011bId4pdj4RUenvDJSvmBe#wechat_redirect) 

作者：**鸿洋**   分享者： 张明云

> 1、概述
 
> 2、增量文件的生成与合并
 
> 4、客户端的行为
 
> 5、增量更新后安装

> 6、总结


#前沿技术

###1 [Android状态栏着色实践](http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650237284&idx=1&sn=6e3b30cc63d675bae8cb3e7c9563384e&chksm=8863980bbf14111d1e3ac9522ae56433a3486d58e7ad5b58c1894270a126823983fc3a6017ac&mpshare=1&scene=1&srcid=1009sLFkbEHMoTOgXpYIvkB1#wechat_redirect) 

作者：**郭霖**   分享者： Amore

> 本篇分享了状态栏着色的三种方案，针对不同的情况进行了具体分析。

> 1．第一种方案

> 2．第二种方案

> 3．第三种方案

###2 [Android 性能优化典范（六）](http://mp.weixin.qq.com/s?__biz=MzA4MzEwOTkyMQ==&mid=2667376274&idx=1&sn=b35396dc9e2749da076b94979c9c5424&chksm=84f33fdcb384b6cac52b98a29cf379178afe69d6761e92b4b7ad35627c722ac3dbf6887a28c7&mpshare=1&scene=1&srcid=1011ObiSQI9cClmRpq89H8XD#wechat_redirect) 

作者：**胡凯**   分享者： 张明云

> 1．App Launch time 101

> 2．App Launch Time & Activity Creation

> 3．App Launch Time & Bloated Application Objects

> 4．App Launch Time & Theme Launch Screens

> 5．Smaller APKs: A Checklist

> 6．VectorDrawable for smaller APKs

###3 [vysor的实现原理是什么？](http://www.zhihu.com/question/46229570/answer/124043146) 

作者：**知乎问答**   分享者： 咫尺星尘

> vysor这是一款被大家称作神器的工具，在chrome安装一个插件无需root就能连接android，实现同步手机操作和投影显示。
android屏幕共享和远程协助这类功能的应用都是什么实现原理？


###4 [微信终端跨平台组件 mars 系列（一） - 高性能日志模块xlog](http://mp.weixin.qq.com/s?__biz=MzAwNDY1ODY2OQ==&mid=2649286403&idx=1&sn=7b258bc931150b93d282f2c64a4a7d08&chksm=8334c381b4434a97920cd5d2a443f6c8c5c159a23bb5baf11a2018db9a2bc1b1a634a3eb0870&scene=0#wechat_redirect) 

作者：**garryyan**   分享者： 育禧

> 1．常规方案

> 2．进一步思考

> 3．mars 的日志模块xlog

> 4．mmap

> 5．压缩

> 6．总结

###5 [技术盒子丨当你喜刷刷时，你可知为何朋友圈能这么流畅？](https://zhuanlan.zhihu.com/p/22934938?from=timeline&isappinstalled=0) 

作者：**Larry**   分享者： 宗恒

> 1．那么，朋友圈如何保障流畅？

> 2．胖瘦数据分离：胖子给我闪一边

> 3．快速渲染：旧瓶装新酒

#学习与感悟

###1 [冰冻三尺非一日之寒-博客篇](http://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650821177&idx=1&sn=b0f58bbcb9338b85578e4b2fddb87d62&chksm=80b786a7b7c00fb1ab2bcd19bfd1494c4add73eefc829640232b0615d5f81cab2da3ddfcec42&mpshare=1&scene=1&srcid=1010ZLwr5cXsmpBn483svOkR#wechat_redirect) 

作者：**鸿洋**   分享者： 张明云

> 1、感悟

> 2、历史

> 3、问答

> 4、未来

###2 [Google Interview University - 坚持完成这套学习手册，你就可以去 Google 面试了](https://zhuanlan.zhihu.com/p/22881223) 

作者：**掘金翻译计划**   分享者： 张明云

> 1、关于 Google

> 2、相关视频资源

> 3、面试过程 & 通用的面试准备

> 4、......

###3 [从概念到底层技术，一篇文看懂重塑世界的区块链](http://mp.weixin.qq.com/s?__biz=MjM5MDE0Mjc4MA==&mid=2650994345&idx=1&sn=0999eb0d4e2b0819751a6b73334c2b0c&chksm=bdbf0efa8ac887ec4eab294794fec20f6883f181f25eb3bf0c41cb1eb27c62a0789e47251772&mpshare=1&scene=1&srcid=1011ye7sos9wDVG1Rg5SyZPq#wechat_redirect) 

作者：**赵铭、陈浩**   分享者： 井芳哥

> 1、重塑世界的区块链技术

> 2、如何从技术角度理解区块链？


###4 [一名程序员十年技术之路的思考与感悟](http://mp.weixin.qq.com/s?__biz=MjM5MTM0NjQ2MQ==&mid=2650140065&idx=1&sn=769c47d1e45a78830c00235bc4633506&mpshare=1&scene=1&srcid=1012buzVQwl5ig2SHQD7wn3R#wechat_redirect) 

作者：**赵建春**   分享者： 宗恒

> 1．借助导师、同事资源。这样可以让自己快速解决遇到的问题，少走很多弯路

> 2．系统化的学习工作所需要的是基础知识，而不是到百度或谷歌查资料解决

> 3．除了工作中需要的新知识外，建议在工作一段时间后再回头系统性学习相关专业基础知识

> 4．借助平台学习成长

> 5．如果可以对自己进行细分定位会更好






