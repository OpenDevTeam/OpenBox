# 一、前言

&emsp;&emsp;标题起得屌了点，文章只能给大家带来理论知识，能不能上天还是得各位亲自实践。文中涉及到很多自己的理解，能力有限，有问题的地方还请指正。

&emsp;&emsp;很多人把自定义View想得复杂了，以为有多高深，主要还是没有实践过，没有足够的自信；但也有很多人把自定义View想得简单了，以为摸清View的几个关键回调、知道自定义属性和Android的消息分发机制就算是老司机了，其实对于自定义View来讲，设计、排版、效率都是很费脑筋的，我在github上到现在都没发现一个像样的图文混排自定义View。

&emsp;&emsp;常见的Android中自定义View主要有两种类型：

- **组合控件：**通过Android的基础控件(TextView、CheckBox、Button、ProgressBar等)组合而成，比如试题控件（TextView+VideoGroup）、下拉刷新、瀑布流控件、带左/右滑功能的控件、视频控件等，这种自定义View的难点在于程序的逻辑处理；

- **完全自定义控件：**继承自View、TextureView或SurfaceView，然后重写核心的回调方法，以View为例，按需复写其构造、onMeasure、onLayout、onTouchEvent、onDraw、onAttachedToWindow、onDetachedFromWindow等方法，这种自定义View的难点在于程序的设计、效率优化和排版，比如输入法中的手写控件、图文混排控件（现在很多都是通过webview加载网页实现了）、词典取词控件、图表控件、个性化进度条、弹幕显示控件、Markdown控件、IDE代码编辑控件等。

&emsp;&emsp;按照上面这种方式分只是便于理解，很多时候有些控件既有组合，又需要复写所继承类的回调方法。

# 二、自定义View的价值

- 能够做到基础控件无法做到的效果，为应用的表现增色；

- 在多个应用并行开发的团队，将公用的交互效果提取成自定义控件，方便复用，减少不必要的重复劳动；

- 将控件的内部逻辑封装在自定义View中，便于应用内解耦；

# 三、有必要了解的核心知识点

### View、SurfaceView、TextureView的区别

- **View：**普通的View，与宿主窗口共享同一个绘图表面，UI在主线程中绘制，在有无硬件加速的情况下都能工作（没有硬件加速的情况下，canvas的有些方法会失效）；

- **SurfaceView：**继承自View，绘制和显示效率高，因为拥有独立的绘图表面，UI在一个独立的线程中进行绘制，不会占用主线程的资源。SurfaceView的使用和普通的View不一样，需要结合SurfaceHodler一起使用。因为和宿主窗口不是共享同一个绘图表面的原因，笔者在实际使用SurfaceView的过程中发现对其做动画操作会达不到想要的效果（一坨黑色）；

- **TextureView：**继承自View，与SurfaceView相比，TextureView不会创建一个单独的绘图表面，这使得它可以像一般的View一样执行一些变换操作，比如移动、动画等等，但TextureView必须在硬件加速开启的窗口中才能正常工作；

### View的三大核心方法onMeasure、onLayout、onDraw

- **onMeasure：**用于测量视图的大小；

- **onLayout：**用于给视图进行布局；

- **onDraw：**用于对视图进行绘制；

### 自定义属性

&emsp;&emsp;对于自定义View的一些属性设置，除了可以在自定义View中提供公开接口外，还可以通过自定义属性，在对自定义View布局时就指定，这样可以简化用户使用控件的复杂度，实现自定义属性的步骤如下：

- 在自定义View工程的res/values文件夹下新建一个attrs.xml的文件，在里面定义自定义属性的ID、属性和属性对应的类型，eg：

	<declare-styleable name="DictView" >
		<attr name="textSize" format="dimension" />
		<attr name="textColor" format="reference|color" />
		
	    <attr name="typeface">
	        <enum name="normal" value="0" />
	        <enum name="sans" value="1" />
	        <enum name="serif" value="2" />
	        <enum name="monospace" value="3" />
	    </attr>
		
	    <attr name="width" format="dimension" />
	    <attr name="height" format="dimension" />
	</declare-styleable>

