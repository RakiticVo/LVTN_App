package com.example.lvtn_app.View.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;

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
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreateNewPersonalTaskFragment extends DialogFragment {
    //Khai báo
    TextInputLayout create_task_name_text_input_layout, create_task_decription_text_input_layout, create_task_start_date_text_input_layout;
    ImageButton ibtn_back_create_task, ibtn_create_task_start_date;
    Button btn_create_new_personal_task, btn_cancel_create_new_personal_task;

    SharedPreferences sharedPreferences;

    DateFormat dateFormat = new DateFormat();
    Calendar myCalendar = Calendar.getInstance();

    //Personal task information
    String creator = "";
    String tasktname = "";
    String decription = "";
    String startdate = "";

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

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("User", Context.MODE_PRIVATE);
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
                    tasktname = create_task_name_text_input_layout.getEditText().getText().toString();
                    startdate = create_task_start_date_text_input_layout.getEditText().getText().toString();
                    creator = sharedPreferences.getString("userName_txt", "abc");

                    if (create_task_decription_text_input_layout.getEditText().getText().length() == 0){
                        decription = " ";
                    }else decription = create_task_decription_text_input_layout.getEditText().getText().toString();

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    Toast.makeText(getContext(), "" + tasktname + "\n"
                                            + decription + "\n"
                                            + startdate + "\n"
                                            + creator + "\n", Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(getContext(), "Create new project success", Toast.LENGTH_SHORT).show();
                                    // Todo: create Instance My Task to create task
                                    MyTasksFragment.instance.createTask(tasktname, decription, startdate, creator);
                                    dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    Toast.makeText(getContext(), "Create error!!!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you want to create task: " + tasktname + "?").setPositiveButton("Yes", dialogClickListener)
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