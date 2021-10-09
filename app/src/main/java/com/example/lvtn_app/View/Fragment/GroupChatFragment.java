package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.GroupChatAdapter;
import com.example.lvtn_app.Adapter.ProjectsAdapter;
import com.example.lvtn_app.Controller.Retrofit.ApiService;
import com.example.lvtn_app.Controller.Retrofit.ApiUtils;
import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.Model.Message;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Notification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
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

    DatabaseReference reference;

    SharedPreferences sharedPreferences, sharedPreferences_chat;
    int id_user, id_group;

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
        id_user = sharedPreferences.getInt("userId_txt", -1);
        userName = sharedPreferences.getString("userName_txt", "User Name");
        sharedPreferences_chat = requireContext().getSharedPreferences("Chat", Context.MODE_PRIVATE);
        id_group = sharedPreferences_chat.getInt("groupChat_id", -1);
        groupChat_list = new ArrayList<>();
        groupChat_search = new ArrayList<>();
        messageArrayList = new ArrayList<>();
        groupChat_list.add(new GroupChat(1, "Scrum 1", "", userName, "" , userName));
        groupChat_list.add(new GroupChat(2, "Test 1", "", userName, "", userName));
        groupChat_list.add(new GroupChat(3, "Test 2", "", userName, "", userName));

        recyclerViewGroupChat.setLayoutManager(new LinearLayoutManager(getContext()));
        groupChatAdapter = new GroupChatAdapter(getContext(), groupChat_list);
        recyclerViewGroupChat.setAdapter(groupChatAdapter);

        getGroupChatListByUser(id_user);
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
                        if (groupChat.getGroupName().toLowerCase(Locale.ROOT).contains(s)){
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
                updateToken(task.getResult().toString());
            }
        });
        return view;
    }

    //Todo: Hàm tạo ra một Group Chat
    // - Gọi Api Service để thêm 1 Group chat trên database ----- (Done)
    // - Thêm một Group Chat mới và hiển thị lên màn hình ----- (Done)
    public void createGroupChat(String name, String avatar, String creator){
        GroupChatFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getContext(), "" + avatar + "\n" + creator, Toast.LENGTH_SHORT).show();
                ApiService insertNewGroupChat = ApiUtils.connectRetrofit();
                insertNewGroupChat.isCreateNewGroupChatSuccess(name, avatar, creator, "This group has just been created", "")
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            ApiService getLastGroupChatID = ApiUtils.connectRetrofit();
                            getLastGroupChatID.getGroupChat().enqueue(new Callback<ArrayList<GroupChat>>() {
                                @Override
                                public void onResponse(Call<ArrayList<GroupChat>> call, Response<ArrayList<GroupChat>> response) {
                                    if (response.body() != null){
                                        int last = response.body().get(response.body().size()-1).getId_Group();
                                        insertNewUserForGroupChat(id_user, last);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ArrayList<GroupChat>> call, Throwable t) {
                                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e("BBB", "onFailure: " + t.getMessage() );
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("BBB", "onFailure: " + t.getMessage() );
                        }
                    });
            }
        });
    }

    //Todo: Hàm tạo thêm User là leader cho Group Chat
    // - Gọi Api Service để thêm 1 Leader cho Group chat trên database ----- (Done)
    // - Gọi và cập nhật lại List Group Chat trên màn hình ----- (Done)//
    public void insertNewUserForGroupChat(int id_user, int last){
        ApiService insertNewUserForGroupChat = ApiUtils.connectRetrofit();
        insertNewUserForGroupChat.isCreateNewUserForGroupChatSuccess(id_user, last, "Leader").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getContext(), "Create " + response.body().toLowerCase(), Toast.LENGTH_SHORT).show();
                getGroupChatListByUser(id_user);
                recyclerViewGroupChat.scrollToPosition(groupChat_list.size() - 1);
                recyclerViewGroupChat.clearFocus();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("BBB", "onFailure: " + t.getMessage() );
            }
        });
    }

    //Todo: Hàm lấy Group Chat list theo id của User
    // - Gọi Api Service tìm kiếm các Group Chat mà User có tham gia ----- (Done)
    // - Reset lại groupchat list và hiển thị lên màn hình ----- (Done)//
    public void getGroupChatListByUser(int id_user){
        groupChat_list.clear();
        ApiService getGroupChatList = ApiUtils.connectRetrofit();
//        Toast.makeText(getContext(), "" + id_user, Toast.LENGTH_SHORT).show();
        if (id_user > 0) {
//            Toast.makeText(getContext(), "" + sharedPreferences_user.getString("userName_txt", ""), Toast.LENGTH_SHORT).show();
            getGroupChatList.getGroupChatListByUser(id_user).enqueue(new Callback<ArrayList<GroupChat>>() {
                @Override
                public void onResponse(Call<ArrayList<GroupChat>> call, Response<ArrayList<GroupChat>> response) {
                    for (GroupChat groupChat : response.body()) {
                        groupChat_list.add(new GroupChat(groupChat.getId_Group(), groupChat.getGroupName(), groupChat.getGroupImage(),
                                groupChat.getGroupCreator(), groupChat.getGroupLastMess(), groupChat.getGroupLastSender()));
                    }
                    groupChatAdapter.notifyDataSetChanged();
                    recyclerViewGroupChat.scrollToPosition(0);
                    recyclerViewGroupChat.clearFocus();
                }

                @Override
                public void onFailure(Call<ArrayList<GroupChat>> call, Throwable t) {
                    groupChat_list.clear();
                    groupChatAdapter.notifyDataSetChanged();
//                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("BBB", "onFailure: " + t.getMessage() );
                }
            });
        }
    }

    public void readMessage()
    {
        messageArrayList.clear();
        reference = FirebaseDatabase.getInstance().getReference("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageArrayList.add(message);
                }
                if (messageArrayList.size() == 0){
                    messageArrayList = new ArrayList<>();
                }
//                }
                for (GroupChat groupChat : groupChat_list){
                    if (groupChat.getId_Group() == messageArrayList.get(messageArrayList.size() - 1).getId_Group()){
                        groupChat.setGroupLastMess(messageArrayList.get(messageArrayList.size() - 1).getMessage());
                        groupChat.setGroupLastSender(messageArrayList.get(messageArrayList.size() - 1).getSender());
                    }
                }
                groupChatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notification");
        Token refreshtoken = new Token(token);
        reference.child("Message").child(id_group+"").child(userName).setValue(refreshtoken);
    }
}