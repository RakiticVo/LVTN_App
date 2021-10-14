package com.example.lvtn_app.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.GroupChatFragment;
import com.example.lvtn_app.View.Fragment.MyTasksFragment;
import com.example.lvtn_app.View.Fragment.NotificationFragment;
import com.example.lvtn_app.View.Fragment.ProfileFragment;
import com.example.lvtn_app.View.Fragment.ProjectsFragment;

public class MainActivity extends AppCompatActivity {
    // Khai báo
    ImageButton ibtn_projects,ibtn_mytasks, ibtn_chat, ibtn_notification, ibtn_profile;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        declareView();
        ibtn_projects.setImageResource(R.drawable.projects_2);

        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ibtn_projects.setImageResource(R.drawable.projects_2);
        ibtn_mytasks.setImageResource(R.drawable.my_tasks_1);
        ibtn_chat.setImageResource(R.drawable.chat_1);
        ibtn_notification.setImageResource(R.drawable.notification_1);
        ibtn_profile.setImageResource(R.drawable.profile_1);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_main, new ProjectsFragment()).commit();

        ibtn_projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtn_projects.setImageResource(R.drawable.projects_2);
                ibtn_mytasks.setImageResource(R.drawable.my_tasks_1);
                ibtn_chat.setImageResource(R.drawable.chat_1);
                ibtn_notification.setImageResource(R.drawable.notification_1);
                ibtn_profile.setImageResource(R.drawable.profile_1);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new ProjectsFragment()).commit();
            }
        });

        ibtn_mytasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtn_mytasks.setImageResource(R.drawable.my_tasks_2);
                ibtn_projects.setImageResource(R.drawable.projects_1);
                ibtn_chat.setImageResource(R.drawable.chat_1);
                ibtn_notification.setImageResource(R.drawable.notification_1);
                ibtn_profile.setImageResource(R.drawable.profile_1);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new MyTasksFragment()).commit();
            }
        });

        ibtn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtn_mytasks.setImageResource(R.drawable.my_tasks_1);
                ibtn_projects.setImageResource(R.drawable.projects_1);
                ibtn_chat.setImageResource(R.drawable.chat_2);
                ibtn_notification.setImageResource(R.drawable.notification_1);
                ibtn_profile.setImageResource(R.drawable.profile_1);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new GroupChatFragment()).commit();
            }
        });

        ibtn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtn_mytasks.setImageResource(R.drawable.my_tasks_1);
                ibtn_projects.setImageResource(R.drawable.projects_1);
                ibtn_chat.setImageResource(R.drawable.chat_1);
                ibtn_notification.setImageResource(R.drawable.notification_3);
                ibtn_profile.setImageResource(R.drawable.profile_1);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new NotificationFragment()).commit();
            }
        });

        ibtn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtn_mytasks.setImageResource(R.drawable.my_tasks_1);
                ibtn_projects.setImageResource(R.drawable.projects_1);
                ibtn_chat.setImageResource(R.drawable.chat_1);
                ibtn_notification.setImageResource(R.drawable.notification_1);
                ibtn_profile.setImageResource(R.drawable.profile_2);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new ProfileFragment()).commit();
            }
        });
    }

    public void declareView(){
        ibtn_projects = findViewById(R.id.ibtn_projects);
        ibtn_mytasks = findViewById(R.id.ibtn_mytasks);
        ibtn_chat = findViewById(R.id.ibtn_chat);
        ibtn_notification = findViewById(R.id.ibtn_notification);
        ibtn_profile = findViewById(R.id.ibtn_profile);
    }

}