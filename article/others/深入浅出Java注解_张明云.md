# 深入浅出Java注解

## 什么是注解

&emsp;&emsp;注解对于开发人员来讲既熟悉又陌生，熟悉是因为只要你是做开发，都会用到注解（常见的@Override）；陌生是因为即使不使用注解也照常能够进行开发；注解不是必须的，但了解注解有助于我们深入理解某些第三方框架（比如Android Support Annotations、JUnit、xUtils、ActiveAndroid等），提高工作效率。

&emsp;&emsp;Java注解又称为标注，是Java从1.5开始支持加入源码的特殊语法元数据；Java中的类、方法、变量、参数、包都可以被注解。这里提到的元数据是描述数据的数据，结合实例来说明：
	
	<string name="app_name">AnnotionDemo</string>

&emsp;&emsp;这里的"app_name"就是描述数据"AnnotionDemo"的数据，这是在配置文件中写的，注解是在源码中写的，如下所示：

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        new Thread(new Runnable(){
            @Override
            public void run(){
                setTextInOtherThread();
            }
        }).start();
    }

&emsp;&emsp;在上面的代码中，在MainActivity.java中复写了父类Activity.java的onCreate方法，使用到了@Override注解。但即使不加上@Override注解标记代码，程序也能够正常运行。那这里的@Override注解有什么用呢？使用它有什么好处？事实上，@Override是告诉编译器这个方法是一个重写方法，如果父类中不存在该方法，编译器会报错，提示该方法不是父类中的方法。如果不小心拼写错误，将onCreate写成了onCreat，而且没有使用@Override注解，程序依然能够编译通过，但运行结果和期望的大不相同。从示例可以看出，注解有助于阅读代码。

&emsp;&emsp;使用注解很简单，根据注解类的@Target所修饰的对象范围，可以在类、方法、变量、参数、包中使用“@+注解类名+[属性值]”的方式使用注解。比如：

	@UiThread
    private void setTextInOtherThread(@StringRes int resId){
        TextView threadTxtView = (TextView)MainActivity.this.findViewById(R.id.threadTxtViewId);
        threadTxtView.setText(resId);
    }

**特别说明：**

- 注解仅仅是元数据，和业务逻辑无关，所以当你查看注解类时，发现里面没有任何逻辑处理；

- javadoc中的@author、@version、@param、@return、@deprecated、@hide、@throws、@exception、@see是标记，并不是注解；

## 注解的作用

- **格式检查：**告诉编译器信息，比如被@Override标记的方法如果不是父类的某个方法，IDE会报错；

- **减少配置：**运行时动态处理，得到注解信息，实现代替配置文件的功能；

- **减少重复工作：**比如第三方框架xUtils，通过注解@ViewInject减少对findViewById的调用，类似的还有（JUnit、ActiveAndroid等）；

## 注解是如何工作的？

&emsp;&emsp;注解仅仅是元数据，和业务逻辑无关，所以当你查看注解类时，发现里面没有任何逻辑处理，eg：

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ViewInject {
	
	    int value();
	
	    /* parent view id */
	    int parentId() default 0;
	}

&emsp;&emsp;如果注解不包含业务逻辑处理，必然有人来实现这些逻辑。注解的逻辑实现是元数据的用户来处理的，注解仅仅提供它定义的属性（类/方法/变量/参数/包）的信息，注解的用户来读取这些信息并实现必要的逻辑。当使用java中的注解时（比如@Override、@Deprecated、@SuppressWarnings）JVM就是用户，它在字节码层面工作。如果是自定义的注解，比如第三方框架[ActiveAndroid](https://github.com/pardom/ActiveAndroid)，它的用户是每个使用注解的类，所有使用注解的类都需要继承Model.java，在Model.java的构造方法中通过反射来获取注解类中的每个属性：

	public TableInfo(Class<? extends Model> type) {
		mType = type;

		final Table tableAnnotation = type.getAnnotation(Table.class);

        if (tableAnnotation != null) {
			mTableName = tableAnnotation.name();
			mIdName = tableAnnotation.id();
		}
		else {
			mTableName = type.getSimpleName();
        }

        // Manually add the id column since it is not declared like the other columns.
        Field idField = getIdField(type);
        mColumnNames.put(idField, mIdName);

        List<Field> fields = new LinkedList<Field>(ReflectionUtils.getDeclaredColumnFields(type));
        Collections.reverse(fields);

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                final Column columnAnnotation = field.getAnnotation(Column.class);
                String columnName = columnAnnotation.name();
                if (TextUtils.isEmpty(columnName)) {
                    columnName = field.getName();
                }

                mColumnNames.put(field, columnName);
            }
        }

	}