- 在自定义View带attrs参数的构造方法中解析自定义属性值：

	TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DictView, defStyle, 0);
	int n = a.getIndexCount();
	for(int i = 0; i < n; i++)
	{
		int attr = a.getIndex(i);
		if(attr == com.test.dict.R.styleable.DictView_textSize){
			textSize = a.getDimensionPixelSize(attr, textSize);
		}else if(attr == com.test.dict.R.styleable.DictView_textColor){
			textColor = a.getColorStateList(attr);
		}else if(attr == com.test.dict.R.styleable.DictView_typeface){
			typefaceIndex = a.getInt(attr, typefaceIndex);
		}else if(attr == com.test.dict.R.styleable.DictView_width){
			setWidth(a.getDimensionPixelSize(attr, mWidth));
		}else if(attr == com.test.dict.R.styleable.DictView_height){
			setHeight(a.getDimensionPixelSize(attr, mHeight));
		}
	}
	a.recycle();

&emsp;&emsp;对自定义属性的解析需要注意两点：

1.TypedArray使用完成后一定要调用其recycle方法，否则会有内存泄露的问题；

2.如果自定义View在一个单独的module中（不属于主工程），对attr的获取不能使用switch-case语句，要用if...else，具体原因之前有介绍过，详见：[在Android library中不能使用switch-case语句访问资源ID的原因分析及解决方案](http://zmywly8866.github.io/2014/12/24/android-can-not-use-switch-to-load-resource-in-libproject-solution.html)

&emsp;&emsp;完成自定义属性的定义后，就可以在布局自定义View的过程中使用自定义属性了，具体步骤如下：

- 在xml布局文件的根标签或者需要使用自定义属性的标签中指定自定义属性的命名空间，其中这里的dictview就是命名空间，是可以随意指定的：

		xmlns:dictview="http://schemas.android.com/apk/res-auto"

- 在自定义View的布局中使用自定义属性，所有自定义属性的设置都是在指定的命名空间下的，因为是自定义，所以不能用android这个命名空间：

		<com.test.dictview.DictView
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_centerHorizontal="true"
	        android:layout_centerVertical="true"
	        android:text="@string/hello_world"
	        dictview:textColor="@android:color/white"
	        dictview:typeface="sans" />

### 双缓冲

&emsp;&emsp;在移动设备中很容易出现效率问题，对于效率问题的处理，主要方法是时间换空间或者空间换时间；自定义View可能存在显示的效率问题，可以通过双缓冲来解决这个问题，双缓冲就是用空间换时间的典型例子，同一个View在内存中创建了两份同样大小的内存，一份用于绘制，一份用于显示，绘制是绘制在Bitmap上，显示就是将这张bitmap显示在画布上。

### 硬件加速

&emsp;&emsp;在Android设备中，硬件加速默认是开启的，有些应用出于内存占用（开启硬件加速会占用更多的内存）和应用特征的考虑（没什么动画，基本没有涉及到需要GPU的操作），会在AndroidManifest.xml中关掉硬件加速，这会导致自定义View时，canvas的某些方法不能正常使用，为了让自定义View达到更好的表现效果，建议不要关掉有用到自定义View界面的硬件加速（因为在View层面只能关闭硬件加速，无法开启硬件加速，所以只能控制Activity和Window层面的硬件加速）。

### 图文混排：

&emsp;&emsp;涉及到图文混排的自定义View，一定要将排版和显示这两件事情分开，因为排版耗时但不涉及到UI的更新，可以在线程中处理，但显示必须要更新UI，所以在onDraw方法里面尽量不要做耗时和逻辑处理，只纯粹做显示操作。对于排版可以考虑异步，或者先完成排版，后续只需要直接显示即可，这得具体问题具体分析。

&emsp;&emsp;同时显示也有技巧，为了节省内存，可以考虑做缓存，一个控件可能不只一页内容，可以在内存中缓存当前页和当前页的前、后两页，当滑动时，始终按照这种策略更新缓存内容就可以了，这样既达到了节省内存、又提高效率的目的。

### getHistorySize

&emsp;&emsp;对于有涉及到触摸操作的自定义View（比如手写控件），是在onTouchEvent方法中接收触摸消息的，但限于Android系统和设备本身的情况，底层上报的点信息不一定能够实时通过MotionEvent回调到上层，底层1秒钟可能传了几百个点，但onTouchEvent方法中接收到的可能只有几十个点，如果需要更为平滑地点信息，可以借助MotionEvent的getHistorySize方法获取底层上报的更多点信息，关于getHistorySize的解释，请参见参考资料中对平滑手写签名效果的介绍。

### SpannableString

&emsp;&emsp;使用过SpannableString的都知道，可以通过它将同一串字符中的不同文字做不同的处理，比如某些文字的颜色、字体、背景色、大小等有变化，都可以通过它来设置，熟练掌握SpannableString对于灵活自定义View会有很大地帮助。

# 四、参考资料

- [Creating Custom Views](https://developer.android.com/training/custom-views/index.html)

- [推翻自己和过往，重学自定义View](http://blog.csdn.net/lfdfhl/article/details/51671038)

- [Android LayoutInflater原理分析，带你一步步深入了解View(一)](http://blog.csdn.net/guolin_blog/article/details/12921889)

- [Android视图绘制流程完全解析，带你一步步深入了解View(二)](http://blog.csdn.net/guolin_blog/article/details/16330267)

- [Android视图状态及重绘流程分析，带你一步步深入了解View(三)](http://blog.csdn.net/guolin_blog/article/details/17045157)

- [Android自定义View的实现方法，带你一步步深入了解View(四)](http://blog.csdn.net/guolin_blog/article/details/17357967)

- [Android 深入理解Android中的自定义属性](http://blog.csdn.net/lmj623565791/article/details/45022631)

- [公共技术点之 View 事件传递](http://a.codekk.com/detail/Android/Trinea/%E5%85%AC%E5%85%B1%E6%8A%80%E6%9C%AF%E7%82%B9%E4%B9%8B%20View%20%E4%BA%8B%E4%BB%B6%E4%BC%A0%E9%80%92)

- [Android 触摸及手势操作GestureDetector](http://blog.csdn.net/xyz_lmn/article/details/16826669)

- [通过Spannable对象设置textview的样式](http://www.cnblogs.com/tianzhijiexian/p/4222393.html)

- [Android 5.0(Lollipop)中的SurfaceTexture，TextureView, SurfaceView和GLSurfaceView](http://blog.csdn.net/jinzhuojun/article/details/44062175)

- [Android视图SurfaceView的实现原理分析](http://blog.csdn.net/luoshengyang/article/details/8661317)

- [Android硬件加速](http://blog.csdn.net/xushuaic/article/details/38975915)

- [Android手写优化-平滑的签名效果实现](http://zmywly8866.github.io/2014/12/29/android-smooth-signatures.html)

- [Android手写优化-更为平滑的签名效果实现](http://zmywly8866.github.io/2014/12/29/android-smoother-signatures.html)

# 五、优质开源项目

- [awesome-android-ui](https://github.com/wasabeef/awesome-android-ui)

- [Android 开源项目分类汇总](https://github.com/Trinea/android-open-project)

- [Android平台下的原生Markdown解析器](https://github.com/zzhoujay/Markdown)

- [MarkdownEditors](https://github.com/qinci/MarkdownEditors)

- [Android开源弹幕引擎](https://github.com/Bilibili/DanmakuFlameMaster)

- [DraweeTextView](https://github.com/Bilibili/drawee-text-view)

- [Android图文混排控件MixtureTextView](https://github.com/hongyangAndroid/MixtureTextView)

- [markers](https://github.com/dsandler/markers)

- Android系统应用源码中的各种自定义控件

# 六、忠告

&emsp;&emsp;千万别一言不合就自定义，能够用Android基础控件解决的问题就尽量用基础控件，其次是用基础控件的组合，如果是确实有必要自定义才考虑自定义。自定义的控件既需要耗费较长的开发时间，又不一定能保证有基础控件那么高的效率（基础控件都是谷歌优化过了的）。

