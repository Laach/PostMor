package com.mdhgroup2.postmor;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import android.os.SystemClock;
import android.view.View;

import com.mdhgroup2.postmor.database.interfaces.IAccountRepository;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;

import static com.mdhgroup2.postmor.Notifications.CHANNEL_1_ID;


public class MainActivity extends AppCompatActivity {

    AppBarConfiguration appBarConfiguration;
    public MainActivityViewModel mViewModel;
    private NotificationManagerCompat notificationManager;
    private int amountOfNewMessages = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final MainActivity main = this;
        notificationManager = NotificationManagerCompat.from(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.initDb(getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel = ViewModelProviders.of(main).get(MainActivityViewModel.class);
                        setContentView(R.layout.activity_main);
                        NavController navController = Navigation.findNavController(main, R.id.nav_host_fragment);
                        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
                        NavigationUI.setupActionBarWithNavController(main, navController, appBarConfiguration);

                    }
                });
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        IAccountRepository repo = DatabaseClient.getAccountRepository();
                        while (true) {
                            if(mViewModel != null && repo.isLoggedIn()) {
                                amountOfNewMessages = mViewModel.checkForNewMessages();
                                if (amountOfNewMessages > 0) {
                                    sendNotification(amountOfNewMessages);
                                    amountOfNewMessages = 0;
                                }
                            }
                            SystemClock.sleep(5000);
                        }
                    }
                };
                Thread myThread = new Thread(myRunnable);
                myThread.start();
            }
        }).start();




        super.onCreate(savedInstanceState);

    }

    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    public void sendNotification(int amountOfNewMessages){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_markunread_white_24dp)
                .setContentTitle("You have received " + amountOfNewMessages + " new letter(s).")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1,notification);
    }
}
