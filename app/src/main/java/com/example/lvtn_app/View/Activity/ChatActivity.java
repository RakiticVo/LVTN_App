package com.example.lvtn_app.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.MessageAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Controller.Retrofit.ApiServiceFirebase;
import com.example.lvtn_app.Controller.Retrofit.RetrofitClient;
import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.Model.Group_Chat_Users;
import com.example.lvtn_app.Model.Message;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.GroupChatFragment;
import com.example.lvtn_app.View.NotificationMessage.Data;
import com.example.lvtn_app.View.NotificationMessage.MyResponse;
import com.example.lvtn_app.View.NotificationMessage.Sender;
import com.example.lvtn_app.View.NotificationMessage.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
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
    ValueEventListener seenListener;

    SharedPreferences sharedPreferences, sharedPreferences_chat;
    SharedPreferences.Editor editor;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference1, reference2, reference3;
    String id_group, id_user;

    DateFormat dateFormat = new DateFormat();
    Date date = Calendar.getInstance().getTime();
    String currentDate = "";
    String currentTime = "";

    //Message infomation
    String message_ID;
    String message_group_ID;
    String message_sender;
    String message_img_sender;
    String message_text;
    String message_send_time;
    String message_send_date;
    String message_send_status;

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

        sharedPreferences_chat = getSharedPreferences("Chat", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("user_ID", "token");
        id_group = sharedPreferences_chat.getString("group_ID", "token");

        apiService = RetrofitClient.getClient("https://fcm.googleapis.com/").create(ApiServiceFirebase.class);

        currentDate = dateFormat.formatDate(date);
        currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
        messageArrayList = new ArrayList<>();
        temp = new ArrayList<>();
        messageArrayList.add(new Message("1", id_group, "Rakitic", " ", "test 1", currentTime, currentDate, ""));
        messageArrayList.add(new Message("2", id_group, "Rakitic Võ", " ", "test 1", currentTime, currentDate, ""));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewMessage.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(this, messageArrayList);
        recyclerViewMessage.setAdapter(messageAdapter);

        if (recyclerViewMessage.getAdapter().getItemCount() > 0) {
            changeLayout();
        }

        auth =FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChats").child(id_group);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GroupChat groupChat = snapshot.getValue(GroupChat.class);
                tvGroupChatName.setText(groupChat.getGroup_Name());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        messageArrayList.clear();
        readMessage(id_group);
//        seenMessage(id_group, id_user);

        //Bắt sự kiện
        if (!edt_send_message.isFocusableInTouchMode()){
            edt_send_message.setFocusableInTouchMode(true);
            if (recyclerViewMessage.getAdapter().getItemCount() > 0) {
                changeLayout();
            }
        }

        ibtn_back_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("chat", "chatActivity");
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        ibtn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String mess =  edt_send_message.getText().toString().trim() ;
                currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
                if (!mess.equals("")) {
                    if (id_user.equals(firebaseUser.getUid())){
                        reference1 = FirebaseDatabase.getInstance().getReference("Users").child(id_user);
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);

                                //Get all information of message
                                message_group_ID = id_group;
                                message_sender = user.getUser_Name();
                                message_img_sender = user.getUser_Avatar();
                                message_text = mess;
                                message_send_time = currentTime;
                                message_send_date = currentDate;
                                editor = sharedPreferences.edit();
                                editor.putString("user_Name", user.getUser_Name());
                                editor.putString("user_Avatar", user.getUser_Avatar());
                                editor.commit();
                                sendMessage(message_group_ID, message_sender, message_img_sender, message_text, message_send_time, message_send_date);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
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

    public void changeLayout(){
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

    private void sendMessage(String message_group_ID, String message_sender, String message_img_sender, String message_text, String message_send_time, String message_send_date)
    {
        reference2 = FirebaseDatabase.getInstance().getReference("Chats");
        message_ID = reference2.push().getKey().toString();
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("message_ID", message_ID);
        hashMap.put("message_group_ID", message_group_ID);
        hashMap.put("message_sender", message_sender);
        hashMap.put("message_img_sender", message_img_sender);
        hashMap.put("message_text", message_text);
        hashMap.put("message_send_time", message_send_time);
        hashMap.put("message_send_date", message_send_date);
        hashMap.put("message_send_status", "sent");
        reference2.child(message_group_ID).child(message_ID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(ChatActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    readMessage(message_group_ID);
//                    GroupChatFragment.getInstance().readMessage();
                }
            }
        });
        seenMessage(message_group_ID, firebaseUser.getUid());

        final String msg = message_text;
        reference3 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (notify){
                    getUserIDAndSendNoti(message_group_ID, user.getUser_Name(), msg, user.getUser_ID());
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserIDAndSendNoti(String id_group_receive, String sender_name, String message, String userid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User_List_By_Group_Chat").child(id_group_receive);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Group_Chat_Users users = dataSnapshot.getValue(Group_Chat_Users.class);
                    if (!userid.equals(users.getUser_ID())){
                        list.add(users.getUser_ID());
                    }
                }
                sendNotification(list, sender_name, message, id_group_receive);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(ArrayList<String> receiver, String sender_name, String message, String id_group){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        for (String s : receiver){
            Query query = tokens.orderByKey().equalTo(s);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Token token = dataSnapshot.getValue(Token.class);
                        Data data = new Data(firebaseUser.getUid(), id_group, sender_name+": " + message,
                                "New message", s);
                        Sender sender = new Sender(data, token.getToken());
                        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if (response.code() == 200){
                                    if (response.body().success != 1){
                                        Toast.makeText(ChatActivity.this, "Failed!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void seenMessage (String id_group, String id_user)
    {
        reference2 = FirebaseDatabase.getInstance().getReference("Chats").child(id_group);
        seenListener = reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Message message = dataSnapshot.getValue(Message.class);
                    if(message.getMessage_group_ID().equals(id_group))
                    {
                        if (!message.getMessage_sender().equals(id_user)){
                            HashMap<String , Object> hashMap = new HashMap<>();
//                            Log.e("seen", "VAO Seen");
                            hashMap.put("message_send_status", "seen");
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

    private void readMessage(String id_group)
    {
        messageArrayList.clear();
        reference2 = FirebaseDatabase.getInstance().getReference("Chats").child(id_group);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
//                    Toast.makeText(ChatActivity.this, "" + message.getMessage_group_ID(), Toast.LENGTH_SHORT).show();
                    if (message.getMessage_group_ID().equals(id_group)){
                        messageArrayList.add(message);
                    }
                    messageAdapter.notifyDataSetChanged();
                }
                if (messageArrayList.size() == 0){
//                    messageArrayList.add(new Message("abc", id_group, "Chí Thiện", " ", "This group has been created", currentTime, currentDate, " "));
//                    Toast.makeText(ChatActivity.this, "" + messageArrayList.get(0).getMessage_sender() + "\n"
//                            + messageArrayList.get(0).getMessage_text() + "\n"
//                            + messageArrayList.get(0).getMessage_send_date() + "\n"
//                            + messageArrayList.get(0).getMessage_send_time() + "\n"
//                            + messageArrayList.get(0).getMessage_group_ID() + "\n", Toast.LENGTH_SHORT).show();
                    messageAdapter.notifyDataSetChanged();
                }
                if (messageArrayList.size() > 0){
                    recyclerViewMessage.scrollToPosition(messageArrayList.size() - 1);
                    messageAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}