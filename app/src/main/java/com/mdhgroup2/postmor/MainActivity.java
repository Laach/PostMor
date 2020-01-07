package com.mdhgroup2.postmor;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import com.mdhgroup2.postmor.database.repository.DatabaseClient;


public class MainActivity extends AppCompatActivity {

    AppBarConfiguration appBarConfiguration;
    public MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final MainActivity dis = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.initDb(getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel = ViewModelProviders.of(dis).get(MainActivityViewModel.class);
                        setContentView(R.layout.activity_main);
                        NavController navController = Navigation.findNavController(dis, R.id.nav_host_fragment);
                        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
                        NavigationUI.setupActionBarWithNavController(dis, navController, appBarConfiguration);
                    }
                });

            }
        }).start();
        super.onCreate(savedInstanceState);
    }

    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
