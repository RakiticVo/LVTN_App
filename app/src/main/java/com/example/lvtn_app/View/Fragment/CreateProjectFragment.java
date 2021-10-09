package com.example.lvtn_app.View.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.IssueTypeAdapter;
import com.example.lvtn_app.Adapter.ProjectTypeAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.IssueType;
import com.example.lvtn_app.Model.ProjectType;
import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProjectFragment extends DialogFragment {
    //Khai báo
    Button btn_create_project, btn_cancel_create_project;
    TextInputLayout create_project_name_text_input_layout, create_project_estimate_finish_date_text_input_layout, create_project_detail_text_input_layout;
    ImageButton ibtn_calendar_create_project;
    Spinner spinner_create_project_type;
    ArrayList<ProjectType> projectType_list;
    ProjectTypeAdapter projectTypeAdapter;

    SharedPreferences sharedPreferences;

    DateFormat dateFormat = new DateFormat();
    Calendar myCalendar = Calendar.getInstance();

    //Project Information
    String username = "";
    String projectname = "";
    String estimedate = "";
    String detail = "";
    String datecreate = "";
    String projecttype = "";

    private static final String TAG = "CreateProjectFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set up
        View view = inflater.inflate(R.layout.fragment_create_project, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btn_create_project = view.findViewById(R.id.btn_create_project);
        btn_cancel_create_project = view.findViewById(R.id.btn_cancel_create_project);
        ibtn_calendar_create_project = view.findViewById(R.id.ibtn_calendar_create_project);
        spinner_create_project_type = view.findViewById(R.id.spinner_create_project_type);

        create_project_name_text_input_layout = view.findViewById(R.id.create_project_name_text_input_layout);
        create_project_estimate_finish_date_text_input_layout = view.findViewById(R.id.create_project_estimate_finish_date_text_input_layout);
        create_project_detail_text_input_layout = view.findViewById(R.id.create_project_detail_text_input_layout);

        create_project_estimate_finish_date_text_input_layout.getEditText().setText(dateFormat.formatDate(Calendar.getInstance().getTime()));

        projectType_list = new ArrayList<>();
        projectType_list.add(new ProjectType(R.drawable.project_1, "Normal"));
        projectType_list.add(new ProjectType(R.drawable.project_kanban, "Kanban"));
        projectType_list.add(new ProjectType(R.drawable.project_scrum, "Scrum"));
        projectType_list.add(new ProjectType(R.drawable.project_personal, "Personal"));
        projectType_list.add(new ProjectType(R.drawable.project_bussiness, "Bussiness"));

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                create_project_estimate_finish_date_text_input_layout.getEditText().setText(dateFormat.formatDate((myCalendar.getTime())));
                checkDate(create_project_estimate_finish_date_text_input_layout.getEditText().getText().toString());
            }
        };

        projectTypeAdapter = new ProjectTypeAdapter(getContext(), projectType_list);
        spinner_create_project_type.setAdapter(projectTypeAdapter);

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("User", Context.MODE_PRIVATE);

        //Bắt sự kiện
        //Todo: Xử lý sự kiện rời khỏi fragment
        btn_cancel_create_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Todo: Xử lý sự kiện chọn ngày trong Calendar View
        ibtn_calendar_create_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Todo: Xử lý sự kiện lấy Project Type
        spinner_create_project_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                projecttype = projectType_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng Project name
        create_project_name_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (create_project_name_text_input_layout.getEditText().getText().length() == 0){
                        create_project_name_text_input_layout.setError("Please enter Project's name!!!");
                        create_project_name_text_input_layout.setErrorEnabled(true);
                    }else create_project_name_text_input_layout.setErrorEnabled(false);
                }else{
                    create_project_name_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                create_project_name_text_input_layout.setError("Please enter project's name!!!");
                                create_project_name_text_input_layout.setErrorEnabled(true);
                            }else create_project_name_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng Project Estimate Finish Date
        create_project_estimate_finish_date_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (create_project_estimate_finish_date_text_input_layout.getEditText().getText().length() == 0){
                        create_project_estimate_finish_date_text_input_layout.setError("Please enter finish date!!!");
                        create_project_estimate_finish_date_text_input_layout.setErrorEnabled(true);
                    }else checkDate(create_project_estimate_finish_date_text_input_layout.getEditText().getText().toString());
                }else{
                    create_project_estimate_finish_date_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (create_project_name_text_input_layout.getEditText().getText().length() == 0){
                                create_project_name_text_input_layout.setError("Please enter finish date!!!");
                                create_project_name_text_input_layout.setErrorEnabled(true);
                            }else create_project_name_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện tạo ra một Project
        // - Kiểm tra rỗng và sự chính xác của các Text ----- (Done)
        // - Lấy ra text và cần sử dụng ----- (Done)
        // - Gọi Instance của MyTasksFragment để tạo ra một Project----- (Done)
        // - Gọi Api Service để thêm Project trên database ----- (Incomplete)
        btn_create_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (create_project_name_text_input_layout.getEditText().getText().length()==0){
                    create_project_name_text_input_layout.setError("Please enter project's name!!!");
                    create_project_name_text_input_layout.setErrorEnabled(true);
                }else {
                    create_project_name_text_input_layout.setErrorEnabled(false);
                }
                if (create_project_estimate_finish_date_text_input_layout.getEditText().getText().length()==0){
                    create_project_estimate_finish_date_text_input_layout.setError("Please enter finish date!!!");
                    create_project_estimate_finish_date_text_input_layout.setErrorEnabled(true);
                }else checkDate(create_project_estimate_finish_date_text_input_layout.getEditText().getText().toString());

                if (create_project_name_text_input_layout.isErrorEnabled() || create_project_estimate_finish_date_text_input_layout.isErrorEnabled()){
                    Toast.makeText(getContext(), "Please check error", Toast.LENGTH_SHORT).show();
                }else{
                    create_project_estimate_finish_date_text_input_layout.setErrorEnabled(false);
                    username = sharedPreferences.getString("userName_txt", "User Name");
                    projectname = create_project_name_text_input_layout.getEditText().getText().toString();
                    estimedate = create_project_estimate_finish_date_text_input_layout.getEditText().getText().toString();
                    datecreate = dateFormat.formatDate(Calendar.getInstance().getTime());
                    detail = create_project_detail_text_input_layout.getEditText().getText().toString();
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
//                                        Toast.makeText(getContext(), "" + projectname + "\n"
//                                                + detail + "\n"
//                                                + estimedate + "\n"
//                                                + username + "\n"
//                                                + datecreate, Toast.LENGTH_SHORT).show();
                                    ProjectsFragment.getInstance().createProject(projectname, detail, estimedate, projecttype, datecreate, username, getrandomColor());
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
                    builder.setMessage("Do you want to create project: " + projectname + "?").setPositiveButton("Yes", dialogClickListener)
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
                    create_project_estimate_finish_date_text_input_layout.setErrorEnabled(false);
                    boolean isCheck = dateFormat.checkDate(rightDate);
                    if (isCheck){
                        create_project_estimate_finish_date_text_input_layout.setErrorEnabled(false);
                    }else {
                        create_project_estimate_finish_date_text_input_layout.setError("Wrong start day!!!");
                        create_project_estimate_finish_date_text_input_layout.setErrorEnabled(true);
                    }
                }else {
                    create_project_estimate_finish_date_text_input_layout.setError("Wrong format. Ex: dd/MM/yyy");
                    create_project_estimate_finish_date_text_input_layout.setErrorEnabled(true);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    //Hàm lấy ngẩu nhiên màu sắc
    public int getrandomColor(){
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return color;
    }
}