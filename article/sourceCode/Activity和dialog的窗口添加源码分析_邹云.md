本文是最近看Window有关源码的部分记录和分析, 可能有差错; 另有很多代码,比较枯燥

先说部分结论

1. Window是一个抽象类, 具体的实现是PhoneWindow
2. android系统中, 每个界面,对应着一个window; 但其实在android系统中window也是一个抽象的概率,它是以view的形式存在; 在使用中, 无法直接访问Window, 只能通过WindowManager才能访问Window; 每个Window都对应一个View和一个ViewRootImpl, ViewRootImpl是连接Window和WMS的桥梁, WMS的一些消息,通过ViewRootImpl转发给View;
3. WindowManager继承自ViewManager(间接证明Window其实对应的是View?)，常用的只有三个方法：addView、updateView和removeView
4. 各种Window的不同, 主要是 token及type的不同
5. app中控制window, 是通过`WindowManager.LayoutParams`去控制, eg: 通过`x`,`y`,`gravity`去控制位置...

--------------------------------------------------------------------------------

# Activity 的window添加

Activity创建之后, 在ActivityThread中 调用 Activity#attach, 进行一些初始化操作
<pre><code>
final void attach(Context context, ActivityThread aThread,
		Instrumentation instr, IBinder token, int ident,
	        Application application, Intent intent, ActivityInfo info,
	        CharSequence title, Activity parent, String id,
	        NonConfigurationInstances lastNonConfigurationInstances,
	        Configuration config, String referrer, IVoiceInteractor voiceInteractor) {
	    attachBaseContext(context);
	    ....
	    //创建对应的Window 并设置callback, 其实为PhoneWindow
	    mWindow = new PhoneWindow(this);
	    mWindow.setCallback(this);
	    mWindow.setOnWindowDismissedCallback(this);
	    ....
	    // 设置Window的WindowManager, 对Window的mWindowManager赋值,
	    // 事实上, Window中 并未使用传递进去的windowManager, 而是在此方法中 调用WindowManagerImpl.createLocalWindowManager 重新创建了一个
	    mWindow.setWindowManager(
	            (WindowManager)context.getSystemService(Context.WINDOW_SERVICE),
	            mToken, mComponent.flattenToString(),
	            (info.flags & ActivityInfo.FLAG_HARDWARE_ACCELERATED) != 0);
	    if (mParent != null) {
	        mWindow.setContainer(mParent.getWindow());
	    }
	    // 把window对象中的 windowManager 关联到 Activity的 mWindowManager
	    mWindowManager = mWindow.getWindowManager();
	    ...
	  }
</code></pre>

在Activity#attach中, 可以看出创建了一个PhoneWindow对象, 并且通过`context.getSystemService(Context.WINDOW_SERVICE)`设置了WindowManager对象, 还通过Window#getWindowManager对Activity的mWindowManager赋值, 这样 Activity的Window WindowManager就关联起来了;

接着看上面代码,里面会通过context.getSystemService(Context.WINDOW_SERVICE)拿到一个windowManager对象, 这个window对象是什么呢? 如果对Context有过研究的话, 可以知道Context的实际对象是ContetImpl, 其中所有通过 getSystemService返回的对象, 都是通过static的代码块静态添加的, 只需找WINDOW_SERVICE注册的地方(在6.0中 注册的代码 已经提取到'android.app.SystemServiceRegistry#registerService'中了)

<pre><code>
registerService(Context.WINDOW_SERVICE, WindowManager.class,
               new CachedServiceFetcher<WindowManager>() {
           @Override
           public WindowManager createService(ContextImpl ctx) {
               return new WindowManagerImpl(ctx.getDisplay());
           }});
</code></pre>

