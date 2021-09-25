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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class CreateNewPassFragment extends DialogFragment {
    //Khai báo
    TextInputLayout old_password_text_input_layout, new_password_text_input_layout;
    Button btn_confirm_create_new_password, btn_cancel_create_new_password;
    SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Khai báo
        View view = inflater.inflate(R.layout.fragment_create_new_pass, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        old_password_text_input_layout = view.findViewById(R.id.old_password_text_input_layout);
        new_password_text_input_layout = view.findViewById(R.id.new_password_text_input_layout);
        btn_confirm_create_new_password = view.findViewById(R.id.btn_confirm_create_new_password);
        btn_cancel_create_new_password = view.findViewById(R.id.btn_cancel_create_new_password);

        mSharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("User", Context.MODE_PRIVATE);

        //Bắt sự kiện
        old_password_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (old_password_text_input_layout.getEditText().getText().length() == 0){
                        old_password_text_input_layout.setError("Please enter Password");
                        old_password_text_input_layout.setErrorEnabled(true);
                    }else if(old_password_text_input_layout.getEditText().getText().length() <6){
                        old_password_text_input_layout.setError("Password must be more than 6 characters");
                        old_password_text_input_layout.setErrorEnabled(true);
                    }else if (!old_password_text_input_layout.getEditText().getText().toString().equals(mSharedPreferences.getString("password_txt", "123"))){
                        old_password_text_input_layout.setError("Password is incorrect");
                        old_password_text_input_layout.setErrorEnabled(true);
                    }else old_password_text_input_layout.setErrorEnabled(false);
                }
            }
        });

        new_password_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (new_password_text_input_layout.getEditText().getText().length() == 0){
                        new_password_text_input_layout.setError("Please enter New Password");
                        new_password_text_input_layout.setErrorEnabled(true);
                    }else if(new_password_text_input_layout.getEditText().getText().length() <6){
                        new_password_text_input_layout.setError("Password must be more than 6 characters");
                        new_password_text_input_layout.setErrorEnabled(true);
                    }else if (new_password_text_input_layout.getEditText().getText().toString().equals(old_password_text_input_layout.getEditText().getText().toString())){
                            new_password_text_input_layout.setError("Password is duplicated");
                            new_password_text_input_layout.setErrorEnabled(true);
                    }else new_password_text_input_layout.setErrorEnabled(false);
                }
            }
        });

        btn_confirm_create_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_password_text_input_layout.getEditText().getText().toString().equals(old_password_text_input_layout.getEditText().getText().toString())){
                    new_password_text_input_layout.setError("Password is duplicated");
                    new_password_text_input_layout.setErrorEnabled(true);
                }else {
                    new_password_text_input_layout.setErrorEnabled(false);
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    // Todo: remember check date must be >= today
                                    Toast.makeText(getContext(), "Create new password success", Toast.LENGTH_SHORT).show();
                                    Objects.requireNonNull(getDialog()).dismiss();
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

        btn_cancel_create_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }
}