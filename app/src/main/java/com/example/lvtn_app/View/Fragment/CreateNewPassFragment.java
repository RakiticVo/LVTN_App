package com.example.lvtn_app.View.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lvtn_app.Controller.Retrofit.ApiService;
import com.example.lvtn_app.Controller.Retrofit.ApiUtils;
import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewPassFragment extends DialogFragment {
    //Khai báo
    TextInputLayout password_text_input_layout, new_password_text_input_layout;
    Button btn_confirm_create_new_password, btn_cancel_create_new_password;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;

    //User Information for Update userPass
    int id_user;
    String userPass = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set up
        View view = inflater.inflate(R.layout.fragment_create_new_pass, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        password_text_input_layout = view.findViewById(R.id.password_text_input_layout);
        new_password_text_input_layout = view.findViewById(R.id.new_password_text_input_layout);
        btn_confirm_create_new_password = view.findViewById(R.id.btn_confirm_create_new_password);
        btn_cancel_create_new_password = view.findViewById(R.id.btn_cancel_create_new_password);

        mSharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();

        Toast.makeText(getContext(), "" + mSharedPreferences.getString("userPassword_txt", "123"), Toast.LENGTH_SHORT).show();

        //Bắt sự kiện
        //Todo: Xử lý sự kiện rời khỏi fragment
        btn_cancel_create_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng cho Password
        password_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (password_text_input_layout.getEditText().getText().length() == 0){
                        password_text_input_layout.setError("Please enter password");
                        password_text_input_layout.setErrorEnabled(true);
                    }else CheckPassword(password_text_input_layout, mSharedPreferences.getString("userPassword_txt", "123"));
                }else {
                    password_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                password_text_input_layout.setError("Please enter password");
                                password_text_input_layout.setErrorEnabled(true);
                            }else password_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            CheckPassword(password_text_input_layout, mSharedPreferences.getString("userPassword_txt", "123"));
                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng cho New Password
        new_password_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (new_password_text_input_layout.getEditText().getText().length() == 0){
                        new_password_text_input_layout.setError("Please enter new password");
                        new_password_text_input_layout.setErrorEnabled(true);
                    }else CheckPassword(new_password_text_input_layout, mSharedPreferences.getString("userPassword_txt", "123"));
                }else{
                    new_password_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                new_password_text_input_layout.setError("Please enter new password");
                                new_password_text_input_layout.setErrorEnabled(true);
                            }else new_password_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện cập nhật password mới:
        // - Kiểm tra rỗng và sự chính xác của các Text ----- (Done)
        // - Lấy ra text và cần sử dụng ----- (Done)
        // - Gọi Api Service để cập nhật password trên database ----- (Done)
        // - Gọi SharedPreferences.Editor để cập nhật password trên thiết bị ----- (Done)
        btn_confirm_create_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: Check permission
                if (password_text_input_layout.getEditText().getText().length() == 0){
                    password_text_input_layout.setError("Please enter password!!!");
                    password_text_input_layout.setErrorEnabled(true);
                }else {
                    CheckPassword(password_text_input_layout, mSharedPreferences.getString("userPassword_txt", "123"));
                }
                if (new_password_text_input_layout.getEditText().getText().length() == 0){
                    new_password_text_input_layout.setError("Please enter password!!!");
                    new_password_text_input_layout.setErrorEnabled(true);
                }else CheckPassword(new_password_text_input_layout, mSharedPreferences.getString("userPassword_txt", "123"));

                if (password_text_input_layout.isErrorEnabled() || new_password_text_input_layout.isErrorEnabled()) {
                    Toast.makeText(getContext(), "Please check error!!!", Toast.LENGTH_SHORT).show();
                }else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    id_user = mSharedPreferences.getInt("userId_txt", -1);
                                    userPass = new_password_text_input_layout.getEditText().getText().toString();
                                    Toast.makeText(getContext(), "" + id_user + "\n" + userPass, Toast.LENGTH_SHORT).show();
                                    if (id_user != -1){
                                        updateUserPass(id_user, userPass);
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    Toast.makeText(getContext(), "Create error!!!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you want to create new password?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            }
        });

        return view;
    }

    public void updateUserPass(int id_user, String userPass){
        ApiService updateUserPass = ApiUtils.connectRetrofit();
        updateUserPass.isUpdateUserPassSuccess(id_user, userPass).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("SUCCESS")){
                    Toast.makeText(getContext(), "Update success" , Toast.LENGTH_SHORT).show();
                    editor.putString("userPassword_txt", userPass);
                    editor.commit();
                    Objects.requireNonNull(getDialog()).dismiss();
                }else {
                    Toast.makeText(getContext(), "Update failed" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "" + call + "\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void CheckPassword(TextInputLayout textInputLayout, String password){
        if(textInputLayout.getEditText().getText().toString().length() <6){
            textInputLayout.setError("Password must be more than 6 characters");
            textInputLayout.setErrorEnabled(true);
        }else if (textInputLayout.getEditText().getText().toString().equals(password)){
            if (textInputLayout != password_text_input_layout){
                textInputLayout.setError("Duplicate password");
                textInputLayout.setErrorEnabled(true);
            }
        }else textInputLayout.setErrorEnabled(false);
    }
}