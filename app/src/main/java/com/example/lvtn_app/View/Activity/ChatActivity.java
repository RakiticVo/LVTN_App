package com.example.lvtn_app.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.MessageAdapter;
import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.Model.Message;
import com.example.lvtn_app.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    //Khai báo
    ImageButton ibtn_back_chat, ibtn_setting_chat, ibtn_send_message;
    TextView tvGroupChatName;
    EditText edt_send_message;
    RecyclerView recyclerViewMessage;
    MessageAdapter messageAdapter;
    SharedPreferences sharedPreferences;
    ArrayList<Message> messageArrayList;


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
        sharedPreferences = Objects.requireNonNull(this).getSharedPreferences("User", Context.MODE_PRIVATE);

        //Get data
        int id = getIntent().getIntExtra("groupChat_id", 129);
        String name = getIntent().getStringExtra("groupChat_name");

        //Set data
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        messageArrayList = new ArrayList<>();
        messageArrayList.add(new Message(1, id, "Rakitic", "", "test 1", currentTime));
        messageArrayList.add(new Message(2, id, "Rakitic Võ", "", "test 1", currentTime));
        messageArrayList.add(new Message(3, id, sharedPreferences.getString("username_txt", "User Name"), "", "test 1", currentTime));
        messageArrayList.add(new Message(4, id, "Thiện Võ", "", "test sjakhfjkashjk wafhuaufsaj 123 sadfa asjkfhkjsa sajfkasue dasdasd 1", currentTime));
        messageArrayList.add(new Message(5, id, sharedPreferences.getString("username_txt", "User Name"), "", "test sjakhfjkashjk wafhuaufsaj 123 sadfa asjkfhkjsa sajfkasue dasdasd 1", currentTime));
        //Set up
        Toast.makeText(this, "" + id, Toast.LENGTH_SHORT).show();
        if (name != null){
            tvGroupChatName.setText(name);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewMessage.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(this, messageArrayList);
        recyclerViewMessage.setAdapter(messageAdapter);

        chageLayout();

        //Bắt sự kiện
        if (!edt_send_message.isFocusableInTouchMode()){
            edt_send_message.setFocusableInTouchMode(true);
            chageLayout();
        }

        ibtn_back_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ibtn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mess =  edt_send_message.getText().toString().trim() ;
                if (!mess.equals(""))
                {
//                    sendMessage(userChat.getTokenSender(), userChat.getId() ,mess);
                    updateLastMessage(id, sharedPreferences.getString("username_txt", "User Name"), mess, currentTime);
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

    private void updateLastMessage(int id_groupchat, String sender, String message, String time){
        messageArrayList.add(new Message(messageArrayList.size()+1, id_groupchat, sender, "", message, time));
        messageAdapter.notifyDataSetChanged();
        chageLayout();
    }

    public void chageLayout(){
        recyclerViewMessage.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom <= oldBottom) {
                    recyclerViewMessage.smoothScrollToPosition(recyclerViewMessage.getAdapter().getItemCount() - 1);
                }
            }
        });
    }
}