package cn.glassx.wear.demos;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;

/**
 * @author Chaos
 */
public class NotificationActivity extends Activity {

    private static final int NOTIFICATION_REPLY_ID       = 8;
    private static final int NOTIFICATION_PAGES_ID       = 9;
    private static final int NOTIFICATION_GROUP_ONE_ID   = 9;
    private static final int NOTIFICATION_GROUP_TWO_ID   = 10;
    private static final int NOTIFICATION_GROUP_THREE_ID = 11;

    public static final String VOICE_REPLY = "cn.glassx.wear.voice.reply";

    public static final String GROUP_KEY_DEMO = "demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }

    public void createReplyNotification(View v) {

        Intent replyIntent = new Intent(this, ReplyActivity.class);
        PendingIntent replyPendingIntent = PendingIntent.getActivity(this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteInput remoteInput = new RemoteInput.Builder(VOICE_REPLY)
                .setLabel(getString(R.string.reply_title))
                .setChoices(getResources().getStringArray(R.array.reply_choices))
                .build();

        Notification.Action action = new Notification.Action
                .Builder(R.drawable.ic_action_reply, getString(R.string.reply_label), replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.reply_notification_title))
                .setContentText(getString(R.string.reply_notification_content))
                .extend(new Notification.WearableExtender().addAction(action))
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_REPLY_ID, notification);
    }

    /**
     * support.v4包的方法暂时会造成 com.google.android.wearable.app 进程崩溃(Android Wear守护进程)
     * 建议使用4.4W自带的方法创建
     */
    public void createReplyNotificationByV4(View v) {

        Intent replyIntent = new Intent(this, ReplyActivity.class);
        PendingIntent replyPendingIntent = PendingIntent.getActivity(this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        android.support.v4.app.RemoteInput remoteInput =
                new android.support.v4.app.RemoteInput.Builder(VOICE_REPLY)
                        .setLabel(getString(R.string.reply_label))
                        .setChoices(getResources().getStringArray(R.array.reply_choices))
                        .build();

        NotificationCompat.Action action =
                new NotificationCompat.Action
                        .Builder(R.drawable.ic_action_reply, getString(R.string.reply_notification_title), replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.reply_notification_title))
                        .setContentText(getString(R.string.reply_notification_content))
                        .extend(new NotificationCompat.WearableExtender().addAction(action))
                        .build();

        NotificationManagerCompat.from(this).notify(NOTIFICATION_REPLY_ID, notification);
    }

    public void createPagesNotification(View v) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.glassx_small)
                        .setContentTitle("Page 1")
                        .setContentText("Short message");

        NotificationCompat.BigTextStyle secondPageStyle =
                new NotificationCompat.BigTextStyle()
                        .setBigContentTitle("Page 2")
                        .bigText("A lot of text...\nA lot of text...\nA lot of text...\nA lot of text...\nA lot of text...\nA lot of text...\nA lot of text...\nA lot of text...\n");

        Notification secondPageNotification =
                new NotificationCompat.Builder(this)
                        .setStyle(secondPageStyle)
                        .build();

        Notification twoPageNotification =
                new NotificationCompat.WearableExtender()
                        .addPage(secondPageNotification)
                        .extend(notificationBuilder)
                        .build();

        NotificationManagerCompat.from(this).notify(NOTIFICATION_PAGES_ID, twoPageNotification);
    }

    public void createGroupNotification(View v) {

        String title = getString(R.string.notification_title);
        String content = getString(R.string.notification_content);

        Notification notif1 = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(getString(R.string.notification_content))
                .setSmallIcon(R.drawable.glassx_small)
                .setGroup(GROUP_KEY_DEMO)
                .setGroupSummary(true)
                .build();

        Notification notif2 = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.glassx_small)
                .setGroup(GROUP_KEY_DEMO)
                .setGroupSummary(true)
                .build();

        Notification notif3 = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.glassx_small)
                .setGroup(GROUP_KEY_DEMO)
                .setGroupSummary(true)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_GROUP_ONE_ID, notif1);
        notificationManager.notify(NOTIFICATION_GROUP_TWO_ID, notif2);
        notificationManager.notify(NOTIFICATION_GROUP_THREE_ID, notif3);
    }
}