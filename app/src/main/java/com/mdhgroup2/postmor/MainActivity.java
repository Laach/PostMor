package com.mdhgroup2.postmor;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.mdhgroup2.postmor.database.repository.DatabaseClient;


public class MainActivity extends AppCompatActivity {

    AppBarConfiguration appBarConfiguration;
    public MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.initDb(getApplicationContext());
            }
        }).start();
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get dps width to adapt for screen size
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        int dpWidth = (int)(displayMetrics.widthPixels / displayMetrics.density + 0.5);
        mViewModel.screenWidthDp = dpWidth;
        if(dpWidth>= 600)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

}