可以看见,实际的对象是WindowManagerImpl; (另外 Activity本身重写了getSystemService方法,如果使用android.app.Activity#getSystemService, 返回的其实不是这个对象, 下面会说到);

上面注释说到, android.view.Window#setWindowManager中, 并未使用传递进去的WindowManager,而创建了一个新对象, 可以看一下代码
<pre><code>
public void setWindowManager(WindowManager wm, IBinder appToken, String appName,
            boolean hardwareAccelerated) {
        // activity把token传进来, 保存在Window.mAppToken
        mAppToken = appToken;
        mAppName = appName;
        mHardwareAccelerated = hardwareAccelerated
                || SystemProperties.getBoolean(PROPERTY_HARDWARE_UI, false);
        if (wm == null) {
            wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        //创建了一个新的WindowManager, 并且把this 传递给了WindowManager
        mWindowManager = ((WindowManagerImpl)wm).createLocalWindowManager(this);
    }
</code></pre>

而在android.view.WindowManagerImpl#createLocalWindowManager中, 可以明显看出

<pre><code>
// Context.getSystemService 获得的WindowManager实例, 是没有parentWindow的
public WindowManagerImpl(Display display) {
        this(display, null);
    }

private WindowManagerImpl(Display display, Window parentWindow) {
    mDisplay = display;
    mParentWindow = parentWindow;
}

public WindowManagerImpl createLocalWindowManager(Window parentWindow) {
    // 将传递的 window对象保存, 对于activity来说,会将PhoneWindow对应的对象传入, 而对于 Context.getSystemService 获得的WindowManager实例, 是没有parentWindow的
    return new WindowManagerImpl(mDisplay, parentWindow);
}
</code></pre>

window时我们看见的窗口,接下来看在Activity的window是怎么显示出来, 看`Activity.makeVisiable`

<pre><code>
void makeVisible() {
    if (!mWindowAdded) {
        ViewManager wm = getWindowManager();
        // windowManger中 添加window的根view( 即 decorView)
        wm.addView(mDecor, getWindow().getAttributes());
        mWindowAdded = true;
    }
    mDecor.setVisibility(View.VISIBLE);
}
</code></pre>

上面已经说过, WindowManager对应的实例是WindowManagerImpl, 接着看android.view.WindowManagerImpl#addView

<pre><code>
 private final WindowManagerGlobal mGlobal = WindowManagerGlobal.getInstance();

@Override
public void addView(@NonNull View view, @NonNull ViewGroup.LayoutParams params) {
    applyDefaultToken(params);
    mGlobal.addView(view, params, mDisplay, mParentWindow);
}
</code></pre>

代码很简单, 最后调用的是 WindowManagerGlobal#addView, 并且WindowManagerGlobal是一个单例, 看下android.view.WindowManagerGlobal#addView的代码

<pre><code>
public void addView(View view, ViewGroup.LayoutParams params,
            Display display, Window parentWindow) {
              ...
         // 首先获得 layoutParams
        final WindowManager.LayoutParams wparams = (WindowManager.LayoutParams) params;
        if (parentWindow != null) {
            // parentWindow是 WindowManagerImpl的成员变量mParentWindow;
            // 对于 activity来说, 即在mWindow.setWindowManager中 调用WindowManagerImpl.createLocalWindowManager(Window)去创建 mWindowManager实例时, 传入的phoneWindow对象
            // 调用下面这个方法, 会调整 WindowManager.LayoutParams, 主要是 设置 token 和硬件加速的标志位
            parentWindow.adjustLayoutParamsForSubWindow(wparams);
        } else {
            // If there's no parent, then hardware acceleration for this view is
            // set from the application's hardware acceleration setting.
            final Context context = view.getContext();
            if (context != null
                    && (context.getApplicationInfo().flags
                            & ApplicationInfo.FLAG_HARDWARE_ACCELERATED) != 0) {
                wparams.flags |= WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
            }
        }

        ViewRootImpl root;
        View panelParentView = null;
        ....
        // ViewRootImpl是一个很重要的类, 是ViewParent的子类, 可以说是Window和WMS之间的桥梁, 所有和View有关的Evnet,layout,draw,都在底层产生后通过WMS->ViewRootImpl传递给view
        root = new ViewRootImpl(view.getContext(), display);

        view.setLayoutParams(wparams);

        mViews.add(view);
        mRoots.add(root);
        mParams.add(wparams);

        ...
        root.setView(view, wparams, panelParentView);
        ...
}
</code></pre>

在WindowManagerGlobal中, 会为每一个window创建对应的ViewRootImpl, 并把对应的 decorView, ViewRootImpl 和 WindowManager.LayoutParams 保存, 最后把decorView 通过android.view.ViewRootImpl#setView添加进WMS 妈蛋,好累啊, 终于快到WMS了

接着看一下android.view.ViewRootImpl#setView

<pre><code>
public void setView(View view, WindowManager.LayoutParams attrs, View panelParentView) {
      ...
      // 把Window对应的View保存下来, 对应的其实就是 PhoneWindow的 mDecorView
      mView = view;
      ...
      // 调用WMS后返回的结果
     int res; /* = WindowManagerImpl.ADD_OKAY; */

     // Schedule the first layout -before- adding to the window
     // manager, to make sure we do the relayout before receiving
     // any other events from the system.
     // 请求 layout
     requestLayout();
    ....

    // 调用 WMS添加window, 并返回一个结果 用于判定添加的结果
    res = mWindowSession.addToDisplay(mWindow, mSeq, mWindowAttributes,
                            getHostVisibility(), mDisplay.getDisplayId(),
                            mAttachInfo.mContentInsets, mAttachInfo.mStableInsets,
                            mAttachInfo.mOutsets, mInputChannel);
        ......                              

    // 根据返回的结果 判断是否添加成功, 没成功 就throw exception, 经常看见的services打开dialog的异常 就在这儿产生
    if (res < WindowManagerGlobal.ADD_OKAY) {
        mAttachInfo.mRootView = null;
        mAdded = false;
        mFallbackEventHandler.setView(null);
        unscheduleTraversals();
        setAccessibilityFocus(null, null);
        switch (res) {
            case WindowManagerGlobal.ADD_BAD_APP_TOKEN:
            case WindowManagerGlobal.ADD_BAD_SUBWINDOW_TOKEN:
                throw new WindowManager.BadTokenException(
                        "Unable to add window -- token " + attrs.token
                        + " is not valid; is your activity running?");
            case WindowManagerGlobal.ADD_NOT_APP_TOKEN:
                throw new WindowManager.BadTokenException(
                        "Unable to add window -- token " + attrs.token
                        + " is not for an application");   
            .....
          }                          
     ......
}
</code></pre>

ViewRootImpl里面, 首先会把window对应的decorView存一份, 然后通过mWindowSession向WMS发消息, mWindowSession是通过WindowManagerGlobal的静态方法获得, 前面说过,这玩意是一个单例, 在哪儿都可以拿到; android本身是一个CS架构, 调用WMS,需要先获取WMS对应的client, 而client端的获取,就是同 WindowManagerGlobal.initialize()进行, 连通WMS的服务端; android.view.WindowManagerGlobal#getWindowSession, 是通过getWindowManagerService().openSession()获得, 最后返回的是com.android.server.wm.Session, 而mWindowSession.addToDisplay最终调用的会是 com.android.server.wm.Session#addToDisplay, 最终会还是会调用com.android.server.wm.WindowManagerService#addWindow; 这一段在WMS Server端的代码也比较多, 就不贴代码了, 最后在WindowManagerService#addWindow, 会对WindowManager.LayoutParams做一些检验 并返回值, 下面会说到;

上面说过事件是通过WMS传递给ViewRootImpl,然后传递给View,Activity, 具体事件在ViewRootImpl的分发过程, 可以看这篇博客[here](http://blog.csdn.net/singwhatiwanna/article/details/50775201)
[阅读原文](https://github.com/OpenDevTeam/OpenBox/blob/master/topic/%5BAndroid%E6%8A%80%E6%9C%AF%E4%B8%93%E9%A2%98%5DAPK%E7%98%A6%E8%BA%AB%E7%9C%8B%E8%BF%99%E4%B8%80%E7%AF%87%E6%96%87%E7%AB%A0%E5%B0%B1%E5%A4%9F%E4%BA%86.md)

# dialog的Window创建过程

1.dialog的窗口创建显示过程,其实与activity相似, 最后对应的Window其实也是PhoneWindow 首先直接看Dialog的构造方法

<pre><code>
    Dialog(@NonNull Context context, @StyleRes int themeResId, boolean createContextThemeWrapper) {
        ......
        // 用getSystemService获取 WindowManger实例
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 直接创建一个 PhoneWindow的实例
        final Window w = new PhoneWindow(mContext);
        mWindow = w;
        w.setCallback(this);
        w.setOnWindowDismissedCallback(this);
        w.setWindowManager(mWindowManager, null, null);
        w.setGravity(Gravity.CENTER);
        mListenersHandler = new ListenersHandler(this);
    }
</code></pre>

代码很明显, 首先获取windowManager实例, 然后创建window对象,然后在设置callback,对PhoneWindow的实例设置WindowManager;(这里有一点和Activity不同, Activity是调用Window的setWindowManager后, 把Window对应的WindowManger获取,然后赋值给自己的变量Activity#mWindowManger, 而dialog没这个操作; dialog对应的Window对象,就只是传入的Context实例去getSystemService所得); 接下来,就是设置dialog的view,和Activity一样,都是调用setContentView, 看下代码

<pre><code>
public void setContentView(@LayoutRes int layoutResID) {
        mWindow.setContentView(layoutResID);
    }
</code></pre>

代码很简单,直接调用的`mWindow.setContentView`, 把对应的视图,添加到PhoneWindow的DecorView中去; 接下来就是显示了, 看一下Dialog#show方法

<pre><code>
    public void show() {
        ...
        // 调用Dialog#onCreate方法, 通常也是在onCreate方法中,改变Window.LayoutParams, 从而调整dialog的宽高, 动画, 显示位置
        onStart();
        .......
        mDecor = mWindow.getDecorView();
        WindowManager.LayoutParams l = mWindow.getAttributes();
        // 调用WindowManager去显示
        mWindowManager.addView(mDecor, l);
        mShowing = true;

    }
</code></pre>

显示的代码逻辑,和Activity显示的一样, 就是调用WindowManger#addView方法 在这儿就有一个开发中常遇到的问题, Activity可以直接调用show()显示一个dialog, 而Service调用会报错,解决这个问题也很简单,一般就是声明使用系统Window的权限, 然后在设置dialog的type, dialog.getWindow().setType(LayoutParams.TYPE_SYSTEM_ERROR)<br>
一般错误如下<br>
![](http://obh9ec69s.bkt.clouddn.com/Application_dialog_error.png)

那产生这种情况,具体原因是什么呢?<br>
首先说下结论,产生这个情况是因为WindowManger实例的缘故;我们看代码,分析具体的原因,首先肯定传入的Context对象不同,而context的不同,会影响什么呢, 在上面dialog创建过程中说过, dialog中的WindowMange对象, 是Context#getSystemService获得, 在Activity获取WindowManger时候,我提到过Activity重写了getSystemService方法, 看一下Activity#getSystemService的代码

<pre><code>
@Override
    public Object getSystemService(@ServiceName @NonNull String name) {
        ...
        // 获取WMS时, 直接返回自身的windowManger
        if (WINDOW_SERVICE.equals(name)) {
            return mWindowManager;
        }
        ...
        return super.getSystemService(name);
    }
</code></pre>

在传入的Context是Activity实例时, 获取到的WindowManger, 就是Activity自身的mWindowManager,而其他的context对象,获取的都是默认的WindowManger; 这2个WindowManger实例有什么不同呢, 上面也说到过,Activity的WindowManger创建过程中, 把其对应的PhoneWindow实例传给了WindowManager#mParentView, 最后在调用WindowManagerGlobal#addView时, 会调用`parentWindow.adjustLayoutParamsForSubWindow(wparams)`, 从而调整了LayoutParams, 设置了window的token;

下一个问题,调用dialog.getWindow().setType(LayoutParams.TYPE_SYSTEM_ERROR)为啥就不报错了呢?<br>
首先,看一下这个错误在哪儿抛出来的, 在上面的Activity的window显示流程中,说道window创建过程,最后会调用android.view.ViewRootImpl#setView, 这个方法中, 会调用WMS服务端, 并且返回一个值, 根据返回值,确定WMS服务端是否添加成功, 如果返回值不是`WindowManagerGlobal.ADD_OKAY`, 就抛出的异常, 其中有一个异常就是;<br>
接着在看WMS服务端,什么时候会返回这个, 看一下`com.android.server.wm.WindowManagerService#addWindow`

<pre><code>
//com.android.server.wm.WindowManagerService#addWindow
public int addWindow(Session session, IWindow client, int seq,
            WindowManager.LayoutParams attrs, int viewVisibility, int displayId,
            Rect outContentInsets, Rect outStableInsets, Rect outOutsets,
            InputChannel outInputChannel) {
              ...
        // 检查权限, mPolicy是 com.android.server.policy.PhoneWindowManager 的实例
        int res = mPolicy.checkAddPermission(attrs, appOp);
        if (res != WindowManagerGlobal.ADD_OKAY) {
            return res;
        }
        ...
        // token为null时, 对type进行判断
        WindowToken token = mTokenMap.get(attrs.token);
        if (token == null) {
            if (type >= FIRST_APPLICATION_WINDOW && type <= LAST_APPLICATION_WINDOW) {
                Slog.w(TAG, "Attempted to add application window with unknown token "
                      + attrs.token + ".  Aborting.");
                return WindowManagerGlobal.ADD_BAD_APP_TOKEN;
            }
            ...
          }
        .....
}

//com.android.server.policy.PhoneWindowManager#checkAddPermission
com.android.server.policy.PhoneWindowManager#checkAddPermission{
    int type = attrs.type;
    ....
    // 根据类型 判断权限
    String permission = null;
    switch (type) {
        case TYPE_TOAST:
            // XXX right now the app process has complete control over
            // this...  should introduce a token to let the system
            // monitor/control what they are doing.
            outAppOp[0] = AppOpsManager.OP_TOAST_WINDOW;
            break;
        case TYPE_DREAM:
        case TYPE_INPUT_METHOD:
        case TYPE_WALLPAPER:
        case TYPE_PRIVATE_PRESENTATION:
        case TYPE_VOICE_INTERACTION:
        case TYPE_ACCESSIBILITY_OVERLAY:
            // The window manager will check these.
            break;
        case TYPE_PHONE:
        case TYPE_PRIORITY_PHONE:
        case TYPE_SYSTEM_ALERT:
        case TYPE_SYSTEM_ERROR:
        case TYPE_SYSTEM_OVERLAY:
            permission = android.Manifest.permission.SYSTEM_ALERT_WINDOW;
            outAppOp[0] = AppOpsManager.OP_SYSTEM_ALERT_WINDOW;
            break;
        default:
            permission = android.Manifest.permission.INTERNAL_SYSTEM_WINDOW;
    }
    ....

    //权限不够  也返回错误代码
    if (mContext.checkCallingOrSelfPermission(permission)
                    != PackageManager.PERMISSION_GRANTED) {
        return WindowManagerGlobal.ADD_PERMISSION_DENIED;
    }
    ...
}
</code></pre>

上面代码可以看见, 首先回去检测权限, 如果是type系统的window, 则需要权限, 如果没有权限, 最后一样会抛出异常<br>
当token为null时, 会去检查type, 如果type和对, 也会抛异常;<br>
而Activity可以显示dialog, 上面的代码已经分析了, 是对dialog的windowLayoutParams设置了token, 从而通过检测

上面说了, 设置系统弹窗, 需要声明权限, 没有权限也会失败,在国内各种rom的手机上, 系统弹窗的权限,有可能会被默认禁止掉, 有没有 _**不用系统弹窗权限, 而显示系统弹窗**_ 的呢 其实也是可以的,可以设置弹窗的类型是Toast(其实toast也是一个window,下面再说), 从而规避这个, 具体的分析可以看下面2篇博客 有些手机上弹框需要权限,通过`TYPE_TOAST`突破权限的分析 [here](http://www.jianshu.com/p/167fd5f47d5c/comments/776666), 和[here](http://www.liaohuqiu.net/cn/posts/android-windows-manager/)

--------------------------------------------------------------------------------

还有PopupWindow 和Toast的window, 以后在写吧 - -
