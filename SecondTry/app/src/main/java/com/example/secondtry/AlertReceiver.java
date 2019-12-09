package com.example.secondtry;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

//import static com.example.secondtry.MainActivity.mainList;

public class AlertReceiver extends BroadcastReceiver {
    private NotificationManagerCompat notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = String.valueOf(intent.getIntExtra("name", 0));
        String message = String.valueOf(intent.getIntExtra("message", 0));



        Notification notification = new NotificationCompat.Builder(context, ReminderApp.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_clock)
                .setContentTitle(name)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }
}
