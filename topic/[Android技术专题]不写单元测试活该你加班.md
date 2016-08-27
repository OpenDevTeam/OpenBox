&emsp;&emsp;笔者在项目中实际有写过单元测试的代码，也用过一些单元测试的框架，但对单元测试的理解都很浅显，直到有一次在InfoQ编辑徐川主导的微信群里面看了蘑菇街小创同学的分享，加深了我对单元测试的兴趣和理解，他针对android平台的单元测试写了一个系列的文章，从什么是单元测试、单元测试的意义、各种方法怎样做单元测试、单元测试和集成测试的区别、各种测试框架和开源库在写单元测试时如何很好地被使用、以及如何mock、在PC上运行需要依赖android设备环境的测试等方面都做了非常详细的介绍，下文中的很多观念都是看了他的文章吸收得来的。

&emsp;&emsp;坦诚的讲，本人目前只是深刻认识到了单元测试对于实际项目开发的意义，但对于单元测试技术的研究，还是要向小创同学多多学习。文末的参考资料贴出了他写的一系列关于单元测试的文章，非常值得阅读，还有他创建了一个android单元测试交流群，用于针对android平台单元测试技术的交流，如果有兴趣，可以关注他的公众号，里面有介绍如何加群：

![小创作](http://chriszou.com/assets/images/qrcode_for_xiaochuangzuo.jpg)

## 一、什么是单元测试？

为了测试某个类中的某一个方法能否正常工作，而写的测试代码。

单元的定义：代码中可度量的最小单元（函数/方法）；

是否正常工作：不同的输入对应的输出是否与预期一致。

## 二、单元测试有必要吗？

### 1 对是否有必要写单元测试的疑惑

- 没有价值：不做单元测试一样地开发，并没有什么问题（解释：）；

- 浪费时间：写单元测试需要大量的时间，还不如写具体的实现，具体的实现能看到明显的效果，但单元测试可能耽误正常的迭代进度；

- 无法测试：比如无返回值的方法、UI等；

### 2 不写单元测试会存在的一些问题：

- 要有足够的耐心：改一个参数，需要重新运行一遍程序；

- 没有足够的自信：每次提测和发布，心惊胆战，对自己写的程序没有信心；

- 要有足够的时间：必须要等到测试发现bug后才去改善；

- bug太多，程序很难稳定：可以看下你自己开发的应用，如果有做异常采集，上报的大多数异常问题，都是因为程序没有做好容错导致的，比如空指针、被除数为0、数组越界等；

### 3 单元测试能够解决的问题

- 效率：如果没有单元测试就必须把程序运行起来测试；运行一次单元测试，最多几分钟，cover得比较全面，相比于执行程序，效率高很多很多；

- 质量：对于每个最小单元，针对不同输入对应的输出有与预期做对比，能够减少因为参数导致的异常问题，同时提测和发布版本的时候，有信心；

- 提升设计能力：为了每个单元都可测，需要将每个方法拆得尽量独立，如果不拆得足够独立，就无法测试，间接可以提高程序设计能力；

- 代码重用：跑过单元测试的代码，稳定性能够得到保证，可以在其它项目或者项目重构时重复利用；

- 缩短测试周期：程序自测（开发人员写单元测试、手动跑基本功能、跑monkey都属于自测）可以提高产品提测的质量，避免返工；同时核心功能的稳定有助于缩短黑盒测试的周期。

## 三、哪些可以做单元测试？

- 任何方法都可以做单元测试；

- 从必要性来讲，针对UI相关的做单元测试必要性不大，并且很多东西需要主观判断；所以只针对Model和Control层做测试；

- 私有方法同样可以测试（反射，或者在测试时改为public方法），但非public方法是这个类的实现细节，其它类并不关心，不用测试；

## 四、关于单元测试的一些概念

### 1 分类

#### 按测试内容分：

- 功能测试：和UI无关，测试IO操作、算法、流程等；

- UI测试：测试UI交互逻辑，比如点击、登陆等；

#### 按是否依赖设备分：

- 不依赖Android设备，只需要运行在JVM上的；→真正的单元测试，执行快，效率高；

- 依赖Android设备（模拟器/真机），需要程序运行时状态信息的，比如获取磁盘空间、四大组件的上下文信息、异步任务、消息传递等；→其实是集成测试，需要运行整个程序，执行慢，效率低；

### 2 测试框架

#### 如果没有框架该如何做单元测试

- 自己写程序进行逻辑判断（麻烦、加入测试程序有bug怎么办？）；

- 在console中观察测试结果；

#### 测试框架能够提高测试效率

JUnit、Instrumentation test、Espresso、UI Automator、Robolectric、Appium、Robotium

- [JUnit](http://junit.org/junit4/)：能够直接在PC上执行；

- AndroidTest：需要依赖Android设备；

- [Robolectric](http://robolectric.org/)：在不需要依赖Android环境的前提下，实现在PC上直接运行Android的单元测试；

- [Robotium](https://github.com/RobotiumTech/robotium)：第三方UI测试框架；

- [Espresso](https://google.github.io/android-testing-support-library/docs/espresso/index.html)：Google退出的UI测试框架；

- [UI Automator](https://google.github.io/android-testing-support-library/docs/uiautomator/index.html)：流程的UI测试框架；

### 3 覆盖率

- 衡量单元测试质量，通过覆盖率测试，可以明确知道哪部分代码已经被单元测试覆盖到，哪部分没有进行单元测试；常用的单元测试插件有Emma、JaCoCo；

### 4 JUnit框架中的常用方法

- setUp/@Before：在每个单元测试方法执行之前调用；

- tearDown/@After：在每个单元测试方法执行后调用；

- setUpBeforeClass/@BeforeClass：在每个单元测试类运行前调用；

- tearDownAfterClass/@AfterClass：在每个单元测试类运行完成后调用。

- Junit3中每个测试方法必须以test打头，Junit4中增加了注解，对方法名没有要求，@Test就可以。

### 5 一个单元测试的流程

- setUp：设置前提条件，比如初始化；

- 执行动作：调用被测方法，并得到返回结果；

- 验证结果：验证获取的结果和预期是否一致；

### 6 关于Mock

&emsp;&emsp;在写单元测试的过程中，我们可能会发现需要和系统内的某个模块或系统外某个实体交互，而这些模块或实体在您做单元测试的时候可能并不存在，比如您遇到了数据库、遇到了驱动程序等。这时开发人员就需要使用mock技术来完成单元测试。

&emsp;&emsp;mock就是创建一个类的虚假的对象，在测试环境中，用来替换掉真实的对象，以达到两个目的：

- 验证这个对象的某些方法的调用情况，调用了多少次，参数是什么等等；

- 指定这个对象的某些方法的行为，返回特定的值，或者是执行特定的动作。

&emsp;&emsp;要使用mock技术，就需要使用mock框架，[Mockito](http://mockito.org/)和[Jmockit](http://jmockit.org/)是android平台两个常用的mock框架，其中Mockito不能mock static method和final class、final method，但Jmockit可以。

### 7 依赖注入在单元测试中的使用

&emsp;&emsp;上文中提到的Mock技术就是创建一个类的虚假的对象，在测试环境中用来替换掉真实的对象，但如何在测试环境下，将某个类替换成mock的对象就需要使用到依赖注入了，他的基本理念是，某一个类（比如说DataActivity），用到的内部对象（比如说DataModel）的创建过程不在DataActivity内部去new，而是由外部去创建好DataModel的实例，然后通过某种方式set给DataActivity。这种模式应用是非常广泛的，尤其是在测试的时候。常见的依赖注入框架有：[Roboguice](https://github.com/roboguice/roboguice)、[Dagger](http://square.github.io/dagger/)、[Dagger2](http://google.github.io/dagger/)。

&emsp;&emsp;在实际写单元测试的过程中，mock技术会经常用到，所有非常有必要熟悉其中一种依赖注入框架，关于依赖注入的详细解释可以参见[公共技术点之依赖注入](http://b.codekk.com/blogs/detail/54cfab086c4761e5001b253c)。

## 五、单元测试集成到Jenkins

- Jenkins上不需要任何改动，执行现有的gradle命令会自动执行单元测试，测试不通过会报编译错误；

## 六、说明

- 不要指望对某个方法的单元测试一次能够写得足够完美，单元测试也是需要持续迭代的（比如入参考虑得不全面、单元测试粒度没有足够细等）；

- 并不是所有针对源码级别写的测试代码都叫单元测试，针对具体某一个方法的测试叫单元测试，涉及到UI层面、必须要运行程序才能跑的测试叫集成测试，比如很多基于android平台的第三方UI测试框架；

- test和androidTest文件夹的区别：如果你是用Android Studio做开发，在创建工程的时候，src文件夹下会同时生成三个文件夹main、test、androidTest，其中test和androidTest是专门针对源码级别的白盒测试的，test文件夹用于写不依赖设备环境的单元测试，即直接在PC上即可运行的测试，特点是测试效率高；androidTest文件夹用于写需要在设备上才能运行的测试，比如测试依赖android API和设备环境的时候(context、IO操作、UI测试等)，就需要在这个文件夹下面写单元测试了，其特点是必须要编译生成APK后才能测试，效率低；

- 测试驱动开发（TDD）的这种软件开发方法提倡先写测试程序，再才编码实现具体的功能；

## 七、参考资料

- [Android Developer Site：Test Your App](https://developer.android.com/studio/test/index.html)

- [谷歌官方单元测试Demo](https://github.com/googlesamples/android-testing)

- [关于安卓单元测试，你需要知道的一切](http://chriszou.com/2016/06/07/android-unit-testing-everything-you-need-to-know.html)

- [Android单元测试在蘑菇街支付金融部门的实践](http://chriszou.com/2016/04/25/android-unit-testing-wechat-group-share.html)

- [Android单元测试: 首先，从是什么开始](http://chriszou.com/2016/04/13/android-unit-testing-start-from-what.html)

- [Android单元测试（二）：再来谈谈为什么](http://chriszou.com/2016/04/16/android-unit-testing-about-why.html)

- [Android单元测试(三)：JUnit单元测试框架的使用](http://chriszou.com/2016/04/18/android-unit-testing-junit.html)

- [Android单元测试（四）：Mock以及Mockito的使用](http://chriszou.com/2016/04/29/android-unit-testing-mockito.html)

- [Android单元测试（五）：依赖注入，将mock方便的用起来](http://chriszou.com/2016/05/06/android-unit-testing-di.html)

- [Android单元测试（六）：使用dagger2来做依赖注入，以及在单元测试中的应用](http://chriszou.com/2016/05/10/android-unit-testing-di-dagger.html)

- [Android单元测试（七）：Robolectric，在JVM上调用安卓的类](http://chriszou.com/2016/06/05/robolectric-android-on-jvm.html)

- [安卓单元测试(八)：Junit Rule的使用](http://chriszou.com/2016/07/09/junit-rule.html)

- [安卓单元测试（九）：使用Mockito Annotation快速创建Mock](http://chriszou.com/2016/07/16/mockito-annotation.html)

- [Android单元测试(十)：DaggerMock：The Power of Dagger2, The Ease of Mockito](http://chriszou.com/2016/07/24/android-unit-testing-daggermock.html)

- [安卓单元测试(十一)：异步代码怎么测试](http://chriszou.com/2016/08/06/android-unit-testing-async.html)

- [Android单元测试研究与实践](http://tech.meituan.com/Android_unit_test.html)

- [Android Testing Support Library](https://google.github.io/android-testing-support-library/)