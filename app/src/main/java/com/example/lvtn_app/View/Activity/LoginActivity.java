package com.example.lvtn_app.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.LoginAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Controller.Service.NotificationService;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.User_Issue_List;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Notifications;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class LoginActivity extends AppCompatActivity {
    //Khai báo
    TabLayout tabLayout;
    ViewPager viewPager;
    float v=0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseUser firebaseUser;

    static LoginActivity instance;

    public static LoginActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Set up
        tabLayout = findViewById(R.id.acc_tabLayout);
        viewPager = findViewById(R.id.acc_viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabTextColors(Color.BLACK, Color.BLACK);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final LoginAdapter loginAdapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(loginAdapter);

        instance = this;

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        sharedPreferences = this.getSharedPreferences("Config_language", Context.MODE_PRIVATE);
        String lang = sharedPreferences.getString("Current_Lang", "abcdef");
        String current_lang = this.getResources().getConfiguration().locale.toString();
        if (!current_lang.equals(lang)){
            loadLocale(lang);
        }
    }

    private void setLocate(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().
                updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        editor = sharedPreferences.edit();
        editor.putString("Current_Lang", language);
        editor.commit();
    }

    public void loadLocale(String language){
        if (!language.equals("abcdef")){
            setLocate(language);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Toast.makeText(MainActivity.this, "On Start", Toast.LENGTH_SHORT).show();
        stopService(new Intent(this, NotificationService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(this, NotificationService.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        stopService(new Intent(this, NotificationService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(MainActivity.this, "On Stop", Toast.LENGTH_SHORT).show();
        startService(new Intent(this, NotificationService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Toast.makeText(MainActivity.this, "On Stop", Toast.LENGTH_SHORT).show();
        startService(new Intent(this, NotificationService.class));
    }
}