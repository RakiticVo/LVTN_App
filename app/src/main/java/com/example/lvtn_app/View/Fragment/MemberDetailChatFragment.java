package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

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
import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberDetailChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberDetailChatFragment extends DialogFragment {
    //Declare
    CircleImageView avatar_member_detail;
    TextView tv_username_member_detail, tv_email_member_detail, tv_phoneNumber_member_detail, tv_position_member_detail;
    ImageButton ibtn_back_member_detail;
    Button btn_confirm_update_profile;
    TextInputLayout position_member_detail_text_input_layout;

    SharedPreferences sharedPreferences_user, sharedPreferences_group;
    String username = "";
    String leader = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MemberDetailChatFragment() {
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
        View view = inflater.inflate(R.layout.fragment_member_detail_chat, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        avatar_member_detail = view.findViewById(R.id.avatar_member_detail_chat);
        tv_username_member_detail = view.findViewById(R.id.tv_username_member_detail_chat);
        tv_email_member_detail = view.findViewById(R.id.tv_email_member_detail_chat);
        tv_phoneNumber_member_detail = view.findViewById(R.id.tv_phoneNumber_member_detail_chat);
        tv_position_member_detail = view.findViewById(R.id.tv_position_member_detail_chat);
        ibtn_back_member_detail = view.findViewById(R.id.ibtn_back_member_detail_chat);
        btn_confirm_update_profile = view.findViewById(R.id.btn_confirm_update_profile_chat);
        position_member_detail_text_input_layout = view.findViewById(R.id.position_member_detail_chat_text_input_layout);

        sharedPreferences_user = Objects.requireNonNull(getContext()).getSharedPreferences("User", Context.MODE_PRIVATE);
        sharedPreferences_group = Objects.requireNonNull(getContext()).getSharedPreferences("Chat", Context.MODE_PRIVATE);

        Bundle bundle = getArguments();
        int member_id = bundle.getInt("member_id_txt", -1);
        String member_avatar = bundle.getString("member_avatar_txt", "");
        String member_name = bundle.getString("member_name_txt", "MemberName");
        String member_email = bundle.getString("member_email_txt", "Email");
        String member_phone = bundle.getString("member_phone_txt", "Phone");
        String member_position = bundle.getString("member_position_txt", "Position");

        username = sharedPreferences_user.getString("userName_txt", "abc");
        leader = sharedPreferences_group.getString("groupChat_creator", "abc");

        //Set data
        if (member_avatar.length() >0){
            Glide.with(getContext()).load(member_avatar).centerCrop().into(avatar_member_detail);
        }
        tv_username_member_detail.setText(member_name);
        tv_email_member_detail.setText(member_email);
        tv_phoneNumber_member_detail.setText(member_phone);
        tv_position_member_detail.setText(member_position);
        position_member_detail_text_input_layout.getEditText().setText(member_position);

        Toast.makeText(getContext(), "" + member_name + "\n" + username +"\n" + leader, Toast.LENGTH_SHORT).show();

        if (username.equals(leader)){
            btn_confirm_update_profile.setVisibility(View.VISIBLE);
            tv_position_member_detail.setVisibility(View.GONE);
            position_member_detail_text_input_layout.setVisibility(View.VISIBLE);
        }else {
            btn_confirm_update_profile.setVisibility(View.GONE);
            tv_position_member_detail.setVisibility(View.VISIBLE);
            position_member_detail_text_input_layout.setVisibility(View.GONE);
        }

        //Event Handling
        ibtn_back_member_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_confirm_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (member_id>0 && position_member_detail_text_input_layout.getEditText().getText().toString().length()>0){
                    position_member_detail_text_input_layout.setErrorEnabled(false);
                    MemberChatFragment.getInstance().updatePosition(member_id-1, position_member_detail_text_input_layout.getEditText().getText().toString());
                    dismiss();
                }else{
                    position_member_detail_text_input_layout.setError("Please enter Position");
                    position_member_detail_text_input_layout.setErrorEnabled(true);
                }
            }
        });

        return view;
    }
}