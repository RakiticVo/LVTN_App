package com.example.lvtn_app.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.MessageAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Controller.Retrofit.ApiService;
import com.example.lvtn_app.Controller.Retrofit.ApiServiceFirebase;
import com.example.lvtn_app.Controller.Retrofit.ApiUtils;
import com.example.lvtn_app.Controller.Retrofit.RetrofitClient;
import com.example.lvtn_app.Model.Message;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.GroupChatFragment;
import com.example.lvtn_app.View.Notification.Data;
import com.example.lvtn_app.View.Notification.MyResponse;
import com.example.lvtn_app.View.Notification.Sender;
import com.example.lvtn_app.View.Notification.Token;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    //Khai báo
    ImageButton ibtn_back_chat, ibtn_setting_chat, ibtn_send_message;
    TextView tvGroupChatName;
    EditText edt_send_message;
    RecyclerView recyclerViewMessage;
    MessageAdapter messageAdapter;
    ArrayList<Message> messageArrayList, temp;
    private DatabaseReference reference;
    ValueEventListener seenListener;

    SharedPreferences sharedPreferences, sharedPreferences_chat;

    DateFormat dateFormat = new DateFormat();
    Date date = Calendar.getInstance().getTime();
    String currentDate = "";
    String currentTime = "";

    int id_group = 0, id_user = 0;
    String groupName = "";
    String userName = "";
    String avatar = "";

    ApiServiceFirebase apiService;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Khai báo
        ibtn_back_chat = findViewById(R.id.ibtn_back_chat);
        ibtn_setting_chat = findViewById(R.id.ibtn_setting_chat);
        ibtn_send_message = findViewById(R.id.ibtn_send_message);
        tvGroupChatName = findViewById(R.id.tvGroupChatName);
        edt_send_message = findViewById(R.id.edt_send_message);
        recyclerViewMessage = findViewById(R.id.recyclerViewMessage);
        recyclerViewMessage.setHasFixedSize(true);

        sharedPreferences_chat = Objects.requireNonNull(this).getSharedPreferences("Chat", Context.MODE_PRIVATE);
        id_group = sharedPreferences_chat.getInt("groupChat_id", -1);
        groupName = sharedPreferences_chat.getString("groupChat_name", "name");

        sharedPreferences = Objects.requireNonNull(this).getSharedPreferences("User", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName_txt", "User Name");
        id_user = sharedPreferences.getInt("userId_txt", -1);
        avatar = sharedPreferences.getString("avatar_PI_txt", " ");

        apiService = RetrofitClient.getClient("https://fcm.googleapis.com/").create(ApiServiceFirebase.class);

        currentDate = dateFormat.formatDate(date);
        currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
        messageArrayList = new ArrayList<>();
        temp = new ArrayList<>();
        messageArrayList.add(new Message(1, id_group, "Rakitic", " ", "test 1", currentTime, currentDate, ""));
        messageArrayList.add(new Message(2, id_group, "Rakitic Võ", avatar, "test 1", currentTime, currentDate, ""));
        //Set up
//        Toast.makeText(this, "" + id_group, Toast.LENGTH_SHORT).show();
//        readMessage();
        if (groupName != null){
            tvGroupChatName.setText(groupName);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewMessage.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(this, messageArrayList);
        recyclerViewMessage.setAdapter(messageAdapter);

        if (recyclerViewMessage.getAdapter().getItemCount() > 0) {
            chageLayout();
        }

        readMessage();
        seenMessage(id_group, userName);

        //Bắt sự kiện
        if (!edt_send_message.isFocusableInTouchMode()){
            edt_send_message.setFocusableInTouchMode(true);
            if (recyclerViewMessage.getAdapter().getItemCount() > 0) {
                chageLayout();
            }
        }

        ibtn_back_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                GroupChatFragment.getInstance().readMessage();
            }
        });

        ibtn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String mess =  edt_send_message.getText().toString().trim() ;
                if (!mess.equals(""))
                {
//                    sendMessage(userChat.getTokenSender(), userChat.getId() ,mess);
                    currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
//                    Toast.makeText(ChatActivity.this,
//                                            "" + id_group + "\n" +
//                                                    userName + "\n" +
//                                                    avatar + "\n" +
//                                                    mess + "\n" +
//                                                    currentTime + "\n" +
//                                                    currentDate + "\n" ,Toast.LENGTH_SHORT).show();
//                    updateLastMessage(id_group, userName, avatar, mess, currentTime.toString(), currentDate.toString());
                    sendMessage(id_group, userName, avatar, mess, currentTime,  currentDate);
                }
                edt_send_message.setText("");
            }
        });

        ibtn_setting_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, SettingChatActivity.class);
                startActivity(intent);
            }
        });
    }

    public void chageLayout(){
        recyclerViewMessage.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom <= oldBottom) {
                    recyclerViewMessage.scrollToPosition(recyclerViewMessage.getAdapter().getItemCount() - 1);
                }
            }
        });
    }

    private void sendMessage(int id_Group, String sender, String img_sender, String message, String send_date, String send_time)
    {
        reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("id_Group", id_Group);
        hashMap.put("sender", sender);
        hashMap.put("img_sender", img_sender);
        hashMap.put("message", message);
        hashMap.put("send_date", send_date);
        hashMap.put("send_time", send_time);
        hashMap.put("status", "sent");
        reference.child("Chat").push().setValue(hashMap);
        updateLastMess(id_Group, message, sender);
        readMessage();

        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                temp.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message.getId_Group() == id_group){
                        temp.add(message);
                    }
                }
                String lastSender = temp.get(temp.size() - 1).getSender().toString();
                if (userName.equals(userName)){
                    if (notify){
                        sendNotification(id_Group, lastSender, msg);
                    }
                    notify = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(int id_group, String lastSender, String msg){
        DatabaseReference token = FirebaseDatabase.getInstance().getReference("Notification").child("Message").child(id_group+"");
        Query query = token.orderByKey().equalTo(lastSender);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Token token1 = dataSnapshot.getValue(Token.class);
//                    Toast.makeText(ChatActivity.this, "ABC: " + token1.getToken(), Toast.LENGTH_SHORT).show();
                    Data data = new Data(id_group, R.mipmap.ic_launcher, lastSender + ": " + msg, "New message", lastSender);
                    Sender sender = new Sender(data, token1.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                                    Toast.makeText(ChatActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
//                                            Toast.makeText(ChatActivity.this, "Failed" + response.body(), Toast.LENGTH_SHORT).show();
                                            Log.e("TAG", "onResponse: " + response.body());
                                        }else {
                                            Toast.makeText(ChatActivity.this, "Sucesss" + response.body(), Toast.LENGTH_SHORT).show();
                                            Log.e("TAG", "onResponse: " + response.body());
                                        }
                                    }else {
                                        Toast.makeText(ChatActivity.this, "Failed code" + response.code(), Toast.LENGTH_SHORT).show();
                                        Log.e("TAG", "onResponse: " + response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("TAG", "onFailure: " + t.getMessage());
                                    Toast.makeText(ChatActivity.this, "Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seenMessage (int id_group, String userName)
    {
        reference = FirebaseDatabase.getInstance().getReference("Chat");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Message message = dataSnapshot.getValue(Message.class);
                    // token là người gửi.
                    //     Log.e("L","Receiver"+ message.getReceiver() + " | sender " + message.getSender() + " | " + message.getStatus()+ "token" + token );
                    if(message.getId_Group() == id_group)
                    {
                        if (!message.getSender().equals(userName)){
                            HashMap<String , Object> hashMap = new HashMap<>();
                            Log.e("seen", "VAO Seen");
                            hashMap.put("status", "seen");
                            dataSnapshot.getRef().updateChildren(hashMap);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    private void readMessage()
    {
        reference = FirebaseDatabase.getInstance().getReference("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message.getId_Group() == id_group){
                        messageArrayList.add(message);
                    }
                }
                if (messageArrayList.size() == 0){
                    messageArrayList = new ArrayList<>();
                }
//                for (Message message : messageArrayList){
//                    Toast.makeText(ChatActivity.this, ""
//                                + message.getId_Chat() + "\n"
//                                + message.getId_Group() + "\n"
//                                + message.getSender() + "\n"
//                                + message.getImg_sender() + "\n"
//                                + message.getMessage() + "\n"
//                                + message.getSend_time() + "\n"
//                                + message.getSend_date() + "\n", Toast.LENGTH_SHORT).show();
//                }
                messageAdapter.notifyDataSetChanged();
                recyclerViewMessage.scrollToPosition(messageArrayList.size() - 1);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateLastMess(int id_Group, String groupLastMess, String groupLastSender){
        ApiService updateLastMess = ApiUtils.connectRetrofit();
        updateLastMess.isUpdateLastMessSuccess(id_Group, groupLastMess, groupLastSender).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Toast.makeText(ChatActivity.this, "Update" + response.body().toLowerCase(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GroupChatFragment.getInstance().readMessage();
    }
}