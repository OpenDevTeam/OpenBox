#Android应用Preference

###一、前言
本文主要给大家讲述下本人使用Preference的经验和体会。为了使短信项目和系统之间的耦合度更低，使用简单，达到代码可读性更好，又要达到策划的需求，根据以上原因选择了Preference组件。



###二、Preference介绍

####1、了解Preference组件基础知识

Preference布局采用Preference组件以及继承Preference的组件，比如EditTextPreference等，不像普通的Activity用layout来做自己的界面，它是用专属的xml/preference.xml来构建自己的界面，然后在类中加入此xml。在布局之前先介绍几个Preference组件。


#####（1）常用的组件

+ 组合控件：

        PreferenceCategory :类似于LinearLayou、RelativeLayout，用于组合一组Preference，使布局更具备层次感 。
        PreferenceScreen  : 所有Preference元素的根节点。


						  Preference 控件家庭          View控件家庭       控件含义
                          Preference                  TextView           文本框
                          CheckPreference             CheckBox           单选框
                          EditTextPreference          EditText          输入文本框 
                          ListPreference              ListView          列表框
                          RingtonePreference          ——               铃声

     SwitchPreference、DialogPreference、ListPreference等组件


#### （2）公共属性

Preference元素的通用XML Attributes说明：

android:key ---- 每个Preference控件独一无二的"ID",唯一表示此Preference,相当于Layout中的id；

android:title ---- 每个Preference在PreferenceScreen布局上显示的标题——大标题；

android:summary ---- 每个Preference在PreferenceScreen布局上显示的标题——小标题(可以没有)；

android:defaultValue ----默认值。 例如，CheckPreference的默认值可为"true"，默认为选中状态；

android:enabled ---- 表示该Preference是否可用状态；

android:dependency ---- 表示一个Preference(用A表示)的可用状态依赖另外一个Preference(用B表示)。B可用，则A可用；B不可用，则A不可用；

android:layout ---自定义布局layout，注意:layout布局里的id需要用系统id，比如使用TextView控件，id为title，以此类推。


####（3）Preference自定义布局

显示Preference布局结构的方法为：

使我们的Activity继承PreferenceActivity，然后在onCreate()方法中通过addPreferencesFromResource(R.xml.preference)(自定义的

Preference 布局)。

+ 一般布局直接使用Preference的控件


    	<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android">
	 
	    <PreferenceScreen
	        android:key="@string/sms_disabled_pref_key"
	        android:title="@string/sms_disabled_pref_title"
	        android:persistent="false">
	        <intent
	            android:action="android.provider.Telephony.ACTION_CHANGE_DEFAULT">
	            <extra android:name="package" android:value="com.android.messaging" />
	        </intent>
	    </PreferenceScreen>

	    <PreferenceScreen
	        android:key="@string/sms_enabled_pref_key"
	        android:title="@string/sms_enabled_pref_title"
	        android:persistent="false">
	        <intent
	            android:action="android.provider.Telephony.ACTION_CHANGE_DEFAULT">
	        </intent>
	    </PreferenceScreen>

        <CheckBoxPreference
            android:key="@string/auto_retrieve_mms_pref_key"
            android:title="@string/auto_retrieve_mms_pref_title"
            android:summary="@string/auto_retrieve_mms_pref_summary"
            android:defaultValue="@bool/auto_retrieve_mms_pref_default" />

        <CheckBoxPreference
            android:key="@string/auto_retrieve_mms_when_roaming_pref_key"
            android:dependency="@string/auto_retrieve_mms_pref_key"
            android:title="@string/auto_retrieve_mms_when_roaming_pref_title"
            android:summary="@string/auto_retrieve_mms_when_roaming_pref_summary"
            android:defaultValue="@bool/auto_retrieve_mms_when_roaming_pref_default" />
   
        <CheckBoxPreference
            android:key="@string/delivery_reports_pref_key"
            android:title="@string/delivery_reports_pref_title"
            android:summary="@string/delivery_reports_pref_summary"
            android:defaultValue="@bool/delivery_reports_pref_default" />
     
    	</PreferenceScreen>

####2、自定义布局

  主要是借助Preference组件提供属性android:layout

**Preference.xml:**

    <PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android">

	    <!--彩信-->
	    <PreferenceCategory
	        android:layout="@layout/preference_category_widget"
	        android:title="@string/mms_text">
	        <!--自动下载-->
	        <SwitchPreference
	            android:layout="@layout/preference_item"
	            android:key="@string/auto_retrieve_mms_pref_key"
	            android:title="@string/automatic_downloads_pref_title"
	            android:defaultValue="@bool/automatic_downloads_pref_default" />
	
	        <!--漫游时自动下载-->
	        <SwitchPreference
	            android:layout="@layout/preference_item"
	            android:key="@string/auto_retrieve_mms_when_roaming_pref_key"
	            android:title="@string/roaming_automatic_downloads_pref_title"
	            android:defaultValue="@bool/roaming_automatic_downloads_pref_default" />
	
	        <!--送达报告-->
	        <SwitchPreference
	            android:layout="@layout/preference_item"
	            android:key="@string/delivery_reports_mms_pref_key"
	            android:title="@string/delivery_reports_prefs_title"
	            android:summary="@string/delivery_reports_mms_prefs_summary"
	            android:defaultValue="@bool/delivery_reports_pref_default" />
	
	        <!--已接收报告-->
	        <SwitchPreference
	            android:layout="@layout/preference_item"
	            android:key="@string/reports_received_pref_key"
	            android:title="@string/reports_received_pref_title"
	            android:summary="@string/reports_received_pref_summary"
	            android:defaultValue="@bool/reports_received_pref_default" />
	    </PreferenceCategory>
	
    </PreferenceScreen>

