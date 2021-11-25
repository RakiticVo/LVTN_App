package com.example.lvtn_app.Controller.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.Model.Joining_Group_Chat;
import com.example.lvtn_app.Model.Joining_Project;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.Project_Issue_Request;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Activity.MainActivity;
import com.example.lvtn_app.View.Notifications;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 5;
    private NotificationManagerCompat notificationManagerCompat;
    FirebaseUser firebaseUser;
    SharedPreferences sharedPreferences_language;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        if (firebaseUser != null) {
            startTimer();
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e("TAG", "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.e("TAG", "onDestroy");
        stoptimertask();
        super.onDestroy();
    }

    final Handler handler = new Handler();

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 500, Your_X_SECS * 1000L); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        //TODO CALL NOTIFICATION FUNC
                        getNotificationGroupID();
                        getNotificationProjectID();
                        getNotificationIssue();
                    }
                });
            }
        };
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
        sharedPreferences_language = this.getSharedPreferences("Config_language", Context.MODE_PRIVATE);
        String lang = sharedPreferences_language.getString("Current_Lang", "abcdef");
        String msg = "";
        if (!lang.equals("abcdef")){
            switch (lang){
                case "en":
                    msg = "You have Invitation from " + groupName + " Group";
                    break;
                case "vi":
                    msg = "Bạn có lời mời từ nhóm " + groupName;
                    break;
            }
        }

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
                .setContentTitle(this.getString(R.string.joining_request))
                .setContentText(msg)
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
                        if (joiningProject != null){
                            if (dataSnapshot.getKey().equals(firebaseUser.getUid())
                                    && !joiningProject.getStatus().equals("received")){
                                getProjectNameAndSendNoti(joiningProject);
                            }
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
        sharedPreferences_language = this.getSharedPreferences("Config_language", Context.MODE_PRIVATE);
        String lang = sharedPreferences_language.getString("Current_Lang", "abcdef");
        String msg = "";
        if (!lang.equals("abcdef")){
            switch (lang){
                case "en":
                    msg = "You have Invitation from " + projectName + " Project";
                    break;
                case "vi":
                    msg = "Bạn có lời mời từ dự án " + projectName;
                    break;
            }
        }
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
                .setContentTitle(this.getString(R.string.joining_request))
                .setContentText(msg)
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

    public void getNotificationIssue(){
        ArrayList<String> project_issue_id_list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Project_Issue_Request");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    project_issue_id_list.add(dataSnapshot.getKey());
                }
                getNotificationIssueByProjectID(project_issue_id_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getNotificationIssueByProjectID(ArrayList<String> project_issue_id_list){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Project_Issue_Request");
        for (String s : project_issue_id_list){
            reference.child(s).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Project_Issue_Request projectIssueRequest = dataSnapshot.getValue(Project_Issue_Request.class);
                        if (!projectIssueRequest.getStatus().equals("received")){
//                            Toast.makeText(MainActivity.this, "Successs", Toast.LENGTH_SHORT).show();
                            getIssueNameAndSendNotification(projectIssueRequest);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void getIssueNameAndSendNotification(Project_Issue_Request projectIssueRequest){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Projects").child(projectIssueRequest.getProject_ID());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Project project = snapshot.getValue(Project.class);
                sendNotificationIssueByProject(projectIssueRequest, project.getProject_Name());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotificationIssueByProject(Project_Issue_Request projectIssueRequest, String projectName) {
        String leader_ID = projectIssueRequest.getLeader_ID();
        String channelId = getString(R.string.default_notification_channel_id);
        sharedPreferences_language = this.getSharedPreferences("Config_language", Context.MODE_PRIVATE);
        String lang = sharedPreferences_language.getString("Current_Lang", "abcdef");
        String msg = "";
        if (!lang.equals("abcdef")){
            switch (lang){
                case "en":
                    msg = "You have been assigned an issue from " +  projectName + " Project";
                    break;
                case "vi":
                    msg = "Bạn đã được giao một công việc ở dự án " + projectName;
                    break;
            }
        }
        int j = Integer.parseInt(leader_ID.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("project_issue_ID", projectIssueRequest.getProject_ID());
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this, Notifications.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(this.getString(R.string.issue_request))
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
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
