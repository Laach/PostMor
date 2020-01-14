package com.mdhgroup2.postmor;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notifications extends Application {
    public static final String CHANNEL_1_ID = "Notification message";

    @Override
    public void onCreate(){
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("Notifications");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