## 注解和配置文件的区别

&emsp;&emsp;通过上面的描述可以发现，其实注解干的很多事情，通过配置文件也可以干，比如为类设置配置属性；但注解和配置文件是有很多区别的，在实际编程过程中，注解和配置文件配合使用在工作效率、低耦合、可拓展性方面才会达到权衡。

**配置文件：**

使用场合：

- 外部依赖的配置，比如build.gradle中的依赖配置；

- 同一项目团队内部达成一致的时候；

- 非代码类的资源文件（比如图片、布局、数据、签名文件等）；

优点：

- 降低耦合，配置集中，容易扩展，比如Android应用多语言支持；

- 对象之间的关系一目了然，比如strings.xml；

- xml配置文件比注解功能齐全，支持的类型更多，比如drawable、style等；

缺点：

- 繁琐；

- 类型不安全，比如R.java中的都是资源ID，用TextView的setText方法时传入int值时无法检测出该值是否为资源ID，但@StringRes可以；

**注解：**

使用场合：

- 动态配置信息；

- 代为实现程序逻辑（比如xUtils中的@ViewInject代为实现findViewById）；

- 代码格式检查，比如Override、Deprecated、NonNull、StringRes等，便于IDE能够检查出代码错误；

优点：

- 在class文件中，提高程序的内聚性；

- 减少重复工作，提高开发效率，比如findViewById。

缺点：

- 如果对annotation进行修改，需要重新编译整个工程；

- 业务类之间的关系不如XML配置那样一目了然；

- 程序中过多的annotation，对于代码的简洁度有一定影响；

- 扩展性较差；

## 常见注解

### API

&emsp;&emsp;Android开发过程中使用到的注解主要来自如下几个地方：

- Android SDK：在包android.annotation下；

- Android Annotation Support包：在包android.support.annotation下；

- JDK：在包java.lang下；

- 第三方框架中的自定义注解；

### 最常见注解

@Override

&emsp;&emsp;属于标记注解，不需要设置属性值；只能添加在方法的前面，用于标记该方法是复写的父类中的某个方法，如果在父类没有的方法前面加上@Override注解，编译器会报错：

	@Target({ElementType.METHOD})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Override {
	}

@Deprecated

&emsp;&emsp；属于标记注解，不需要设置属性值；可以对构造方法、变量、方法、包、参数标记，告知用户和编译器被标记的内容已不建议被使用，如果被使用，编译器会报警告，但不会报错，程序也能正常运行：

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PACKAGE, ElementType.PARAMETER, ElementType.TYPE})
	public @interface Deprecated {
	}

@SuppressWarnings

&emsp;&emsp；可以对构造方法、变量、方法、包、参数标记，用于告知编译器忽略指定的警告，不用再编译完成后出现警告信息：

	@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE})
	@Retention(RetentionPolicy.SOURCE)
	public @interface SuppressWarnings {
	    String[] value();
	}

@TargetApi

&emsp;&emsp;可以对接口、方法、构造方法标记，如果在应用中指定minSdkVersion为8，但有地方需要使用API 11中的方法，为了避免编译器报错，在调用API11中方法的接口、方法或者构造方法前面加上@Target(11)，这样该方法就可以使用<=11的API接口了。虽然这样能够避免编译器报错，但在运行时需要注意，不能在API低于11的设备中使用该方法，否则会crash（可以获取程序运行设备的API版本来判断是否调用该方法）：

	@Target({TYPE, METHOD, CONSTRUCTOR})
	@Retention(RetentionPolicy.CLASS)
	public @interface TargetApi {
	    /**
	     * This sets the target api level for the type..
	     */
	    int value();
	}

@SuppressLint

