package com.riseapps.taplor.Executor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.riseapps.taplor.R;
import com.riseapps.taplor.Utils.SharedPreferenceSingelton;
import com.riseapps.taplor.View.MainActivity;

public class BoostReciever extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        new SharedPreferenceSingelton().saveAs(context, "Boost", 3);
        NotificationUtils mNotificationUtils = new NotificationUtils(context);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        if(Build.VERSION.SDK_INT>= 26) {
            Notification.Builder nb = mNotificationUtils.
                    getChannelNotification(context.getString(R.string.notification_title), context.getString(R.string.notification_context),largeIcon);
            mNotificationUtils.getManager().notify(NOTIFICATION_ID, nb.build());
        }else {

            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 2, i, PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
            mBuilder.setGroupSummary(true);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentTitle(context.getString(R.string.notification_title));
            mBuilder.setContentText(context.getString(R.string.notification_context));
            mBuilder.setTicker(context.getString(R.string.notification_title));
            mBuilder.setOngoing(false);
            mBuilder.setSmallIcon(R.drawable.ic_notification);
            mBuilder.setLargeIcon(largeIcon);
            mBuilder.setVibrate(new long[]{100, 100});
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (mNotificationManager != null) {
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        }
    }
}
