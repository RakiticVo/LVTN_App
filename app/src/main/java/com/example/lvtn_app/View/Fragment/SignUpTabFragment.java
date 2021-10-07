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
import android.widget.EditText;
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
 * Use the {@link SignUpTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpTabFragment extends Fragment {
    // Khai báo
    TextInputLayout user_name_signup_text_input_layout, email_signup_text_input_layout, pass_signup_text_input_layout, checkpass_signup_text_input_layout;
    Button signup;

    FirebaseAuth auth;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //New User Information
    String userName = "";
    String userEmail = "";
    String userPass = "";
    String checkpass = "";
    int id_user = -1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpTabFragment newInstance(String param1, String param2) {
        SignUpTabFragment fragment = new SignUpTabFragment();
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
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_sign_up_tab, container,false);

        user_name_signup_text_input_layout = viewGroup.findViewById(R.id.user_name_signup_text_input_layout);
        email_signup_text_input_layout = viewGroup.findViewById(R.id.email_signup_text_input_layout);
        pass_signup_text_input_layout = viewGroup.findViewById(R.id.pass_signup_text_input_layout);
        checkpass_signup_text_input_layout = viewGroup.findViewById(R.id.checkpass_signup_text_input_layout);
        signup = viewGroup.findViewById(R.id.btn_signup);

        auth = FirebaseAuth.getInstance();
        sharedPreferences = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Xử lý sự kiện
        user_name_signup_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (user_name_signup_text_input_layout.getEditText().getText().length() == 0){
                        user_name_signup_text_input_layout.setError("Please enter User name");
                        user_name_signup_text_input_layout.setErrorEnabled(true);
                    }else{
                        user_name_signup_text_input_layout.setErrorEnabled(false);
                    }
                }else {
                    user_name_signup_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                user_name_signup_text_input_layout.setError("Please enter User name");
                                user_name_signup_text_input_layout.setErrorEnabled(true);
                            }else{
                                user_name_signup_text_input_layout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        email_signup_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (email_signup_text_input_layout.getEditText().getText().length() == 0){
                        email_signup_text_input_layout.setError("Please enter Email name");
                        email_signup_text_input_layout.setErrorEnabled(true);
                    }else if (!isValidEmail(email_signup_text_input_layout.getEditText().getText().toString())){
                        email_signup_text_input_layout.setError("Incorrect Email format");
                        email_signup_text_input_layout.setErrorEnabled(true);
                    }else{
                        user_name_signup_text_input_layout.setErrorEnabled(false);
                    }
                }else {
                    email_signup_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                email_signup_text_input_layout.setError("Please enter Email name");
                                email_signup_text_input_layout.setErrorEnabled(true);
                            }else{
                                email_signup_text_input_layout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        pass_signup_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (pass_signup_text_input_layout.getEditText().getText().length() == 0){
                        pass_signup_text_input_layout.setError("Please enter Password");
                        pass_signup_text_input_layout.setErrorEnabled(true);
                    }else if (pass_signup_text_input_layout.getEditText().getText().length() < 6){
                        pass_signup_text_input_layout.setError("Password must be more 6 characters");
                        pass_signup_text_input_layout.setErrorEnabled(true);
                    } else {
                        pass_signup_text_input_layout.setErrorEnabled(false);
                    }
                }else{
                    pass_signup_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                pass_signup_text_input_layout.setError("Please enter Password");
                                pass_signup_text_input_layout.setErrorEnabled(true);
                            }else {
                                pass_signup_text_input_layout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        checkpass_signup_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (checkpass_signup_text_input_layout.getEditText().getText().length() == 0){
                        checkpass_signup_text_input_layout.setError("Please enter Password");
                        checkpass_signup_text_input_layout.setErrorEnabled(true);
                    }else if (checkpass_signup_text_input_layout.getEditText().getText().length() < 6){
                        checkpass_signup_text_input_layout.setError("Password must be more 6 characters");
                        checkpass_signup_text_input_layout.setErrorEnabled(true);
                    }else if (!checkpass_signup_text_input_layout.getEditText().getText().toString().equals(pass_signup_text_input_layout.getEditText().getText().toString())){
                        checkpass_signup_text_input_layout.setError("Incorrect password");
                        checkpass_signup_text_input_layout.setErrorEnabled(true);
                    }else {
                        checkpass_signup_text_input_layout.setErrorEnabled(false);
                    }
                }else{
                    checkpass_signup_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                checkpass_signup_text_input_layout.setError("Please enter Password");
                                checkpass_signup_text_input_layout.setErrorEnabled(true);
                            }else {
                                checkpass_signup_text_input_layout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = user_name_signup_text_input_layout.getEditText().getText().toString().trim();
                userEmail = email_signup_text_input_layout.getEditText().getText().toString().trim();
                userPass = pass_signup_text_input_layout.getEditText().getText().toString().trim();
                checkpass = checkpass_signup_text_input_layout.getEditText().getText().toString().trim();

                if (userName.length() == 0){
                    user_name_signup_text_input_layout.setError("Please enter user name!!!");
                    user_name_signup_text_input_layout.setErrorEnabled(true);
                }else user_name_signup_text_input_layout.setErrorEnabled(false);

                if (userEmail.length() == 0){
                    email_signup_text_input_layout.setError("Please enter user email!!!");
                    email_signup_text_input_layout.setErrorEnabled(true);
                }else email_signup_text_input_layout.setErrorEnabled(false);

                if (userPass.length() == 0){
                    pass_signup_text_input_layout.setError("Please enter user password!!!");
                    pass_signup_text_input_layout.setErrorEnabled(true);
                }else if (userPass.length() < 6) {
                    pass_signup_text_input_layout.setError("Password must be more 6 characters");
                    pass_signup_text_input_layout.setErrorEnabled(true);
                }else {
                    pass_signup_text_input_layout.setErrorEnabled(false);
                }

                if (checkpass.length() == 0){
                    pass_signup_text_input_layout.setError("Please enter confirm password!!!");
                    pass_signup_text_input_layout.setErrorEnabled(true);
                }else if (checkpass.length() < 6) {
                    pass_signup_text_input_layout.setError("Password must be more 6 characters");
                    pass_signup_text_input_layout.setErrorEnabled(true);
                }else if (!checkpass_signup_text_input_layout.getEditText().getText().toString().equals(pass_signup_text_input_layout.getEditText().getText().toString())){
                    checkpass_signup_text_input_layout.setError("Incorrect password");
                    checkpass_signup_text_input_layout.setErrorEnabled(true);
                }else {
                    checkpass_signup_text_input_layout.setErrorEnabled(false);
                }

                if (user_name_signup_text_input_layout.isErrorEnabled() ||
                    email_signup_text_input_layout.isErrorEnabled() ||
                    pass_signup_text_input_layout.isErrorEnabled() ||
                    checkpass_signup_text_input_layout.isErrorEnabled()){
                    Toast.makeText(getContext(), "Please check error!!!", Toast.LENGTH_SHORT).show();
                }else {
                    ApiService insertNewUser = ApiUtils.connectRetrofit();
                    insertNewUser.isCreateNewUserSuccess(userName, userEmail, userPass).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String result = response.body();
                            if (result.equals("SUCCESS")){
                                ApiService getUser = ApiUtils.connectRetrofit();
                                getUser.getUser().enqueue(new Callback<ArrayList<User>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                                        ArrayList<User> temp = response.body();
                                        if (temp != null){
                                            id_user = temp.get(temp.size()-1).getId_user();
                                        }
                                        Toast.makeText(getContext(), "Create success", Toast.LENGTH_SHORT).show();
                                        ApiService insertNewUserInfomation = ApiUtils.connectRetrofit();
                                        insertNewUserInfomation.isInsertNewUserInformationSuccess(id_user).enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                String result2 = response.body();
                                                if (!result2.equals("FAILED")){
//                                                        Toast.makeText(getContext(), "" + response.body(), Toast.LENGTH_SHORT).show();
                                                    registerUser(userName, userEmail, userPass);
                                                }else {
                                                    Toast.makeText(getContext(), "Failed \n" + call + "\n" + result2, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                Toast.makeText(getContext(), "Failed \n" + call + "\n" + t, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                                    }
                                });
                            }else Toast.makeText(getContext(), "" + result, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getContext(), "" + call +"\n"+ t , Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onFailure: " + t );
                        }
                    });
                }
            }
        });
        return  viewGroup;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void registerUser(String userName, String userEmail, String userPass){
        auth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String user_ID = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(user_ID);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("User_ID", user_ID);
                    hashMap.put("User_Name", userName);
                    hashMap.put("User_Email", userEmail);
                    hashMap.put("User_Pass", userPass);
                    hashMap.put("User_Status", "online");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                editor.putInt("userId_txt", id_user);
                                editor.putString("userName_txt", userName);
                                editor.putString("userEmail_txt", userEmail);
                                editor.putString("userPassword_txt", userPass);
                                editor.putBoolean("userStatus_txt", true);
                                editor.putBoolean("userChecked", true);
                                editor.commit();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    });
                }
            }
        });
    }
}