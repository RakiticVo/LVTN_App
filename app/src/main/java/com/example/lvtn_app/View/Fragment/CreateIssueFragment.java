package com.example.lvtn_app.View.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.AssigneeAdapter;
import com.example.lvtn_app.Adapter.IssueTypeAdapter;
import com.example.lvtn_app.Adapter.PriorityAdapter;
import com.example.lvtn_app.Adapter.ProcessTypeAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.IssueType;
import com.example.lvtn_app.Model.ProcessType;
import com.example.lvtn_app.Model.Project_Users;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.example.lvtn_app.Model.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CreateIssueFragment extends DialogFragment {
    //Khai báo
    Spinner spinner_issue_type_create, spinner_detail_process_create, spinner_detail_priority_issue_create, spinner_detail_assignee_issue_create;
    ImageButton ibtn_back_issue_create, calendar_start_date_issue_create, calendar_estimate_date_finish_issue_create, calendar_finish_date_issue_create;
    TextInputLayout start_date_create_text_input_layout, estimate_date_finish_create_text_input_layout,
            description_issue_create_text_input_layout, issue_name_create_text_input_layout,
            finish_date_issue_create_text_input_layout;
    Button btn_create_issue,btn_cancel_create_issue;
    LinearLayout linearLayout_finish_date;

    //Array list for Spinner
    ArrayList<IssueType> issueType_list;
    ArrayList<ProcessType> processType_list;
    ArrayList<Priority> priority_list;
    ArrayList<User> member_list;

    //Adapter for Spinner
    IssueTypeAdapter issueTypeAdapter;
    ProcessTypeAdapter processTypeAdapter;
    PriorityAdapter priorityAdapter;
    AssigneeAdapter assigneeAdapter;

    //Check date
    DateFormat dateFormat = new DateFormat();
    Calendar myCalendar = Calendar.getInstance();

    SharedPreferences sharedPreferences_user, sharedPreferences_project;
    String user_ID = "";
    String project_ID = "";

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference, databaseReference1, databaseReference2, databaseReference3, databaseReference4;
    AppCompatActivity activity;

    //New issue information
    String issue_ID;
    String issue_Name = " ";
    String issue_ProcessType = " ";
    String issue_Description = " ";
    String issue_Type = " ";
    String issue_StartDate = " ";
    String issue_Priority = " ";
    String issue_Assignee = " ";
    String issue_EstimateFinishDate = " ";
    String issue_Creator = " ";
    String issue_project_ID = " ";
    String issue_FinishDate = " ";

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
        calendar_estimate_date_finish_issue_create = view.findViewById(R.id.calendar_estimate_date_finish_issue_create);
        calendar_finish_date_issue_create = view.findViewById(R.id.calendar_finish_date_issue_create);

        issue_name_create_text_input_layout = view.findViewById(R.id.issue_name_create_text_input_layout);
        description_issue_create_text_input_layout = view.findViewById(R.id.description_issue_create_text_input_layout);
        start_date_create_text_input_layout = view.findViewById(R.id.start_date_create_text_input_layout);
        estimate_date_finish_create_text_input_layout = view.findViewById(R.id.estimate_date_finish_create_text_input_layout);
        finish_date_issue_create_text_input_layout = view.findViewById(R.id.finish_date_issue_create_text_input_layout);

        linearLayout_finish_date = view.findViewById(R.id.linearLayout_finish_date);

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
        member_list.add(new User("1", "Chí Thiện", "chithien@gmail.com",
                "1", "0942920838"," ","Leader", " ", " ", " "));

        issueTypeAdapter = new IssueTypeAdapter(getContext(), issueType_list);
        spinner_issue_type_create.setAdapter(issueTypeAdapter);

        processTypeAdapter = new ProcessTypeAdapter(getContext(), processType_list);
        spinner_detail_process_create.setAdapter(processTypeAdapter);

        priorityAdapter = new PriorityAdapter(getContext(), priority_list);
        spinner_detail_priority_issue_create.setAdapter(priorityAdapter);

        assigneeAdapter = new AssigneeAdapter(getContext(), member_list);
        spinner_detail_assignee_issue_create.setAdapter(assigneeAdapter);

        sharedPreferences_user = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        sharedPreferences_project = requireContext().getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
        user_ID = sharedPreferences_user.getString("user_ID", "abc");
        project_ID = sharedPreferences_project.getString("project_ID", "abc");
//        member_list.clear();
        activity = (AppCompatActivity) getContext();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference3 = FirebaseDatabase.getInstance().getReference("User_List_By_Project").child(project_ID);
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> list = new ArrayList();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Project_Users user = dataSnapshot.getValue(Project_Users.class);
                    list.add(user.getUser_ID());
                }
                if (list.size() > 0){
                    member_list.clear();
                    databaseReference4 = FirebaseDatabase.getInstance().getReference("Users");
                    for (String s : list){
//                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                        databaseReference4.child(s).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                member_list.add(user);
                                assigneeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                assigneeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        assigneeAdapter.notifyDataSetChanged();

        start_date_create_text_input_layout.getEditText().setText(dateFormat.formatDate((Calendar.getInstance().getTime())));
        estimate_date_finish_create_text_input_layout.getEditText().setText(dateFormat.formatDate((Calendar.getInstance().getTime())));

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
                estimate_date_finish_create_text_input_layout.getEditText().setText(dateFormat.formatDate((myCalendar.getTime())));
                checkDate(estimate_date_finish_create_text_input_layout.getEditText().getText().toString());
            }
        };

        DatePickerDialog.OnDateSetListener date3 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                finish_date_issue_create_text_input_layout.getEditText().setText(dateFormat.formatDate((myCalendar.getTime())));
                checkDate(finish_date_issue_create_text_input_layout.getEditText().getText().toString());
            }
        };


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
        calendar_estimate_date_finish_issue_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Todo: Xử lý sự kiện chọn ngày từ Calendar View
        calendar_finish_date_issue_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date3, myCalendar
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

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng cho Issue Estimate finish date
        estimate_date_finish_create_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (estimate_date_finish_create_text_input_layout.getEditText().getText().length() == 0){
                        estimate_date_finish_create_text_input_layout.setError("Please enter estimate finish date!!!");
                        estimate_date_finish_create_text_input_layout.setErrorEnabled(true);
                    }else {
                        estimate_date_finish_create_text_input_layout.setErrorEnabled(false);
                        checkRightDate(estimate_date_finish_create_text_input_layout);
                        if (estimate_date_finish_create_text_input_layout.getEditText().getText().length() > 0){
                            try {
                                Date date1 = dateFormat.sdf.parse(start_date_create_text_input_layout.getEditText().getText().toString());
                                Date date2 = dateFormat.sdf.parse(estimate_date_finish_create_text_input_layout.getEditText().getText().toString());
                                if (date2.getTime() < date1.getTime()
                                        || start_date_create_text_input_layout.getEditText().getText().toString()
                                        .equals(estimate_date_finish_create_text_input_layout.getEditText().getText().toString())){
                                    estimate_date_finish_create_text_input_layout.setError("Wrong finish day!!!");
                                    estimate_date_finish_create_text_input_layout.setErrorEnabled(true);
                                }else estimate_date_finish_create_text_input_layout.setErrorEnabled(false);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }else{
                    estimate_date_finish_create_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (estimate_date_finish_create_text_input_layout.getEditText().getText().length() == 0){
                                estimate_date_finish_create_text_input_layout.setError("Please enter estimate finish date!!!");
                                estimate_date_finish_create_text_input_layout.setErrorEnabled(true);
                            }else {
                                estimate_date_finish_create_text_input_layout.setErrorEnabled(false);
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

        //Todo: Xử lý sự kiện lấy Issue Type
        spinner_issue_type_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                issue_Type = issueType_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện lấy Issue Process Type
        spinner_detail_process_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                issue_ProcessType = processType_list.get(position).getName();
                if (issue_ProcessType.toLowerCase().equals("done")){
                    linearLayout_finish_date.setVisibility(View.VISIBLE);
                }else {
                    linearLayout_finish_date.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện lấy Issue Priority
        spinner_detail_priority_issue_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                issue_Priority = priority_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện lấy Issue Assignee
        spinner_detail_assignee_issue_create.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                issue_Assignee = member_list.get(position).getUser_Name();
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

                if (start_date_create_text_input_layout.getEditText().getText().length() == 0){
                    start_date_create_text_input_layout.setError("Please enter start date!!!");
                    start_date_create_text_input_layout.setErrorEnabled(true);
                }else {
                    start_date_create_text_input_layout.setErrorEnabled(false);
                    checkDate(start_date_create_text_input_layout.getEditText().getText().toString());
                }

                if (estimate_date_finish_create_text_input_layout.getEditText().getText().length() == 0){
                    estimate_date_finish_create_text_input_layout.setError("Please enter estimate finish date!!!");
                    estimate_date_finish_create_text_input_layout.setErrorEnabled(true);
                }else {
                    estimate_date_finish_create_text_input_layout.setErrorEnabled(false);
                    checkRightDate(estimate_date_finish_create_text_input_layout);
                }

                if (estimate_date_finish_create_text_input_layout.getEditText().getText().length() > 0){
                    try {
                        Date date1 = dateFormat.sdf.parse(start_date_create_text_input_layout.getEditText().getText().toString());
                        Date date2 = dateFormat.sdf.parse(estimate_date_finish_create_text_input_layout.getEditText().getText().toString());
                        if (date2.getTime() < date1.getTime()
                            || start_date_create_text_input_layout.getEditText().getText().toString()
                                .equals(estimate_date_finish_create_text_input_layout.getEditText().getText().toString())){
                            estimate_date_finish_create_text_input_layout.setError("Wrong finish day!!!");
                            estimate_date_finish_create_text_input_layout.setErrorEnabled(true);
                        }else estimate_date_finish_create_text_input_layout.setErrorEnabled(false);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (issue_ProcessType.toLowerCase().equals("done")
                        && finish_date_issue_create_text_input_layout.getEditText().getText().toString().length() > 0){
                    if (dateFormat.isValidDate(finish_date_issue_create_text_input_layout.getEditText().getText().toString())){
                        checkRightDate(finish_date_issue_create_text_input_layout);
                        if (finish_date_issue_create_text_input_layout.getEditText().getText().length() > 0){
                            try {
                                Date date1 = dateFormat.sdf.parse(start_date_create_text_input_layout.getEditText().getText().toString());
                                Date date2 = dateFormat.sdf.parse(finish_date_issue_create_text_input_layout.getEditText().getText().toString());
                                if (date2.getTime() < date1.getTime()
                                        || start_date_create_text_input_layout.getEditText().getText().toString()
                                        .equals(finish_date_issue_create_text_input_layout.getEditText().getText().toString())){
                                    finish_date_issue_create_text_input_layout.setError("Wrong finish day!!!");
                                    finish_date_issue_create_text_input_layout.setErrorEnabled(true);
                                }else finish_date_issue_create_text_input_layout.setErrorEnabled(false);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        finish_date_issue_create_text_input_layout.setError("Wrong finish date!!!");
                        finish_date_issue_create_text_input_layout.setErrorEnabled(true);
                    }
                }else {
                    finish_date_issue_create_text_input_layout.setError("Please enter finish date!!!!");
                    finish_date_issue_create_text_input_layout.setErrorEnabled(false);
                }

                if (issue_name_create_text_input_layout.isErrorEnabled() || estimate_date_finish_create_text_input_layout.isErrorEnabled()
                        || start_date_create_text_input_layout.isErrorEnabled() || finish_date_issue_create_text_input_layout.isErrorEnabled()){
                    Toast.makeText(getContext(), "Please check error!!!", Toast.LENGTH_SHORT).show();
                }else{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    issue_Name = issue_name_create_text_input_layout.getEditText().getText().toString();
                                    issue_StartDate = start_date_create_text_input_layout.getEditText().getText().toString();
                                    issue_EstimateFinishDate = estimate_date_finish_create_text_input_layout.getEditText().getText().toString();
                                    issue_FinishDate = finish_date_issue_create_text_input_layout.getEditText().getText().toString();
                                    if (issue_FinishDate.length() == 0){
                                        issue_FinishDate = " ";
                                    }

                                    if (description_issue_create_text_input_layout.getEditText().getText().length() == 0){
                                        issue_Description = " ";
                                    }else issue_Description = description_issue_create_text_input_layout.getEditText().getText().toString();
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
                                    if (!user_ID.equals("abc") && user_ID.equals(firebaseUser.getUid())){
                                        if (!project_ID.equals("abc")){
                                            issue_Creator = user_ID;
                                            issue_project_ID = project_ID;
                                            reference = FirebaseDatabase.getInstance().getReference("Issues").child(project_ID);
                                            issue_ID = reference.push().getKey();
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("issue_ID", issue_ID);
                                            hashMap.put("issue_Name", issue_Name);
                                            hashMap.put("issue_ProcessType", issue_ProcessType);
                                            hashMap.put("issue_Description", issue_Description);
                                            hashMap.put("issue_Type", issue_Type);
                                            hashMap.put("issue_StartDate", issue_StartDate);
                                            hashMap.put("issue_Priority", issue_Priority);
                                            hashMap.put("issue_Assignee", issue_Assignee);
                                            hashMap.put("issue_EstimateFinishDate", issue_EstimateFinishDate);
                                            hashMap.put("issue_Creator", issue_Creator);
                                            hashMap.put("issue_project_ID", issue_project_ID);
                                            hashMap.put("issue_FinishDate", issue_FinishDate);
                                            reference.child(issue_ID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        databaseReference1 = FirebaseDatabase.getInstance().getReference("Issue_List_By_Project");
                                                        HashMap<String, Object> hashMap1 = new HashMap<>();
                                                        hashMap1.put("issue_ID", issue_ID);
                                                        hashMap1.put("project_ID", project_ID);
                                                        databaseReference1.child(project_ID).setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    databaseReference2 = FirebaseDatabase.getInstance().getReference("Issue_List_By_User");
                                                                    HashMap<String, Object> hashMap2 = new HashMap<>();
                                                                    hashMap2.put("issue_ID", issue_ID);
                                                                    hashMap2.put("user_name", issue_Assignee);
                                                                    hashMap2.put("project_ID", project_ID);
                                                                    databaseReference2.child(user_ID).setValue(hashMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
                                                                                Toast.makeText(activity, "Create success", Toast.LENGTH_SHORT).show();
                                                                            }else Toast.makeText(activity, "Create failed", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                }else Toast.makeText(activity, "Create failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else {
                                                        Toast.makeText(activity, "Create failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else {
                                            Toast.makeText(getContext(), "project_ID: " + project_ID, Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(getContext(), "user_ID:" + user_ID, Toast.LENGTH_SHORT).show();
                                    }
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
                    if (issue_ProcessType.equals("ToDo")){
                        boolean isCheck = dateFormat.checkDate(rightDate);
                        if (isCheck){
                            start_date_create_text_input_layout.setErrorEnabled(false);
                        }else {
                            start_date_create_text_input_layout.setError("Wrong day!!!");
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
    public void checkRightDate(TextInputLayout textInputLayout){
        String date = textInputLayout.getEditText().getText().toString();
        if (!dateFormat.isValidDate(date)){
            textInputLayout.setError("Wrong format. Ex: dd/MM/yyy");
            textInputLayout.setErrorEnabled(true);
        }else{
            try {
                Date date1 = dateFormat.sdf.parse(date);
                if (date1.getTime() < Calendar.getInstance().getTime().getTime()){
                    textInputLayout.setError("Wrong start day!!!");
                    textInputLayout.setErrorEnabled(true);
                }else textInputLayout.setErrorEnabled(false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}