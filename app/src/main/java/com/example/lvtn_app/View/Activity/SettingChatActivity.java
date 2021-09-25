package com.example.lvtn_app.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.MemberFragment;
import com.example.lvtn_app.View.Fragment.MyTasksFragment;

public class SettingChatActivity extends AppCompatActivity {

    TextView tv_show_member_setting_chat, tv_show_warning_delete_setting_chat;
    FrameLayout frame_setting_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_chat);

        tv_show_member_setting_chat = findViewById(R.id.tv_show_member_setting_chat);
        tv_show_warning_delete_setting_chat = findViewById(R.id.tv_show_warning_delete_setting_chat);
        frame_setting_chat = findViewById(R.id.frame_setting_chat);

        tv_show_member_setting_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().add(R.id.frame_setting_chat, new MemberFragment()).addToBackStack("MemberFragment return SettingChatActivity").commit();
            }
        });

        tv_show_warning_delete_setting_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingChatActivity.this)
                        .setMessage("Do you want to delete this group?")
                        .setPositiveButton("Yes", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        Toast.makeText(SettingChatActivity.this, "You choose Yes", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SettingChatActivity.this, "You choose No", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

    }
}