# Android Nougat
![nougat](article/android/android_nougat/img/nougat.png)

Android 7.0 经过5个开发者预览版本的改善，终于在8.22日正式推送，并确定版本名为Nougat(牛轧糖)

根据官方的介绍，Android Nougat的主要更新有：

* 性能

    JIT编译器、VR模式、Vulkan™ API
	> 使用新的JIT编辑器是的系统及应用有更快的启动速度同时使用更少的内存，在系统更新时你再也不会有“Android is upgrading”的过程；
	>
	> VR模式对之后会发布的DayDream会有更好的支持；
	>
	> [Vulkan™ API](https://www.khronos.org/vulkan/ "Vulkan™ API") 是一款新的3D渲染API，使游戏应用拥有更绚丽的显示效果和渲染效率，不过目前该API仅适用于支持Vulkan的硬件设备，如Nextus 5X 、6P等 

* 功耗与数据

    Doze模式、优化电池管理、data Saver模式、后台数据访问
* 使用效率

    分屏模式、picture-in-picture、最近任务快速切换（双击菜单键）
	>Android 7.0开始支持应用分屏多任务，可以将手机屏幕拆分为两个显示区域，每个显示区域相当于一个单独的桌面，可以同时打开不同的应用
	>
	>Pip模式指在Android TV以及Tablet上支持应用以悬浮窗体显示
	>
	>最近任务快速切换（双击菜单键）
	>
	>![quick_switch](/img/quick_switch.gif)
* 通知

    捆绑通知、直接回复、通知控制

	>Android 7.0重新设计了通知系统的显示和设置方式
* 系统可用性提升

    自定义快捷设置、重新设计快捷设置栏、重新设计设置、紧急信息、锁屏壁纸

	>Andorid 7.0的设置模块经过交互的重新设置，操作更加便捷，同时系统提供了自定义设置的接口，允许应用通过实现特定服务和接口实现应用设置集成到系统设置及下拉选项中
* Emoji

    Unicode 9 emoji Emoji表情更新
* 隐私和安全

    direct boot、静默系统应用更新、基于文件的加密、文件访问控制
* 设备安装与迁移

    Android备份将保存更多设置信息
* 多语言

    本地多语言支持、新的语言及语言设置
* 辅助功能

    可变文字转语音（TTS）速度
* ...

###系统在设置和通知上的优化

* 重新设计了Notification系统，Notification拥有更加丰富的交互操作以及自定义其显示样式

![notification_setting](/img/notification_setting.gif)
![direct_replay](/img/direct_replay.gif)

* 重新设计了设置的交互，使设置操作更加快捷

![quick_settings](/img/quick_settings.gif)
![settings](/img/settings.gif)
![all_setting](/img/all_setting.gif)

###作为应用开发人员，我们最感兴趣的部分主要是分屏模式、通知系统和性能、功耗相关

####分屏模式

1. 默认设置  在当前窗口创建Activity并跳转
2. android:resizeableActivity="false"  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  不作为分屏窗口显示
3. intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);  在另一窗口中创建并显示Activity

![multi_windows](/img/multi_windows.gif)

####Pip(Picture-in-picture)模式
如果使用过Youtube应用，一定对其视频播放可作为悬浮窗显示在应用上层的操作体验印象深刻，Pip模式与之类似允许Activity以窗体的方式显示在其他Activity上层，不影响底层Activity的操作，遗憾的是目前Pip模式仅支持Android TV以及Tablet版本

####通知系统

Notification在原有Notifacation.Action之上添加了Direct Reply功能

> Tips：<br/>
> RemoteInput<br/>
> addRemoteInput(mRemoteInput)

![direct_replay](/img/direct_replay.gif)

使用Direct Reply Notification

	RemoteInput mRemoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel("Replay").build();
	Intent intent = new Intent(this, TestActivity.class);
	intent.putExtra("notification_replay", true);
	PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification.Action action = new Notification.Action.Builder(R.drawable.notification_icon, "Replay", mPendingIntent).
		addRemoteInput(mRemoteInput)
		.build();

	Notification notification = new Notification.Builder(getApplicationContext())
		.setContentTitle("ContentTitle")
		.setContentText("ContentText")
		.setSmallIcon(R.drawable.notification_icon)
		.setActions(action)
		.build();

	NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
	notificationManagerCompat.notify(NOTIFICATION_ID, notification);

![direct_reply_release](/img/direct_reply_release.png)

Notification.Action: 我们可以根据需要为Notification添加多个操作按钮 setActions(Action... actions)

完整代码

    import android.app.Activity;
    import android.app.Notification;
    import android.app.PendingIntent;
    import android.app.RemoteInput;
    import android.content.Intent;
    import android.os.Bundle;
    import android.support.v4.app.NotificationManagerCompat;
    import android.util.Log;
    import android.view.View;

    public class TestActivity extends Activity {
        private static final String TAG = "TestActivity";

        private static final String KEY_TEXT_REPLY = "key_test_reply";
        private static int NOTIFICATION_ID = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_test);

            findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    send();
                }
            });

            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().getBoolean("notification_replay")) {
                    Bundle remoteInput = RemoteInput.getResultsFromIntent(getIntent());
                    if (remoteInput != null) {
                        Log.d(TAG, "onCreate: " + remoteInput.getCharSequence(KEY_TEXT_REPLY));
                    }
                    sendReceiverNotification();
                }
            }
        }

        private void sendReceiverNotification() {
            Notification secondNotification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentText("Message sent.")
                    .build();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(NOTIFICATION_ID, secondNotification);
        }

        public void send() {
            RemoteInput mRemoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel("Replay").build();
            Intent intent = new Intent(this, TestActivity.class);
            intent.putExtra("notification_replay", true);
            PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Action action = new Notification.Action.Builder(R.drawable.notification_icon, "Replay", mPendingIntent).
                    addRemoteInput(mRemoteInput)
                    .build();

            Notification.Action actionTest = new Notification.Action.Builder(R.drawable.notification_icon, "Test", mPendingIntent)
                    .build();

            Notification notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("ContentTitle")
                    .setContentText("ContentText")
                    .setSmallIcon(R.drawable.notification_icon)
                    .setActions(action,actionTest)
                    .build();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(NOTIFICATION_ID, notification);
        }
    }

有关Notification的详细设置可参考官方文档 [https://developer.android.com/guide/topics/ui/notifiers/notifications.html](https://developer.android.com/guide/topics/ui/notifiers/notifications.html)

####Quick Settings Tile API
在Android 7.0上系统提供了用户添加自己的设置选项下拉设置内容中，具体参考android.service.quicksettings.Tile

彩蛋：当系统开启开发者权限后，你会在设置编辑中看到显示图层便捷和GPU渲染的快捷设置项，很好用的一个设计

![developer_mode](/img/developer_mode.gif)

####功耗

Android 7.0中进一步增强Doze对CPU唤醒的控制和网络限制

####私有文件权限

Android 7.0 修改了私有文件的访问权限，在Android 7.0上读写私有文件出会提示SecurityException.FileUriExposedException异常

### Android O & Fuchsia

Google Fuchsia项目

https://fuchsia.googlesource.com/

https://fuchsia-review.googlesource.com

> 参考文档：<br/>
> [https://www.android.com/versions/nougat-7-0/](https://www.android.com/versions/nougat-7-0/)<br/>
> [https://developer.android.com/about/versions/nougat/index.html](https://developer.android.com/about/versions/nougat/index.html)
