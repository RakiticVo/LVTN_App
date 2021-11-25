package com.example.lvtn_app.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Controller.Service.NotificationService;
import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.Joining_Group_Chat;
import com.example.lvtn_app.Model.Joining_Project;
import com.example.lvtn_app.Model.NotificationIssueToday;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.Project_Issue_Request;
import com.example.lvtn_app.Model.User_Issue_List;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.DashBoardFragment;
import com.example.lvtn_app.View.Fragment.GroupChatFragment;
import com.example.lvtn_app.View.Fragment.MyTasksFragment;
import com.example.lvtn_app.View.Fragment.ScheduleFragment;
import com.example.lvtn_app.View.Fragment.NotificationFragment;
import com.example.lvtn_app.View.Fragment.ProfileFragment;
import com.example.lvtn_app.View.Fragment.ProjectsFragment;
import com.example.lvtn_app.View.NotificationMessage.Token;
import com.example.lvtn_app.View.Notifications;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    // Khai báo
    public MeowBottomNavigation meowBottomNavigation;
    SharedPreferences sharedPreferences, sharedPreferences_language;
    SharedPreferences.Editor editor;
    private NotificationManagerCompat notificationManagerCompat;
    DateFormat dateFormat;
    Date currentDate;

    private final int ID_PROJECTS = 1;
    private final int ID_MY_TASKS = 2;
    private final int ID_CHAT = 3;
    private final int ID_NOTIFICATION = 4;
    private final int ID_PROFILE = 5;

    FirebaseUser firebaseUser;
    DatabaseReference referenceIssueToDay, referenceNotificationToday;
    ValueEventListener valueEventListenerIssueToDay, valueEventListenerNotificationToday;


    static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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

        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
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

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    updateToken(task.getResult());
                }
            }
        });

        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("userID", firebaseUser.getUid());

        dateFormat = new DateFormat();
        currentDate = Calendar.getInstance().getTime();

        getNotificationJoiningGroupChat();
        getNotificationProject();
        getNotificationIssue();

        getJoiningGroupChatID();
        getJoiningProjectID();
        getIssueProjectID();

        String object_ID = getIntent().getStringExtra("group_ID");
        if (object_ID != null) {
            meowBottomNavigation.show(ID_NOTIFICATION, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new NotificationFragment()).commit();
        }else {
            object_ID = getIntent().getStringExtra("project_ID");
            if (object_ID != null) {
                meowBottomNavigation.show(ID_NOTIFICATION, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new NotificationFragment()).commit();
            }else {
                object_ID = getIntent().getStringExtra("chat");
                if (object_ID != null){
                    meowBottomNavigation.show(ID_CHAT, true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new GroupChatFragment()).commit();
                }else{
                    object_ID = getIntent().getStringExtra("project_issue_ID");
                    if (object_ID != null){
                        meowBottomNavigation.show(ID_NOTIFICATION, true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new NotificationFragment()).commit();
                    }else {
                        object_ID = getIntent().getStringExtra("currentDateIssues");
                        if (object_ID != null){
                            referenceIssueToDay.removeEventListener(valueEventListenerIssueToDay);
                            meowBottomNavigation.show(ID_MY_TASKS, true);
                            MyTasksFragment myTasksFragment = new MyTasksFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("schedule", 1);
                            myTasksFragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, myTasksFragment).commit();
                        }else {
                            object_ID = getIntent().getStringExtra("groupchat");
                            if (object_ID != null){
                                meowBottomNavigation.show(ID_CHAT, true);
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new GroupChatFragment()).commit();
                            }else {
                                meowBottomNavigation.show(ID_PROJECTS, true);
                                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, new ProjectsFragment()).commit();
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

    public void getNotificationJoiningGroupChat(){
        ArrayList<String> group_id_list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Group_Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    group_id_list.add(dataSnapshot.getKey());
                }
                getNotificationJoiningByGroupID(group_id_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getNotificationJoiningByGroupID(ArrayList<String> group_id_list){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Group_Chat");
        for (String s : group_id_list){
            reference.child(s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Joining_Group_Chat joiningGroupChat = dataSnapshot.getValue(Joining_Group_Chat.class);
                        if (dataSnapshot.getKey().equals(firebaseUser.getUid())
                            && !joiningGroupChat.getStatus().equals("received")){
                            getGroupNameAndSendNotification(joiningGroupChat);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void getGroupNameAndSendNotification(Joining_Group_Chat joiningGroupChat){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChats").child(joiningGroupChat.getGroup_ID());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GroupChat groupChat = snapshot.getValue(GroupChat.class);
                sendNotificationJoiningByGroupchat(joiningGroupChat, groupChat.getGroup_Name());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotificationJoiningByGroupchat(Joining_Group_Chat joiningGroupChat, String groupName) {
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

    public void getNotificationProject(){
        ArrayList<String> project_id_list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Project");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    project_id_list.add(dataSnapshot.getKey());
                }
                getNotificationJoiningByProjectID(project_id_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getNotificationJoiningByProjectID(ArrayList<String> project_id_list){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Project");
        for (String s : project_id_list){
            reference.child(s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Joining_Project joiningProject = dataSnapshot.getValue(Joining_Project.class);
                        if (dataSnapshot.getKey().equals(firebaseUser.getUid())
                                && !joiningProject.getStatus().equals("received")){
                            getProjectNameAndSendNotification(joiningProject);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void getProjectNameAndSendNotification(Joining_Project joiningProject){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Projects").child(joiningProject.getProject_ID());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Project project = snapshot.getValue(Project.class);
                sendNotificationJoiningByProject(joiningProject, project.getProject_Name());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotificationJoiningByProject(Joining_Project joiningProject, String projectName) {
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


    public void getJoiningProjectID(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Project");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> temp = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    temp.add(dataSnapshot.getKey());
                }
                if (temp.size() > 0){
                    updateStatusJoiningProjectRequest(temp);
                }else {
                    getJoiningGroupChatID();
                }
//                Toast.makeText(activity, "" + temp, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateStatusJoiningProjectRequest(ArrayList<String> projectID){
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Project");
        for (String s : projectID){
            reference1.child(s).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Joining_Project joiningProject = snapshot.getValue(Joining_Project.class);
                    if (joiningProject != null){
                        if (!joiningProject.getStatus().equals("received")){
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Notifications")
                                    .child("Joining_Project").child(s).child(firebaseUser.getUid());
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("status", "received");
                            reference2.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        //  Toast.makeText(activity, "Update joining project request success", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void getJoiningGroupChatID(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Group_Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> temp = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    temp.add(dataSnapshot.getKey());
                }
                if (temp.size() > 0) {
                    updateStatusJoiningGroupRequest(temp);
                }else {
                    getIssueProjectID();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateStatusJoiningGroupRequest(ArrayList<String> groupID){
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Notifications").child("Joining_Group_Chat");
        for (String s : groupID){
            reference1.child(s).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Joining_Group_Chat joiningGroupChat = snapshot.getValue(Joining_Group_Chat.class);
                    if (joiningGroupChat != null){
                        if (!joiningGroupChat.getStatus().equals("received")){
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Notifications")
                                    .child("Joining_Group_Chat").child(s).child(firebaseUser.getUid());
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("status", "received");
                            reference2.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        // Toast.makeText(activity, "Update joining group chat request success", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    public void getIssueProjectID(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Project_Issue_Request");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> temp = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    temp.add(dataSnapshot.getKey());
                }
                if (temp.size() > 0) {
                    updateStatusProjectIssueRequest(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getIssueIDToDay(currentDate);
    }

    public void updateStatusProjectIssueRequest(ArrayList<String> projectID){
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Notifications").child("Project_Issue_Request");
        for (String s : projectID){
            reference1.child(s).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Project_Issue_Request projectIssueRequest = dataSnapshot.getValue(Project_Issue_Request.class);
                        if (projectIssueRequest != null){
                            if (!projectIssueRequest.getStatus().equals("received")){
                                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Notifications")
                                        .child("Project_Issue_Request").child(s).child(firebaseUser.getUid()).child(projectIssueRequest.getIssue_ID());
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("status", "received");
                                reference2.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
//                                            Toast.makeText(activity, "Update project issue request success", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
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

    public void getIssueIDToDay(Date currentDate){
        ArrayList<Issue> issues = new ArrayList<>();
        final int[] i = {0};
        referenceIssueToDay = FirebaseDatabase.getInstance().getReference("Issue_List_By_User").child(firebaseUser.getUid());
        valueEventListenerIssueToDay = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    i[0]++;
                    User_Issue_List issue = dataSnapshot.getValue(User_Issue_List.class);
                    getIssueToDay(currentDate, issue.getProject_ID(), issue.getIssue_ID(), issues, i[0], (int) snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        referenceIssueToDay.addValueEventListener(valueEventListenerIssueToDay);
    }

    public void getIssueToDay(Date currentDate, String projectID, String issueID, ArrayList<Issue> issues, int count, int end){
        referenceNotificationToday = FirebaseDatabase.getInstance().getReference("Issues").child(projectID).child(issueID);
        valueEventListenerNotificationToday = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Issue issue = snapshot.getValue(Issue.class);
                if (issue != null){
                    String date = dateFormat.formatDate(currentDate);
                    if (date.equals(issue.getIssue_StartDate())){
                        issues.add(issue);
                    }
                    if (count == end){
//                    Toast.makeText(MainActivity.this, count + "-" + end + "\n Size: " + issues.size(), Toast.LENGTH_SHORT).show();
                        getNotificationIssueToday(date, issues.size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        referenceNotificationToday.addValueEventListener(valueEventListenerNotificationToday);
    }

    public void getNotificationIssueToday(String date, int size){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Issue_Today");
        reference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NotificationIssueToday notificationIssueToday = snapshot.getValue(NotificationIssueToday.class);
                if (notificationIssueToday != null){
                    if (notificationIssueToday.getToday().equals(date)){
                        if (notificationIssueToday.getStatus().equals("sent")){
                            sendNotificationIssueToDay(date, size);
                            updateStatusIssueToday();
                        }
                    }else {
                        updateDateIssueToday(date);
                    }
                }else {
                    createNotificationIssueToday(date, size);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createNotificationIssueToday(String date, int size){
        String s = "You have Issue today";
        if (size > 1){
            s = "You have some Issues today";
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child("Issue_Today");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", s);
        hashMap.put("today", date);
        hashMap.put("status", "sent");
        reference.child(firebaseUser.getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    getNotificationIssueToday(date, size);
                }
            }
        });
    }

    public void updateStatusIssueToday(){
        DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("Notifications").child("Issue_Today");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", "received");
        reference.child(firebaseUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(MainActivity.this, "update status Issue today success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateDateIssueToday(String date){
        DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("Notifications").child("Issue_Today");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("today", date);
        reference.child(firebaseUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(MainActivity.this, "update date Issue today success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendNotificationIssueToDay(String date, int size){
        String channelId = getString(R.string.default_notification_channel_id);
        int j = 123456789;
        sharedPreferences_language = this.getSharedPreferences("Config_language", Context.MODE_PRIVATE);
        String lang = sharedPreferences_language.getString("Current_Lang", "abcdef");
        String msg = "";
        if (!lang.equals("abcdef")){
            switch (lang){
                case "en":
                    msg = "You have Issue today";
                    if (size > 1){
                        msg = "You have some Issues today";
                    }
                    break;
                case "vi":
                    msg = "Bạn có công việc mới trong ngày hôm nay";
                    if (size > 1){
                        msg = "Bạn có một số công việc mới trong ngày hôm nay";
                    }
                    break;
            }
        }
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("currentDateIssues", date);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this, Notifications.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(this.getString(R.string.issue_today))
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
        notificationManagerCompat.notify(i, notification);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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