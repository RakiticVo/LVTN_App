package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lvtn_app.Model.Group_Chat_Users;
import com.example.lvtn_app.Model.Project_Users;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberDetailProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberDetailProjectFragment extends DialogFragment {
    //Declare
    CircleImageView avatar_member_detail;
    TextView tv_username_member_detail, tv_email_member_detail, tv_phoneNumber_member_detail, tv_position_member_detail;
    ImageButton ibtn_back_member_detail;
    Button btn_confirm_update_member;
    TextInputLayout position_member_detail_text_input_layout;

    SharedPreferences sharedPreferences_user, sharedPreferences_project;
    String username = "";
    String leader = "";
    AppCompatActivity activity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MemberDetailProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MemberDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemberDetailProjectFragment newInstance(String param1, String param2) {
        MemberDetailProjectFragment fragment = new MemberDetailProjectFragment();
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
        //Declare
        View view = inflater.inflate(R.layout.fragment_member_detail_project, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        avatar_member_detail = view.findViewById(R.id.avatar_member_detail);
        tv_username_member_detail = view.findViewById(R.id.tv_username_member_detail);
        tv_email_member_detail = view.findViewById(R.id.tv_email_member_detail);
        tv_phoneNumber_member_detail = view.findViewById(R.id.tv_phoneNumber_member_detail);
        tv_position_member_detail = view.findViewById(R.id.tv_position_member_detail);
        ibtn_back_member_detail = view.findViewById(R.id.ibtn_back_member_detail);
        btn_confirm_update_member = view.findViewById(R.id.btn_confirm_update_member);
        position_member_detail_text_input_layout = view.findViewById(R.id.position_member_detail_text_input_layout);

        sharedPreferences_user = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        sharedPreferences_project = requireContext().getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
        activity = (AppCompatActivity) getContext();

        //Set up
        Bundle bundle = getArguments();
        String member_id = bundle.getString("member_id_txt", "abc");
        String project_id = bundle.getString("project_id_txt", "abc");

        username = sharedPreferences_user.getString("userName_txt", "abc");
        leader = sharedPreferences_project.getString("projectLeader_txt", "abc");

        //Set data
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(member_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    if (user.getUser_Avatar().length() > 2){
                        Glide.with(activity).load(user.getUser_Avatar()).into(avatar_member_detail);
                    }
                    tv_email_member_detail.setText(user.getUser_Email());
                    tv_phoneNumber_member_detail.setText(user.getUser_Phone());
                    tv_username_member_detail.setText(user.getUser_Name());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User_List_By_Project").child(project_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Project_Users users = dataSnapshot.getValue(Project_Users.class);
//                        Toast.makeText(context, "" + users.getGroup_ID(), Toast.LENGTH_SHORT).show();
                    if (member_id.equals(users.getUser_ID())){
                        tv_position_member_detail.setText(users.getPosition());
                        position_member_detail_text_input_layout.getEditText().setText(users.getPosition());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Toast.makeText(getContext(), "" + member_name + "\n" + username +"\n" + leader, Toast.LENGTH_SHORT).show();

//        if (username.equals(leader)){
////            btn_confirm_update_member.setVisibility(View.VISIBLE);
//            tv_position_member_detail.setVisibility(View.VISIBLE);
////            position_member_detail_text_input_layout.setVisibility(View.VISIBLE);
//        }else {
////            btn_confirm_update_member.setVisibility(View.GONE);
//            tv_position_member_detail.setVisibility(View.VISIBLE);
////            position_member_detail_text_input_layout.setVisibility(View.GONE);
//        }

        //Event Handling
        ibtn_back_member_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

//        btn_confirm_update_member.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (member_id>0 && position_member_detail_text_input_layout.getEditText().getText().toString().length()>0){
//                    position_member_detail_text_input_layout.setErrorEnabled(false);
//                    MemberProjectFragment.getInstance().updatePosition(member_id-1, position_member_detail_text_input_layout.getEditText().getText().toString());
//                    dismiss();
//                }else{
//                    position_member_detail_text_input_layout.setError("Please enter Position");
//                    position_member_detail_text_input_layout.setErrorEnabled(true);
//                }
//            }
//        });

        return view;
    }
}