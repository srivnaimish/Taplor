package com.riseapps.taplor.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.riseapps.taplor.R;
import com.riseapps.taplor.View.Main2Activity;
import com.riseapps.taplor.View.MainActivity;

/**
 * Created by naimish on 25/9/17.
 */

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "Statistics";
    public static final String ANDROID_CHANNEL_NAME = "Rankings";

    public NotificationUtils(Context base) {
        super(base);
        createChannels();
    }

    public void createChannels() {

        // create android channel
        NotificationChannel androidChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            androidChannel.enableLights(true);
            androidChannel.enableVibration(true);
            androidChannel.setLightColor(Color.RED);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(androidChannel);
        }

    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public Notification.Builder getChannelNotification(String title, String body, Bitmap largeIcon, Bitmap image) {
        Intent i = new Intent(this, Main2Activity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 2, i, PendingIntent.FLAG_ONE_SHOT);
        return new Notification.Builder(getApplicationContext())
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(largeIcon)
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(image))
                .setAutoCancel(true)
                .setChannelId(ANDROID_CHANNEL_ID);
    }

}