**preference_category_widget.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="wrap_content"
	    android:orientation="vertical"
	    android:layout_marginLeft="@dimen/preference_layout_marginLeftorRight"
	    android:layout_marginRight="@dimen/preference_layout_marginLeftorRight"
	    android:paddingRight="@dimen/preference_layout_marginLeftorRight"
	    android:paddingLeft="@dimen/preference_layout_marginLeftorRight">

	    <!-- 这个id需要注意，要引用安卓源码中的 -->
	
	    <TextView
	        android:id="@android:id/title"
	        android:gravity="bottom"
	        android:layout_width="match_parent"
	        android:layout_height="@dimen/preference_category_height"
	        android:layout_marginBottom="@dimen/preference_category_margin_bottom"
	        android:textColor="@color/preference_category_text_color"
	        android:textSize="@dimen/preference_category_text_size"
	        android:text="indroduce"/>
	
	     <ImageView
	        android:layout_width="match_parent"
	        android:layout_height="@dimen/dividing_line_height"
	        android:src="@color/common_title_bar_line_color"/>

	</LinearLayout>

**preference_item.xml:**

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="@dimen/preference_title_summary_layout_height"
	    android:gravity="center_vertical"
	    android:orientation="horizontal"
	    android:layout_marginLeft="@dimen/preference_layout_marginLeftorRight"
	    android:layout_marginRight="@dimen/preference_layout_marginLeftorRight"
	    android:paddingRight="@dimen/preference_layout_marginLeftorRight"
	    android:paddingLeft="@dimen/preference_layout_marginLeftorRight"
	    android:background="@drawable/list_selector_background">
	
	    <RelativeLayout
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content">
	
	        <RelativeLayout
	            android:layout_width="match_parent"
	            android:layout_height="wrap_content"
	            android:layout_centerVertical="true">
	            <TextView
	                android:id="@android:id/title"
	                android:layout_width="wrap_content"
	                android:layout_height="wrap_content"
	                android:ellipsize="marquee"
	                android:fadingEdge="horizontal"
	                android:singleLine="true"
	                android:text="title"
	                android:textColor="@drawable/selector_title_color"
	                android:textSize="@dimen/preference_title_layout_size"
	                android:layout_marginBottom="@dimen/preference_layout_margin_bottom"/>
	
	            <TextView
	                android:id="@android:id/summary"
	                android:layout_width="wrap_content"
	                android:layout_height="wrap_content"
	                android:layout_toLeftOf="@android:id/widget_frame"
	                android:maxLines="2"
	                android:text="summary"
	                android:layout_below="@android:id/title"
	                android:textColor="@drawable/selector_summary_color"
	                android:textSize="@dimen/preference_summary_layout_size"/>
	
	        </RelativeLayout>
	
	        <LinearLayout
	            android:id="@android:id/widget_frame"
	            android:layout_width="wrap_content"
	            android:layout_height="wrap_content"
	            android:layout_alignParentRight="true"
	            android:layout_marginLeft="4dp"
	            android:layout_centerVertical="true"
	            android:gravity="center_vertical"
	            android:orientation="vertical" >
	        </LinearLayout>
	
	        <View
	            style="@style/SettingDividerStyle"
	            android:layout_width="match_parent"/>
	    </RelativeLayout>
	</LinearLayout>

**Preference起到架构作用，样式由Layout布局来实现，这样就达到Preference和应用层布局相结合，因此耦合度降低了。**


###3、Preference组件监听事件

Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener，Preference组件监听和OnClickListener一样的。


+ 在PreferenceActivity方法中，一个比较重要的监听点击事件方法为：
booleanon PreferenceTreeClick (PreferenceScreen preferenceScreen, Preference preference)。
	+ 说明 ： 当Preference控件被点击时，触发该方法。
	+ 参数说明： preference   点击的对象。
	+ 返回值： 
		+ true  代表点击事件已成功捕捉，无须执行默认动作或者返回上层调用链。 例如，不跳转至默认Intent。
		+ false 代表执行默认动作并且返回上层调用链。例如，跳转至默认Intent。
 
在我们继承PreferenceActivity的Activity可以重写该方法，来完成我们对Preference事件的捕捉。

**Preference相关的两个重要监听接口**
 
