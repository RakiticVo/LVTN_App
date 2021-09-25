package com.example.lvtn_app.View.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Controller.Retrofit.ApiService;
import com.example.lvtn_app.Controller.Retrofit.ApiUtils;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Activity.LoginActivity;
import com.example.lvtn_app.View.Activity.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    Uri chosenImageUri;
    int user_id;

    Calendar myCalendar = Calendar.getInstance();
    DateFormat dateFormat = new DateFormat();

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;

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

        mSharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();

        //Set data
        user_id = mSharedPreferences.getInt("userId_txt", -1);
        if (user_id > 0){
//            Toast.makeText(getContext(), "" + user_id, Toast.LENGTH_SHORT).show();
            ApiService getUserInfo = ApiUtils.connectRetrofit();
            getUserInfo.getUserInformation().enqueue(new Callback<ArrayList<User>>() {
                @Override
                public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                    ArrayList<User> temp = response.body();
                    for (User user : temp){
                        if (user.getId_user() == user_id){
//                            Toast.makeText(getContext(), "" + user.getId_user() + "\n"
//                                    + user.getUserName() + "\n"
//                                    + user.getUserEmail() + "\n"
//                                    + user.getGender_PI() + "\n"
//                                    + user.getPhone_PI() + "\n"
//                                    + "status: Online" + "\n", Toast.LENGTH_SHORT).show();
                            editor.putString("gender_PI_txt", user.getGender_PI());
                            editor.putString("phone_PI_txt", user.getPhone_PI());
                            editor.putString("dob_PI_txt", user.getDob_PI());
                            editor.putString("address_PI_txt", user.getAddress_PI());
                            editor.putString("avatar_PI_txt", user.getAvatar_PI());
                            editor.commit();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                    Toast.makeText(getContext(), "" + call + "\n" + t, Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "onFailure: " + call + "\n" + t);
                }
            });
        }else {
//            Toast.makeText(getContext(), "" + user_id, Toast.LENGTH_SHORT).show();
        }


        tv_username_profile.setText(mSharedPreferences.getString("userName_txt", "UserName"));
        tv_email_profile.setText(mSharedPreferences.getString("userEmail_txt", ""));
        tv_gender_profile.setText(mSharedPreferences.getString("gender_PI_txt", ""));
        tv_phoneNumber_profile.setText(mSharedPreferences.getString("phone_PI_txt", ""));
        tv_DOB_profile.setText(mSharedPreferences.getString("dob_PI_txt", ""));
        tv_address_profile.setText(mSharedPreferences.getString("address_PI_txt", ""));

        if (!mSharedPreferences.getString("avatar_PI_txt", "").equals("")){
            Glide.with(getContext()).load(Uri.parse(mSharedPreferences.getString("avatar_PI_txt", ""))).centerCrop().into(avatar_profile);
        }else {
            avatar_profile.setImageResource(R.drawable.profile_1);
        }
//        if (!mSharedPreferences.getString("avatar_PI_txt", "").equals("")){
//            Toast.makeText(getContext(), "" + mSharedPreferences.getString("avatar_PI_txt", ""), Toast.LENGTH_SHORT).show();
//            Glide.with(getContext()).load(Uri.parse(mSharedPreferences.getString("avatar_PI_txt", ""))).centerCrop().into(avatar_profile);
//        }else {
//
//        }

        username_profile_text_input_layout.getEditText().setText(mSharedPreferences.getString("userName_txt", "UserName"));
        email_profile_text_input_layout.getEditText().setText(mSharedPreferences.getString("userEmail_txt", ""));
        if (mSharedPreferences.getString("gender_PI_txt", "").toLowerCase().equals("male")){
            rbtn_male.setChecked(true);
        }else{
            rbtn_female.setChecked(true);
        }
        phoneNumber_profile_text_input_layout.getEditText().setText(mSharedPreferences.getString("phone_PI_txt", ""));
        DOB_profile_text_input_layout.getEditText().setText(mSharedPreferences.getString("dob_PI_txt", ""));
        address_profile_text_input_layout.getEditText().setText(mSharedPreferences.getString("address_PI_txt", ""));

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
                ibtn_choose_avatar_profile.setVisibility(View.VISIBLE);

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
                        if (validatePhoneNumber(phoneNumber_profile_text_input_layout.getEditText().getText().toString())) {
                            if (phoneNumber_profile_text_input_layout.getEditText().getText().toString().subSequence(0,1).equals("0")){
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
                if (!isValidEmail(email_profile_text_input_layout.getEditText().getText())) {
                    email_profile_text_input_layout.setError("Wrong format!!! Please try again");
                    email_profile_text_input_layout.setErrorEnabled(true);
                }

                if (validatePhoneNumber(phoneNumber_profile_text_input_layout.getEditText().getText().toString())) {
                    if (!phoneNumber_profile_text_input_layout.getEditText().getText().toString().subSequence(0,1).equals("0")){
                        phoneNumber_profile_text_input_layout.setError("Wrong format!!! Please try again");
                        phoneNumber_profile_text_input_layout.setErrorEnabled(true);
                    }
                } else {
                    phoneNumber_profile_text_input_layout.setError("Wrong format!!! Please try again");
                    phoneNumber_profile_text_input_layout.setErrorEnabled(true);
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
                                    String username = username_profile_text_input_layout.getEditText().getText().toString();
                                    String email = email_profile_text_input_layout.getEditText().getText().toString();
                                    String gender = "Male";
                                    if (rg_gender.getCheckedRadioButtonId() == rbtn_female.getId()) {
                                        gender = rbtn_female.getText().toString();
                                    }
                                    if (rg_gender.getCheckedRadioButtonId() == rbtn_male.getId()) {
                                        gender = rbtn_male.getText().toString();
                                    }
                                    String phone = phoneNumber_profile_text_input_layout.getEditText().getText().toString();
                                    String dob = DOB_profile_text_input_layout.getEditText().getText().toString();
                                    String address = address_profile_text_input_layout.getEditText().getText().toString();

//                                    Toast.makeText(getContext(), "" + username + "\n"
//                                            + email + "\n"
//                                            + gender + "\n"
//                                            + phone + "\n"
//                                            + dob + "\n"
//                                            + address + "\n", Toast.LENGTH_SHORT).show();
                                    editor = mSharedPreferences.edit();
                                    ApiService updateUserName = ApiUtils.connectRetrofit();
                                    updateUserName.isUpdateUserSuccess(user_id,username, email).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            Toast.makeText(getContext(), "Successs: \n" + response.body(), Toast.LENGTH_SHORT).show();
                                            editor.putString("username_txt", username);
                                            editor.putString("userEmail_txt", email);
                                            editor.commit();
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {

                                        }
                                    });
                                    String avatar = "";
                                    if (chosenImageUri != null){
                                        avatar = chosenImageUri.toString();
                                    }
                                    ApiService updateUserInfo = ApiUtils.connectRetrofit();
                                    String finalGender = gender;
                                    String finalAvatar = avatar;
//                                    Toast.makeText(getContext(), "" + user_id, Toast.LENGTH_SHORT).show();
                                    updateUserInfo.isUpdateUserInformationSuccess(user_id, gender, phone, dob, address, avatar).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            Toast.makeText(getContext(), "Successs: \n" + response.body(), Toast.LENGTH_SHORT).show();
                                            editor.putString("gender_PI_txt", finalGender);
                                            editor.putString("phone_PI_txt", phone);
                                            editor.putString("dob_PI_txt", dob);
                                            editor.putString("address_PI_txt", address);
                                            editor.putString("avatar_PI_txt", finalAvatar);
                                            editor.commit();
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {

                                        }
                                    });

                                    if (!mSharedPreferences.getString("avatar_PI_txt", "").equals("")){
                                       Glide.with(getContext()).load(Uri.parse(finalAvatar)).centerCrop().into(avatar_profile);
                                    }else {
                                        avatar_profile.setImageResource(R.drawable.profile_1);
                                    }
                                    tv_username_profile.setText(username);
                                    tv_email_profile.setText(email);
                                    tv_gender_profile.setText(gender);
                                    tv_phoneNumber_profile.setText(phone);
                                    tv_DOB_profile.setText(dob);
                                    tv_address_profile.setText(address);

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

                                    Toast.makeText(getContext(), "Update success", Toast.LENGTH_SHORT).show();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    Toast.makeText(getContext(), "Cancel update", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you want to create this issue?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            }
        });

        ibtn_choose_avatar_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
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
                int id_user = mSharedPreferences.getInt("userId_txt", -1);
                if (id_user > 0){
                    ApiService service = ApiUtils.connectRetrofit();
                    service.isUpdateUserInformationSuccess(id_user, false).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(getContext(), "" + response.body(), Toast.LENGTH_SHORT).show();
                            editor = mSharedPreferences.edit();
                            editor.putBoolean("userChecked", false);
                            editor.commit();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
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
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            chosenImageUri = data.getData();

            Bitmap mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), chosenImageUri);
                Toast.makeText(getContext(), "Update Avatar Success", Toast.LENGTH_SHORT).show();
                avatar_profile.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private static boolean validatePhoneNumber(String phoneNumber) {
        // validate phone numbers of format "1234567890"
        if (phoneNumber.matches("\\d{10}"))
            return true;
            // validating phone number with -, . or spaces
        else if (phoneNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
            return true;
            // validating phone number with extension length from 3 to 5
        else if (phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
            return true;
            // validating phone number where area code is in braces ()
        else if (phoneNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
            return true;
            // Validation for India numbers
        else if (phoneNumber.matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}"))
            return true;
        else if (phoneNumber.matches("\\(\\d{5}\\)-\\d{3}-\\d{3}"))
            return true;

        else if (phoneNumber.matches("\\(\\d{4}\\)-\\d{3}-\\d{3}"))
            return true;
            // return false if nothing matches the input
        else
            return false;
    }
}