&emsp;&emsp;和@Target的功能差不多，但使用范围更广，主要用于避免在lint检查时报错：

	@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
	@Retention(RetentionPolicy.CLASS)
	public @interface SuppressLint {
	    /**
	     * The set of warnings (identified by the lint issue id) that should be
	     * ignored by lint. It is not an error to specify an unrecognized name.
	     */
	    String[] value();
	}

### Android Annotation Support包中的注解介绍：

&emsp;&emsp;Android support library从19.1版本开始引入了一个新的注解库，它包含很多有用的元注解，你能用它们修饰你的代码，帮助你发现bug。Support library自己本身也用到了这些注解，所以作为support library的用户，Android Studio已经基于这些注解校验了你的代码并且标注其中潜在的问题。

&emsp;&emsp;这些注解是作为一个support包提供给开发者使用，要使用他们，需要在build.gradle中添加对android support-annotations的依赖：

	compile 'com.android.support:support-annotations:22.2.0'

&emsp;&emsp;support包中的注解分为如下几大类：

- **Nullness注解：**

&emsp;&emsp;**@Nullable:**用于标记方法参数或者返回值可以为空；

&emsp;&emsp;**@NonNull:**用于标记方法参数或者返回值不能为空，如果为空编译器会报警告；

- **资源类型注解：**

&emsp;&emsp;这类注解主要用于标记方法的参数必须要是指定的资源类型，如果不是，IDE就会报错；因为资源文件都是静态的，所以在编写代码时IDE就知道传值是否错误，可以避免传的资源id错误导致运行时异常。资源类型注解包括@AnimatorRes、@AnimRes、@AnyRes、@ArrayRes、@BoolRes、@ColorRes、@DimenRes、@DrawableRes、@FractionRes、@IdRes、@IntgerRes、@InterpolatorRes、@LayoutRes、@MenuRes、@PluralsRes、@RawRes、@StringRes、@StyleableRes、@StyleRes、@TransitionRes、@XmlRes。

- **类型定义注解：**

&emsp;&emsp;这类注解用于检查“魔幻数”，很多时候，我们使用整型常量代替枚举类型（性能考虑），例如我们有一个IceCreamFlavourManager类，它具有三种模式的操作：VANILLA，CHOCOLATE和STRAWBERRY。我们可以定义一个名为@Flavour的新注解，并使用@IntDef指定它可以接受的值类型：

	public class IceCreamFlavourManager {
	    private int flavour;
	
	    public static final int VANILLA = 0;
	    public static final int CHOCOLATE = 1;
	    public static final int STRAWBERRY = 2;
	
	    @IntDef({VANILLA, CHOCOLATE, STRAWBERRY})
	    public @interface Flavour {
	    }
	
	    @Flavour
	    public int getFlavour() {
	        return flavour;
	    }
	
	    public void setFlavour(@Flavour int flavour) {
	        this.flavour = flavour;
	    }
	}

&emsp;&emsp;这时如果我们使用错误的整型值调用IceCreamFlavourManager.setFlavour时，IDE将报错如下：

