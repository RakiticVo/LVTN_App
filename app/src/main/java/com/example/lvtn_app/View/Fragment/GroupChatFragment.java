package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.GroupChatAdapter;
import com.example.lvtn_app.Adapter.ProjectsAdapter;
import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

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
    ArrayList<GroupChat> groupChat_search = new ArrayList<>();
    SharedPreferences sharedPreferences;

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
        // Ánh xạ
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);
        ibtn_add = view.findViewById(R.id.ibtn_add_group_chat);
        ibtn_search = view.findViewById(R.id.ibtn_search_group_chat);
        search_group_chat_text_input_layout = view.findViewById(R.id.search_group_chat_text_input_layout);
        recyclerViewGroupChat = view.findViewById(R.id.recyclerViewGroupChat);
        tvNoresult_chat = view.findViewById(R.id.tvNoresult_chat);

        instance = this;

        //Tạo dữ liệu
        sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("User", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("username_txt", "User Name");
        groupChat_list = new ArrayList<>();
        groupChat_list.add(new GroupChat(1, "Scrum 1", user));
        groupChat_list.add(new GroupChat(2, "Test 1", user));
        groupChat_list.add(new GroupChat(3, "Test 2", user));

        //Set up
        recyclerViewGroupChat.setLayoutManager(new LinearLayoutManager(getContext()));
        groupChatAdapter = new GroupChatAdapter(getContext(), groupChat_list);
        recyclerViewGroupChat.setAdapter(groupChatAdapter);

        // Bắt sự kiện
        ibtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_group_chat_text_input_layout.setVisibility(View.VISIBLE);
            }
        });

        ibtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateGroupChatFragment dialog = new CreateGroupChatFragment();
                dialog.show(getFragmentManager(), "CreateGroupChatFragment");
            }
        });

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
                        if (groupChat.getName().toLowerCase(Locale.ROOT).contains(s)){
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

        return view;
    }

    public void createGroupChat(String name, Uri avatar, String creator){
        GroupChatFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groupChat_list.add(new GroupChat(groupChat_list.size()+1, name, avatar, creator));
                search_group_chat_text_input_layout.getEditText().setText("");
                search_group_chat_text_input_layout.getEditText().clearFocus();
            }
        });
    }
}