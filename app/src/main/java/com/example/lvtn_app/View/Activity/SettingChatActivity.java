package com.example.lvtn_app.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Controller.Service.NotificationService;
import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.MemberChatFragment;
import com.example.lvtn_app.View.Fragment.MemberProjectFragment;
import com.example.lvtn_app.View.Fragment.ProjectsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingChatActivity extends AppCompatActivity {
    TextView tv_show_member_setting_chat, tv_show_warning_delete_setting_chat;
    LinearLayout linear1;
    FrameLayout frame_setting_chat;
    SharedPreferences sharedPreferences;

    FirebaseUser firebaseUser;

    String id_group;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_chat);

        tv_show_member_setting_chat = findViewById(R.id.tv_show_member_setting_chat);
        tv_show_warning_delete_setting_chat = findViewById(R.id.tv_show_warning_delete_setting_chat);
        linear1 = findViewById(R.id.linear1);
        frame_setting_chat = findViewById(R.id.frame_setting_chat);

        sharedPreferences = getSharedPreferences("Chat", Context.MODE_PRIVATE);
        id_group = sharedPreferences.getString("group_ID", "token");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupChats").child(id_group);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GroupChat groupChat = snapshot.getValue(GroupChat.class);
                if (!groupChat.getGroup_Creator().equals(firebaseUser.getUid())){
                    tv_show_warning_delete_setting_chat.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progressDialog = new ProgressDialog(this);

        tv_show_member_setting_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().add(R.id.frame_setting_chat, new MemberChatFragment()).addToBackStack("MemberChatFragment return SettingChatActivity").commit();
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
//                                        Toast.makeText(SettingChatActivity.this, "Todo: Delete Group Chat", Toast.LENGTH_SHORT).show();
                                        deleteGroupChatAll(id_group);
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(SettingChatActivity.this, "You choose No", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }
    public void deleteGroupChatAll(String id_group){
        progressDialog.setMessage("Deleting...");
        progressDialog.show();
        deleteMessageList(id_group);
    }

    private void deleteMessageList(String id_group) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Messages").child(id_group);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(SettingChatActivity.this, "Delete chat list success", Toast.LENGTH_SHORT).show();
                    deleteUserListByGroupChat(id_group);
                }else {
//                    Toast.makeText(SettingChatActivity.this, "Delete chat list failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteUserListByGroupChat(String id_group) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User_List_By_Group_Chat").child(id_group);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(SettingChatActivity.this, "Delete user list by group chat success", Toast.LENGTH_SHORT).show();
                    deleteGroupChat(id_group);
                }else {
//                    Toast.makeText(SettingChatActivity.this, "Delete user list by group chat failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteGroupChat(String id_group) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GroupChats").child(id_group);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(SettingChatActivity.this, "Delete group chat success", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(SettingChatActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("groupchat", "groupchat");
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else {
//                    Toast.makeText(SettingChatActivity.this, "Delete group chat failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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