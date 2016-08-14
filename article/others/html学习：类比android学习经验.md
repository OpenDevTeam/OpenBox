## html学习：类比android学习经验 ##
## 前言 ##

&emsp;&emsp;本文适合有一定android基础想初步了解学习web前端开发的朋友。以下均为个人理解，很多可能不是百分百就是完全能类比过去，只是比较相似，希望能帮助大家比较好地理解。如有不准确的地方，欢迎大家留言纠正，共同学习！

###demo效果图###

----------

我们先来看看两张效果图。

html：

![](http://i.imgur.com/MQ33JU0.png)

android:

![](http://i.imgur.com/d9h86wC.png)

我们尽量将两个布局弄得比较接近，方便我们后面进行比较。html的结构主要由3个文件组成：index.css、index.html和index.js。而android的我们就相对比较熟悉了，这里就不做解释了。

###html篇###

----------

- **html-->layout.xml布局。**我们先来说说html，其实html比较像是固定网页的整体框架和一些静态固定的布局。其他一些动态的布局就需要结果js和css来实现。特别像android中的布局文件。下面我们结合例子中的代码来看看。

index.html：

![](http://i.imgur.com/VpyCjg7.png)

android的布局文件：

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout
    	xmlns:android="http://schemas.android.com/apk/res/android"
    	android:layout_width="match_parent"
    	android:layout_height="match_parent"
    	android:orientation="vertical">

    	<TextView
        	android:text="登录"
        	android:layout_width="wrap_content"
        	android:layout_height="wrap_content" />

    	<LinearLayout
        	android:layout_width="wrap_content"
        	android:layout_height="wrap_content"
        	android:orientation="horizontal">
        	<TextView
        	    android:layout_width="wrap_content"
        	    android:layout_height="wrap_content"
    	        android:text="用户名:"/>
    	    <EditText
    	        android:id="@+id/nameInput"
    	        android:layout_width="200dp"
    	        android:layout_height="50dp" />
    	</LinearLayout>

    	<LinearLayout
    	    android:layout_width="wrap_content"
    	    android:layout_height="wrap_content"
    	    android:orientation="horizontal">
    	    <TextView
    	        style="@style/pwSize"
    	        android:layout_width="wrap_content"
    	        android:layout_height="wrap_content"
    	        android:text="密码:"/>
    	    <EditText
    	        android:layout_width="200dp"
    	        android:layout_height="50dp" />
    	</LinearLayout>

    	<LinearLayout
    	    android:layout_width="wrap_content"
    	    android:layout_height="wrap_content"
    	    android:orientation="horizontal">
    	    <Button
    	        android:layout_width="wrap_content"
    	        android:layout_height="wrap_content"
    	        android:onClick="onSubmit"
    	        android:text="确定"/>
    	</LinearLayout>

	</LinearLayout>
	

从上面的代码我们看到，无论是从布局结构、控件的名称、还是其里面控件使用的属性，都有很多很相像的地方。就拿例子中的确定按钮控件来看，html:

![](http://i.imgur.com/8bMOdB1.png)

android中按钮的布局：

    <Button
    	android:layout_width="wrap_content"
    	android:layout_height="wrap_content"
    	android:onClick="onSubmit"
    	android:text="确定"/>

可以看到，其实它们两个的写法是非常类似的，包括标签名，点击事件的声明。

除此之外，也有不少其实功能相似，但只是名字是不一样的：

- **div-->Linearlayout。**从上面代码可以看到，div的使用很像LinearLayut。div应该是html里面使用率算是最高的，随便打开一个网站，看他的源代码，基本上铺天盖地的都是div。其实div是一个容器，就相当于android里面的layout，可以方便我们组成各式各样的布局。

- **input-->EditText。**input标签就类似于android的输入框EditText。但是input还相对EditText要更强大点。就像它的名字一样，其实它是输入控件的集合。什么意思呢？它可以根据type属性，更改它的输入类型。也就是说如果把type赋值成“button”，它就可以变成一个按钮；赋值成“checkbox”，就可以变成一个单选框等等...这里就不一一列出来了，想进一步了解的可以查看下相应文档。

- **ul-->ListView。**说一个例子里没有的标签----ul和ol，它们同样是使用率很高的标签。ul和ol都是列表标签，区别就是ul是无序的，ol是有序的。ul就比较像android中的ListView。但是个人感觉ul就更加灵活点，比如说它能轻松地实现横向纵向布局（其实是设置li的样式），但是，要把android中的ListView横过来，那基本是不可能的，但是可以用别的代替。如果你不依赖任何工具框架，用原生态的css开发，那么你就会发现ul的用途还是挺广的，包括能实现导航栏，tabbar，下拉框等等...

###CSS篇###

----------

- **css-->styles.xml和anim中的.xml文件集合。**css整体来说，主要负责html中的样式和动画，这个就比较像是android中的styles.xml + anim中的.xml文件。css中很多样式属性，你都可以根据android的猜到个7、8成，比如width,margin-top,color,font-size等等...下面，我们也结合例子中的代码看看。

index.css:

![](http://i.imgur.com/Lko46O1.png)

android的styles.xml:

	<style name="pwSize" parent="pwMarginLeft">
        <item name="android:textSize">30sp</item>
    </style>

    <style name="pwMarginLeft" >
        <item name="android:layout_marginLeft">20dp</item>
    </style>

&emsp;&emsp;在css中前缀带“.”的，就是寻找其对应名称的class。“#”是对应id。而没有带任何前缀的，就是所有这类型标签默认会添加这个css样式。

- **class。**class在css中是非常重要的。class应该算是一个别名的作用。个人觉得其实就像android里面style定义的id差不多。不过，android中一般来说一个控件只能定义一个style。像例子中，如果我们要把设置文字大小和设置左边距像css中定义成两个的话，就只能通过parent="pwMarginLeft"来实现继承，从而能使用到两个style。而在html里面，你可以通过空格隔开来定义多个class。如果不使用css各种工具框架，你会发现增加和删除class结合css可以实现各种页面变化的效果，包括显示隐藏，颜色、大小修改等等。

&emsp;&emsp;如果你使用类似bootstrap这样的css工具框架，你不难发现，他就是基本依靠class来完成各种样式功能的调用。

###javascript篇###

----------

- **js-->逻辑处理的.java。**js整体来说，主要负责html中的逻辑处理部分，相当于controller的角色。老规矩，我们还是结合例子中的代码来看。

index.js:

![](http://i.imgur.com/MDTtczX.png)

android对应的代码应该是：

	public void onSubmit(View v){
        EditText editText = (EditText)findViewById(R.id.nameInput);
        Log.d("demoMainActivity",editText.getText().toString());
    }

这两段代码实现的功能是一样的。先获取输入框：html的document.getElementById()就是android的findViewById()。接着是通过控件获取其值。最后就是打印值，console.log()就是android的Log。

- **function。**function是js里面的函数。不过，它也可以充当类的角色。其实在js里面，function和var都可以实现类的效果的。可能刚刚开始从android到接触js，最看不习惯的就是function里面的参数（因为没有数据类型），和返回值不需要在function前面定义返回的数据类型，直接return返回就可以。这个可能也是跟html的发展历史有相当的关系。不过function的使用还是挺方便的，它可以在function里多重嵌套function,但是这样的话，相对代码的整洁度就不会很高。

- **var-->Object。**对于js中的var，我个人觉得就比较像是android中的Object。无论是各种数据类型int、string、boolean...，还是结构体,甚至是函数体都可以赋值给它。这就跟android中的Object一样，所有东西都是继承于Object,所以也都可以赋值给它。

下面我们另外举一个例子来了解下var:
    
![](http://i.imgur.com/HWFiVXL.png)    

再看看android中的Object:

    Object num = 1;
    Object str = "haha";
    Object bl = false;

上图可以看到，数据类型、结构体和函数，其实都是可以赋值给var的，所以大家在命名的时候，需要注意命名规则。本人就试过，一个变量和一个函数命名是一样的，导致调用函数的时候，一直报错，说没有找到（先定义变量再定义的函数）。

- **"==="。**说到var了，可以顺便说说"==="。这个对于一个android开发者来说，"=="肯定用得非常多，但是"==="可能没有见过。我们先来看一段代码：

![](http://i.imgur.com/Src3pvJ.png)

输出：

![](http://i.imgur.com/RG7GGNy.png)

从输出可以很清楚地看到，"=="和"==="的区别："=="不区分类型，而"==="会区分数据类型。当然还有"!==",也是同理。

###怎么引用呢？###

----------

&emsp;&emsp;说了半天，那究竟html是怎么引用css和js的呢？

&emsp;&emsp;细心的同学可能早就找到他们了，他们就在一开始html的代码里。在<head>标签中，通过<link>和<script>我们就可以分别引入对应的css和js文件。

###总结###

----------

&emsp;&emsp;总的来说，在html的学习过程中还是能类比android来学习的。当然今天说到的这些，也只是html的冰山一角，正在开发中用到的远远不止这些。也有很多很好用的工具框架，例如：angularjs、bootstrap、less等等...我们都可以学习下，能让我们的工作事半功倍！希望本文对于刚想开始了解html，而且对android有一定开发经验的同学有所帮助。
