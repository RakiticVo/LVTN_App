package com.example.lvtn_app.View.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.AssigneeAdapter;
import com.example.lvtn_app.Adapter.IssueTypeAdapter;
import com.example.lvtn_app.Adapter.PriorityAdapter;
import com.example.lvtn_app.Adapter.ProcessTypeAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.IssueType;
import com.example.lvtn_app.Model.Member;
import com.example.lvtn_app.Model.ProcessType;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.example.lvtn_app.Model.Priority;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CreateIssueFragment extends DialogFragment {
    //Khai báo
    Spinner spinner_issue_type_create, spinner_detail_process_create, spinner_detail_priority_issue_create, spinner_detail_assignee_issue_create;
    ImageButton ibtn_back_issue_create, calendar_start_date_issue_create, calendar_finish_date_issue_create;
    TextInputLayout start_date_create_text_input_layout, estimate_time_create_text_input_layout, description_issue_create_text_input_layout, issue_name_create_text_input_layout, finish_date_issue_create_text_input_layout;
    Button btn_create_issue,btn_cancel_create_issue;

    //Array list for Spinner
    ArrayList<IssueType> issueType_list;
    ArrayList<ProcessType> processType_list;
    ArrayList<Priority> priority_list;
    ArrayList<Member> member_list;

    //Adapter for Spinner
    IssueTypeAdapter issueTypeAdapter;
    ProcessTypeAdapter processTypeAdapter;
    PriorityAdapter priorityAdapter;
    AssigneeAdapter assigneeAdapter;

    //Check date
    DateFormat dateFormat = new DateFormat();
    Calendar myCalendar = Calendar.getInstance();

    SharedPreferences sharedPreferences_user, sharedPreferences_project;

    //New issue information
    String issuename = "";
    String process_type = "";
    String decription = "";
    String issue_type = "";
    String startdate = "";
    String priority = "";
    String assignee = "";
    String estimatetime = "";
    String creator = "";
    int project_id = -1;
    String finishdate = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set up
        View view = inflater.inflate(R.layout.fragment_create_issue, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ibtn_back_issue_create = view.findViewById(R.id.ibtn_back_issue_create);
        calendar_start_date_issue_create = view.findViewById(R.id.calendar_start_date_issue_create);
        calendar_finish_date_issue_create = view.findViewById(R.id.calendar_finish_date_issue_create);

        issue_name_create_text_input_layout = view.findViewById(R.id.issue_name_create_text_input_layout);
        description_issue_create_text_input_layout = view.findViewById(R.id.description_issue_create_text_input_layout);
        start_date_create_text_input_layout = view.findViewById(R.id.start_date_create_text_input_layout);
        estimate_time_create_text_input_layout = view.findViewById(R.id.estimate_time_create_text_input_layout);
        finish_date_issue_create_text_input_layout = view.findViewById(R.id.finish_date_issue_create_text_input_layout);

        btn_create_issue = view.findViewById(R.id.btn_create_issue);
        btn_cancel_create_issue = view.findViewById(R.id.btn_cancel_create_issue);

        spinner_issue_type_create = view.findViewById(R.id.spinner_issue_type_create);
        spinner_detail_process_create = view.findViewById(R.id.spinner_detail_process_create);
        spinner_detail_assignee_issue_create = view.findViewById(R.id.spinner_detail_assignee_issue_create);
        spinner_detail_priority_issue_create = view.findViewById(R.id.spinner_detail_priority_issue_create);

        issueType_list = new ArrayList<>();
        issueType_list.add(new IssueType(R.drawable.task, "Task"));
        issueType_list.add(new IssueType(R.drawable.bug, "Bug"));
        issueType_list.add(new IssueType(R.drawable.user_story, "Story"));

        processType_list = new ArrayList<>();
        processType_list.add(new ProcessType(R.drawable.todo, "ToDo"));
        processType_list.add(new ProcessType(R.drawable.inprogress, "InProgress"));
        processType_list.add(new ProcessType(R.drawable.done, "Done"));

        priority_list = new ArrayList<>();
        priority_list.add(new Priority(R.drawable.high, "High"));
        priority_list.add(new Priority(R.drawable.medium, "Medium"));
        priority_list.add(new Priority(R.drawable.low, "Low"));

        member_list = new ArrayList<>();
        member_list.add(new Member(1, "Chí Thiện", ""));
        member_list.add(new Member(1, "Thiện Võ", "https://progameguides.com/wp-content/uploads/2021/07/Genshin-Impact-Character-Raiden-Shogun-1.jpg"));
        member_list.add(new Member(1, "Rakitic Võ", "" ));

        issueTypeAdapter = new IssueTypeAdapter(getContext(), issueType_list);
        spinner_issue_type_create.setAdapter(issueTypeAdapter);

        processTypeAdapter = new ProcessTypeAdapter(getContext(), processType_list);
        spinner_detail_process_create.setAdapter(processTypeAdapter);

        priorityAdapter = new PriorityAdapter(getContext(), priority_list);
        spinner_detail_priority_issue_create.setAdapter(priorityAdapter);

        assigneeAdapter = new AssigneeAdapter(getContext(), member_list);
        spinner_detail_assignee_issue_create.setAdapter(assigneeAdapter);

        start_date_create_text_input_layout.getEditText().setText(dateFormat.formatDate((Calendar.getInstance().getTime())));

        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                start_date_create_text_input_layout.getEditText().setText(dateFormat.formatDate((myCalendar.getTime())));
                checkDate(start_date_create_text_input_layout.getEditText().getText().toString());
            }
        };

        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                finish_date_issue_create_text_input_layout.getEditText().setText(dateFormat.formatDate((myCalendar.getTime())));
                checkDate(finish_date_issue_create_text_input_layout.getEditText().getText().toString());
            }
        };

        sharedPreferences_user = Objects.requireNonNull(getActivity()).getSharedPreferences("User", Context.MODE_PRIVATE);
        sharedPreferences_project = Objects.requireNonNull(getActivity()).getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);

        //Bắt sự kiện
        //Todo: Xử lý sự kiện rời khỏi fragment
        ibtn_back_issue_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Todo: Xử lý sự kiện rời khỏi fragment
        btn_cancel_create_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Todo: Xử lý sự kiện chọn ngày từ Calendar View
        calendar_start_date_issue_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Todo: Xử lý sự kiện chọn ngày từ Calendar View
        calendar_finish_date_issue_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng cho Issue name
        issue_name_create_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (issue_name_create_text_input_layout.getEditText().getText().length() == 0){
                        issue_name_create_text_input_layout.setError("Please enter issue name!!!");
                        issue_name_create_text_input_layout.setErrorEnabled(true);
                    }else issue_name_create_text_input_layout.setErrorEnabled(false);
                }else {
                    issue_name_create_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length()==0){
                                issue_name_create_text_input_layout.setError("Please Enter Issue name!!!");
                                issue_name_create_text_input_layout.setErrorEnabled(true);
                            }else issue_name_create_text_input_layout.setErrorEnabled(false);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng cho Issue Estimate time
        estimate_time_create_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (estimate_time_create_text_input_layout.getEditText().getText().length() == 0){
                        estimate_time_create_text_input_layout.setError("Please enter estimate time!!!");
                        estimate_time_create_text_input_layout.setErrorEnabled(true);
                    }else {
                        estimate_time_create_text_input_layout.setErrorEnabled(false);
                        checkRightEstimateTime(estimate_time_create_text_input_layout.getEditText().getText().toString());
                    }
                }else{
                    estimate_time_create_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (estimate_time_create_text_input_layout.getEditText().getText().length() == 0){
                                estimate_time_create_text_input_layout.setError("Please enter estimate time!!!");
                                estimate_time_create_text_input_layout.setErrorEnabled(true);
                            }else {
                                estimate_time_create_text_input_layout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng cho Issue Start Date
        start_date_create_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (start_date_create_text_input_layout.getEditText().getText().length() == 0){
                        start_date_create_text_input_layout.setError("Please enter start date!!!");
                        start_date_create_text_input_layout.setErrorEnabled(true);
                    }else {
                        start_date_create_text_input_layout.setErrorEnabled(false);
                        checkDate(start_date_create_text_input_layout.getEditText().getText().toString());
                    }
                }else {
                    start_date_create_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (start_date_create_text_input_layout.getEditText().getText().length() == 0){
                                start_date_create_text_input_layout.setError("Please enter start date!!!");
                                start_date_create_text_input_layout.setErrorEnabled(true);
                            }else {
                                start_date_create_text_input_layout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng cho Issue Finish Date
        finish_date_issue_create_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkDate(finish_date_issue_create_text_input_layout.getEditText().getText().toString());
                    try {
                        Date date1 = dateFormat.sdf.parse(start_date_create_text_input_layout.getEditText().getText().toString());
                        Date date2 = dateFormat.sdf.parse(finish_date_issue_create_text_input_layout.getEditText().getText().toString());
                        if (date2.getTime() < date1.getTime()) {
                            finish_date_issue_create_text_input_layout.setError("Wrong finish day!!!");
                            finish_date_issue_create_text_input_layout.setErrorEnabled(true);
                        } else finish_date_issue_create_text_input_layout.setErrorEnabled(false);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //Todo: Xử lý sự kiện lấy Issue Type
        spinner_issue_type_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                issue_type = issueType_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện lấy Issue Process Type
        spinner_detail_process_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                process_type = processType_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện lấy Issue Priority
        spinner_detail_priority_issue_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priority = priority_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện lấy Issue Assignee
        spinner_detail_assignee_issue_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                assignee = member_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện xác nhận tạo một Issue
        // - Kiểm tra rỗng cho các Issue Name, Start Date và Estimate Time ----- (Done)
        // - Lấy các text cần dùng ----- (Done)
        // - Gọi một Instance để tạo ra một Issue ----- (Done)
        btn_create_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo: Check permission
                if (issue_name_create_text_input_layout.getEditText().getText().length() == 0){
                    issue_name_create_text_input_layout.setError("Please enter issue name!!!");
                    issue_name_create_text_input_layout.setErrorEnabled(true);
                }else issue_name_create_text_input_layout.setErrorEnabled(false);
                checkDate(start_date_create_text_input_layout.getEditText().getText().toString());
                checkRightEstimateTime(estimate_time_create_text_input_layout.getEditText().getText().toString());
                if (finish_date_issue_create_text_input_layout.getEditText().getText().length() > 0){
                    try {
                        Date date1 = dateFormat.sdf.parse(start_date_create_text_input_layout.getEditText().getText().toString());
                        Date date2 = dateFormat.sdf.parse(finish_date_issue_create_text_input_layout.getEditText().getText().toString());
                        if (date2.getTime() < date1.getTime()){
                            finish_date_issue_create_text_input_layout.setError("Wrong finish day!!!");
                            finish_date_issue_create_text_input_layout.setErrorEnabled(true);
                        }else finish_date_issue_create_text_input_layout.setErrorEnabled(false);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (issue_name_create_text_input_layout.isErrorEnabled() || estimate_time_create_text_input_layout.isErrorEnabled()
                        || start_date_create_text_input_layout.isErrorEnabled() || finish_date_issue_create_text_input_layout.isErrorEnabled()){
                    Toast.makeText(getContext(), "Please check error!!!", Toast.LENGTH_SHORT).show();
                }else{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    creator = sharedPreferences_user.getString("userName_txt", "User Name");
                                    project_id = sharedPreferences_project.getInt("id_project", -1);
                                    issuename = issue_name_create_text_input_layout.getEditText().getText().toString();
                                    startdate = start_date_create_text_input_layout.getEditText().getText().toString();
                                    estimatetime = estimate_time_create_text_input_layout.getEditText().getText().toString();

                                    if (description_issue_create_text_input_layout.getEditText().getText().length() == 0){
                                        decription = "";
                                    }else decription = description_issue_create_text_input_layout.getEditText().getText().toString();

                                    if (finish_date_issue_create_text_input_layout.getEditText().getText().length() == 0){
                                        finishdate = "";
                                    }else  finishdate = finish_date_issue_create_text_input_layout.getEditText().getText().toString();
//                                    Toast.makeText(getContext(), "" + creator + "\n"
//                                            + issuename + "\n"
//                                            + process_type + "\n"
//                                            + decription + "\n"
//                                            + issue_type + "\n"
//                                            + startdate + "\n"
//                                            + priority + "\n"
//                                            + assignee + "\n"
//                                            + estimatetime + "\n"
//                                            + project_id + "\n"
//                                            + finishdate + "\n", Toast.LENGTH_LONG).show();
                                    Log.e("TAG1", "onClick: " + creator + "\n"
                                            + issuename + "\n"
                                            + process_type + "\n"
                                            + decription + "\n"
                                            + issue_type + "\n"
                                            + startdate + "\n"
                                            + priority + "\n"
                                            + assignee + "\n"
                                            + estimatetime + "\n"
                                            + project_id + "\n"
                                            + finishdate + "\n");

                                    DashBoardFragment.getInstance().createIssue(issuename, process_type, decription, issue_type, startdate, priority, assignee, estimatetime, creator, project_id, finishdate);
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
                    builder.setMessage("Do you want to create this issue?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            }
        });

        return view;
    }

    //Check date is right
    public void checkDate(String date){
        if (dateFormat.isValidDate(date)){
            try {
                Date rightDate = dateFormat.sdf.parse(date);
                //        Toast.makeText(getContext(), "" + rightDate, Toast.LENGTH_SHORT).show();
                if (rightDate != null){
                    start_date_create_text_input_layout.setErrorEnabled(false);
//                    Toast.makeText(getContext(), "" + process_type, Toast.LENGTH_SHORT).show();
                    if (process_type.equals("ToDo")){
                        boolean isCheck = dateFormat.checkDate(rightDate);
                        if (isCheck){
                            start_date_create_text_input_layout.setErrorEnabled(false);
                        }else {
                            start_date_create_text_input_layout.setError("Wrong start day!!!");
                            start_date_create_text_input_layout.setErrorEnabled(true);
                        }
                    }
                }else {
                    start_date_create_text_input_layout.setError("Wrong format!!! Ex: dd/MM/yyyy");
                    start_date_create_text_input_layout.setErrorEnabled(true);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkRightEstimateTime(String date){
        String last = date.substring(date.length() - 1);
        if (date.length() >= 0 && date.length() < 2 || (!last.equals("d") && !last.equals("m") && !last.equals("w") && !last.equals("y"))) {
            estimate_time_create_text_input_layout.setError("Wrong format!!! Ex: 1d, 2w, 3m");
            estimate_time_create_text_input_layout.setErrorEnabled(true);
        } else {
            estimate_time_create_text_input_layout.setErrorEnabled(false);
        }
    }
}