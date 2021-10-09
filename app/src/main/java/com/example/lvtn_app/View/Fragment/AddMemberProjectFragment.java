package com.example.lvtn_app.View.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMemberProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMemberProjectFragment extends DialogFragment {
    //Khai báo
    TextInputLayout create_email_add_member_text_input_layout;
    Button btn_invite_member, btn_cancel_invite_member;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddMemberProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateMemberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMemberProjectFragment newInstance(String param1, String param2) {
        AddMemberProjectFragment fragment = new AddMemberProjectFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_member_project, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        create_email_add_member_text_input_layout = view.findViewById(R.id.create_email_add_member_text_input_layout);
        btn_invite_member = view.findViewById(R.id.btn_invite_member);
        btn_cancel_invite_member = view.findViewById(R.id.btn_cancel_invite_member);

        //Bắt sự kiện
        //Todo: Xử lý sự kiện rời khỏi fragment
        btn_cancel_invite_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng cho Email Member
        create_email_add_member_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    checkRightEmail(create_email_add_member_text_input_layout.getEditText().getText().toString());
                }else{
                    create_email_add_member_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                create_email_add_member_text_input_layout.setError("Please enter Email's member!!!");
                                create_email_add_member_text_input_layout.setErrorEnabled(true);
                            }else {
                                create_email_add_member_text_input_layout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        //ToDo: Xử lý sự kiện xác nhận mới tham gia dự án:
        // - Kiểm tra định dạng và khác rỗng cho email -------- (Done)
        // - Kiểm tra User có trong danh sách User list của Project theo email hay không? -------- (Incomplete)
        // - Nếu không có xử lý thông báo mời. Ngược lại báo đã có. -------- (Incomplete)
        // - Hoặc kiểm tra với User trong Database nếu không có email thông báo không có người dùng -------- (Incomplete)
        // - Gọi Api Serive để lưu thông báo mời trên database nếu có User tồn tại và chưa được thêm vào -------- (Incomplete)
        btn_invite_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRightEmail(create_email_add_member_text_input_layout.getEditText().getText().toString());
                if (create_email_add_member_text_input_layout.isErrorEnabled()){
                    Toast.makeText(getContext(), "Please check error", Toast.LENGTH_SHORT).show();
                }else {
                    create_email_add_member_text_input_layout.setErrorEnabled(false);
//                        Toast.makeText(getContext(), "Invite email: " + edt_email_add_member.getText(), Toast.LENGTH_SHORT).show();
                    // Todo: Search member via Email
                    // Todo: If member is is null show message else create Member and send message invite
                    String username = "Rakitic";
                    String position = "Developer";
                    String avatar = "https://upanh123.com/wp-content/uploads/2020/09/f676521b471839e8428f79b94441d641.jpg";
                    String phone = "0942920838";
                    String email = create_email_add_member_text_input_layout.getEditText().getText().toString();
                    int status = 1;

//                        Toast.makeText(getContext(), "" + edt_email_add_member.getText() + "\n"
//                                + username + "\n"
//                                + position + "\n"
//                                + status + "\n"
//                                + avatar + "\n", Toast.LENGTH_SHORT).show();
                    MemberProjectFragment.getInstance().AddMember(username, email, avatar, phone, position, status);
                    dismiss();
                }
            }
        });

        return view;
    }

    //ToDo: Kiểm tra định dạng email
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //ToDo: Kiểm tra có phải là 1 email hay không?
    public void checkRightEmail(String email){
        if (!isValidEmail(email)){
            create_email_add_member_text_input_layout.setError("Wrong format!!! Please try again");
            create_email_add_member_text_input_layout.setErrorEnabled(true);
        }else {
            create_email_add_member_text_input_layout.setErrorEnabled(false);
        }
    }
}