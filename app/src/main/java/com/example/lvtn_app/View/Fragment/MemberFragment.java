package com.example.lvtn_app.View.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.lvtn_app.Adapter.ProjectsAdapter;
import com.example.lvtn_app.Model.Member;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;

public class MemberFragment extends Fragment implements MemberAdapter.ItemClickListener{
    //Khai báo
    ImageButton ibtn_add_member, ibtn_remove_member;
    RecyclerView recyclerViewMembers;
    Button btn_confirm_delete_members;
    TextView tv_show_member_delete, tv1, tvNoresult_member;
    TextInputLayout search_member_text_input_layout;
    ArrayList<Member> member_list, delete_member_list, member_search_list;
    MemberAdapter memberAdapter;
    MemberDeleteAdapter memberDeleteAdapter;

    static MemberFragment instance;

    public static MemberFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khai báo
        View view = inflater.inflate(R.layout.fragment_member, container, false);

        ibtn_add_member = view.findViewById(R.id.ibtn_add_member);
        ibtn_remove_member = view.findViewById(R.id.ibtn_remove_member);
        btn_confirm_delete_members = view.findViewById(R.id.btn_confirm_delete_members);
        recyclerViewMembers = view.findViewById(R.id.recyclerViewMembers);
        tv_show_member_delete = view.findViewById(R.id.tv_show_member_delete);
        tv1 = view.findViewById(R.id.tv1);
        search_member_text_input_layout = view.findViewById(R.id.search_member_text_input_layout);
        tvNoresult_member = view.findViewById(R.id.tvNoresult_member);

        instance = this;

        // Set data
        member_list = new ArrayList<>();
        delete_member_list = new ArrayList<>();
        member_search_list = new ArrayList<>();
        member_list.add(new Member(1, "Chí Thiện", "chithien@gmail.com",
                "https://progameguides.com/wp-content/uploads/2021/07/Genshin-Impact-Character-Raiden-Shogun-1.jpg",
                "0942920838","Leader",true));
        member_list.add(new Member(2, "Thiện Võ", "thienvo@gmail.com",
                "https://progameguides.com/wp-content/uploads/2021/07/Genshin-Impact-Character-Raiden-Shogun-1.jpg",
                "0942920838", "Developer", true));
        member_list.add(new Member(3, "Rakitic Võ", "rakiticvo@gmail.com",
                "https://progameguides.com/wp-content/uploads/2021/07/Genshin-Impact-Character-Raiden-Shogun-1.jpg",
                "0942920838", "Developer",true));
        member_list.add(new Member(4, "Võ Rakitic", "vorakitic@gmail.com",
                "https://progameguides.com/wp-content/uploads/2021/07/Genshin-Impact-Character-Raiden-Shogun-1.jpg",
                "0942920838", "Bussiness Analyst", true));

        //Set up RecyclerView
        recyclerViewMembers.setLayoutManager(new LinearLayoutManager(getContext()));
        memberAdapter = new MemberAdapter(getContext(), member_list);
        memberDeleteAdapter = new MemberDeleteAdapter(getContext(), member_list);
        memberAdapter.setClickListener(this::onItemClick);
        recyclerViewMembers.setAdapter(memberAdapter);


        //Bắt sự kiện
        ibtn_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMemberFragment dialog = new AddMemberFragment();
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
                        for (Member member : member_list) {
//                        Toast.makeText(getContext(), "" + groupChat.getName().toLowerCase(), Toast.LENGTH_SHORT).show();
                            if (member.getName().toLowerCase(Locale.ROOT).contains(s)) {
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
                        for (Member member : member_list){
//                        Toast.makeText(getContext(), "" + groupChat.getName().toLowerCase(), Toast.LENGTH_SHORT).show();
                            if (member.getName().toLowerCase(Locale.ROOT).contains(s)){
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
                for (Member m : delete_member_list){
                    tv_show_member_delete.setText(tv_show_member_delete.getText() + m.getName() + "-" + m.getPosition() + "\n");
                    for (Member n : member_list){
                        if (n.getId() == m.getId()){
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
        MemberDetailFragment memberDetailFragment = new MemberDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("member_id_txt", member_list.get(position).getId());
        bundle.putString("member_avatar_txt", member_list.get(position).getAvatar());
        bundle.putString("member_name_txt", member_list.get(position).getName());
        bundle.putString("member_email_txt", member_list.get(position).getEmail());
        bundle.putString("member_phone_txt", member_list.get(position).getPhone());
        bundle.putString("member_position_txt", member_list.get(position).getPosition());
        memberDetailFragment.setArguments(bundle);
        memberDetailFragment.show(getFragmentManager(), "MemberDetailFragment");
    }

    public void createMember(String name, String email, String avatar, String phone, String position, boolean status){
        MemberFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int id = (member_list.size()) + 1;
                member_list.add(new Member(id, name, email, avatar, phone, position, status));
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
}