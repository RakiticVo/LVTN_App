package com.example.lvtn_app.View.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.lvtn_app.Controller.Retrofit.ApiService;
import com.example.lvtn_app.Controller.Retrofit.ApiUtils;
import com.example.lvtn_app.Model.Member;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Activity.ChatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    ArrayList<User> memberArrayList = new ArrayList<>();

    @Override
    public void onNewToken(String token) {
        Log.d("TAG", "Refreshed token: " + token);
        updateToken(token);
    }

    private void updateToken(String token) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("User", Context.MODE_PRIVATE);
        String userName  = sharedPreferences.getString("userName_txt", "User Name");
        SharedPreferences sharedPreferences_chat = getApplication().getSharedPreferences("Chat", Context.MODE_PRIVATE);
        int id_group = sharedPreferences_chat.getInt("groupChat_id", -1);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notification");
        Token refreshtoken = new Token(token);
        reference.child("Message").child(id_group+"").child(userName).setValue(refreshtoken);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented =  remoteMessage.getData().get("sented");
        getUserListByGroupChat();
        for (User user : memberArrayList) {
            if (!user.getUserName().equals(sented)){
                sendNotification(remoteMessage);
            }
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Map<String, String> dataPayload = remoteMessage.getData();
        String id_group = dataPayload.get("id_group");
        String icon = dataPayload.get("icon");
        String body = dataPayload.get("body");
        String title = dataPayload.get("title");

        Log.e("TAG", "sendNotification: " +id_group+ "\n"
                                                    + "icon" + "\n"
                                                    + "body" + "\n"
                                                    + "title" + "\n");

        String channelId = getString(R.string.default_notification_channel_id);

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(id_group);
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id_group", id_group);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

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
        notificationManager.notify(i, builder.build());
    }

    public void getUserListByGroupChat(){
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("User", Context.MODE_PRIVATE);
        String userName  = sharedPreferences.getString("userName_txt", "User Name");
        SharedPreferences sharedPreferences_chat = getApplication().getSharedPreferences("Chat", Context.MODE_PRIVATE);
        int id_group = sharedPreferences_chat.getInt("groupChat_id", -1);
        memberArrayList.clear();
//        Toast.makeText(getContext(), "" + id_group, Toast.LENGTH_SHORT).show();
        ApiService getUserListByGroupChat = ApiUtils.connectRetrofit();
//        Toast.makeText(getContext(), "" + id_group, Toast.LENGTH_SHORT).show();
        if (id_group > 0) {
//            Toast.makeText(getContext(), "" + sharedPreferences_user.getString("userName_txt", ""), Toast.LENGTH_SHORT).show();
            getUserListByGroupChat.getUserListByGroupChat(id_group).enqueue(new Callback<ArrayList<User>>() {
                @Override
                public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
//                Toast.makeText(getContext(), "Create success" + response.body().size(), Toast.LENGTH_SHORT).show();
                    for (User user : response.body()) {
                        memberArrayList.add(new User(user.getId_user(), user.getUserName(), user.getUserEmail(),
                                user.getStatus(), user.getPhone_PI(), user.getAvatar_PI(), user.getPosition()));
                    }
//                    for (User user : member_list){
//                        Toast.makeText(getContext(), "" + user.getId_user() + "\n"
//                                + user.getUserName() + "\n"
//                                + user.getUserEmail() + "\n"
//                                + user.getStatus() + "\n"
//                                + user.getPhone_PI() + "\n"
//                                + user.getAvatar_PI() + "\n"
//                                + user.getPosition() + "\n", Toast.LENGTH_LONG).show();
//                    }
                }

                @Override
                public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                }
            });
        }
    }
}
