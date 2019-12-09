package com.example.secondtry;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ReminderApp extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    @Override
    public void onCreate(){
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //MUST REINSTALL APP TO SEE ANY CHANGES TO THE FOLLOWING LINES
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Reminder Notification", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is a Reminder Notification");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "Reminder Notification", NotificationManager.IMPORTANCE_LOW);
            channel2.setDescription("This is a Reminder Notification");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
