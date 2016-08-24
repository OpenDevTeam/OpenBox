###这篇文章主要是给大家引入android里面的ORM框架——GreenDao。

##### 一.为什么要给大家介绍这个GreenDao呢？(Why)
+	1.先介绍一下ORM的概念，其全称叫做对象关系映射（Object Relation Mapping），是一种程序设计技术，用于实现面向对象编程语言里不同类型系统的数据之间的转换。从效果上说，它其实是创建了一个可在编程语言里使用的“虚拟对象数据库”。
面向对象是从软件工程基本原则(如耦合、聚合、封装)的基础上发展起来的，而关系数据库则是从数学理论发展而来的，两者之间是不匹配的，而ORM作为项目中间件形式实现数据在不同场景下数据关系映射，对象关系映射是一种为了解决面向对象与关系数据库存在的互不匹配的现象的技术，ORM就是这样而来的。

+ 
   2.之前做Web(Java EE)开发，也是一开始使用SQL语句操作数据库的增删改查，但是如果一个表中有上百个字段的时候，使用SQL语句就会很麻烦。于是Web中就引入了像Hibernate、ibatis等操作对象的数据库框架。
+   3.小编找到了目前的几个ORM框架：ORMLite、greendao、ormndroid、androrm、ActiveAndroid。这里主要介绍当下比较流行的两个框架：ormlite和GreenDao。
最开始现在stackoverflow输入sqlite orm关键字，搜索出了一些相关的文章：链接地址 ，你会发现很有趣的是，ormlite的作者Gray和greenDao的开发团队green robot同时出现在一些帖子中，向提问者推荐他们的框架。当然也会有一些热心的朋友推荐ormdroid。其实不论在stackoverflow上还是官网上，greenDao的团队的比较都是针对ORMLite的，可见，其主要的竞争对手，或许称互相促进者更合适，毕竟他们没有什么商业利益，完全开源。所以开源的ORM框架中，ormLite和greenDao是最火的。其它几种就不做比较，有兴趣的朋友可以到官网看下。下面对ORMLite和GreenDao做个简单的比较：

	ormlite
基于注解和反射的的方式,导致ormlite性能有着一定的损失(注解其实也是利用了反射的原理)

	优点：
文档较全面，社区活跃，有好的维护，使用简单，易上手。

	缺点：
基于反射，效率较低

	GreenDao

	官网中明确指明了其首要设计目标:
	+	Maximum performance (probably the fastest ORM for Android)：系能最大化
	+	Easy to use APIs：便于使用
	+	Highly optimized for Android：对于Android高度优化
	+	Minimal memory consumption：最小化内存开销
	+	Small library size, focus on the essentials：较小的文件体积，只集中在必要的部分上。

	优点：
效率很高，插入和更新的速度是sqlite的2倍，加载实体的速度是ormlite的4.5倍。官网测试结果：http://greendao-orm.com/features/
文件较小（<100K），占用更少的内存 ，但是需要create Dao，
操作实体灵活：支持get，update，delete等操作

	缺点：
学习成本较高。其中使用了一个java工程根据一些属性和规则去generate一些基础代码，类似于javaBean但会有一些规则，另外还有QueryBuilder、Dao等API，所以首先要明白整个过程，才能方便使用。没有ORMLite那样封装的完整，不过greenDao的官网上也提到了这一点，正是基于generator而不是反射，才使得其效率高的多。

	另外GreenDao支持Protocol buffers协议数据的直接存储 ，如果通过protobuf协议和服务器交互，不需要任何的映射。
Protocol Buffers协议：以一种高效可扩展的对结构化数据进行编码的方式。google内部的RPC协议和文件格式大部分都是使用它。
   
所以，GreenDao你值得拥有：

+	(1).使用起来真的很简单，完全面向对象，到底有多简单，请继续往下看。

*	(2).性能评测
	
	![](http://i.imgur.com/bnyTpeX.png)

*	(3).尤其是操作数据库频繁的时候，其优势更加明显。
#####	二.这个GreenDao到底是个啥玩意儿？(What)
greenDAO是一个可以帮助Android开发者快速将Java对象映射到SQLite数据库的表单中的ORM解决方案，通过使用一个简单的面向对象API，开发者可以对Java对象进行存储、更新、删除和查询。
简单的说就是两个特点：

* 1.数据库直接操作对象
* 2.自动生成代码

##### 三.如何在项目中使用GreenDao呢？(How)

*	1.引进GreenDao项目

 	Android studio :

	    compile 'org.greenrobot:greendao:2.2.1'
	    compile 'org.greenrobot:greendao-generator:2.2.0'

*	2.提取出的代码生成器(通用)的三个类：
	*	DBTable.java 
	*	DBField.java 
	*	DBCodeGenerate.java
		
	用于生成表、字段以及对应的对象，简单的说，可以理解为一个对象对应一个table,对面里面的属性对应table里面的字段。


DBTable.java

	源码见GitHub com\open\common\database\DBTable.java

DBField.java

	源码见GitHub com\open\common\database\DBField.java


DBCodeGenerate.java

	
	源码见GitHub com\open\common\database\DBCodeGenerate.java

*	3.用以上三个类生成代码，范例如下：
（注意：一定要在main方法中执行生成。）

      CodeGenerate.java

		源码见GitHub DataBaseDemo com\open\common\sdk\dbdemo\CodeGenerate.java

*	4.run上面这个main方法即可在E:/GreenDao/src生成得到如下代码类：
	
	![](http://i.imgur.com/0aAnHNl.png)


*	5.自己再写一个供自己使用的封装的类  DBHelper.java
	
		源码见GitHub com\open\common\sdk\dbdemo\DBHelper.java
*	6.使用示例：
	
	*	增：
		
			UserInfoDao userInfoDao = DBHelper.getInstance(this).getUserInfoDao();
		    userInfoDao.insert(userObj);	    
	*	删：
		
            UserInfoDao userInfoDao = DBHelper.getInstance(this).getUserInfoDao();
		    userInfoDao.delete(userObj);
	*	改：
			
			UserInfoDao userInfoDao = DBHelper.getInstance(this).getUserInfoDao();
			userInfoDao.update(userObj);
	*	查：
		
		    UserInfoDao userInfoDao = DBHelper.getInstance(this).getUserInfoDao();
			UserInfo user=userInfoDao.queryBuilder().unique(); 

       怎么样，是不是用起来很爽，UserInfoDao里面的方法肯定不止这四个，还有很多方便操作数据库的api，心动不如行动，赶快尝试一下吧~~

[官方网站地址](http://greenrobot.org/greendao/)


[Demo地址]（https://github.com/OpenDevTeam/OpenBox/tree/master/demo/greendao/greendao）
