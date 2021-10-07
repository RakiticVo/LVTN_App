package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.MemberAdapter;
import com.example.lvtn_app.Adapter.MemberDeleteAdapter;
import com.example.lvtn_app.Controller.Retrofit.ApiService;
import com.example.lvtn_app.Controller.Retrofit.ApiUtils;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberChatFragment extends Fragment implements MemberAdapter.ItemClickListener{
    //Khai báo
    ImageButton ibtn_add_member, ibtn_remove_member;
    RecyclerView recyclerViewMembers;
    Button btn_confirm_delete_members;
    TextView tv_show_member_delete, tv1, tvNoresult_member;
    TextInputLayout search_member_text_input_layout;
    ArrayList<User> member_list, delete_member_list, member_search_list;
    MemberAdapter memberAdapter;
    MemberDeleteAdapter memberDeleteAdapter;

    SharedPreferences sharedPreferences_chat, sharedPreferences_user;
    String user = "";
    String creator = "";
    int id_group;

    static MemberChatFragment instance;

    public static MemberChatFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khai báo
        View view = inflater.inflate(R.layout.fragment_member_chat, container, false);

        ibtn_add_member = view.findViewById(R.id.ibtn_add_member_chat);
        ibtn_remove_member = view.findViewById(R.id.ibtn_remove_member_chat);
        btn_confirm_delete_members = view.findViewById(R.id.btn_confirm_delete_members_chat);
        recyclerViewMembers = view.findViewById(R.id.recyclerViewMembers_chat);
        tv_show_member_delete = view.findViewById(R.id.tv_show_member_delete_chat);
        tv1 = view.findViewById(R.id.tv1_chat);
        search_member_text_input_layout = view.findViewById(R.id.search_member_chat_text_input_layout);
        tvNoresult_member = view.findViewById(R.id.tvNoresult_member_chat);

        instance = this;

        sharedPreferences_chat = Objects.requireNonNull(getContext()).getSharedPreferences("Chat", Context.MODE_PRIVATE);
        sharedPreferences_user = Objects.requireNonNull(getContext()).getSharedPreferences("User", Context.MODE_PRIVATE);
        user = sharedPreferences_user.getString("userName_txt", "User");
        creator = sharedPreferences_chat.getString("groupChat_creator", "creator");
        id_group = sharedPreferences_chat.getInt("groupChat_id", -1);
        if (user.equals(creator)){
            ibtn_add_member.setVisibility(View.VISIBLE);
            ibtn_remove_member.setVisibility(View.VISIBLE);
        }else{
            ibtn_add_member.setVisibility(View.GONE);
            ibtn_remove_member.setVisibility(View.GONE);
        }

        member_list = new ArrayList<>();
        delete_member_list = new ArrayList<>();
        member_search_list = new ArrayList<>();
        member_list.add(new User(1, "Chí Thiện", "chithien@gmail.com",
                1, "0942920838","","Leader"));

        //Set up
        recyclerViewMembers.setLayoutManager(new LinearLayoutManager(getContext()));
        memberAdapter = new MemberAdapter(getContext(), member_list);
        memberDeleteAdapter = new MemberDeleteAdapter(getContext(), member_list);
        memberAdapter.setClickListener(this::onItemClick);
        recyclerViewMembers.setAdapter(memberAdapter);

        getUserListByGroupChat();

        //Bắt sự kiện
        ibtn_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMemberChatFragment dialog = new AddMemberChatFragment();
                dialog.show(getFragmentManager(), "CreateMemberFragment");
            }
        });

        ibtn_remove_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewMembers.setAdapter(memberDeleteAdapter);
                btn_confirm_delete_members.setVisibility(View.VISIBLE);
                tv_show_member_delete.setVisibility(View.VISIBLE);
                tv1.setVisibility(View.VISIBLE);
                ibtn_remove_member.setEnabled(false);
            }
        });

        search_member_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    tv1.setVisibility(View.GONE);
                    tv_show_member_delete.setVisibility(View.GONE);
                }
            }
        });

        search_member_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (recyclerViewMembers.getAdapter() == memberAdapter) {
                    if (s.length() > 0) {
                        member_search_list.clear();
                        for (User member : member_list) {
//                        Toast.makeText(getContext(), "" + groupChat.getName().toLowerCase(), Toast.LENGTH_SHORT).show();
                            if (member.getUserName().toLowerCase(Locale.ROOT).contains(s)) {
                                member_search_list.add(member);
                            }
                        }
                        if (member_search_list.size() > 0) {
                            recyclerViewMembers.setVisibility(View.VISIBLE);
                            tvNoresult_member.setVisibility(View.GONE);
                            memberAdapter = new MemberAdapter(getContext(), member_search_list);
                            recyclerViewMembers.setAdapter(memberAdapter);
                        } else {
                            recyclerViewMembers.setVisibility(View.GONE);
                            tvNoresult_member.setVisibility(View.VISIBLE);
                        }
                    }else {
                        recyclerViewMembers.setVisibility(View.VISIBLE);
                        tvNoresult_member.setVisibility(View.GONE);
                        memberAdapter = new MemberAdapter(getContext(), member_list);
                        recyclerViewMembers.setAdapter(memberAdapter);
                    }
                }

                if (recyclerViewMembers.getAdapter() == memberDeleteAdapter){
                    if (s.length()>0){
                        member_search_list.clear();
                        for (User member : member_list){
//                        Toast.makeText(getContext(), "" + groupChat.getName().toLowerCase(), Toast.LENGTH_SHORT).show();
                            if (member.getUserName().toLowerCase(Locale.ROOT).contains(s)){
                                member_search_list.add(member);
                            }
                        }
                        if (member_search_list.size()>0){
                            recyclerViewMembers.setVisibility(View.VISIBLE);
                            tvNoresult_member.setVisibility(View.GONE);
                            memberDeleteAdapter = new MemberDeleteAdapter(getContext(), member_search_list);
                            recyclerViewMembers.setAdapter(memberDeleteAdapter);
                        }else {
                            recyclerViewMembers.setVisibility(View.GONE);
                            tvNoresult_member.setVisibility(View.VISIBLE);
                        }
                    }else {
                        recyclerViewMembers.setVisibility(View.VISIBLE);
                        tvNoresult_member.setVisibility(View.GONE);
                        memberDeleteAdapter = new MemberDeleteAdapter(getContext(), member_list);
                        recyclerViewMembers.setAdapter(memberDeleteAdapter);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_confirm_delete_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_member_list = memberDeleteAdapter.getMembers_checked();
                for (User m : delete_member_list){
                    tv_show_member_delete.setText(tv_show_member_delete.getText() + m.getUserName() + "-" + m.getPosition() + "\n");
                    for (User n : member_list){
                        if (n.getId_user() == m.getId_user()){
                            member_list.remove(n);
                            break;
                        }
                    }
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_show_member_delete.setVisibility(View.GONE);
                        tv1.setVisibility(View.GONE);
                    }
                }, 2000);
                memberAdapter.notifyDataSetChanged();
                recyclerViewMembers.setAdapter(memberAdapter);
                btn_confirm_delete_members.setVisibility(View.GONE);
                ibtn_remove_member.setEnabled(true);
            }
        });

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        MemberDetailChatFragment memberDetailChatFragment = new MemberDetailChatFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("member_id_txt", member_list.get(position).getId_user());
        bundle.putString("member_avatar_txt", member_list.get(position).getAvatar_PI());
        bundle.putString("member_name_txt", member_list.get(position).getUserName());
        bundle.putString("member_email_txt", member_list.get(position).getUserEmail());
        bundle.putString("member_phone_txt", member_list.get(position).getPhone_PI());
        bundle.putString("member_position_txt", member_list.get(position).getPosition());
        memberDetailChatFragment.setArguments(bundle);
        memberDetailChatFragment.show(getFragmentManager(), "MemberDetailFragment");
    }

    public void AddMember(String name, String email, String avatar, String phone, String position, int status){
        MemberChatFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int id = (member_list.size()) + 1;
                member_list.add(new User(id, name, email, status, phone, avatar, position));
                memberAdapter.notifyDataSetChanged();
                memberDeleteAdapter.notifyDataSetChanged();
            }
        });
    }

    public void updatePosition(int id, String position){
        member_list.get(id).setPosition(position);
        memberAdapter.notifyDataSetChanged();
        memberDeleteAdapter.notifyDataSetChanged();
    }

    public void getUserListByGroupChat(){
        member_list.clear();
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
                        if (user.getPosition().toLowerCase().equals("leader")){
                            member_list.add(new User(user.getId_user(), user.getUserName(), user.getUserEmail(),
                                    user.getStatus(), user.getPhone_PI(), user.getAvatar_PI(), user.getPosition()));
                            break;
                        }
                    }
                    for (User user : response.body()) {
                        if (!user.getPosition().toLowerCase().equals("leader")) {
                            member_list.add(new User(user.getId_user(), user.getUserName(), user.getUserName(),
                                    user.getStatus(), user.getPhone_PI(), user.getAvatar_PI(), user.getPosition()));
                        }
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
                    memberAdapter.notifyDataSetChanged();
                    memberDeleteAdapter.notifyDataSetChanged();
                    recyclerViewMembers.scrollToPosition(0);
                    recyclerViewMembers.clearFocus();
                }

                @Override
                public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                }
            });
        }
    }
}