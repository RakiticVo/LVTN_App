package com.example.lvtn_app.View.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lvtn_app.Adapter.LanguageAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.IssueType;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Activity.LoginActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    //Khai báo
    TextView tv_username_profile, tv_email_profile, tv_gender_profile, tv_phoneNumber_profile, tv_DOB_profile, tv_address_profile;
    TextInputLayout username_profile_text_input_layout, email_profile_text_input_layout, phoneNumber_profile_text_input_layout, DOB_profile_text_input_layout, address_profile_text_input_layout;
    RadioGroup rg_gender;
    RadioButton rbtn_male, rbtn_female;
    ImageButton ibtn_choose_avatar_profile, ibtn_calendar_dob, ibtn_edit_profile, ibtn_privacy_profile, ibtn_help_center_profile, ibtn_about_us_profile, ibtn_back_profile;
    Button btn_logout, btn_confirm_update_profile;
    CircleImageView avatar_profile;
    Spinner spinner_language;
    LanguageAdapter languageAdapter;
    ArrayList<String> languages;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    AppCompatActivity activity;

    StorageReference storageReference;
    private static final int IMG_REQUEST = 1234;
    private Uri imageUri;
    private StorageTask uploadTask;

    //User info
    String user_ID;
    String user_Name;
    String user_Email;
    String user_Pass;
    String user_Gender;
    String user_Phone;
    String user_DOB;
    String user_Address;
    String user_Avatar;


    Calendar myCalendar = Calendar.getInstance();
    DateFormat dateFormat = new DateFormat();

    SharedPreferences mSharedPreferences, sharedPreferences_language;
    SharedPreferences.Editor editor, editor_language;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        //Khai báo
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tv_username_profile = view.findViewById(R.id.tv_username_profile);
        tv_email_profile = view.findViewById(R.id.tv_email_profile);
        tv_gender_profile = view.findViewById(R.id.tv_gender_profile);
        tv_phoneNumber_profile = view.findViewById(R.id.tv_phoneNumber_profile);
        tv_DOB_profile = view.findViewById(R.id.tv_DOB_profile);
        tv_address_profile = view.findViewById(R.id.tv_address_profile);

        tv_address_profile.setMovementMethod(new ScrollingMovementMethod());

        username_profile_text_input_layout = view.findViewById(R.id.username_profile_text_input_layout);
        email_profile_text_input_layout = view.findViewById(R.id.email_profile_text_input_layout);
        phoneNumber_profile_text_input_layout = view.findViewById(R.id.phoneNumber_profile_text_input_layout);
        DOB_profile_text_input_layout = view.findViewById(R.id.DOB_profile_text_input_layout);
        address_profile_text_input_layout = view.findViewById(R.id.address_profile_text_input_layout);

        rg_gender = view.findViewById(R.id.rg_gender);
        rbtn_male = view.findViewById(R.id.rbtn_male);
        rbtn_female = view.findViewById(R.id.rbtn_female);

        ibtn_choose_avatar_profile = view.findViewById(R.id.ibtn_choose_avatar_profile);
        ibtn_calendar_dob = view.findViewById(R.id.ibtn_calendar_dob);
        ibtn_edit_profile = view.findViewById(R.id.ibtn_edit_profile);
        ibtn_privacy_profile = view.findViewById(R.id.ibtn_privacy_profile);
        ibtn_help_center_profile = view.findViewById(R.id.ibtn_help_center_profile);
        ibtn_about_us_profile = view.findViewById(R.id.ibtn_about_us_profile);
        ibtn_back_profile = view.findViewById(R.id.ibtn_back_profile);

        btn_logout = view.findViewById(R.id.btn_logout);
        btn_confirm_update_profile = view.findViewById(R.id.btn_confirm_update_profile);

        avatar_profile = view.findViewById(R.id.avatar_profile);

        activity = (AppCompatActivity) getContext();

        sharedPreferences_language = requireActivity().getSharedPreferences("Config_language", Context.MODE_PRIVATE);

        spinner_language = view.findViewById(R.id.spinner_language);
        languages = new ArrayList<>();
        languages.add("en");
        languages.add("vi");
        languageAdapter = new LanguageAdapter(activity, languages);
        spinner_language.setAdapter(languageAdapter);
        String lang = sharedPreferences_language.getString("Current_Lang", "abcdef");
        String current_lang = requireContext().getResources().getConfiguration().locale.toString();
        if (!current_lang.equals(lang)){
            loadLocale(lang);
        }
        if (!lang.equals("abcdef")){
//            Toast.makeText(activity, "" + lang, Toast.LENGTH_SHORT).show();
            for (int i = 0; i < languages.size(); i++) {
                if (lang.equals(languages.get(i))){
                    spinner_language.setSelection(i);
                }
            }
        }else {
            Locale locale = Locale.getDefault();
            lang = locale.toString();
//            Toast.makeText(activity, "" + lang, Toast.LENGTH_SHORT).show();
            for (int i = 0; i < languages.size(); i++) {
                if (locale.toString().equals(languages.get(i))){
                    spinner_language.setSelection(i);
                }
            }
        }
        spinner_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        setLocate(languages.get(0).toString());
                        if (!current_lang.equals(languages.get(0).toString())){
                            refreshFragment();
                        }
                        break;
                    case 1:
                        setLocate(languages.get(1).toString());
                        if (!current_lang.equals(languages.get(1).toString())){
                            refreshFragment();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                DOB_profile_text_input_layout.getEditText().setText(dateFormat.formatDate(myCalendar.getTime()));
                try {
                    Date date = dateFormat.sdf.parse(DOB_profile_text_input_layout.getEditText().getText().toString());
                    if (date.getTime() < Calendar.getInstance().getTime().getTime()) {
                        DOB_profile_text_input_layout.setErrorEnabled(false);
                    } else {
                        DOB_profile_text_input_layout.setError("Wrong day!!! Please try again");
                        DOB_profile_text_input_layout.setErrorEnabled(true);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        mSharedPreferences = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();

        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Uploads");
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
        String uid = mSharedPreferences.getString("user_ID", "token");
        user_ID = firebaseUser.getUid();

        if (!user_ID.equals("token") && uid.equals(user_ID)){
            reference = FirebaseDatabase.getInstance().getReference("Users").child(user_ID);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
//                        Toast.makeText(getContext(), "" + user.getUser_ID(), Toast.LENGTH_SHORT).show();

                        user_ID = user.getUser_ID();
                        user_Name = user.getUser_Name();
                        user_Email = user.getUser_Email();
                        user_Pass = user.getUser_Pass();
                        user_Gender = user.getUser_Gender();
                        user_Phone = user.getUser_Phone();
                        user_DOB = user.getUser_DOB();
                        user_Address = user.getUser_Address();
                        user_Avatar= user.getUser_Avatar();

                        editor.putString("user_Email", user_Email);
                        editor.putString("user_Pass", user_Pass);
                        editor.commit();

                        tv_username_profile.setText(user_Name);
                        username_profile_text_input_layout.getEditText().setText(user_Name);

                        tv_email_profile.setText(user_Email);
                        email_profile_text_input_layout.getEditText().setText(user_Email);

                        tv_gender_profile.setText(user_Gender);
                        if (user_Gender.equals(" ")){
                            rbtn_male.setChecked(true);
                        }else if (user_Gender.toLowerCase().equals("male")){
                            rbtn_male.setChecked(true);
                        }else{
                            rbtn_female.setChecked(true);
                        }

                        tv_phoneNumber_profile.setText(user_Phone);
                        phoneNumber_profile_text_input_layout.getEditText().setText(user_Phone);

                        tv_DOB_profile.setText(user_DOB);
                        DOB_profile_text_input_layout.getEditText().setText(user_DOB);

                        tv_address_profile.setText(user_Address);
                        address_profile_text_input_layout.getEditText().setText(user_Address);

                        if (user_Avatar != null && user_Avatar.length() > 0
                            && !user_Avatar.equals(" ")){
                            Glide.with(activity).load(user_Avatar).centerCrop().into(avatar_profile);
                        }else {
                            avatar_profile.setImageResource(R.drawable.profile_1);
                        }

                    }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //Bắt sự kiện
        ibtn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_username_profile.setVisibility(View.GONE);
                tv_email_profile.setVisibility(View.GONE);
                tv_gender_profile.setVisibility(View.GONE);
                tv_phoneNumber_profile.setVisibility(View.GONE);
                tv_DOB_profile.setVisibility(View.GONE);
                tv_address_profile.setVisibility(View.GONE);

                username_profile_text_input_layout.setVisibility(View.VISIBLE);
                email_profile_text_input_layout.setVisibility(View.VISIBLE);
                rg_gender.setVisibility(View.VISIBLE);
                phoneNumber_profile_text_input_layout.setVisibility(View.VISIBLE);
                DOB_profile_text_input_layout.setVisibility(View.VISIBLE);
                ibtn_calendar_dob.setVisibility(View.VISIBLE);
                address_profile_text_input_layout.setVisibility(View.VISIBLE);
                ibtn_back_profile.setVisibility(View.VISIBLE);

                btn_logout.setVisibility(View.GONE);
                btn_confirm_update_profile.setVisibility(View.VISIBLE);
            }
        });

        username_profile_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (username_profile_text_input_layout.getEditText().getText().length() < 1) {
                        username_profile_text_input_layout.setError("Please enter username!!!");
                        username_profile_text_input_layout.setErrorEnabled(true);
                    } else {
                        username_profile_text_input_layout.setErrorEnabled(false);
                    }
                } else {
                    username_profile_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0) {
                                username_profile_text_input_layout.setError("Please enter username!!!");
                                username_profile_text_input_layout.setErrorEnabled(true);
                            } else username_profile_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        email_profile_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (email_profile_text_input_layout.getEditText().getText().length() < 1) {
                        email_profile_text_input_layout.setError("Please enter email!!!");
                        email_profile_text_input_layout.setErrorEnabled(true);
                    } else {
                        if (isValidEmail(email_profile_text_input_layout.getEditText().getText())) {
                            email_profile_text_input_layout.setErrorEnabled(false);
                        } else {
                            email_profile_text_input_layout.setError("Wrong format!!! Please try again");
                            email_profile_text_input_layout.setErrorEnabled(true);
                        }
                    }
                } else {
                    email_profile_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0) {
                                email_profile_text_input_layout.setError("Please enter email!!!");
                                email_profile_text_input_layout.setErrorEnabled(true);
                            } else email_profile_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        phoneNumber_profile_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (phoneNumber_profile_text_input_layout.getEditText().getText().length() < 1) {
                        phoneNumber_profile_text_input_layout.setError("Please enter phone number!!!");
                        phoneNumber_profile_text_input_layout.setErrorEnabled(true);
                    } else {
                        if (phoneNumber_profile_text_input_layout.getEditText().getText().toString().length() > 9
                            && phoneNumber_profile_text_input_layout.getEditText().getText().toString().length() < 12) {
                            if (phoneNumber_profile_text_input_layout.getEditText().getText().toString().subSequence(1,2).equals("0")){
                                phoneNumber_profile_text_input_layout.setErrorEnabled(false);
                            }else {
                                phoneNumber_profile_text_input_layout.setError("Wrong format!!! Please try again");
                                phoneNumber_profile_text_input_layout.setErrorEnabled(true);
                            }
                        } else {
                            phoneNumber_profile_text_input_layout.setError("Wrong format!!! Please try again");
                            phoneNumber_profile_text_input_layout.setErrorEnabled(true);
                        }
                    }
                } else {
                    phoneNumber_profile_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0) {
                                phoneNumber_profile_text_input_layout.setError("Please enter phone number!!!");
                                phoneNumber_profile_text_input_layout.setErrorEnabled(true);
                            } else phoneNumber_profile_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        ibtn_calendar_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DOB_profile_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (DOB_profile_text_input_layout.getEditText().getText().length() < 1) {
                        DOB_profile_text_input_layout.setError("Please enter day of birth!!!");
                        DOB_profile_text_input_layout.setErrorEnabled(true);
                    } else {
                        try {
                            Date date = dateFormat.sdf.parse(DOB_profile_text_input_layout.getEditText().getText().toString());
                            if (date.getTime() < Calendar.getInstance().getTime().getTime()) {
                                DOB_profile_text_input_layout.setErrorEnabled(false);
                            } else {
                                DOB_profile_text_input_layout.setError("Wrong day!!! Please try again");
                                DOB_profile_text_input_layout.setErrorEnabled(true);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    DOB_profile_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0) {
                                DOB_profile_text_input_layout.setError("Please enter day of birth!!!");
                                DOB_profile_text_input_layout.setErrorEnabled(true);
                            } else DOB_profile_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        ibtn_back_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_logout.setVisibility(View.VISIBLE);
                tv_username_profile.setVisibility(View.VISIBLE);
                tv_email_profile.setVisibility(View.VISIBLE);
                tv_gender_profile.setVisibility(View.VISIBLE);
                tv_phoneNumber_profile.setVisibility(View.VISIBLE);
                tv_DOB_profile.setVisibility(View.VISIBLE);
                tv_address_profile.setVisibility(View.VISIBLE);

                username_profile_text_input_layout.setVisibility(View.GONE);
                email_profile_text_input_layout.setVisibility(View.GONE);
                rg_gender.setVisibility(View.GONE);
                phoneNumber_profile_text_input_layout.setVisibility(View.GONE);
                DOB_profile_text_input_layout.setVisibility(View.GONE);
                ibtn_calendar_dob.setVisibility(View.GONE);
                address_profile_text_input_layout.setVisibility(View.GONE);
                btn_confirm_update_profile.setVisibility(View.GONE);
                ibtn_back_profile.setVisibility(View.GONE);
            }
        });

        btn_confirm_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username_profile_text_input_layout.getEditText().getText().length() == 0){
                    username_profile_text_input_layout.setError("Wrong format!!! Please try again");
                    username_profile_text_input_layout.setErrorEnabled(true);
                }else username_profile_text_input_layout.setErrorEnabled(false);

                if (!isValidEmail(email_profile_text_input_layout.getEditText().getText())) {
                    email_profile_text_input_layout.setError("Wrong format!!! Please try again");
                    email_profile_text_input_layout.setErrorEnabled(true);
                }else email_profile_text_input_layout.setErrorEnabled(false);

                if (phoneNumber_profile_text_input_layout.getEditText().getText().length() < 1) {
                    phoneNumber_profile_text_input_layout.setError("Please enter phone number!!!");
                    phoneNumber_profile_text_input_layout.setErrorEnabled(true);
                } else {
                    if (phoneNumber_profile_text_input_layout.getEditText().getText().toString().length() > 9
                            && phoneNumber_profile_text_input_layout.getEditText().getText().toString().length() < 12) {
                        if (phoneNumber_profile_text_input_layout.getEditText().getText().toString().subSequence(1,2).equals("0")){
                            phoneNumber_profile_text_input_layout.setErrorEnabled(false);
                        }else {
                            phoneNumber_profile_text_input_layout.setError("Wrong format!!! Please try again");
                            phoneNumber_profile_text_input_layout.setErrorEnabled(true);
                        }
                    } else {
                        phoneNumber_profile_text_input_layout.setError("Wrong format!!! Please try again");
                        phoneNumber_profile_text_input_layout.setErrorEnabled(true);
                    }
                }

                try {
                    Date date = dateFormat.sdf.parse(DOB_profile_text_input_layout.getEditText().getText().toString());
                    if (date.getTime() < Calendar.getInstance().getTime().getTime()) {
                        DOB_profile_text_input_layout.setErrorEnabled(false);
                    } else {
                        DOB_profile_text_input_layout.setError("Wrong day!!! Please try again");
                        DOB_profile_text_input_layout.setErrorEnabled(true);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (username_profile_text_input_layout.isErrorEnabled() || email_profile_text_input_layout.isErrorEnabled()
                    || phoneNumber_profile_text_input_layout.isErrorEnabled() || DOB_profile_text_input_layout.isErrorEnabled()){
                    Toast.makeText(getContext(), "Please check error", Toast.LENGTH_SHORT).show();
                }else{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    user_Name = username_profile_text_input_layout.getEditText().getText().toString();
                                    user_Email = email_profile_text_input_layout.getEditText().getText().toString();
                                    if (rg_gender.getCheckedRadioButtonId() == rbtn_female.getId()) {
                                        user_Gender = rbtn_female.getText().toString();
                                    }
                                    if (rg_gender.getCheckedRadioButtonId() == rbtn_male.getId()) {
                                        user_Gender = rbtn_male.getText().toString();
                                    }
                                    user_Phone = phoneNumber_profile_text_input_layout.getEditText().getText().toString();
                                    user_DOB = DOB_profile_text_input_layout.getEditText().getText().toString();
                                    user_Address = address_profile_text_input_layout.getEditText().getText().toString();
                                    if (user_Address.length() == 0){
                                        user_Address = " ";
                                    }

                                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                    progressDialog.setMessage("Updating");
                                    progressDialog.show();

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("user_Name", user_Name);
                                    hashMap.put("user_Email", user_Email);
                                    hashMap.put("user_Gender", user_Gender);
                                    hashMap.put("user_Phone", user_Phone);
                                    hashMap.put("user_DOB", user_DOB);
                                    hashMap.put("user_Address", user_Address);

                                    reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getContext(), "Update success", Toast.LENGTH_SHORT).show();
                                                tv_username_profile.setText(user_Name);
                                                tv_email_profile.setText(user_Email);
                                                tv_gender_profile.setText(user_Gender);
                                                tv_phoneNumber_profile.setText(user_Phone);
                                                tv_DOB_profile.setText(user_DOB);
                                                tv_address_profile.setText(user_Address);

                                                btn_logout.setVisibility(View.VISIBLE);
                                                tv_username_profile.setVisibility(View.VISIBLE);
                                                tv_email_profile.setVisibility(View.VISIBLE);
                                                tv_gender_profile.setVisibility(View.VISIBLE);
                                                tv_phoneNumber_profile.setVisibility(View.VISIBLE);
                                                tv_DOB_profile.setVisibility(View.VISIBLE);
                                                tv_address_profile.setVisibility(View.VISIBLE);

                                                username_profile_text_input_layout.setVisibility(View.GONE);
                                                email_profile_text_input_layout.setVisibility(View.GONE);
                                                rg_gender.setVisibility(View.GONE);
                                                phoneNumber_profile_text_input_layout.setVisibility(View.GONE);
                                                DOB_profile_text_input_layout.setVisibility(View.GONE);
                                                ibtn_calendar_dob.setVisibility(View.GONE);
                                                address_profile_text_input_layout.setVisibility(View.GONE);
                                                btn_confirm_update_profile.setVisibility(View.GONE);
                                                ibtn_back_profile.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                    progressDialog.dismiss();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    Toast.makeText(getContext(), "Cancel update", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you want to save changes?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            }
        });

        ibtn_choose_avatar_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent();
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(photoPickerIntent, IMG_REQUEST);
            }
        });

        ibtn_privacy_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewPassFragment dialog = new CreateNewPassFragment();
                dialog.show(getFragmentManager(), "CreateNewPassFragment");
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("user_Status", "offline");
                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            auth.signOut();
                            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_Email", user_Email);
                            editor.putString("user_Pass", user_Pass);
                            editor.commit();
                            AppCompatActivity activity = (AppCompatActivity) getContext();
                            Intent intent = new Intent(activity, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getContext(), "Failed" + task.getResult(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }

    private void setLocate(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        requireContext().getResources().
                updateConfiguration(configuration, requireContext().getResources().getDisplayMetrics());
        editor_language = sharedPreferences_language.edit();
        editor_language.putString("Current_Lang", language);
        editor_language.commit();
    }

    public void refreshFragment(){
        ProfileFragment profileFragment = new ProfileFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, profileFragment).commit();
    }

    public void loadLocale(String language){
        if (!language.equals("abcdef")){
            setLocate(language);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // Todo: Upload Image and insert information to Database
    private void uploadImage(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("user_Avatar", mUri);
                        reference.updateChildren(hashMap);

                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(getContext(), "Failed" + task.getException(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMG_REQUEST && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload into database", Toast.LENGTH_SHORT).show();
            }else {
                uploadImage();
            }
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}