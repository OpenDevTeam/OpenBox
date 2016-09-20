# 开发中遇到的问题

1. Actvity中,多个Fragment嵌套, 需要监听嵌套的Fragment中的内容变化, 如下图

  ![](http://obh9ec69s.bkt.clouddn.com/NestMessage.png)

  FragmentA需要监听NestFragment中view的变化

2. 数据预加载, 首页有大量数据需要请求网络, 而欢迎界面有1s停顿, 需要把首页发送的网络请求,提前到欢迎界面发送, 但要版本首页能收到请求数据后的回调

3. app架构上, 数据层和和视图层解耦

# 什么是EventBus?

本文的EventBus,是指greenrobot的 [EventBus](https://github.com/greenrobot/EventBus), 主要以EventBus3.0 讲解; EventBus事件总线, 用于简化Android程序内,各个组件,线程之间的事件传递; 订阅发布模式,将事件的接收者和发布者解耦,一旦publisher发出消息,subscribe自己按需改变; 我个人喜欢把它拿来和BroadCast比较; 整个流程示意图如下 ![](http://obh9ec69s.bkt.clouddn.com/EventBus-Publish-Subscribe.png)

Publisher: 事件发送者,事件产生的地方 Subscriber: 事件接收者, 接收处理事件的地方 每个Publisher发送事件, 通过EventBus把事件分发到各个Subscriber, 同时Pulisher和Subscriber之间保持透明<br>
整个流程很广播类似, Publisher发送事件,就类似我们发送一个广播, 在接收广播的地方,注册一下, 我们就可以接收广播发出来的事件, 然后就可以处理; Subscriber就类似广播处理器;

# 使用场景

1. 复杂逻辑下的对象传递
2. 函数的调用者与被调用者需要低耦合,或者框架设计之初,无法预料到的调用

eg. 上面的使用场景,在我们代码中时长出现的场景就是,监听器的传递,回调函数和各种Listener

# 怎么使用

1. 在gradle中添加依赖

  ```
  dependencies {
  compile 'org.greenrobot:eventbus:3.0.0'
  }
  ```

2. 注册和取消注册 在要接收消息的类中`register` `unregister`, 和广播的注册类似, 一般在activity的 onCreate 和 onDestory 方法中进行

  ```java
  EventBus.getDefault().register( this );
  EventBus.getDefault().unregister( this );
  ```

3. 申明处理消息的函数; 在接收消息的函数上,加上`@Subscribe`, EventBus是按函数参数的类型确认消息的接收者的, 此函数只能有且仅有一个参数;

  ```java
  @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = false)
  public void onEvent( TestEvent  testEvent ){    
     Log.e( "zy", ">>>> receiverEvent");
  }
  ```

  只需要在函数上加上 `@Subscribe` 注解即可, 此注解还可以带上额外的参数

  - `threadMode`<br>
    用于指定此函数运行的线程, 是一个Enum, 有4个常量, `MAIN` `BACKGROUND` `ASYNC` `POSTING`, 默认为`POSTING`<br>
    `ThreadMode.MAIN` 在主线程中运行<br>
    `ThreadMode.POSTING` 跟消息发送者在同一线程运行<br>
    `ThreadMode.BACKGROUND` 后台线程, 如果发送消息的线程就是后台线程,就直接执行; 如果不是, 则会把消息放在队列中,依次执行<br>
    `ThreadMode.ASYNC` 后台线程, 消息会在单独的线程中执行,用了线程池,多个消息会同时执行

  - `priority` 优先级, 值越小优先级越低,当有多个方法处理同一个消息时,处理的顺序,默认为0

  - `sticky` 是否接收黏性消息, 和黏性广播相同, 默认为false

4. 发送消息 所谓的消息,就只是一个java对象, 发送消息就是把这个对象,传递给处理消息的函数; EventBus消息和EventBus的对象实例有关, 用一个EventBus对象发送的消息,必须是用同一个EventBus对象注册的才能收到消息.

  ```java
  // 发送黏性消息
  EventBus.getDefault().postSticky( new TestEvent() );
  // 发送普通的消息
  EventBus.getDefault().post( new TestEvent() );
  ```

  发送的消息有2种, `sticky`黏性消息, 当消息发送出去之后,如果没有消息接收者处理这个消息,此消息会暂时存储在eventBus实例中, 当后面注册接受者时,如果合适的处理者, 将会把消息给处理者去处理;我个人喜欢用这个来做数据的预加载;

5. 提升性能, 增加编译时注解处理 由于android机器本身性能有限,一般不建议使用运行时注解,EventBus的注解声明为Runtime, 但它同时支持编译时注解和运行时注解, 当没配置编译时注解处理器时, 会自动通过反射查找运行时的注解;

  1. 添加注解处理器依赖

    ```java
    buildscript {
    ...
    dependencies {
    classpath 'com.android.tools.build:gradle:2.1.0'
    // 在最外层添加gradle的插件依赖
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'
    }
    ...
    }
    // 项目中 增加注解处理器插件
    apply plugin: 'com.neenbedankt.android-apt'
    dependencies {
    compile 'org.greenrobot:eventbus:3.0.0'
    // 添加注解处理器
    apt 'org.greenrobot:eventbus-annotation-processor:3.0.1'
    }
    apt {
    arguments {
    // 注解处理器 最终生成的java文件位置
    eventBusIndex "com.zy.test.MyEventBusIndex"
    }
    }
    ```

  2. 初始化EventBus时, 使用注解处理器生成的类文件

    ```java
    mEventBus = EventBus.builder().addIndex( new MyEventBusIndex() ).build();
    ```

    EventBus的消息和EventBus实例有关系, 自己配置的EventBus实例,一般需要用单例保存, 确保发送和接收消息的地方,使用的是同一个实例

# 关于其他的一些细节

- 消息处理者的继承<br>
  EventBus的消息处理者,是可以继承的, 父类中的消息处理器, 在子类中仍可使用; 这是一个比较好的功能, 比如通用的消息接收处理,我们在BaseActivity中声明一次, 子类都可以使用了; 此功能可以关闭, 在构建Eventbus实例时, 调用 `EventBus.builder().eventInheritance( false )` ; 官方的说法是关闭后可以提供20%的性能;

- 黏性消息<br>
  非常实用的功能, 我一般用来做预加载数据; 每种消息类型,最多存储一个黏性消息, 和黏性广播类似; 消息处理者. 声明为`sticky = true`, 依然可以接收普通消息

- 进程间的通讯<br>
  Eventbus的发送消息和消息处理是和Eventbus实例有关的, 是无法跨进程传递消息的; 如果涉及到进程间通讯, 还是要使用android系统的接口

# 对比

1. Boardcast<br>
  优点: 可以指定运行线程, 消息处理可继承, 代码简单, 消息处理可继承, 低延迟, 对消息数据无要求(不需要实现Parcelable或者Serializable接口) 缺点: 无法跨进程

2. LocalBroadcastManager<br>
  这个除了广播的低延迟外, Boardcast的缺点都有, 并且它还不能不能跨进程, 没有黏性广播

3. RxBus<br>
  RxBus并不是指某个框架, 泛指用Rxjava实现的,类似EventBus的功能; 一般使用PublishSubject 构建Rx对象, 使用ofType按区分事件类型 相比起来, Eventbus上手简单, 可继承, 有黏性事件; 而Rx可以有统一的异常处理, 可以对消息数据进行变换处理,但由于有RxJava本身的学习难度,不易上手
