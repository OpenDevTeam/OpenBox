## 零、前言

&emsp;&emsp;性能优化在一款产品的迭代过程中非常重要；程序实现了功能、还原产品原型只能保证程序能用，但如果要让用户更愿意使用，产品得好用。试想一下如果你开发的产品启动慢、页面显示需要长时间转圈加载、页面切换卡顿、黑白屏、用一会机器就发烫、耗内存、OOM、程序切换到后台后占用内存无法释放......，这些问题就像正在玩游戏时弹出提示框这类糟糕的用户体验一样让用户恼火，如果用户不得不使用你的产品，可能还会一直忍受；但如果有很多同类竞品，糟糕的用户体验会大大影响留存率。有时候产品在市场上的表现差，真不能全怪产品和运营，程序体验问题也是很大一部分原因。

&emsp;&emsp;但大部分产品并没有足够重视性能问题，随便打开一个应用，即使是大厂出品，也极大可能存在过渡绘制和内存泄露的问题；也有可能是开发人员意识到了程序存在性能问题，但完成迭代就够忙的了，哪有时间去做这类不能体现绩效的事情。其实在越来越重视体验，同类产品竞争越来越激烈的环境下，对于开发人员来讲，只完成迭代，把功能做完远远不够，最重要的是把产品做好，让更多人愿意使用。重视性能问题，优化产品的体验，比改几个无关痛痒的bug会有价值得多。

&emsp;&emsp;网上能够找到很多关于性能优化很有价值的参考资料（详见文末），包括腾讯、阿里、魅族、豌豆荚、小米、UC等知名互联网公司都做过关于APP性能优化的分享，如果你专注于应用开发，并且想做一款备受欢迎的产品，性能优化是你进阶路上必须去学习和实践的。

## 一、性能问题分类

&emsp;&emsp;除了交互、视觉、内容方面的问题外，在用户使用过程中，给用户造成烦恼的问题都可以归结为是性能问题，比如上文中列出的这些都属于性能问题，按照影响的方面不同，可以分为如下几大类：

- **内存问题：**耗内存、OOM、程序切换到后台后占用内存无法释放（OOM会影响产品的稳定性；耗内存、内存泄露会影响整机的性能；占用内存多预示着留给其它应用的剩余内存空间小）；

- **功耗问题：**发烫（耗电）；

- **流畅度问题：**启动慢、页面显示需要长时间转圈加载、页面切换卡顿、黑白屏（卡慢崩会让人烦躁）；

&emsp;&emsp;针对上面一系列的性能问题，谷歌官方提供了各种各样的工具来针对性的解决各个方面的问题，也有很多不错的第三方工具值得尝试：

- **内存问题：**提供了Android Studio的静态代码检测功能、Android Monitor；第三方内存泄露分析工具Leakcanary、MAT；

- **功耗问题：**提供了GPU呈现模式、battery-historian、Android Monitor；

- **流畅度问题：**提供了Android Studio的静态代码检测功能、Android Monitor、HierarchyViewer、StrictMode、过渡绘制检测工具、TraceView等；

