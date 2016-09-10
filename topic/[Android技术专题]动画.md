&emsp;&emsp;在Android应用开发中我们每天都在接触动画（Activity开、关，页面滑动、点击按钮等都有动画效果），但入门容易，真正做好很难。如果要把效果做得自然、看上去很爽需要你有一定的美感；如果要把效果做得足够流畅，你需要深入研究动画，弄懂其中的细节和原理。

### 一、在Android平台可以通过如下几种方式实现动画效果

#### 补间动画

&emsp;&emsp;补间动画即开发者只需要指定开始、结束的关键帧，动画变化的“中间帧”则由系统根据补间动画加速器算法计算补齐。补间动画包括旋转(RotateAnimation)、透明(AlphaAnimation)、位移(TranslateAnimation)和缩放(ScaleAnimation)。补间动画的使用有如下几个特点：

- 某个动画的实现既可以是一种动画效果，也可以是多种补间动画的组合（需要用到set标签或者AnimationSet类）；

- 补间动画的实现既可以在xml中定义，也可以通过代码实现；

- 补间动画的变化规律可以通过插值器（补间动画加速器）Interpolator控制，系统定义了常见的插值器，如果不能满足你的要求，可以自定义插值器；

- 补间动画的难点在于根据参考点对位置的关系换算，如果处理不好，可能要弄很久。

#### 帧动画

&emsp;&emsp;帧动画是一帧一帧的显示动画效果。创建帧动画除了通过在xml中用animation-list作为根节点、item定义每一帧要显示的图片之外，也可以用Java代码的形式来创建帧动画。帧动画需要用到的核心类是AnimationDrawable。

&emsp;&emsp;帧动画的特点是：

- 实现简单；

- 效率低；

- 视觉工作量大。

#### 属性动画

&emsp;&emsp;属性动画是在Android 3.0开始引入的一种动画模式（如果想在Android 3.0之前的版本中使用属性动画，可以引用JakeWharton开源的[NineOldAndroids](https://github.com/JakeWharton/NineOldAndroids)），有了属性动画，可以考虑再也不使用补间动画和帧动画了，它功能强大、使用灵活，强烈建议在实际编码中使用属性动画。

&emsp;&emsp;关于属性动画的介绍网上已经有很多优秀的技术文章，比如郭霖的[Android属性动画完全解析(上)，初识属性动画的基本用法](http://blog.csdn.net/guolin_blog/article/details/43536355)、[Android属性动画完全解析(中)，ValueAnimator和ObjectAnimator的高级用法](http://blog.csdn.net/guolin_blog/article/details/43816093)和[Android属性动画完全解析(下)，Interpolator和ViewPropertyAnimator的用法](http://blog.csdn.net/guolin_blog/article/details/44171115)，所以不再赘述。

#### GIF

&emsp;&emsp;GIF是一种图片格式，它分为静态GIF和动画GIF两种，扩展名为.gif，是一种压缩位图格式，支持透明背景图像，适用于多种操作系统，“体型”很小，网上很多小动画都是GIF格式。其实GIF是将多幅图像保存为一个图像文件，从而形成动画，最常见的就是通过一帧帧的动画串联起来的搞笑gif图，所以归根到底GIF仍然是图片文件格式。但GIF只能显示256色。

&emsp;&emsp;GIF有天然的劣势，图片质量很低，有很多应用的Splash界面会直接播放gif格式的文件，避免通过xml和代码实现起来过于复杂，github上优秀的GIF开源库有：[GifView](https://github.com/Cutta/GifView)和[android-gif-drawable](https://github.com/koral--/android-gif-drawable)。

#### 视频

&emsp;&emsp;有很多应用的引导界面是直接播放的视频，特别是一些大型的游戏，反编译它的代码可以看到，就是一个视频文件。视频文件在分辨率适配上会比较麻烦，制作成本也比较大。

#### SVG

&emsp;&emsp;SVG是可缩放矢量图形，他是基于可扩展标记语言，用于描述二维矢量图形的一种图形格式。它严格遵从XML语法，并用文本格式的描述性语言来描述图像内容，因此是一种和图像分辨率无关的矢量图形格式。SVG格式具备目前网络流行的jpg和png等格式无法具备的优势：可以任意放大图形显示，但绝不会以牺牲图像质量为代价;可在SVG图像中保留可编辑和可搜寻的状态;平均来讲，SVG文件比其它格式的图像文件要小很多，因而下载也很快。关于SVG更为详细的介绍可以参考这篇文章：[Android实现炫酷SVG动画效果](http://blog.csdn.net/crazy__chen/article/details/47728241)。


#### 其它

&emsp;&emsp;还可以通过自定义类，用定时器给ImageView更换背景图片的方式实现动画；如果使用到了游戏引擎，游戏引擎中对动画的渲染效率有增强，动画效果会很流畅。

### 二、使用动画过程中遇到过哪些坑

- 在有动画效果的界面，强烈建议不要关掉硬件加速，在没有硬件加速的环境下运行动画效果会显得不流畅；

- 动画（特别是循环播放的动画）效果功耗很大（因为CPU和GPU在高负荷持续工作呀），为了降低功耗，尽量控制动画的大小，以及动画出现的时机，最好是用户触发某个操作后执行动画效果，不要一进入界面就执行动画；

- 谨慎使用AnimationDrawable，在5.0之前会很耗内存；并且AnimationDrawable中的每一帧图片在使用完后不能释放，否则在下次使用时会直接报异常；还有每一帧图片不宜过大，否则会卡成翔；

### 三、有哪些关于动画的优质开源项目

- [awesome-android-ui](https://github.com/wasabeef/awesome-android-ui)

- [BaseAnimation](https://github.com/z56402344/BaseAnimation)

- [AnimationEasingFunctions](https://github.com/daimajia/AnimationEasingFunctions)

- [AndroidViewAnimations](https://github.com/daimajia/AndroidViewAnimations)

- [Yalantis组织-开源了很多优秀的动画库](https://github.com/Yalantis?page=1)

- [android-pathview](https://github.com/geftimov/android-pathview)

### 四、参考资料

- [如何高效学习Android动画？](https://www.zhihu.com/question/27718787)

- [Property Animation](https://developer.android.com/guide/topics/graphics/prop-animation.html)

- [View Animation](https://developer.android.com/guide/topics/graphics/view-animation.html)

- [Drawable Animation](https://developer.android.com/guide/topics/graphics/drawable-animation.html)

- [公共技术点之 Android 动画基础](http://a.codekk.com/detail/Android/lightSky/%E5%85%AC%E5%85%B1%E6%8A%80%E6%9C%AF%E7%82%B9%E4%B9%8B%20Android%20%E5%8A%A8%E7%94%BB%E5%9F%BA%E7%A1%80)

- [Android属性动画完全解析(上)，初识属性动画的基本用法](http://blog.csdn.net/guolin_blog/article/details/43536355)

- [Android属性动画完全解析(中)，ValueAnimator和ObjectAnimator的高级用法](http://blog.csdn.net/guolin_blog/article/details/43816093)

- [Android属性动画完全解析(下)，Interpolator和ViewPropertyAnimator的用法](http://blog.csdn.net/guolin_blog/article/details/44171115)

- [Android实现炫酷SVG动画效果](http://blog.csdn.net/crazy__chen/article/details/47728241)