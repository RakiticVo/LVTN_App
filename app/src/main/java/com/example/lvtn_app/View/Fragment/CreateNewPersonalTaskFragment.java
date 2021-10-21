package com.example.lvtn_app.View.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class CreateNewPersonalTaskFragment extends DialogFragment {
    //Khai báo
    TextInputLayout create_task_name_text_input_layout, create_task_decription_text_input_layout, create_task_start_date_text_input_layout;
    ImageButton ibtn_back_create_task, ibtn_create_task_start_date;
    Button btn_create_new_personal_task, btn_cancel_create_new_personal_task;

    SharedPreferences sharedPreferences;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    //Personal task information
    String task_ID;
    String task_Name;
    String task_Description;
    String task_StartDate;
    String task_Creator;

    DateFormat dateFormat = new DateFormat();
    Calendar myCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set up
        View view = inflater.inflate(R.layout.fragment_create_new_personal_task, container, false);

        create_task_name_text_input_layout = view.findViewById(R.id.create_task_name_text_input_layout);
        create_task_decription_text_input_layout = view.findViewById(R.id.create_task_decription_text_input_layout);
        create_task_start_date_text_input_layout = view.findViewById(R.id.create_task_start_date_text_input_layout);
        ibtn_back_create_task = view.findViewById(R.id.ibtn_back_create_task);
        ibtn_create_task_start_date = view.findViewById(R.id.ibtn_create_task_start_date);
        btn_create_new_personal_task = view.findViewById(R.id.btn_create_new_personal_task);
        btn_cancel_create_new_personal_task = view.findViewById(R.id.btn_cancel_create_new_personal_task);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        sharedPreferences = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        Bundle bundle = getArguments();
        String selectedDate = bundle.getString("selectedDate", "");

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                create_task_start_date_text_input_layout.getEditText().setText(dateFormat.formatDate((myCalendar.getTime())));
                checkDate(create_task_start_date_text_input_layout.getEditText().getText().toString());
            }
        };

        if (selectedDate.length() > 0){
            create_task_start_date_text_input_layout.getEditText().setText(selectedDate);
        }else create_task_start_date_text_input_layout.getEditText().setText(dateFormat.formatDate(Calendar.getInstance().getTime()));

        //Bắt sự kiện
        //Todo: Xử lý sự kiện rời khỏi fragment
        btn_cancel_create_new_personal_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        //Todo: Xử lý sự kiện rời khỏi fragment
        ibtn_back_create_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng Create Task(Issue) Start Date
        create_task_start_date_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    checkDate(create_task_start_date_text_input_layout.getEditText().getText().toString());
                }
            }
        });

        //Todo: Xử lý sự kiện chọn ngày trong Calendar View
        ibtn_create_task_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng Task Name
        create_task_name_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if (create_task_name_text_input_layout.getEditText().getText().length() == 0){
                        create_task_name_text_input_layout.setError("Please enter task name!!!");
                        create_task_name_text_input_layout.setErrorEnabled(true);
                    }else create_task_name_text_input_layout.setErrorEnabled(false);
                }else {
                    create_task_name_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                create_task_name_text_input_layout.setError("Please enter task name!!!");
                                create_task_name_text_input_layout.setErrorEnabled(true);
                            }else  create_task_name_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng Task Start Date
        create_task_start_date_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if (create_task_start_date_text_input_layout.getEditText().getText().length() == 0){
                        create_task_start_date_text_input_layout.setError("Please enter start date!!!");
                        create_task_start_date_text_input_layout.setErrorEnabled(true);
                    }else {
                        checkDate(create_task_start_date_text_input_layout.getEditText().getText().toString());
                    }
                }else {
                    create_task_start_date_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                create_task_start_date_text_input_layout.setError("Please enter start date!!!");
                                create_task_start_date_text_input_layout.setErrorEnabled(true);
                            }else  create_task_start_date_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện tạo ra một Task(Issue) cá nhân
        // - Kiểm tra rỗng và sự chính xác của các Text ----- (Done)
        // - Lấy ra text và cần sử dụng ----- (Done)
        // - Gọi Instance của MyTasksFragment để tạo ra một Personal Task ----- (Done)
        // - Gọi Api Service để thêm một Task trên database ----- (Incomplete)
        btn_create_new_personal_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (create_task_name_text_input_layout.getEditText().getText().length() == 0){
                    create_task_name_text_input_layout.setError("Please enter task name!!!");
                    create_task_name_text_input_layout.setErrorEnabled(true);
                }else create_task_name_text_input_layout.setErrorEnabled(false);

                if (create_task_start_date_text_input_layout.getEditText().getText().length() == 0){
                    create_task_start_date_text_input_layout.setError("Please enter start date!!!");
                    create_task_start_date_text_input_layout.setErrorEnabled(true);
                }else checkDate(create_task_start_date_text_input_layout.getEditText().getText().toString());

                if (create_task_name_text_input_layout.isErrorEnabled() || create_task_start_date_text_input_layout.isErrorEnabled()){
                    Toast.makeText(getContext(), "Please check error!!!", Toast.LENGTH_SHORT).show();
                }else{
                    auth = FirebaseAuth.getInstance();
                    firebaseUser = auth.getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Tasks");
                    task_ID = reference.push().getKey().toString();
                    task_Name = create_task_name_text_input_layout.getEditText().getText().toString();
                    task_StartDate = create_task_start_date_text_input_layout.getEditText().getText().toString();
                    task_Creator = firebaseUser.getUid();

                    if (create_task_decription_text_input_layout.getEditText().getText().length() == 0){
                        task_Description = " ";
                    }else task_Description = create_task_decription_text_input_layout.getEditText().getText().toString();

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("task_ID", task_ID);
                                    hashMap.put("task_Name", task_Name);
                                    hashMap.put("task_Description", task_Description);
                                    hashMap.put("task_StartDate", task_StartDate);
                                    hashMap.put("task_Creator", task_Creator);
                                    AppCompatActivity activity = (AppCompatActivity) getContext();
                                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                    progressDialog.setMessage("Waiting for login");
                                    progressDialog.show();
                                    reference.child(task_Creator).child(task_ID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                try {
                                                    Date start_date = dateFormat.sdf.parse(task_StartDate);
                                                    MyTasksFragment.getInstance().calendarView.setCurrentDate(start_date);
                                                    MyTasksFragment.getInstance().setDay(start_date);
                                                    String today = new SimpleDateFormat("EE, dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
                                                    MyTasksFragment.getInstance().tv_today.setText(today);
                                                    MyTasksFragment.getInstance().getTaskByDate(task_StartDate);
                                                    progressDialog.dismiss();
                                                    Toast.makeText(activity, "Create success", Toast.LENGTH_SHORT).show();
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }else {
                                                Toast.makeText(activity, "Create failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    Toast.makeText(getContext(), "Cancel create", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you want to create task: " + task_Name + "?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            }
        });

        return view;
    }

    //Hàm kiểm tra ngày có đúng định dạng không
    public void checkDate(String date){
        if (dateFormat.isValidDate(date)){
            try {
                Date rightDate = dateFormat.sdf.parse(date);
                //        Toast.makeText(getContext(), "" + rightDate, Toast.LENGTH_SHORT).show();
                if (rightDate != null){
                    create_task_start_date_text_input_layout.setError("");
                    boolean isCheck = dateFormat.checkDate(rightDate);
                    if (isCheck){
                        create_task_start_date_text_input_layout.setErrorEnabled(false);
                    }else {
                        create_task_start_date_text_input_layout.setError("Wrong start day!!!");
                        create_task_start_date_text_input_layout.setErrorEnabled(true);
                    }
                }else {
                    create_task_start_date_text_input_layout.setError("Wrong format. Ex: dd/MM/yyy");
                    create_task_start_date_text_input_layout.setErrorEnabled(true);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}