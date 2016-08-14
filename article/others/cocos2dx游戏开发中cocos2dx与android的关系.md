## cocos2dx游戏开发中cocos2dx与android的关系 ##

这篇文章适合已经用cocos2dx开发过游戏（demo也可以）的人和对cocos2dx有兴趣的人阅读。

**简述：**cocos2dx开发的游戏由GLSurfaceView加c、c++代码生成的.so组成。前者可以当做android里的普通view使用，比如放在一个布局里，弹一个popwindow，指定大小等。后者是通过在一个游戏引擎(cocos2dx)里用c，c++代码按照引擎的游戏框架进行开发后通过ndk编译生成的。

下面我将通过演示如何在android端设置cocos2dx游戏界面的大小来说明cocos2dx引擎所运行的GLSurfaceView可以当成一个普通的view来使用。

当创建一个cocos2dx工程后，会生成android工程目录。可以看到主Activity是继承自Cocos2dxActivity的。我们从oncreate函数一路跟下去可以看到init()方法，如下：

	public void init() {
		
    	// FrameLayout
        ViewGroup.LayoutParams framelayout_params =
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                       ViewGroup.LayoutParams.FILL_PARENT);
        FrameLayout framelayout = new FrameLayout(this);
        framelayout.setLayoutParams(framelayout_params);

        ...

        // Cocos2dxGLSurfaceView
		this.mGLSurfaceView = this.onCreateView();
        // change to full screen, add by wangwenhuan
        this.mGLSurfaceView.setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // add to FrameLayout
        framelayout.addView(this.mGLSurfaceView);
		this.mGLSurfaceView.setCocos2dxRenderer(new Cocos2dxRenderer());
        
		...

        // Set framelayout as the content view
		setContentView(framelayout);
	}
    public Cocos2dxGLSurfaceView onCreateView() {
    	return new Cocos2dxGLSurfaceView(this);
    }

可以看到创建了一个FrameLayout作为Activity的布局，然后又创建了一个Cocos2dxGLSurfaceView的实例（继承自GLSurfaceView）加进去，并为其指定了一个实现GLSurfaceView.Renderer接口的实例Cocos2dxRenderer。我们可以看到Cocos2dxGLSurfaceView和Cocos2dxRenderer类里面的代码，默认情况会先执行Cocos2dxGLSurfaceView里的onSizeChanged系统回调，然后再执行Cocos2dxRenderer的初始化代码onSurfaceCreated，代码如下所示：


	/*
	 * This function is called before Cocos2dxRenderer.nativeInit(), so the
	 * width and height is correct.
	 */
	@Override
	protected void onSizeChanged(final int pNewSurfaceWidth, final int pNewSurfaceHeight, final int pOldSurfaceWidth, final int pOldSurfaceHeight) {
		if(!this.isInEditMode()) {
			this.mCocos2dxRenderer.setScreenWidthAndHeight(pNewSurfaceWidth, pNewSurfaceHeight);
		}
	}

	...


	public void onSurfaceCreated(final GL10 pGL10, final EGLConfig pEGLConfig) {
		Cocos2dxRenderer.nativeInit(this.mScreenWidth, this.mScreenHeight);
		this.mLastTickInNanoSeconds = System.nanoTime();
	}

实际设置界面宽高的代码是Cocos2dxRenderer.nativeInit(this.mScreenWidth, this.mScreenHeight)，这是一个native方法，它调用了.so里面的方法，在cocos2dx游戏引擎里面为整个游戏界面设置了一个尺寸。由代码可知默认的界面尺寸是match parent。如果要指定游戏界面的宽高，只需修改nativeInit方法的参数即可。至于如何修改，仁者见仁。我提供一种方法，可以新建一个Cocos2dxRendererEx继承自Cocos2dxRenderer，覆写onSurfaceCreated方法，新建两个成员变量 mScreenWidth 和 mScreenHeight，在创建Cocos2dxRendererEx时，就指定好mScreenWidth和mScreenHeight，覆写onSurfaceCreated方法时再将mScreenWidth和mScreenHeight传给nativeInit即可，当为Cocos2dxGLSurfaceView设置Render时就使用Cocos2dxRendererEx实例。代码如下：

	public Cocos2dxRendererEx(int w, int h) {
		// TODO Auto-generated constructor stub
		mScreenWidth = w;
		mScreenHeight = h;
	}

	@Override
	public void onSurfaceCreated(final GL10 pGL10, final EGLConfig pEGLConfig) {
		Cocos2dxRendererEx.nativeInit(this.mScreenWidth, this.mScreenHeight);
		...
	}