&emsp;&emsp;除了上面提到的这些性能优化工具外，谷歌还在Youtube上提供了一系列关于Android应用性能优化的短视频[Android Performance Patterns](https://www.youtube.com/playlist?list=PLWz5rJ2EKKc9CBxr3BVjPTPoDPLdPIFCE)，介绍如何优化Android各个方面的性能问题。

## 二、性能优化指标

&emsp;&emsp;性能优化的效果仅凭感觉很难衡量，一切应该看数据说话，比如流畅度优化，刷新频率每秒越接近60帧越理想，但只要每秒钟超过24帧人眼就无法辨别了，所以仅凭感觉是无法区分优化前的30帧和优化后的40帧的区别的。为了说明做性能优化有足够的价值，就有必要通过一系列指标来说明优化前后的区别。

&emsp;&emsp;性能指标的定义应该具有可衡量、可比较的特点，所以每项性能指标可以是数值，也可以是一份报告，比如：

- **流畅度：**FPS，即Frams per Second，一秒内的刷新帧数，越接近60帧越好；

- **启动时间：**时间，越短越好；

- **内存泄露：**AS静态代码检测结果、MAT检测结果，内存泄露很难用数值定义，但可以通过将优化前后工具检测的结果对比得出结论。没有内存泄露最好；

- **内存大小：**峰值，峰值越低越好；

- **功耗：**单位时间内的掉电量，掉电量越少越好；

&emsp;&emsp;从上面各项性能指标的定义可以看出，性能优化效果的评估主要是通过对比得出来的，性能如何只是相对的。只要针对同一个应用的同一项指标，优化后比优化前更优，就说明优化是有效果的。

## 三、性能优化原则和方法

### 1、优化原则

&emsp;&emsp;解决性能问题的过程中，遵循以下几个原则，有助于提高解决问题的效率：

- **足够多的测量：**不要凭感觉去检测性能问题、评估性能优化的效果，应该保持足够多的测量，数据不会说谎。使用各种性能工具有助于快速定位问题，这比凭感觉要靠谱得多；

- **使用低配置的设备：**同样的程序，在低端配置的设备中，相同的问题会暴露得更为明显；高配的设备很多时候会让你忽略掉性能问题；

- **权衡利弊：**在能够保证产品稳定、按时交付的前提下去做优化，不能顾此失彼，为了性能优化导致产品迟迟不能交付；

### 2、优化方法

&emsp;&emsp;性能优化的指标很多，乍看上去无从下手，但和解bug一样，只要讲方法，事情就变得迎刃而解。对于大多数问题来讲，只要遵循了解问题→定位问题→分析问题→解决问题→验证问题的思路，基本上都可以解决：

- **了解问题：**对于性能问题来讲，这个步骤只适用于某些明显的性能问题，很多无法感知的性能问题需要通过工具定位；

- **定位问题：**通过工具检测、分析，定位在什么地方存在性能问题；如果很难定位，可以采用排除法（屏蔽部分代码，看现象是否仍然存在，如果还存在，则说明被屏蔽的代码没有问题，这样逐渐缩小问题的范围）；

- **分析问题：**找到问题后，分析针对这个问题该如何解决，确定解决方案；

- **解决问题：**这个没什么可说的，如果是自己无法解决的问题，借助搜索引擎，你遇到过的问题很多人都遇到过，并且极有可能已经被解决了；

- **验证问题：**保证每一次优化都有效，没有产生新问题，保证产品的稳定；

## 四、性能优化工具

&emsp;&emsp;本文重点介绍谷歌官方提供的一系列应用性能优化工具以及值得推荐的第三方性能优化工具，这些工具主要集中在如下几个地方：

- **开发者选项：**GPU呈现模式分析、GPU过渡绘制、严格模式、应用无响应ANR等；

- **IDE中：**Android Studio，比如静态代码检测工具、Memory Monitor、CPU Monitor、NetWork Monitor、GPU Monitor、Layout Inspector、Analyze APK等；

- **SDK中：**sdk\tools，比如DDMS、HierarchyViewer、TraceView等；

- **第三方性能优化工具：**MAT、Leakcanary等；

### 1、开发者选项：

&emsp;&emsp;首先从不需要依赖任何工具，直接借助手机中的开发者选项进行应用性能检测说起。开发者选项需要进入开发者模式后才能在系统设置中显示，对于大多数设备，可以通过如下方式在手机中开启开发者选项：打开“系统设置”→点击进入“关于手机”→连续点击“版本号”选项直至提示已进入“开发者模式”，就可以在“系统设置”中看到“开发者选项了”，打开“开发者选项”，可以看到很多能够帮助开发者检测应用性能的选项：

![开发者选项](http://ww3.sinaimg.cn/large/6d17e381gw1f53sqqvaf1j20f037jgzm.jpg)

- **调试GPU过渡绘制（Visualize GPU Overdraw）：**过渡绘制用于检测你的程序是否存在不必要的绘制(举个栗子：同一个区域存在多个视图，刷新的时候被遮挡的视图也在绘制)，导致显示时的性能问题，它可以帮助开发者解决如下问题：

&emsp;&emsp;(1)找出应用中哪些地方存在不必要的渲染；

&emsp;&emsp;(2)帮助开发者发现哪些地方可以减少渲染，提高程序运行效率；

&emsp;&emsp;显示过渡绘制区域的步骤如下：“开发者选项”→点击“调试GPU 过渡绘制”→点击“显示过渡绘制区域”，一旦使能，对设备中的任何应用都有效：

![Show Overdraw areas](http://ww1.sinaimg.cn/large/6d17e381gw1f53tpuwlv0j20da07v3zf.jpg)

&emsp;&emsp;Android通过不同颜色来区分同一个区域绘制的次数，颜色越深，表示过渡绘制的次数越多，过渡绘制越严重。如下图所示，蓝色表示存在一次过渡绘制；深红色表示同一区域存在4次及以上的过渡绘制：

![Overdraw Layers](http://ww1.sinaimg.cn/large/6d17e381gw1f53tzq6sm0j207z092wev.jpg)

&emsp;&emsp;应用无法完全做到没有过渡绘制，优化是尽量避免不必要的过渡绘制，通常情况下保证同一区域过渡绘制少于三次都是合理的，即只要是出现红色（淡红色和深红色）的地方，就是需要优化的地方：

![Overdraw ](http://ww1.sinaimg.cn/large/6d17e381gw1f53uv8irpfj20el0dcjth.jpg)

&emsp;&emsp;过渡绘制不仅仅会影响程序的刷新频率，还会导致程序启动慢、黑白屏、耗内存等问题，因为过渡绘制主要是因为布局复杂导致，android在加载布局文件的时候，实际上是读取xml文件并解析，然后根据每个视图的关系去测量、绘制、显示每一个视图；复杂的布局会需要更长的解析、测量、绘制、显示时间，也需要更多的内存（这与是否设置了视图背景有关）。在实际开发过程中，有如下几种常见的过渡绘制优化方法：

**(1)使用merge标签：**merge标签就是为减少布局层次而生的，它通过减少View树的层级来优化布局，merge只能作为xml布局的根标签使用（因为Activity的根布局是FrameLayout，所以只有Activity对应的布局文件根标签为FrameLayout时才适合使用merge标签），如果在代码中Inflate带merge标签的布局时，必须为这个自定义View指定一个父ViewGroup，并且设置attachToRoot为true。merge只能够在xml布局文件中使用，没有对应的java类。下面的实例演示了merge标签的用法，通过“GPU过渡绘制”查看优化前后的效果，可以明显看到通过merge标签解决了过渡绘制的问题；通过Hierarch View观察优化前后的视图树，可以明显看到使用merge标签后的视图层级减少了：

**优化前：**

布局文件：

	<?xml version="1.0" encoding="utf-8"?>
	<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
	             android:layout_width="fill_parent"
	             android:layout_height="fill_parent"
	             android:background="@android:color/white">
	    <ImageView
	        android:layout_width="fill_parent"
	        android:layout_height="fill_parent"
	        android:scaleType="center"
	        android:src="@drawable/golden_gate"/>
	    <TextView
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_gravity="center"
	        android:padding="24dp"
	        android:text="No Merge Layout"
	        android:textColor="@android:color/black"/>
	</FrameLayout>

效果图及过渡绘制显示：

![优化前](http://ww1.sinaimg.cn/large/6d17e381gw1f543v0wiwqj207i0dc75i.jpg)

Hierarchy View视图树：

![Hierarchy View](http://ww4.sinaimg.cn/large/6d17e381gw1f544mo7shpj20ci05at8z.jpg)

**优化后：**

布局文件：

	<?xml version="1.0" encoding="utf-8"?>
	<merge xmlns:android="http://schemas.android.com/apk/res/android"
	       android:background="@android:color/white">
	    <ImageView
	        android:layout_width="fill_parent"
	        android:layout_height="fill_parent"
	        android:scaleType="center"
	        android:src="@drawable/golden_gate"/>
	    <TextView
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_gravity="center"
	        android:padding="24dp"
	        android:text="Has Merge Layout"
	        android:textColor="@android:color/black"/>
	</merge>

效果图及过渡绘制显示：

![优化后](http://ww3.sinaimg.cn/large/6d17e381gw1f543vhmps4j207i0dcq49.jpg)

Hierarchy View视图树：

![Hierarchy View](http://ww3.sinaimg.cn/large/6d17e381gw1f544lyphxkj20aa05a3yq.jpg)

**(2)使用ViewStub标签：**在开发应用的时候，经常会遇到这样的情况，在程序运行时根据条件来决定显示/隐藏哪个视图；通常会在布局文件中将其写上去，默认隐藏，然后在代码中根据条件去判断是否显示。这样做的优点是逻辑清晰，但缺点是耗费资源，在布局文件中将某个视图默认设置为invisable或者gone，在Inflate布局文件的时候仍然会被infalte，同样会被实例化、设置属性，但有可能默认被隐藏的视图用户在某一次操作中很可能不会去触发它。为了提高布局文件加载效率和减少额外的资源消耗，强烈建议使用ViewStub标签，ViewStub是一个用于在运行时加载布局资源、不可见、宽高为0的View，在布局文件中使用它只是用于占位，在代码中没有手动加载它时，并不会影响页面的测量、绘制、显示效率，在代码中通过inflate加载ViewStub时，ViewStub会用在布局文件中为其指定的布局文件来代替它自身，通过前面的解释可想而知，ViewStub只能够被inflate一次，一旦加载后ViewStub对象就会被置为空；ViewStub标签有对应的java类ViewStub.java，通过阅读源码可以发现，确实在初始化的时候设置为隐藏、不绘制、宽高为0，并且它复写了View的dispatchDraw和draw方法，这俩方法是空方法，没有调用super的方法，也没有执行自己的代码：

	public final class ViewStub extends View {

		...

	    public ViewStub(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
	        super(context);
	
	        final TypedArray a = context.obtainStyledAttributes(attrs,
	                R.styleable.ViewStub, defStyleAttr, defStyleRes);
	        mInflatedId = a.getResourceId(R.styleable.ViewStub_inflatedId, NO_ID);
	        mLayoutResource = a.getResourceId(R.styleable.ViewStub_layout, 0);
	        mID = a.getResourceId(R.styleable.ViewStub_id, NO_ID);
	        a.recycle();
	
	        setVisibility(GONE);
	        setWillNotDraw(true);
	    }
	
	    @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        setMeasuredDimension(0, 0);
	    }

		@Override
	    public void draw(Canvas canvas) {

	    }
	
	    @Override
	    protected void dispatchDraw(Canvas canvas) {

	    }
		
		...

	}

&emsp;&emsp;下面是ViewStub在Inflate前后的布局及视图树：

**xml文件布局：**

activity_viewstub_layout.xml:

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout
	    android:id="@+id/activity_view_stub_layout"
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="vertical">
	    <include
	        android:id="@+id/inclueId"
	        android:layout_width="match_parent"
	        android:layout_height="0dp"
	        android:layout_weight="8"
	        android:background="#ff00ff00"
	        layout="@layout/inclue_viewstub_layout"/>
	    />
	    <View
	        android:layout_width="match_parent"
	        android:layout_height="0dp"
	        android:layout_weight="1"
	        android:background="@android:color/black"/>
	    <ViewStub
	        android:id="@+id/viewStubId"
	        android:layout_width="match_parent"
	        android:layout_height="0dp"
	        android:layout_weight="8"
	        android:layout="@layout/inclue_viewstub_layout"/>
	    <Button
	        android:id="@+id/inflateViewStubBtnId"
	        android:layout_width="match_parent"
	        android:layout_height="0dp"
	        android:layout_weight="3"
	        android:text="InflateViewStub"
	        android:onClick="onClick"
	        />
	</LinearLayout>

inclue_viewstub_layout.xml:

	<?xml version="1.0" encoding="utf-8"?>
	<RelativeLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent">
	    <TextView
	        android:id="@+id/titleTxtViewId"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_centerInParent="true"
	        android:text="ViewStub!!!!"
	        android:textColor="@android:color/black"
	        android:textSize="24dp"/>
	</RelativeLayout>

**代码：**
	
	public void onClick(View v){
        switch(v.getId()){
            case R.id.inflateViewStubBtnId:{
                inflateViewStub();
            }
            break;
        }
    }

	private void inflateViewStub(){
        mViewStub = (ViewStub)findViewById(R.id.viewStubId);
        if(null == mViewStub){
            Toast.makeText(this, "ViewStub is Empty", Toast.LENGTH_LONG).show();
            return;
        }
        mViewStub.inflate();
    }


**Inflate前：**

界面效果：

![ViewStub](http://ww2.sinaimg.cn/large/6d17e381gw1f54avmp1fgj207i0dct8u.jpg)

Hierachy View：

![ViewStub视图树](http://ww3.sinaimg.cn/large/6d17e381gw1f54auxdfjdj20b504o0sr.jpg)

**Inflate后：**

界面效果：

![ViewStub](http://ww3.sinaimg.cn/large/6d17e381gw1f54avyprm0j207i0dcaaa.jpg)

Hierachy View：

![ViewStub视图树](http://ww3.sinaimg.cn/large/6d17e381gw1f54avdlqdpj20b904sq2z.jpg)

&emsp;&emsp;可以看到在ViewStub Inflate前ViewStub不占用布局层级，所以不会消耗程序资源；Inflate后会占用布局层级；在试验的过程中，点击两次及以上Inflate按钮时，会弹出“ViewStub is Empty”的Toast，说明mViewStub在实例化一次后再次实例化时会失败，因为在Inflate时已经被replace掉，系统找不到这个资源ID。

**(3)使用Space：**过渡绘制问题是因为绘制引起的，space标签可以只在布局文件中占位，不绘制，Space标签有对应的java类Space.java，通过阅读源码可以发现，它继承至View.java，并且复写了draw方法，该方法为空，既没有调用父类的draw方法，也没有执行自己的代码，表示该类是没有绘制操作的，但onMeasure方法正常调用，说明是有宽高的。

xml布局：

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="fill_parent"
	    android:layout_height="fill_parent"
	    android:orientation="vertical">
	    <TextView
	        android:layout_width="match_parent"
	        android:layout_height="0dp"
	        android:layout_weight="2"
	        android:gravity="center"
	        android:text="TextView 1"
	        android:textColor="@android:color/black"
	        android:textSize="28dp"
	        android:background="@android:color/darker_gray"/>
	    <Space
	        android:layout_width="match_parent"
	        android:layout_height="0dp"
	        android:layout_weight="1"
	        android:background="@android:color/black"/>
	    <TextView
	        android:layout_width="match_parent"
	        android:layout_height="0dp"
	        android:layout_weight="2"
	        android:gravity="center"
	        android:text="TextView 2"
	        android:textColor="@android:color/black"
	        android:textSize="28dp"
	        android:background="@android:color/darker_gray"/>
	    <Space
	        android:layout_width="match_parent"
	        android:layout_height="0dp"
	        android:layout_weight="1"/>
	    <TextView
	        android:layout_width="match_parent"
	        android:layout_height="0dp"
	        android:layout_weight="2"
	        android:gravity="center"
	        android:text="TextView 3"
	        android:textColor="@android:color/black"
	        android:textSize="28dp"
	        android:background="@android:color/darker_gray"/>
	</LinearLayout>


效果图：

&emsp;&emsp;可以看到在布局中给第一个Space控件设置了黑色背景，但从效果图可以看出Space并没有变成黑色，说明没有执行绘制方法。

![效果图](http://ww1.sinaimg.cn/large/6d17e381gw1f555619uutj207i0dct8q.jpg)

Hierarchy View：

&emsp;&emsp;观察视图树也可以看出Space会占用空间（因为有宽高）。

![视图树](http://ww3.sinaimg.cn/large/6d17e381gw1f5557cvtqaj209c05i0sv.jpg)

**(4)去掉不必要的背景：**如果不是通过测量和仔细分析，你很难发现这个不经意的细节会是导致过渡绘制、内存问题的主要原因，每个Activity都会在AndroidManifest.xml中设置主题，主题的目的是设置界面的显示风格，但在设置主题的时候通常情况下默认给Window设置了背景，注意是Window而不是Activity，Activity是依附在Window上的，Android系统在刷新整个界面时不仅仅是刷新Activity，还会刷新Window。如果默认没有去掉window的背景，并且在布局文件中给Activity设置了背景，就会存在过渡绘制的问题，具体情况可以看下面的实例：

activity_background_layout.xml（这里为了演示在布局文件中为每个视图设置了背景，在真实情况中没有必要为每个视图都设置）：

	<?xml version="1.0" encoding="utf-8"?>
	<RelativeLayout
	    android:id="@+id/activity_background_layout"
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:background="@android:color/white">
	    <ScrollView
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        android:layout_marginLeft="20dp"
	        android:layout_marginRight="20dp">
	        <TextView
	            android:layout_width="match_parent"
	            android:layout_height="match_parent"
	            android:padding="10dp"
	            android:background="@android:color/white"
	            android:text="@string/more_text"/>
	    </ScrollView>
	</RelativeLayout>


使用该布局的Activity对应的主题如下：

	<style name="AppTheme" parent="@android:style/Theme.Light.NoTitleBar">
    </style>

通过过渡绘制工具检测，存在过渡绘制：

![window优化前](http://ww4.sinaimg.cn/large/6d17e381gw1f59lnukfawj207i0dcgog.jpg)

下面是去掉window背景后的效果（布局文件不变，主题变动如下）：

	<style name="AppTheme" parent="@android:style/Theme.Light.NoTitleBar">
        <item name="android:windowBackground">@null</item>
    </style>

效果图：

![window优化后](http://ww2.sinaimg.cn/large/6d17e381gw1f59lobh28xj207i0dc41e.jpg)

	
**说明：**

&emsp;&emsp;1、在主题中去掉Window的背景时要注意，去掉之后必须重新运行程序检查一下，避免有些Activity并没有设置背景导致界面背景为黑色；

&emsp;&emsp;2、有的程序为了避免冷启动时界面黑屏/白屏的问题，在主题中为window设置了一张图片，然后在布局文件中为Activity也设置了背景，这样既会导致过渡绘制问题，还会导致内存问题（同一个页面两张全屏的图片，双倍内存）；所以这种解决方式并不妥，如果是启动速度问题，直接优化启动速度比这种方式靠谱。
	

**(5)其它:**

1、通过Canvas的clipRect方法控制每个视图每次刷新的区域，这样可以避免刷新不必要的区域，从而规避过渡绘制的问题；

2、如对一个View做Alpha转化，需要先将View绘制出来，然后做Alpha转化，最后将转换后的效果绘制在界面上。通俗点说，做Alpha转化就需要对当前View绘制两遍，可想而知，绘制效率会大打折扣，耗时会翻倍；

3、多和产品、UI沟通，避免过渡设计，过渡绘制最好的解决方案是界面的布局本身就不复杂，这样程序实现出来的界面就会少很多过渡绘制，优化也会简单很多。

&emsp;&emsp;总结一下过渡绘制的检测和解决方案：通过“开发者选项”中的“显示过渡绘制”和Android提供的工具“HierarchyViewer”，以每个界面为单位，可以完全检测出每个界面的过渡绘制问题；因为导致过渡绘制的原因不一，所以也会有多种对应的解决方案：

1、merge标签可以解决相同布局嵌套导致的过渡绘制问题；

2、ViewStub标签可以解决动态加载页面布局，避免默认加载不必要布局的问题；

3、Space标签可以解决只占位、不刷新的视图问题；

4、去掉Window背景可以解决所有界面的过渡绘制问题；

5、clipRect可以解决只刷新固定区域的问题；

6、不必要的alpha值设置可以解决同一视图被多次绘制的问题；

7、最重要的是产品设计合理，多和产品、UI沟通，避免无意义的工作。

- **GPU呈现模式分析（Profiling GPU Rendering）：**

&emsp;&emsp;从Android 4.1开始，在“开发者选项”中提供了GPU呈现模式分析的选项，GPU呈现模式是一个方便你快速观察UI渲染效率的工具，主要作用是实时查看每一帧的渲染效率，定位哪里存在渲染的性能问题；通过如下方式可以打开GPU呈现模式分析：“系统设置”→“开发者选项”→“GPU呈现模式分析”→在弹出的窗口中选择“在屏幕上显示成条形图（On screen as bars）”。

![打开GPU呈现模式](http://ww4.sinaimg.cn/large/6d17e381gw1f59u8ejmf5j20bl09njsj.jpg)

&emsp;&emsp;打开GPU呈现模式后，你可以在机器的任何界面看到如下图所示的条形图，顶部通知栏、当前活动程序（主窗口）、底部导航栏都会有对应的呈现模式条形图，用于观察通知栏、当前活动界面、导航栏的渲染效率。

![条形图](http://ww2.sinaimg.cn/large/6d17e381gw1f59u5m95arj207008w74x.jpg)

&emsp;&emsp;随着界面的刷新，界面上会滚动显示锤子的柱状图来表示每帧画面说需要的渲染时间，柱状图越高表示花费的渲染时间越长。中间有一根绿色的横线，代表每帧的最长渲染时间：16ms，我们需要确保每一帧花费的总时间都低于这条横线，这样才能够避免出现卡顿的问题。

&emsp;&emsp;从图中可以看出，每一条柱状线包含三种颜色，但从Android 6.0开始，你看到的每条柱状线已不止三种颜色：

![Android 6.0](http://ww2.sinaimg.cn/large/6d17e381gw1f59u77w1c2j207i0dcwfu.jpg)

&emsp;&emsp;每种颜色代表每一帧渲染过程中需要完成的某一件事情，因为6.0之前的三种颜色不大能够清晰地帮助我们定位性能问题的具体原因，所以从6.0开始，将每一帧的渲染过程拆分成了8个步骤，每个步骤一种颜色，每种颜色的意义如下：

![每一帧颜色](http://ww4.sinaimg.cn/large/6d17e381gw1f59ule2l88j20bb0fqwff.jpg)

&emsp;&emsp;**(1)Swap Buffers：**表示处理任务的时间，也可以说是CPU等待GPU完成任务的时间，线条越高，表示GPU做的事情越多；

&emsp;&emsp;**(2)Command Issue：**表示执行任务的时间，这部分主要是Android进行2D渲染显示列表的时间，为了将内容绘制到屏幕上，Android需要使用Open GL ES的API接口来绘制显示列表，红色线条越高表示需要绘制的视图更多；

&emsp;&emsp;**(3)Sync & Upload：**表示的是准备当前界面上有待绘制的图片所耗费的时间，为了减少该段区域的执行时间，我们可以减少屏幕上的图片数量或者是缩小图片的大小；

&emsp;&emsp;**(4)Draw：**表示测量和绘制视图列表所需要的时间，蓝色线条越高表示每一帧需要更新很多视图，或者View的onDraw方法中做了耗时操作；

&emsp;&emsp;**(5)Measure/Layout：**表示布局的onMeasure与onLayout所花费的时间，一旦时间过长，就需要仔细检查自己的布局是不是存在严重的性能问题；

&emsp;&emsp;**(6)Animation：**表示计算执行动画所需要花费的时间，包含的动画有ObjectAnimator，ViewPropertyAnimator，Transition等等。一旦这里的执行时间过长，就需要检查是不是使用了非官方的动画工具或者是检查动画执行的过程中是不是触发了读写操作等等；

&emsp;&emsp;**(7)Input Handling：**表示系统处理输入事件所耗费的时间，粗略等于对事件处理方法所执行的时间。一旦执行时间过长，意味着在处理用户的输入事件的地方执行了复杂的操作；

&emsp;&emsp;**(8)Misc Time/Vsync Delay：**表示在主线程执行了太多的任务，导致UI渲染跟不上vSync的信号而出现掉帧的情况；出现该线条的时候，可以在Log中看到这样的日志：

	I/Choreographer(*): Skipped XXX frames! The application may be doing too much work on its main thread

&emsp;&emsp;通过GPU呈现模式可以清晰地检测出导致渲染问题的具体原因，但不能定位是哪一行代码出了问题，从上面的描述可知，减少过渡绘制可以很好地提升GPU呈现模式的表现力；如果要跟踪具体哪一行代码导致了渲染的性能问题，需要借助各种性能检测工具。比如通过TraceView跟踪是否存在耗时操作；通过“显示过渡绘制”跟踪是否存在过渡绘制等。

- **启用严格模式（Strict mode enabled）：**当当前界面在主线程中存在耗时操作时，会闪烁屏幕，但只会提示你存在耗时操作，不会告诉你具体的地方；如果要精确定位具体哪里耗时，应该在代码中添加StrictMode检查，在log中会报详细的耗时信息：

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
                .detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

&emsp;&emsp;关于严苛模式的详细说明，可以参考官网的这篇文章，很详细：[StrictMode](https://developer.android.com/reference/android/os/StrictMode.html)。

- **显示所有“应用无响应”（Show all ANRs）：**当任何一个应用（包括后台应用）无响应时会弹出“App Not Responding”对话框，主要用于识别程序之间是否存在干扰；

&emsp;&emsp;更多关于开发者选项的信息，可以查阅这篇文章：[All about your phone's developer options](http://www.androidcentral.com/all-about-your-phones-developer-options)。

### 2、IDE Android Studio

&emsp;&emsp;Android Studio是谷歌官方提供的集成开发环境（后面简称AS），同样作为Android集成开发环境的Eclipse很好用，但AS更高效、顺手、便捷，这在没有使用AS之前是无法感受到的。AS不仅提供了程序开发、构建、调试的环境，还提供了一系列优化应用质量的工具，这些工具包括静态代码检测工具Inspect Code、Android Monitor、Analyze APK...，同时还集成了Android Device Monitor。通过这三个工具基本上可以检测、分析、定位大部分Android应用的性能问题。

**Inspect Code：**

&emsp;&emsp;AS中的Insepct Code是用于对代码进行静态质量分析的工具，它是lint的增强版，可以检测出来很多潜在的问题，同时给你提供改善建议；它不仅可以对整个工程、某个module、某个文件进行所有规则的代码静态质量检测，还可以针对某一项规则对整个工程进行检测：

![使用方法](http://ww4.sinaimg.cn/large/6d17e381gw1f5bxvxv4ypj20bn0863zk.jpg)

&emsp;&emsp;在Inspection窗口的左侧，有提供了一系列快捷按钮用于快速分析、定位、修复代码中的问题：

![Inspect Tool Window](http://ww3.sinaimg.cn/large/6d17e381jw1f5c2v5j63fj208y08cmy7.jpg)

![快捷按钮说明](http://ww2.sinaimg.cn/large/6d17e381jw1f5c2vcjngyj20xk0l5qa3.jpg)

&emsp;&emsp;打开Settings的Inspections选项，可以看到这个工具能够检测出很多关于Java和Android方面的性能问题，比如布局导致的过渡绘制、在onDraw方法中创建新的实例、Handler内部类导致的潜在内存泄露、使用SparseArray代替HashMap的建议、布局层次太深、TypedArrays和VelocityTrackers没有调用recycle方法导致的内存泄露、存在没有使用的资源文件、系统方法取代自定义实现功能代码块的建议、IO操作导致的内存泄露问题、String和StringBuilder的相互替换等等；随着AS的不断更新，这个功能在不断完善，最新的AS版本中，很多代码层面的问题都能够被检测出来。

![Android Performance issues](http://ww3.sinaimg.cn/large/6d17e381jw1f5byc0bgkkj20i60cumzi.jpg)

![Java Performance issues](http://ww2.sinaimg.cn/large/6d17e381jw1f5byce9knyj20hs0mbgqg.jpg)

&emsp;&emsp;通过这个工具可以删掉无用资源，检测出明显的性能问题，以及对代码可读性和性能方面的建议，使用起来很简单，建议每天作为日常，提交代码前都检测一次，这比在持续集成过程中，使用sonar等代码质量工具分析更方便。

&emsp;&emsp;更多关于Inspect Code的描述参见：

&emsp;&emsp;(1)[Inspection Tool Window](https://www.jetbrains.com/help/idea/2016.1/inspection-tool-window.html)

&emsp;&emsp;(2)[Android Studio提高代码质量必杀技：Inspact Code](https://mp.weixin.qq.com/s?__biz=MzAxMzYyNDkyNA==&mid=2651332108&idx=1&sn=0595ccab1516fcff06f15a8f8f50f8ea&scene=0&key=18e81ac7415f67c48e413ecf278ccbc1010fcdcb4de390beb422b7472f670629f42a2f505bbb63e1fe845ac00a687376&ascene=0&uin=MTYzMjY2MTE1&devicetype=iMac+MacBookPro10%2C1+OSX+OSX+10.11.5+build(15F34)&version=11020201&pass_ticket=LiC35u0zefHyHhMPMgUBxi0eQBs2JHKKlMqSkEJweDs%3D)

**Android Monitor：**

&emsp;&emsp;Andorid Monitor提供了一系列的性能检测工具，通过它可以帮助你剖析应用的性能，以便优化、调试和改善应用各方面的性能问题；Android Monitor可以从如下几个方面对真机/模拟器中正在运行的程序进行性能监控：

&emsp;&emsp;(1)Log日志，包括系统日志和自定义日志；

&emsp;&emsp;(2)实时监控内存、CPU、GPU的使用情况；

&emsp;&emsp;(3)实时监控网络流量的消耗（只适合于真机）；

&emsp;&emsp;(4)采集运行时信息并保存为文件，供工具分析；

**LogCat日志窗口：**

&emsp;&emsp;通过Logcat日志窗口可以查看系统事件以及程序自定义的日志信息，比如GC消息、程序运行时异常日志、当前启动应用的包名及入口等；它不仅提供了实时查看设备日志信息的功能，还有一段时间的日志缓存；同时提供了按照搜索（支持正则表达式）、按照等级/自定义标签/指定包名筛选日志的功能，以帮助你快速定位问题。

&emsp;&emsp;Logcat窗口是以行为单位对日志进行缓存，当窗口中的缓存日志超过指定的行数上限时，会删掉最先缓存的日志。如果你觉得窗口中的日志缓存清除太快了，不便于跟踪问题，可以在AS安装目录下的"bin/idea.properties"文件中增加“idea.cycle.buffer.size=你想缓存的行数”来调整窗口给的日志缓存行数，但建议不要调得太高，否则会严重影响AS的体验，缓存行数越多AS就会越卡，控制在5000行以内已经基本满足需求了。

&emsp;&emsp;在程序中使用Log类打印日志时，TAG的长度建议不要超过23个字符，否则会做截断处理，影响问题的准确跟踪。

&emsp;&emsp;Logcat窗口的左侧有一列工具快捷按钮，方便我们快速找到我们需要的信息：

![Log窗口左侧快捷工具栏](http://ww3.sinaimg.cn/large/6d17e381gw1f5d9smazdaj20b608ggms.jpg)


**内存监控窗口：**[Memory Monitor](https://developer.android.com/studio/profile/am-memory.html)

**CPU监控窗口：**[CPU Monitor](https://developer.android.com/studio/profile/am-cpu.html)

**GPU监控窗口：**[GPU Monitor](https://developer.android.com/studio/profile/am-gpu.html)

**网络流量监控窗口：**[Network Monitor](https://developer.android.com/studio/profile/am-network.html)

**HPROF查看和分析工具：**[HPROF Viewer and Analyzer](https://developer.android.com/studio/profile/am-hprof.html)

**内存分配跟踪工具：**[Allocation Tracker](https://developer.android.com/studio/profile/am-allocation.html)

**函数调用栈分析工具：**[Method Tracer](https://developer.android.com/studio/profile/am-methodtrace.html)

**查看系统信息工具：**[System Information](https://developer.android.com/studio/profile/am-sysinfo.html)

**Analyze APK...：**

![Analyze APK](http://ww2.sinaimg.cn/large/6d17e381gw1f5c9ft0e9ij20hs0cr40k.jpg)

### 3、其它性能优化工具

- [TraceView：正确使用Android性能分析工具——TraceView](http://bxbxbai.github.io/2014/10/25/use-trace-view/)

- [HierarchyViewer：Android UI 优化——使用HierarchyViewer工具](http://blog.csdn.net/xyz_lmn/article/details/14222975)

- [MAT：Android 性能优化之使用MAT分析内存泄露问题](http://blog.csdn.net/xiaanming/article/details/42396507)

- [LeakCanary：LeakCanary 中文使用说明](http://www.liaohuqiu.net/cn/posts/leak-canary-read-me/)

## 五、参考资料

- [Android Performance Patterns](https://www.youtube.com/playlist?list=PLWz5rJ2EKKc9CBxr3BVjPTPoDPLdPIFCE)

- [Android性能优化典范](http://hukai.me/blog/archives/)

- [Speed up your app](http://blog.udinic.com/2015/09/15/speed-up-your-app)（[中文版](http://www.csdn.net/article/2015-11-05/2826130-speed-up-your-app/1)）

- [Android客户端性能优化（魅族资深工程师毫无保留奉献）](http://blog.tingyun.com/web/article/detail/155#rd)

- [Android应用内存泄露分析、改善经验总结](https://zhuanlan.zhihu.com/p/20831913?refer=zmywly8866)

- [内存泄露从入门到精通三部曲之基础知识篇](http://bugly.qq.com/bbs/forum.php?mod=viewthread&tid=21&extra=page%3D4)

- [内存泄露从入门到精通三部曲之排查方法篇](http://bugly.qq.com/bbs/forum.php?mod=viewthread&tid=62&extra=page%3D5)

- [内存泄露从入门到精通三部曲之常见原因与用户实践](http://bugly.qq.com/bbs/forum.php?mod=viewthread&tid=125&highlight=%E5%86%85%E5%AD%98%E6%B3%84%E9%9C%B2)

- [awesome-android-performance](https://github.com/Juude/awesome-android-performance)

- [听云应用性能管理大讲堂历届讲师课件汇总](http://blog.tingyun.com/web/article/detail/515)

- [Infoq演讲：Android内存优化](http://www.infoq.com/cn/presentations/android-memory-optimization)

- [Infoq演讲：移动开发网络性能优化实践](http://www.infoq.com/cn/presentations/performance-optimization-of-mobile-development-network)

- [Infoq演讲：移动应用性能揭秘](http://www.infoq.com/cn/presentations/expose-mobile-application-performance)

- [Infoq演讲：安卓开发中动画效果的实现和优化](http://www.infoq.com/cn/presentations/realization-and-optimization-of-animation-effects-in-android-development)

- [Infoq演讲：Android淘宝客户端用户体验优化实践](http://www.infoq.com/cn/presentations/android-taobao-clients-user-experience-practice)

- [阿里技术沙龙：小米系统性能的优化](http://club.alibabatech.org/resource_detail.htm?topicId=162)

- [阿里技术沙龙：豌豆荚的性能优化简介](http://club.alibabatech.org/resource_detail.htm?topicId=126)

- [Android性能测试工具汇总](http://www.jianshu.com/p/dab8324c5500)