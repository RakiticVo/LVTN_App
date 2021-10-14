package com.example.lvtn_app.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lvtn_app.R;

public class IntroduceActivity extends AppCompatActivity {
    //Khai báo
    ImageView img_logo;
    TextView txt_app_name;
    FrameLayout frame_login;
    private final static int PERMISSION_REQUEST_CODE = 23;
    boolean checkedPermission = false;

    protected AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
    protected AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        img_logo = findViewById(R.id.logo);
        txt_app_name = findViewById(R.id.name_app);
        frame_login = findViewById(R.id.frame_login);
        requestPermision();
    }

    private void runMain(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                img_logo.setVisibility(View.GONE);
//                txt_app_name.setVisibility(View.GONE);
                frame_login.setVisibility(View.VISIBLE);
                Intent intent = new Intent(IntroduceActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        },1800);
    }

    private void requestPermision() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            checkedPermission = true;
            txt_app_name.startAnimation(fadeOut);
            txt_app_name.startAnimation(fadeIn);
            fadeOut.setDuration(500);
            fadeOut.setFillAfter(true);
            fadeIn.setDuration(1200);
            fadeIn.setFillAfter(true);
            fadeIn.setStartOffset(100+fadeIn.getStartOffset());
            runMain();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    checkedPermission = true;
                    runMain();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}