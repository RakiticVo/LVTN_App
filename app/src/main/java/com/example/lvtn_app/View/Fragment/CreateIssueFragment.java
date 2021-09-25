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
    ImageButton ibtn_back_issue_create, calendar_start_date_issue_create;
    TextInputLayout start_date_create_text_input_layout, estimate_time_create_text_input_layout, description_issue_create_text_input_layout, issue_name_create_text_input_layout;
    Button btn_create_issue,btn_cancel_create_issue;
    ArrayList<IssueType> issueType_list;
    ArrayList<ProcessType> processType_list;
    ArrayList<Priority> priority_list;
    ArrayList<Member> member_list;
    IssueTypeAdapter issueTypeAdapter;
    ProcessTypeAdapter processTypeAdapter;
    PriorityAdapter priorityAdapter;
    AssigneeAdapter assigneeAdapter;

    DateFormat dateFormat = new DateFormat();
    Calendar myCalendar = Calendar.getInstance();

    SharedPreferences sharedPreferences;

    private String issue_type, process_type, priority, assignee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Khai báo
        View view = inflater.inflate(R.layout.fragment_create_issue, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ibtn_back_issue_create = view.findViewById(R.id.ibtn_back_issue_create);
        calendar_start_date_issue_create = view.findViewById(R.id.calendar_start_date_issue_create);

        issue_name_create_text_input_layout = view.findViewById(R.id.issue_name_create_text_input_layout);
        description_issue_create_text_input_layout = view.findViewById(R.id.description_issue_create_text_input_layout);
        start_date_create_text_input_layout = view.findViewById(R.id.start_date_create_text_input_layout);
        estimate_time_create_text_input_layout = view.findViewById(R.id.estimate_time_create_text_input_layout);

        btn_create_issue = view.findViewById(R.id.btn_create_issue);
        btn_cancel_create_issue = view.findViewById(R.id.btn_cancel_create_issue);

        spinner_issue_type_create = view.findViewById(R.id.spinner_issue_type_create);
        spinner_detail_process_create = view.findViewById(R.id.spinner_detail_process_create);
        spinner_detail_assignee_issue_create = view.findViewById(R.id.spinner_detail_assignee_issue_create);
        spinner_detail_priority_issue_create = view.findViewById(R.id.spinner_detail_priority_issue_create);

        //Set data
        start_date_create_text_input_layout.getEditText().setText(dateFormat.formatDate(Calendar.getInstance().getTime()));

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

        //Set up
        issueTypeAdapter = new IssueTypeAdapter(getContext(), issueType_list);
        spinner_issue_type_create.setAdapter(issueTypeAdapter);

        processTypeAdapter = new ProcessTypeAdapter(getContext(), processType_list);
        spinner_detail_process_create.setAdapter(processTypeAdapter);

        priorityAdapter = new PriorityAdapter(getContext(), priority_list);
        spinner_detail_priority_issue_create.setAdapter(priorityAdapter);

        assigneeAdapter = new AssigneeAdapter(getContext(), member_list);
        spinner_detail_assignee_issue_create.setAdapter(assigneeAdapter);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                start_date_create_text_input_layout.getEditText().setText(dateFormat.formatDate((myCalendar.getTime())));
                checkDate(start_date_create_text_input_layout.getEditText().getText().toString());
            }
        };

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("User", Context.MODE_PRIVATE);

        //Bắt sự kiện
        ibtn_back_issue_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_cancel_create_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_create_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo: Check permission

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                String username = sharedPreferences.getString("username_txt", "User Name");
                                String issuename = issue_name_create_text_input_layout.getEditText().getText().toString();
                                String decription = description_issue_create_text_input_layout.getEditText().getText().toString();
                                String startdate = start_date_create_text_input_layout.getEditText().getText().toString();
                                String estimatetime = estimate_time_create_text_input_layout.getEditText().getText().toString();

//                                Toast.makeText(getContext(), "" + username + "\n"
//                                        + issuename + "\n"
//                                        + process_type + "\n"
//                                        + decription + "\n"
//                                        + issue_type + "\n"
//                                        + startdate + "\n"
//                                        + priority + "\n"
//                                        + assignee + "\n"
//                                        + estimatetime + "\n", Toast.LENGTH_SHORT).show();

                                DashBoardFragment.getInstance().createIssue(issuename, process_type, decription, issue_type, startdate, priority, assignee, estimatetime, username);
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
        });
        issue_name_create_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0){
                    issue_name_create_text_input_layout.setError("Please Enter Issue name!!!");
                    issue_name_create_text_input_layout.setErrorEnabled(true);
                }else issue_name_create_text_input_layout.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0){
                    issue_name_create_text_input_layout.setError("Please Enter Issue name!!!");
                    issue_name_create_text_input_layout.setErrorEnabled(true);
                }else issue_name_create_text_input_layout.setError("");
            }
        });

        calendar_start_date_issue_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        estimate_time_create_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String last = estimate_time_create_text_input_layout.getEditText().getText().toString();
                last = last.substring(last.length() - 1);
                if (s.length() >= 0 && s.length() < 2 || (!last.equals("d") && !last.equals("m") && !last.equals("w"))) {
                    estimate_time_create_text_input_layout.setError("Ex: 1d, 2w, 3m");
                    estimate_time_create_text_input_layout.setErrorEnabled(true);
                } else {
                    estimate_time_create_text_input_layout.setError("");
//                    estimate_time_create_text_input_layout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        start_date_create_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    // the user is done typing.
                    checkDate(start_date_create_text_input_layout.getEditText().getText().toString());
                }
            }
        });

        spinner_issue_type_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                issue_type = issueType_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_detail_process_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                process_type = processType_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_detail_priority_issue_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priority = priority_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_detail_assignee_issue_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                assignee = member_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    //Check date is right
    public void checkDate(String date){
        Date rightDate = dateFormat.checkFormatDate(date);
//        Toast.makeText(getContext(), "" + rightDate, Toast.LENGTH_SHORT).show();
        if (rightDate != null){
            start_date_create_text_input_layout.setError("");
            boolean isCheck = dateFormat.checkDate(rightDate);
            if (isCheck){
                start_date_create_text_input_layout.setError("");
            }else {
                start_date_create_text_input_layout.setError("Wrong start day!!!");
                start_date_create_text_input_layout.setErrorEnabled(true);
            }
        }else {
            start_date_create_text_input_layout.setError("Wrong format. Ex: dd/MM/yyy");
            start_date_create_text_input_layout.setErrorEnabled(true);
        }
    }
}