![wrong_flavour_error](http://ww3.sinaimg.cn/large/6d17e381gw1f55fz8thrfj20nu06xjt4.jpg)

&emsp;&emsp;IDE甚至会提示我们可以使用的有效的取值：

![ide_suggests_flavours](http://ww4.sinaimg.cn/large/6d17e381gw1f55fzijwfuj20w60a3juj.jpg)

&emsp;&emsp;我们也可以指定整型值作为标志位，也就是说这些整型值可以使用’｜’或者’&’进行与或等操作。如果我们把@Flavour定义为如下标志位：

	@IntDef(flag = true, value = {VANILLA, CHOCOLATE, STRAWBERRY})
    public @interface Flavour {
    }

&emsp;&emsp;那么可以如下调用：

	iceCreamFlavourManager.setFlavour(IceCreamFlavourManager.VANILLA & IceCreamFlavourManager.CHOCOLATE);

- **线程注解：**

&emsp;&emsp;用于标记指定的方法、类（如果一个类中的所有方法都有相同的线程需求，就可以对这个类进行注解，比如View.java就被@UIThread所标记）只能在指定的线程类中被调用，包括：@UiThread、@MainThread、@WorkerThread、@BinderThread；以@UIThread为例，说明这类注解的使用方法：

	public class MainActivity extends Activity{

	    @Override
	    protected void onCreate(Bundle savedInstanceState){
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main_layout);
	        new Thread(new Runnable(){
	            @Override
	            public void run(){
	                setTextInOtherThread(R.string.app_name);
				 // setTextInOtherThread2(R.string.app_name);
	            }
	        }).start();
	    }
	
	    @UiThread
	    private void setTextInOtherThread(@StringRes int resId){
	        TextView threadTxtView = (TextView)MainActivity.this.findViewById(R.id.threadTxtViewId);
	        threadTxtView.setText(resId);
	    }
	
	    private void setTextInOtherThread2(@StringRes final int resId){
	        MainActivity.this.runOnUiThread(new Runnable(){
	            @Override
	            public void run(){
	                TextView threadTxtView = (TextView)MainActivity.this.findViewById(R.id.threadTxtViewId);
	                threadTxtView.setText(resId);
	            }
	        });
	    }
	}

&emsp;&emsp;**@UIThread和@MainThread的区别：**在进程里只有一个主线程。这个就是@MainThread。同时这个线程也是一个@UiThread。比如activity的主要窗口就运行在这个线程上。然而它也有能力为应用创建其他线程。这很少见，一般具备这样功能的都是系统进程。通常是把和生命周期有关的用@MainThread标注，和View层级结构相关的用@UiThread标注。但是由于@MainThread本质上是一个@UiThread，而大部分情况下@UiThread又是一个@MainThread，所以工具(lint ,Android Studio,等等)可以把他们互换，所以你能在一个可以调用@MainThread方法的地方也能调用@UiThread方法，反之亦然。

- **GRB颜色值注解：**

&emsp;&emsp;用于标记传递的颜色值必须是整型值，并且不能是color资源ID；当你的API期望一个颜色资源的时候，可以用@ColorRes标注，但是当你有一个相反的使用场景时，这种用法就不可用了，因为你并不是期望一个颜色资源id，而是一个真实的RGB或者ARGB的颜色值。在这种情况下，你可以使用@ColorInt注解，表示你期望的是一个代表颜色的整数值：

	public void setTextColor(@ColorInt int color);

&emsp;&emsp;有了这个，当你传递一个颜色id而不是颜色值的时候，lint就会标记出这段不正确的代码：

![ColorInf](http://ww4.sinaimg.cn/large/6d17e381gw1f55gcb0joij20k302vdg6.jpg)

- **值约束注解：**

&emsp;&emsp;用于标记参数必须是指定类型的值，并且值的范围必须在约束的范围内，包括@Size、@IntRange、@FloatRange。如果你的参数是一个float或者double类型，并且一定要在某个范围内，你可以使用@FloatRange注解：

	public void setAlpha(@FloatRange(from=0.0, to=1.0) float alpha){
		...
	}

&emsp;&emsp;如果有人使用该API的时候传递一个0-255的值，比如尝试调用setAlpha(128)，那么工具就会捕获这一问题：

![值约束错误](http://ww4.sinaimg.cn/large/6d17e381gw1f55gfr07lvj20k702ldfz.jpg)

&emsp;&emsp;把这些注解应用到参数上是非常有用的，因为用户很有可能会提供错误范围的参数，比如上面的setAlpha例子，有的API是采用0-255的方式，而有的是采用0-1的float值的方式。

&emsp;&emsp;对于数据、集合以及字符串，你可以用@Size注解参数来限定集合的大小(当参数是字符串的时候，可以限定字符串的长度)。举几个例子:

&emsp;&emsp;1、集合不能为空: @Size(min=1)；

&emsp;&emsp;2、字符串最大只能有23个字符: @Size(max=23)；

&emsp;&emsp;3、数组只能有2个元素: @Size(2)；

&emsp;&emsp;4、数组的大小必须是2的倍数 (例如图形API中获取位置的x/y坐标数组: @Size(multiple=2)。

![Size注解](http://ww1.sinaimg.cn/large/6d17e381gw1f55gi1hzq2j20k902v0sw.jpg)

- **权限注解：**

&emsp;&emsp;如果你的方法需要调用者有特定的权限，你可以使用@RequiresPermission注解：

	@RequiresPermission(Manifest.permission.SET_WALLPAPER)
	public abstract void setWallpaper(Bitmap bitmap) throws IOException;

&emsp;&emsp;如果你至少需要权限集合中的一个，你可以使用anyOf属性：

	@RequiresPermission(anyOf = {
	    Manifest.permission.ACCESS_COARSE_LOCATION,
	    Manifest.permission.ACCESS_FINE_LOCATION})
	public abstract Location getLastKnownLocation(String provider);

&emsp;&emsp;如果你同时需要多个权限，你可以用allOf属性：
	
	@RequiresPermission(allOf = {
	    Manifest.permission.READ_HISTORY_BOOKMARKS, 
	    Manifest.permission.WRITE_HISTORY_BOOKMARKS})
	public static final void updateVisitedHistory(ContentResolver cr, String url, boolean real) {

&emsp;&emsp;对于intents的权限，可以直接在定义的intent常量字符串字段上标注权限需求(他们通常都已经被@SdkConstant注解标注过了):
	
	@RequiresPermission(android.Manifest.permission.BLUETOOTH)
	public static final String ACTION_REQUEST_DISCOVERABLE =
	            "android.bluetooth.adapter.action.REQUEST_DISCOVERABLE";

&emsp;&emsp;对于content providers的权限，你可能需要单独的标注读和写的权限访问，所以可以用@Read或者@Write标注每一个权限需求：
	
	@RequiresPermission.Read(@RequiresPermission(READ_HISTORY_BOOKMARKS))
	@RequiresPermission.Write(@RequiresPermission(WRITE_HISTORY_BOOKMARKS))
	public static final Uri BOOKMARKS_URI = Uri.parse("content://browser/bookmarks");

![](http://ww3.sinaimg.cn/large/6d17e381gw1f55gla6jj8j20ka02tt98.jpg)

- **复写方法注解：**

&emsp;&emsp;如果你的API允许使用者重写你的方法，但你又需要你自己的方法(父方法)在重写的时候也被调用，这时候你可以使用@CallSuper标注：

	@CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {

&emsp;&emsp;用了这个后，当重写的方法没有调用父方法时，工具就会给予警告提示：

![](http://ww4.sinaimg.cn/large/6d17e381gw1f55gn4x8irj20jv037wer.jpg)

- **返回值注解：**

&emsp;&emsp;如果你的方法有返回值，你期望调用者用这个值做些事情，那么你可以使用@CheckResult注解标注这个方法。

&emsp;&emsp;你并不需要为每个非空方法都进行标注。它主要的目的是帮助哪些容易被混淆，难以被理解的API的使用者。

&emsp;&emsp;比如，可能很多开发者都对String.trim()一知半解，认为调用了这个方法，就可以让字符串改变以去掉空白字符。如果这个方法被@CheckResult标注，工具就会对那些没有使用trim()返回结果的调用者发出警告。

&emsp;&emsp;Android中，Context#checkPermission这个方法已经被@CheckResult标注了：

	@CheckResult(suggest="#enforcePermission(String,int,int,String)")
    public abstract int checkPermission(@NonNull String permission, int pid, int uid);

&emsp;&emsp;这是非常重要的，因为有些使用context.checkPermission的开发者认为他们已经执行了一个权限 —-但其实这个方法仅仅只做了检查并且反馈一个是否成功的值而已。如果开发者使用了这个方法，但是又不用其返回值，那么这个开发者真正想调用的可能是这个Context#enforcePermission方法，而不是checkPermission。

![](http://ww4.sinaimg.cn/large/6d17e381gw1f55goiipjmj20kc02l3z6.jpg)

- **测试可见注解：**

&emsp;&emsp;你可以把这个注解标注到类、方法或者字段上，以便你在测试的时候可以使用他们。

## 自定义注解

&emsp;&emsp;通过阅读注解类的源码可以发现，任何一个注解类都有如下特征：

- 注解类会被@interface标记；

- 注解类的顶部会被@Documented、@Retention、@Target、@Inherited这四个注解标记（@Documented、@Inherited可选，@Retention、@Target必须要有）；

@UiThread源码：

	@Documented
	@Retention(CLASS)
	@Target({METHOD,CONSTRUCTOR,TYPE})
	public @interface UiThread {
	}

### 元注解

&emsp;&emsp;上文提到的四个注解：@Documented、@Retention、@Target、@Inherited就是元注解，它们的作用是负责注解其它注解，主要是描述注解的一些属性，任何注解都离不开元注解（包括元注解自身，通过元注解可以自定义注解），元注解的用户是JDK，JDK已经帮助我们实现了这四个注解的逻辑。这四个注解在JDK的java.lang.annotation包中。对每个元注解的详细说明如下：

- **@Target：**

&emsp;&emsp;**作用：**用于描述注解的使用范围，即被描述的注解可以用在什么地方；

&emsp;&emsp;**取值：**

&emsp;&emsp;&emsp;&emsp;1、CONSTRUCTOR:构造器；

&emsp;&emsp;&emsp;&emsp;2、FIELD:实例；

&emsp;&emsp;&emsp;&emsp;3、LOCAL_VARIABLE:局部变量；

&emsp;&emsp;&emsp;&emsp;4、METHOD:方法；

&emsp;&emsp;&emsp;&emsp;5、PACKAGE:包；

&emsp;&emsp;&emsp;&emsp;6、PARAMETER:参数;

&emsp;&emsp;&emsp;&emsp;7、TYPE:类、接口(包括注解类型) 或enum声明。

&emsp;&emsp;**示例：**

	/***
	 *
	 * 实体注解接口
	 */
	@Target(value = {ElementType.TYPE})
	@Retention(value = RetentionPolicy.RUNTIME)
	public @interface Entity {
	    /***
	     * 实体默认firstLevelCache属性为false
	     * @return boolean
	     */
	    boolean firstLevelCache() default false;
	    /***
	     * 实体默认secondLevelCache属性为false
	     * @return boolean
	     */
	    boolean secondLevelCache() default true;
	    /***
	     * 表名默认为空
	     * @return String
	     */
	    String tableName() default "";
	    /***
	     * 默认以""分割注解
	     */
	    String split() default "";
	}

- **@Retention：**

&emsp;&emsp;**作用：**表示需要在什么级别保存该注解信息，用于描述注解的生命周期，即被描述的注解在什么范围内有效；

&emsp;&emsp;**取值：**

&emsp;&emsp;&emsp;&emsp;1、SOURCE:在源文件中有效，即源文件保留；

&emsp;&emsp;&emsp;&emsp;2、CLASS:在class文件中有效，即class保留；

&emsp;&emsp;&emsp;&emsp;3、RUNTIME:在运行时有效，即运行时保留；

&emsp;&emsp;**示例：**

	/***
	 * 字段注解接口
	 */
	@Target(value = {ElementType.FIELD})//注解可以被添加在实例上
	@Retention(value = RetentionPolicy.RUNTIME)//注解保存在JVM运行时刻,能够在运行时刻通过反射API来获取到注解的信息
	public @interface Column {
	    String name();//注解的name属性
	}

- **@Documented：**

&emsp;&emsp;**作用：**用于描述其它类型的annotation应该被作为被标注的程序成员的公共API，因此可以被例如javadoc此类的工具文档化。

&emsp;&emsp;**取值：**它属于标记注解，没有成员；

&emsp;&emsp;**示例：**

	@Documented
	@Retention(CLASS)
	@Target({METHOD,CONSTRUCTOR,TYPE})
	public @interface UiThread {
	}

- **@Inherited：**

&emsp;&emsp;**作用：**用于描述某个被标注的类型是可被继承的。如果一个使用了@Inherited修饰的annotation类型被用于一个class，则这个annotation将被用于该class的子类。

&emsp;&emsp;**取值：**它属于标记注解，没有成员；

&emsp;&emsp;**示例：**

	/**  
	 * @author wangsheng 
	 **/  
	@Inherited  
	public @interface Greeting {  
	    public enum FontColor{ BULE,RED,GREEN};  
	    String name();  
	    FontColor fontColor() default FontColor.GREEN;  
	} 

### 如何自定义注解

&emsp;&emsp;使用@interface自定义注解时，自动继承了java.lang.annotation.Annotation接口，由编译程序自动完成其他细节。在定义注解时，不能继承其他的注解或接口。@interface用来声明一个注解，其中的每一个方法实际上是声明了一个配置参数。方法的名称就是参数的名称，返回值类型就是参数的类型（返回值类型只能是基本类型、Class、String、enum）。可以通过default来声明参数的默认值。

- **自定义注解格式：**

		元注解
		public @interface 注解名{
			定义体；
		}

- **注解参数可支持的数据类型：**

&emsp;&emsp;1、所有基本数据类型（int,float,boolean,byte,double,char,long,short)；

&emsp;&emsp;2、String类型；

&emsp;&emsp;3、Class类型；

&emsp;&emsp;4、enum类型；

&emsp;&emsp;5、Annotation类型；

&emsp;&emsp;6、以上所有类型的数组。

&emsp;&emsp;**特别说明：**

&emsp;&emsp;&emsp;&emsp;1、注解类中的方法只能用public或者默认这两个访问权修饰，不写public就是默认，eg：

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface FruitColor {
	    public enum Color{ BULE,RED,GREEN};
	    Color fruitColor() default Color.GREEN;
	}

&emsp;&emsp;&emsp;&emsp;2、如果注解类中只有一个成员，最好把方法名设置为"value"，比如：

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface FruitName {
	    String value() default "";
	}

&emsp;&emsp;&emsp;&emsp;3、注解元素必须有确定的值，要么在定义注解的默认值中指定，要么在使用注解时指定，非基本类型的注解元素的值不可为null。因此, 使用空字符串或0作为默认值是一种常用的做法。

- **实例演示：**

&emsp;&emsp;**ToDo.java：**注解类

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface Todo {
	    public enum Priority {LOW, MEDIUM, HIGH}
	    public enum Status {STARTED, NOT_STARTED}    
	    String author() default "Yash";
	    Priority priority() default Priority.LOW;
	    Status status() default Status.NOT_STARTED;
	}

&emsp;&emsp;**BusinessLogic:**使用注解的类

	public class BusinessLogic {
	    public BusinessLogic() {
	        super();
	    }
	    
	    public void compltedMethod() {
	        System.out.println("This method is complete");
	    }    
	    
	    @Todo(priority = Todo.Priority.HIGH)
	    public void notYetStartedMethod() {
	        // No Code Written yet
	    }
	    
	    @Todo(priority = Todo.Priority.MEDIUM, author = "Uday", status = Todo.Status.STARTED)
	    public void incompleteMethod1() {
	        //Some business logic is written
	        //But its not complete yet
	    }
	
	    @Todo(priority = Todo.Priority.LOW, status = Todo.Status.STARTED )
	    public void incompleteMethod2() {
	        //Some business logic is written
	        //But its not complete yet
	    }
	}

&emsp;&emsp;**TodoReport.java：**解析注解信息
	
	public class TodoReport {
	    public TodoReport() {
	        super();
	    }
	
	    public static void main(String[] args) {
	        getTodoReportForBusinessLogic();
	    }
	
	    /**
	     * 解析使用注解的类，获取通过注解设置的属性
	     */
	    private static void getTodoReportForBusinessLogic() {
	        Class businessLogicClass = BusinessLogic.class;
	        for(Method method : businessLogicClass.getMethods()) {
	            Todo todoAnnotation = (Todo)method.getAnnotation(Todo.class);
	            if(todoAnnotation != null) {
	                System.out.println(" Method Name : " + method.getName());
	                System.out.println(" Author : " + todoAnnotation.author());
	                System.out.println(" Priority : " + todoAnnotation.priority());
	                System.out.println(" Status : " + todoAnnotation.status());
	                System.out.println(" --------------------------- ");
	            }
	        }
	    }
	}

&emsp;&emsp;执行结果如下图所示：

![注解Demo执行结果](http://ww3.sinaimg.cn/large/6d17e381gw1f563v5ogzbj20ep0cgwhe.jpg)

## 参考资料

- [元数据MetaData](http://www.ruanyifeng.com/blog/2007/03/metadata.html)

- [Java中的注解是如何工作的？](http://www.importnew.com/10294.html)

- [深入理解Java：注解](http://www.cnblogs.com/ITtangtang/p/3974531.html)

- [Support Annotations](http://tools.android.com/tech-docs/support-annotations)

- [xUtils3](https://github.com/wyouflf/xUtils3/blob/master/xutils/src/main/java/org/xutils/view/ViewInjectorImpl.java)

- [ActiveAndroid](https://github.com/pardom/ActiveAndroid/blob/master/src/com/activeandroid/Model.java)

