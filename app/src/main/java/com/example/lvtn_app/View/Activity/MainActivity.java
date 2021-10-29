package com.example.lvtn_app.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.Model.Joining_Group_Chat;
import com.example.lvtn_app.Model.Joining_Project;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.GroupChatFragment;
import com.example.lvtn_app.View.Fragment.MyTasksFragment;
import com.example.lvtn_app.View.Fragment.NotificationFragment;
import com.example.lvtn_app.View.Fragment.ProfileFragment;
import com.example.lvtn_app.View.Fragment.ProjectDetailFragment;
import com.example.lvtn_app.View.Fragment.ProjectsFragment;
import com.example.lvtn_app.View.Notifications;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    // Khai báo
    MeowBottomNavigation meowBottomNavigation;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private NotificationManagerCompat notificationManagerCompat;

    private final int ID_PROJECTS = 1;
    private final int ID_MY_TASKS = 2;
    private final int ID_CHAT = 3;
    private final int ID_NOTIFICATION = 4;
    private final int ID_PROFILE = 5;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meowBottomNavigation = findViewById(R.id.meowBottomNavigation);


        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.projects_1));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.my_tasks_1));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.chat_1));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.notification_1));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.profile_1));

        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new ProjectsFragment()).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new MyTasksFragment()).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new GroupChatFragment()).commit();
                        break;
                    case 4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new NotificationFragment()).commit();
                        break;
                    case 5:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new ProfileFragment()).commit();
                        break;
                }
            }
        });

        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                String name;
                switch (item.getId()){
                    case 1:
                        name = "Projects";
                        break;
                    case 2:
                        name = "My Tasks";
                        break;
                    case 3:
                        name = "Chat";
                        break;
                    case 4:
                        name = "Notifications";
                        break;
                    case 5:
                        name = "Profile";
                        break;
                }
            }
        });
        meowBottomNavigation.show(ID_PROJECTS, true);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_main, new ProjectsFragment()).commit();

        this.notificationManagerCompat = NotificationManagerCompat.from(this);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        getNotificationGroupID();
        getNotificationProjectID();

        String object_ID = getIntent().getStringExtra("group_ID");
        if (object_ID != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Group_Chat");
            HashMap<String, Object> hashMap = new HashMap();
            hashMap.put("status", "received");
            reference.child(object_ID).child(firebaseUser.getUid()).updateChildren(hashMap);
            meowBottomNavigation.show(ID_NOTIFICATION, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new NotificationFragment()).commit();
        }else {
            object_ID = getIntent().getStringExtra("project_ID");
            if (object_ID != null) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Project");
                HashMap<String, Object> hashMap = new HashMap();
                hashMap.put("status", "received");
                reference.child(object_ID).child(firebaseUser.getUid()).updateChildren(hashMap);
                meowBottomNavigation.show(ID_NOTIFICATION, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new NotificationFragment()).commit();
            }else {
                object_ID = getIntent().getStringExtra("chat");
                if (object_ID != null){
                    meowBottomNavigation.show(ID_CHAT, true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new GroupChatFragment()).commit();
                } else {
                    meowBottomNavigation.show(ID_PROJECTS, true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new ProjectsFragment()).commit();
                }
            }
        }
    }

    public void getNotificationGroupID(){
        ArrayList<String> group_id_list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Group_Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    group_id_list.add(dataSnapshot.getKey());
                }
                getNotificationByGroupID(group_id_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getNotificationByGroupID(ArrayList<String> group_id_list){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Group_Chat");
        for (String s : group_id_list){
            reference.child(s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Joining_Group_Chat joiningGroupChat = dataSnapshot.getValue(Joining_Group_Chat.class);
                        if (dataSnapshot.getKey().equals(firebaseUser.getUid())
                            && !joiningGroupChat.getStatus().equals("received")){
                            getGroupNameAndSendNoti(joiningGroupChat);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void getGroupNameAndSendNoti(Joining_Group_Chat joiningGroupChat){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChats").child(joiningGroupChat.getGroup_ID());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GroupChat groupChat = snapshot.getValue(GroupChat.class);
                sendNotificationbygroupchat(joiningGroupChat, groupChat.getGroup_Name());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotificationbygroupchat(Joining_Group_Chat joiningGroupChat, String groupName) {
        String leader_ID = joiningGroupChat.getLeader_ID();
        String group_ID = joiningGroupChat.getGroup_ID();
        String channelId = getString(R.string.default_notification_channel_id);
        int j = Integer.parseInt(leader_ID.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("group_ID", group_ID);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this, Notifications.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Joining Request")
                .setContentText("You have Invitation from " +  groupName + " Group")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_PROMO) // Promotion.
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int i = 0;
        if (j > i){
            i = j;
        }
        this.notificationManagerCompat.notify(i, notification);
    }

    public void getNotificationProjectID(){
        ArrayList<String> project_id_list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Project");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    project_id_list.add(dataSnapshot.getKey());
                }
                getNotificationByProjectID(project_id_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getNotificationByProjectID(ArrayList<String> project_id_list){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Project");
        for (String s : project_id_list){
            reference.child(s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Joining_Project joiningProject = dataSnapshot.getValue(Joining_Project.class);
                        if (dataSnapshot.getKey().equals(firebaseUser.getUid())
                                && !joiningProject.getStatus().equals("received")){
                            getProjectNameAndSendNoti(joiningProject);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void getProjectNameAndSendNoti(Joining_Project joiningProject){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Projects").child(joiningProject.getProject_ID());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Project project = snapshot.getValue(Project.class);
                sendNotificationbyproject(joiningProject, project.getProject_Name());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotificationbyproject(Joining_Project joiningProject, String projectName) {
        String leader_ID = joiningProject.getLeader_ID();
        String project_ID = joiningProject.getProject_ID();
        String channelId = getString(R.string.default_notification_channel_id);
        int j = Integer.parseInt(leader_ID.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("project_ID", project_ID);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this, Notifications.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Joining Request")
                .setContentText("You have Invitation from " +  projectName + " Project")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_PROMO) // Promotion.
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int i = 0;
        if (j > i){
            i = j;
        }
        this.notificationManagerCompat.notify(i, notification);
    }
}