package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Controller.Retrofit.ApiService;
import com.example.lvtn_app.Controller.Retrofit.ApiUtils;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Activity.LoginActivity;
import com.example.lvtn_app.View.Activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginTabFragment extends Fragment {
    //Declare
    TextInputLayout email_login_text_input_layout, pass_login_text_input_layout;
    TextView forgetpass;
    CheckBox savepass;
    Button login;

    String txt_email = "";
    String txt_pass = "";
    String password = "";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginTabFragment newInstance(String param1, String param2) {
        LoginTabFragment fragment = new LoginTabFragment();
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
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container,false);

        email_login_text_input_layout = viewGroup.findViewById(R.id.email_login_text_input_layout);
        pass_login_text_input_layout = viewGroup.findViewById(R.id.pass_login_text_input_layout);
        forgetpass = viewGroup.findViewById(R.id.forget_pass_login);
        savepass = viewGroup.findViewById(R.id.cb_save_pass_login);
        login = viewGroup.findViewById(R.id.btn_login);


        sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Set data
        if (sharedPreferences.getBoolean("userChecked", false) == true){
            int id_user = sharedPreferences.getInt("userId_txt", -1);
            if (id_user > 0){
                ApiService service = ApiUtils.connectRetrofit();
                service.isUpdateUserInformationSuccess(id_user, true).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
//                        Toast.makeText(getContext(), "" + response.body(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getContext(), "" + call + "\n" + t, Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "onFailure: " + call + "\n" + t );
                    }
                });
            }else{
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }

        txt_email = sharedPreferences.getString("userEmail_txt", "");
        txt_pass = sharedPreferences.getString("userPassword_txt", "");
        if (txt_email.length() > 0){
            email_login_text_input_layout.getEditText().setText(txt_email);
        }
        if (txt_email.length() > 0){
            pass_login_text_input_layout.getEditText().setText(txt_pass);
        }
        savepass.setChecked(sharedPreferences.getBoolean("userChecked", false));

        //Event handling
        email_login_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (email_login_text_input_layout.getEditText().getText().length() == 0){
                        email_login_text_input_layout.setError("Please enter Email");
                        email_login_text_input_layout.setErrorEnabled(true);
                    }else if (!isValidEmail(email_login_text_input_layout.getEditText().getText().toString())){
                        email_login_text_input_layout.setError("Please enter correct Email format");
                        email_login_text_input_layout.setErrorEnabled(true);
                    } else {
                        email_login_text_input_layout.setErrorEnabled(false);
                    }
                }else {
                    email_login_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                email_login_text_input_layout.setError("Please enter Email");
                                email_login_text_input_layout.setErrorEnabled(true);
                            }else email_login_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        pass_login_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (pass_login_text_input_layout.getEditText().getText().length() == 0){
                        pass_login_text_input_layout.setError("Please enter Password");
                        pass_login_text_input_layout.setErrorEnabled(true);
                    }else if (pass_login_text_input_layout.getEditText().getText().length() < 6){
                        pass_login_text_input_layout.setError("Password must be more 6 characters");
                        pass_login_text_input_layout.setErrorEnabled(true);
                    } else {
                        pass_login_text_input_layout.setErrorEnabled(false);
                    }
                }else{
                    pass_login_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                pass_login_text_input_layout.setError("Please enter Password");
                                pass_login_text_input_layout.setErrorEnabled(true);
                            }else if (s.length() < 6) {
                                pass_login_text_input_layout.setError("Password must be more 6 characters");
                                pass_login_text_input_layout.setErrorEnabled(true);
                            }else {
                                pass_login_text_input_layout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_email = email_login_text_input_layout.getEditText().getText().toString();
                txt_pass = pass_login_text_input_layout.getEditText().getText().toString();
                Boolean isChecked = savepass.isChecked();

                // Todo Check pass word
                for (User user : LoginActivity.getInstance().user_list){
                    if (txt_email.equals(user.getUserEmail())){
                        email_login_text_input_layout.setErrorEnabled(false);
                        if (txt_pass.equals(user.getUserPass())){
                            if (isChecked.equals(true)) {
                                // Nếu có check thì lưu tài khoản và mật khẩu
                                editor.putInt("userId_txt", user.getId_user());
                                editor.putString("userName_txt", user.getUserName());
                                editor.putString("userEmail_txt", txt_email);
                                editor.putString("userPassword_txt", txt_pass);
                                editor.putBoolean("userStatus_txt", true);
                                editor.putBoolean("userChecked", isChecked);
                                editor.commit();
                            }
                            ApiService service = ApiUtils.connectRetrofit();
                            service.isUpdateUserInformationSuccess(user.getId_user(), true).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
//                                    Toast.makeText(getContext(), "" + response.body(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(getContext(), "" + call + "\n" + t, Toast.LENGTH_SHORT).show();
                                    Log.e("TAG", "onFailure: " + call + "\n" + t );
                                }
                            });
                            break;
                        }else {
                            pass_login_text_input_layout.setError("Wrong password!!!");
                            pass_login_text_input_layout.setErrorEnabled(true);
                        }
                    }else {
                        email_login_text_input_layout.setError("Wrong account!!!");
                        email_login_text_input_layout.setErrorEnabled(true);
                    }
                }
            }
        });
        return  viewGroup;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


}