**游戏绘图原理即注意事项：**游戏引擎里的绘图是在gl线程里进行的(GLSurfaceView在setRenderer时会创建一个线程，我称作gl线程)。该线程会不停的执行onDrawFrame这个方法，在onDrawFrame这个方法里我们可以看到它调用了Cocos2dxRenderer.nativeRender()这个native方法用来调用.so（游戏引擎）里的方法来绘图。代码如下：

	public void onDrawFrame(final GL10 gl) {
		...
		Cocos2dxRenderer.nativeRender();

	}
绘图方面我们需要注意：activity中的view一般是在ui线程里绘制的(surfaceView除外)，跟android一样，cocos2dx中如果不在gl线程里做一些图形相关的操作就回出错，比如图片区域显示黑块，图片显示不出来等。cocos2dx中除此之外，内存引用计数的操作(比如addchild,removechild,attain,release)也要在gl线程进行，不然会发生内存错误。

那么，什么情况下会犯上述错误呢，一种很常见的情境如下:

**cocos2dx与android端的通信**一般采用观察者和jni的形式来实现。比如在cocos2dx里显示一个天气预报的界面，点击城市选项弹出一个popwindow用来选择城市,注意因为popwindow是android端的窗口类，所以应该在ui线程显示。当选完城市后要求cocos2dx里面的界面刷新。一般会在coco2dx中注册一个观察者，观察一个对象(一般用一个字符串指定)，绑定一个回调函数，当通知者执行notify操作通知指定观察者时，回调函数就会被执行。一种容易想到的方法是在popwindow中选完城市后dismis时通过jni调用c代码，在c代码里调用notify方法，在回调里做相关界面操作。这种做法就会出现上述问题，因为此时的notify虽然执行的是c里的代码，但是是在ui线程执行的而不是gl线程，正确做法是，可以用scheduleonce，即在下一祯update里执行一次。cocos2dx的node（游戏引擎中的节点概念）的update函数是运行在gl线程的，这个过程的根源是GLSurfaceView的onDrawFrame是在gl线程调用的，在onDrawFrame里会调用jni方法nativeRender,在里面会执行一次所有node的update方法。

**cocos2dx游戏是如何结束的？**cocos2dx游戏退出的方法比较暴力。大家都知道android里的activity退出一般调用finish，把进程的结束权交给系统决定，但是cocos2dx虽然也是一个activity，但官方给出的退出方法是调用导演类Director的end()方法，在end方法里我们可以看到，它实际上是修改一个主循环里的标志，从而让cocos2dx主循环退出，最后调用的还是exit(0),没错，就是exit(0),直接退出整个进程。当然你也可以调用finish来退出，但不建议这么做，因为cocos2dx有很多的单例类，整个进程只初始化一次的，如果你没有处理好，刚退出再点进来是会有问题的。比如每次执行cocos2dxactivity的oncreate时都会创建一个soundpool的线程对象，如果在ondestroy时没有处理它，假设进程一直存在，一直oncreate->ondestroy的话，soodpool线程的数量会一直累加的。

**cocos2dx与android内容管理的差别：**cocos2dx的内存管理和android的内存管理有相似之处，都有引用计数的概念，不同的是android只能依赖gc来回收，而时间无法控制，cocos2dx中是每祯都会回收引用计数为0的内存(前提是你把他们加进了自动回收池，即用cocos2dx提供的对象创建方法create),cocos2dx中还有个功能对解决内存问题非常实用，就是可以用dump接口来打印出当前图片占用的内存状态，哪张图，占用多少内存一目了然，哪些地方内存没有释放掉一下就看出来了。其实cocos2dx内存管理方面最好是对着代码讲，引用计数的概念不是那么好理解的，有兴趣的话，下次再详细讲一下吧。