+ Preference.OnPreferenceChangeListener 该监听器的一个重要方法如下：
boolean onPreferenceChange(Preference preference,Object objValue)。	
	+ 说明：  当Preference的元素值发送改变时，触发该事件。
	+ 返回值：
		+ true  代表将新值写入sharedPreference文件中。
		+ false 则不将新值写入sharedPreference文件
 
+  Preference.OnPreferenceClickListener      该监听器的一个重要方法如下：

    boolean onPreferenceClick(Preference preference)
             说明：当点击控件时触发发生，可以做相应操作。
                             
    

**那么当一个Preference控件实现这两个接口时，当被点击或者值发生改变时，触发方法是如何执行的呢？事实上，它的触发规则如下：**

1、先调用onPreferenceClick()方法，如果该方法返回true，则不再调用onPreferenceTreeClick方法；
如果onPreferenceClick方法返回false，则继续调用onPreferenceTreeClick方法。

2、 onPreferenceChange的方法独立与其他两种方法的运行。也就是说，它总是会运行。

**补充：**

点击某个Preference控件后，会先回调onPreferenceChange()方法，即是否保存值，然后再回调onPreferenceClick以及onPreferenceTreeClick()方法，
因此在onPreferenceClick/onPreferenceTreeClick方法中我们得到的控件值就是最新的Preference控件值。


###4、启动一个新的 activity

Preferencescreen，不仅可以作为设置界面显示，而且还能够启动activity，下面主要是对启动activity的介绍
Preferencescreen中启动activity

例如wireless_settings.xml中有如下片段：

	<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android"
	xmlns:settings="http://schemas.android.com/apk/res/com.seedshope.android">
    <PreferenceScreen
        android:key="wifi_settings"
        android:title="@string/wifi_settings"
        android:summary="@string/wifi_settings_summary" >
        <intent
            android:action="android.intent.action.MAIN"
            android:targetPackage="com.android.settings"
            android:targetClass="com.android.settings.wifi.WifiSettings" />
    </PreferenceScreen>
	</PreferenceScreen>

关于intent标签，其中com.Android.settings是工程的包名，com.android.settings.wifi.WifiSettings是要启动的类。

###二、开发中使用Preference组件遇到的问题

+ 1、Preference组件的样式应用是不能继承使用，如https://github.com/CyanogenMod/android_frameworks_base/blob/cm-11.0/core/res/res/values/styles.xml。

+ 2、Preference自定义时layout布局属性定制需求覆盖系统的布局(id不能随便定义，需要使用系统的id)，比如TextView控件的id命名为title。

+ 3、Preference组件自身底部的线太粗糙，修改Preference的宽度，让其与屏幕宽度一致，无法满足需求。

**首先想到的思路:**

主题设置为自定义PreferenceFragmentListSinglePane样式，结果运行报错，提示无法找到PreferenceFragmentList样式。

framework层使用自定义的PreferenceFragmentListSinglePane样式是可以去掉底部的线，因此应用用该方法不能达到逾期。
	
	<style name="PreferenceFragmentListSinglePane" parent="@android:style/PreferenceFragmentList">
        <item name="android:paddingStart">0dp</item>
        <item name="android:paddingEnd">0dp</item>
        <item name="android:layout_marginStart">0dp</item>
        <item name="android:layout_marginEnd">0dp</item>
        <item name="android:scrollbarStyle">outsideOverlay</item>
        <item name="android:divider">@color/TRANSPARENT</item>
        <item name="android:dividerHeight">0dp</item>
        <item name="android:headerDividersEnabled">false</item><!--header line-->
        <item name="android:footerDividersEnabled">false</item><!--footer line-->
        <!--<item name="android:background">@drawable/preference_item_bg_selector</item>-->
    </style>
	
**处理方法如下：**


**方法一：在onCreateView中用代码设置去掉Preference组件自身的线和Padding（推荐此方法）**

		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View parent = super.onCreateView(inflater, container, savedInstanceState);
            if(null != parent)
            {
                ListView listView = (ListView) parent.findViewById(android.R.id.list);
                if(null != listView)
                {
                    listView.setPadding(0, listView.getPaddingTop(), 0, listView.getPaddingBottom());
                    listView.setDividerHeight(0);
                }
            }
            return parent;
        }

在自定义的Layout布局中添加线和距离屏幕的距离。


**方法二：在Fragment中的onCreateView，用我们自己的的Layout文件替代系统的Layout文件。只是注意文件的Id和布局要和系统的一样。
把文件中设置ListView的Padding的地方改掉就可以了。**

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.preference_list_fragment, container,
                false);

     }


###总结：

Preference组件是android的SharePreferences的衍生品，Preference组件的状态值是默认永久的保存在/data/data/包名/shared_prefs

目录下，因为Android系统的Setting应用及一些符合Android设计思想的应用的设置界面一般都会用它来实现，因此类似于该场景的都可以使用

Preference，比如系统设置、短信都可以使用，简单而又好使用，而且Google原生Android代码中大量的使用了Preference组件。简单说，

Preference组件其实就是Android常见UI组件与SharePreferences的组合封装实现。

 








