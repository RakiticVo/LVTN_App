package com.example.lvtn_app.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.LoginAdapter;
import com.example.lvtn_app.Controller.Service.NotificationService;
import com.example.lvtn_app.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;


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