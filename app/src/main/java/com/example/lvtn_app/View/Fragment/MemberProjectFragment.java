package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.lvtn_app.Adapter.MemberProjectAdapter;
import com.example.lvtn_app.Adapter.MemberProjectDeleteAdapter;
import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.Model.Group_Chat_Users;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.Project_Users;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
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

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberProjectFragment extends Fragment implements MemberAdapter.ItemClickListener{
    //Khai báo
    ImageButton ibtn_add_member, ibtn_remove_member;
    RecyclerView recyclerViewMembers;
    Button btn_confirm_delete_members;
    TextView tvNoresult_member;
    TextInputLayout search_member_text_input_layout;
    ArrayList<User> member_list, delete_member_list, member_search_list;
    MemberProjectAdapter memberAdapter;
    MemberProjectDeleteAdapter memberDeleteAdapter;

    AppCompatActivity activity;
    SharedPreferences sharedPreferences_project, sharedPreferences_user;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference1, reference2, reference3;
    String id_user, id_project;
    ArrayList<String> list;

    static MemberProjectFragment instance;

    public static MemberProjectFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khai báo
        View view = inflater.inflate(R.layout.fragment_member_project, container, false);

        ibtn_add_member = view.findViewById(R.id.ibtn_add_member);
        ibtn_remove_member = view.findViewById(R.id.ibtn_remove_member);
        btn_confirm_delete_members = view.findViewById(R.id.btn_confirm_delete_members);
        recyclerViewMembers = view.findViewById(R.id.recyclerViewMembers);
        search_member_text_input_layout = view.findViewById(R.id.search_member_text_input_layout);
        tvNoresult_member = view.findViewById(R.id.tvNoresult_member);

        instance = this;

        member_list = new ArrayList<>();
        delete_member_list = new ArrayList<>();
        member_search_list = new ArrayList<>();
        member_list.add(new User("1", "Chí Thiện", "chithien@gmail.com",
                "1", "0942920838","","Leader", "", "", ""));

        recyclerViewMembers.setLayoutManager(new LinearLayoutManager(getContext()));
        memberAdapter = new MemberProjectAdapter(getContext(), member_list);
        memberDeleteAdapter = new MemberProjectDeleteAdapter(getContext(), member_list);
        memberAdapter.setClickListener(this::onItemClick);
        recyclerViewMembers.setAdapter(memberAdapter);

        activity = (AppCompatActivity) getContext();
        sharedPreferences_project = requireContext().getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
        sharedPreferences_user = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        id_project = sharedPreferences_project.getString("project_ID", "token");
        id_user = sharedPreferences_user.getString("user_ID", "token");

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        list = new ArrayList<>();
        member_list.clear();
        delete_member_list.clear();
        showMember();

        //Bắt sự kiện
        ibtn_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMemberProjectFragment dialog = new AddMemberProjectFragment();
                dialog.show(getFragmentManager(), "CreateMemberFragment");
            }
        });

        ibtn_remove_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewMembers.setAdapter(memberDeleteAdapter);
                btn_confirm_delete_members.setVisibility(View.VISIBLE);
                ibtn_remove_member.setEnabled(false);
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
                            if (member.getUser_Name().toLowerCase(Locale.ROOT).contains(s)) {
                                member_search_list.add(member);
                            }
                        }
                        if (member_search_list.size() > 0) {
                            recyclerViewMembers.setVisibility(View.VISIBLE);
                            tvNoresult_member.setVisibility(View.GONE);
                            memberAdapter = new MemberProjectAdapter(getContext(), member_search_list);
                            recyclerViewMembers.setAdapter(memberAdapter);
                        } else {
                            recyclerViewMembers.setVisibility(View.GONE);
                            tvNoresult_member.setVisibility(View.VISIBLE);
                        }
                    }else {
                        recyclerViewMembers.setVisibility(View.VISIBLE);
                        tvNoresult_member.setVisibility(View.GONE);
                        memberAdapter = new MemberProjectAdapter(getContext(), member_list);
                        recyclerViewMembers.setAdapter(memberAdapter);
                    }
                }

                if (recyclerViewMembers.getAdapter() == memberDeleteAdapter){
                    if (s.length()>0){
                        member_search_list.clear();
                        for (User member : member_list){
//                        Toast.makeText(getContext(), "" + groupChat.getName().toLowerCase(), Toast.LENGTH_SHORT).show();
                            if (member.getUser_Name().toLowerCase(Locale.ROOT).contains(s)){
                                member_search_list.add(member);
                            }
                        }
                        if (member_search_list.size()>0){
                            recyclerViewMembers.setVisibility(View.VISIBLE);
                            tvNoresult_member.setVisibility(View.GONE);
                            memberDeleteAdapter = new MemberProjectDeleteAdapter(getContext(), member_search_list);
                            recyclerViewMembers.setAdapter(memberDeleteAdapter);
                        }else {
                            recyclerViewMembers.setVisibility(View.GONE);
                            tvNoresult_member.setVisibility(View.VISIBLE);
                        }
                    }else {
                        recyclerViewMembers.setVisibility(View.VISIBLE);
                        tvNoresult_member.setVisibility(View.GONE);
                        memberDeleteAdapter = new MemberProjectDeleteAdapter(getContext(), member_list);
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
                if (delete_member_list.size() > 0){
                    for (User m : delete_member_list){
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User_List_By_Project").
                                child(id_project).child(m.getUser_ID());
                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                                    member_list.clear();
                                    delete_member_list.clear();
                                    showMember();
                                    recyclerViewMembers.setAdapter(memberAdapter);
                                    btn_confirm_delete_members.setVisibility(View.GONE);
                                    ibtn_remove_member.setEnabled(true);
                                }
                            }
                        });
                    }
                }else {
//                    Toast.makeText(activity, "Success delete", Toast.LENGTH_SHORT).show();
                    memberDeleteAdapter.notifyDataSetChanged();
                    memberAdapter.notifyDataSetChanged();
                    recyclerViewMembers.setAdapter(memberAdapter);
                    btn_confirm_delete_members.setVisibility(View.GONE);
                    ibtn_remove_member.setEnabled(true);
                }
            }
        });

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        MemberDetailProjectFragment memberDetailProjectFragment = new MemberDetailProjectFragment();
        Bundle bundle = new Bundle();
        bundle.putString("member_id_txt", member_list.get(position).getUser_ID());
        memberDetailProjectFragment.setArguments(bundle);
        memberDetailProjectFragment.show(getFragmentManager(), "MemberDetailFragment");
    }

    public void showMember(){
        member_list.clear();
        delete_member_list.clear();
        ArrayList<String> listUser_ID = new ArrayList<>();
        if (id_user.equals(firebaseUser.getUid())){
            listUser_ID.clear();
            member_list.clear();
            delete_member_list.clear();
            reference1 = FirebaseDatabase.getInstance().getReference("Projects").child(id_project);
            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Project project = snapshot.getValue(Project.class);
                    if (project.getProject_Leader().equals(id_user)){
                        ibtn_add_member.setVisibility(View.VISIBLE);
                        ibtn_remove_member.setVisibility(View.VISIBLE);
                    }else {
                        ibtn_add_member.setVisibility(View.GONE);
                        ibtn_remove_member.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            reference2 = FirebaseDatabase.getInstance().getReference("User_List_By_Project").child(id_project);
            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listUser_ID.clear();
                    String leader = "";
//                    Toast.makeText(activity, "" + listUser_ID.size(), Toast.LENGTH_SHORT).show();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Group_Chat_Users user = dataSnapshot.getValue(Group_Chat_Users.class);
                        listUser_ID.add(user.getUser_ID());
                        if (user.getPosition().toLowerCase().equals("leader")){
                            leader = user.getUser_ID();
                        }
//                        Toast.makeText(activity, "" + listUser_ID.size(), Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(activity, "" + listUser_ID.size(), Toast.LENGTH_SHORT).show();
                    getUserListByProject(listUser_ID, leader);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    public void getUserListByProject(ArrayList<String> list, String leader){
        reference3 = FirebaseDatabase.getInstance().getReference("Users");
        member_list.clear();
        delete_member_list.clear();
        for (String s : list){
            if (s.equals(leader)){
                reference3.child(s).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        member_list.clear();
                        delete_member_list.clear();
                        User user = snapshot.getValue(User.class);
//                    Toast.makeText(activity, "" + groupChat.getGroup_ID(), Toast.LENGTH_SHORT).show();
                        member_list.add(user);
                        delete_member_list.add(user);
                        memberDeleteAdapter.notifyDataSetChanged();
                        memberAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        member_list.clear();
                        delete_member_list.clear();
                        memberDeleteAdapter.notifyDataSetChanged();
                        memberAdapter.notifyDataSetChanged();
                    }
                });
                list.remove(s);
                break;
            }
        }

        for (String s : list){
            member_list.clear();
            delete_member_list.clear();
//            Toast.makeText(activity, "" + s, Toast.LENGTH_SHORT).show();
            reference3.child(s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
//                    Toast.makeText(activity, "" + groupChat.getGroup_ID(), Toast.LENGTH_SHORT).show();
                    member_list.add(user);
                    delete_member_list.add(user);
                    memberDeleteAdapter.notifyDataSetChanged();
                    memberAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    member_list.clear();
                    delete_member_list.clear();
                    memberDeleteAdapter.notifyDataSetChanged();
                    memberAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}