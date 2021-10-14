package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.GroupChatAdapter;
import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.Model.Group_Chat_Users;
import com.example.lvtn_app.Model.Message;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Notification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupChatFragment extends Fragment {
    //Khai báo
    ImageButton ibtn_search, ibtn_add;
    TextInputLayout search_group_chat_text_input_layout;
    TextView tvNoresult_chat;
    RecyclerView recyclerViewGroupChat;
    GroupChatAdapter groupChatAdapter;
    ArrayList<GroupChat> groupChat_list;
    ArrayList<GroupChat> groupChat_search;
    ArrayList<Message> messageArrayList, messages;
    ArrayList<String> list;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference1, reference2, reference3;
    AppCompatActivity activity;

    SharedPreferences sharedPreferences, sharedPreferences_chat;
    String id_user, id_group;

    String userName;

    static GroupChatFragment instance;

    public static GroupChatFragment getInstance() {
        return instance;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupChatFragment newInstance(String param1, String param2) {
        GroupChatFragment fragment = new GroupChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set up
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);
        ibtn_add = view.findViewById(R.id.ibtn_add_group_chat);
        ibtn_search = view.findViewById(R.id.ibtn_search_group_chat);
        search_group_chat_text_input_layout = view.findViewById(R.id.search_group_chat_text_input_layout);
        recyclerViewGroupChat = view.findViewById(R.id.recyclerViewGroupChat);
        tvNoresult_chat = view.findViewById(R.id.tvNoresult_chat);

        instance = this;

        sharedPreferences = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        sharedPreferences_chat = requireContext().getSharedPreferences("Chat", Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString("user_ID", "token");
        userName = sharedPreferences.getString("userName_txt", "User Name");
        id_group = sharedPreferences_chat.getString("group_ID", "token");
        groupChat_list = new ArrayList<>();
        groupChat_search = new ArrayList<>();
        messageArrayList = new ArrayList<>();
        groupChat_list.add(new GroupChat("1", "Scrum 1", "", userName, "" , userName));
        groupChat_list.add(new GroupChat("2", "Test 1", "", userName, "", userName));
        groupChat_list.add(new GroupChat("3", "Test 2", "", userName, "", userName));

        recyclerViewGroupChat.setLayoutManager(new LinearLayoutManager(getContext()));
        groupChatAdapter = new GroupChatAdapter(getContext(), groupChat_list);
        recyclerViewGroupChat.setAdapter(groupChatAdapter);

        list = new ArrayList<>();
        activity = (AppCompatActivity) getContext();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        if (id_user.equals(firebaseUser.getUid())){
            list.clear();
            groupChat_list.clear();
            showGroupChatList(id_user);
        }
        readMessage();

        // Bắt sự kiện
        //Todo: Xử lý sự kiện chuyển đến Fragment tạo Group Chat
        ibtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateGroupChatFragment dialog = new CreateGroupChatFragment();
                dialog.show(getFragmentManager(), "CreateGroupChatFragment");
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng Search Group Chat
        search_group_chat_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    groupChat_search.clear();
                    for (GroupChat groupChat : groupChat_list){
//                        Toast.makeText(getContext(), "" + groupChat.getName().toLowerCase(), Toast.LENGTH_SHORT).show();
                        if (groupChat.getGroup_Name().toLowerCase(Locale.ROOT).contains(s)){
                            groupChat_search.add(groupChat);
                        }
                    }
                    if (groupChat_search.size()>0){
                        recyclerViewGroupChat.setVisibility(View.VISIBLE);
                        tvNoresult_chat.setVisibility(View.GONE);
                        groupChatAdapter = new GroupChatAdapter(getContext(), groupChat_search);
                        recyclerViewGroupChat.setAdapter(groupChatAdapter);
                    }else {
                        recyclerViewGroupChat.setVisibility(View.GONE);
                        tvNoresult_chat.setVisibility(View.VISIBLE);
                    }
                }else {
                    recyclerViewGroupChat.setVisibility(View.VISIBLE);
                    tvNoresult_chat.setVisibility(View.GONE);
                    groupChatAdapter = new GroupChatAdapter(getContext(), groupChat_list);
                    recyclerViewGroupChat.setAdapter(groupChatAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    updateToken(task.getResult());
                }
            }
        });
        return view;
    }

    public void readMessage()
    {
        messageArrayList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(id_group);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageArrayList.add(message);
                }
                if (messageArrayList.size() == 0){
                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    messageArrayList = new ArrayList<>();
                }
                if (messageArrayList.size() > 0){
                    String sender = messageArrayList.get(messageArrayList.size() - 1).getMessage_sender();
                    String message = messageArrayList.get(messageArrayList.size() - 1).getMessage_text();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GroupChats").child(id_group);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("group_LastSender", sender);
                    hashMap.put("group_LastMess", message);
                    databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                showGroupChatList(id_user);
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        groupChatAdapter.notifyDataSetChanged();
    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

    public void showGroupChatList(String id_user){
        list.clear();
        reference1 = FirebaseDatabase.getInstance().getReference("GroupChats");
        AppCompatActivity activity = (AppCompatActivity) getContext();
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    GroupChat groupChat = dataSnapshot.getValue(GroupChat.class);
                    reference2 = FirebaseDatabase.getInstance().getReference("User_List_By_Group_Chat").child(groupChat.getGroup_ID());
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                Group_Chat_Users user = dataSnapshot1.getValue(Group_Chat_Users.class);
                                if (user.getUser_ID().equals(id_user)){
                                    list.add(user.getGroup_ID());
                                }
                            }
//                            Toast.makeText(getContext(), "" + list.size(), Toast.LENGTH_SHORT).show();
                            getGroupChatListByUser(list);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getGroupChatListByUser(ArrayList<String> list){
        AppCompatActivity activity = (AppCompatActivity) getContext();
        reference3 = FirebaseDatabase.getInstance().getReference("GroupChats");
        groupChat_list.clear();
        for (String s : list){
//            Toast.makeText(activity, "" + s, Toast.LENGTH_SHORT).show();
            reference3.child(s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GroupChat groupChat = snapshot.getValue(GroupChat.class);
//                    Toast.makeText(activity, "" + groupChat.getGroup_ID(), Toast.LENGTH_SHORT).show();
                    groupChat_list.add(groupChat);
                    groupChatAdapter.notifyDataSetChanged();
//                    Toast.makeText(activity, "" + groupChat_list.size(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    groupChatAdapter.notifyDataSetChanged();
                }
            });
            groupChatAdapter.notifyDataSetChanged();
        }
    }
}