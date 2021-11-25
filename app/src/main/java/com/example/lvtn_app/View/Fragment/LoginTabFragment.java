package com.example.lvtn_app.View.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Activity.ChatActivity;
import com.example.lvtn_app.View.Activity.LoginActivity;
import com.example.lvtn_app.View.Activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginTabFragment extends Fragment {
    //Declare
    TextInputLayout email_login_text_input_layout, pass_login_text_input_layout;
    TextView forgetpass;
    Button login;

    String txt_email = "";
    String txt_pass = "";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    AppCompatActivity activity;

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
        login = viewGroup.findViewById(R.id.btn_login);

        activity = (AppCompatActivity) getContext();

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        sharedPreferences = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(activity.getString(R.string.watiing_for_login));

        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null){
            progressDialog.show();
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("user_Status", "online");
            reference1.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        editor.putString("user_ID", firebaseUser.getUid());
                        editor.commit();
                        progressDialog.dismiss();
                        Toast.makeText(activity, activity.getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        activity.finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(activity, activity.getString(R.string.failed) + task.getResult(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        txt_email = sharedPreferences.getString("user_Email", "");
        txt_pass = sharedPreferences.getString("user_Pass", "");
//        Toast.makeText(getContext(), "" + txt_email + "\n" + txt_pass, Toast.LENGTH_SHORT).show();
        if (txt_email.length() > 0){
            email_login_text_input_layout.getEditText().setText(txt_email);
        }
        if (txt_email.length() > 0){
            pass_login_text_input_layout.getEditText().setText(txt_pass);
        }

        //Event handling
        email_login_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (email_login_text_input_layout.getEditText().getText().length() == 0){
                        email_login_text_input_layout.setError(activity.getString(R.string.enterEmail));
                        email_login_text_input_layout.setErrorEnabled(true);
                    }else if (!isValidEmail(email_login_text_input_layout.getEditText().getText().toString())){
                        email_login_text_input_layout.setError(activity.getString(R.string.enterCorrectEmailFormat));
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
                                email_login_text_input_layout.setError(activity.getString(R.string.enterEmail));
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
                        pass_login_text_input_layout.setError(activity.getString(R.string.enterPassword));
                        pass_login_text_input_layout.setErrorEnabled(true);
                    }else if (pass_login_text_input_layout.getEditText().getText().length() < 6){
                        pass_login_text_input_layout.setError(activity.getString(R.string.pass_6_char));
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
                                pass_login_text_input_layout.setError(activity.getString(R.string.enterPassword));
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
                txt_email = email_login_text_input_layout.getEditText().getText().toString().toLowerCase().trim();
                txt_pass = pass_login_text_input_layout.getEditText().getText().toString().toLowerCase().trim();

                if (txt_email.length() == 0){
                    email_login_text_input_layout.setError(activity.getString(R.string.enterEmail));
                    email_login_text_input_layout.setErrorEnabled(true);
                }else if (!isValidEmail(txt_email)){
                    email_login_text_input_layout.setError(activity.getString(R.string.enterCorrectEmailFormat));
                    email_login_text_input_layout.setErrorEnabled(true);
                }else{
                    email_login_text_input_layout.setErrorEnabled(false);
                }

                if (txt_pass.length() == 0){
                    pass_login_text_input_layout.setError(activity.getString(R.string.enterPassword));
                    pass_login_text_input_layout.setErrorEnabled(true);
                }else if (txt_pass.length() < 6) {
                    pass_login_text_input_layout.setError(activity.getString(R.string.pass_6_char));
                    pass_login_text_input_layout.setErrorEnabled(true);
                }else {
                    pass_login_text_input_layout.setErrorEnabled(false);
                }

                if (email_login_text_input_layout.isErrorEnabled() || pass_login_text_input_layout.isErrorEnabled()){
                    Toast.makeText(activity, activity.getString(R.string.checkError), Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(txt_email, txt_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("user_Status", "online");
                                reference1.updateChildren(hashMap);
                                editor.putString("user_ID", firebaseUser.getUid());
                                editor.commit();
                                Toast.makeText(activity, activity.getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                Intent intent = new Intent(activity, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(activity, activity.getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
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

}