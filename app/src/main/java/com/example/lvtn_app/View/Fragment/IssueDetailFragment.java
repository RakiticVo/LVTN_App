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
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.AssigneeAdapter;
import com.example.lvtn_app.Adapter.IssueTypeAdapter;
import com.example.lvtn_app.Adapter.PriorityAdapter;
import com.example.lvtn_app.Adapter.ProcessTypeAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.IssueType;
import com.example.lvtn_app.Model.ProcessType;
import com.example.lvtn_app.Model.Project;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IssueDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssueDetailFragment extends DialogFragment {
    //Khai báo
    Spinner spinner_process_issue_detail, spinner_issue_type_detail, spinner_priority_issue_detail, spinner_assignee_issue_detail;
    ImageView img_issue_type_detail;
    TextView tv_name_issue_detail, tv_issue_finish_date;
    ImageButton ibtn_back_issue_detail, ibtn_confirm_done_issue, calendar_start_date_issue_detail;
    TextInputLayout description_issue_detail_text_input_layout, start_date_issue_detail_text_input_layout, estimate_time_issue_detail_text_input_layout;
    Button btn_update_issue_detail, btn_cancel_issue_detail;

    //Array List for Spinner
    ArrayList<IssueType> issueType_list;
    ArrayList<ProcessType> processType_list;
    ArrayList<Priority> priority_list;
    ArrayList<User> member_list;

    //Adapter for Spinner
    IssueTypeAdapter issueTypeAdapter;
    ProcessTypeAdapter processTypeAdapter;
    PriorityAdapter priorityAdapter;
    AssigneeAdapter assigneeAdapter;

    //Date formate
    final Calendar myCalendar = Calendar.getInstance();
    DateFormat dateFormat = new DateFormat();
    Date date1 = new Date();

    //SharedPreferences
    SharedPreferences sharedPreferences_user, sharedPreferences_project, sharedPreferences_issue;
    String user_ID, project_ID;
    AppCompatActivity activity;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference, databaseReference1, databaseReference2, databaseReference3;
    //New issue information
    String issue_ID;
    String issue_Name = "";
    String issue_ProcessType = "";
    String issue_Description = "";
    String issue_Type = "";
    String issue_StartDate = "";
    String issue_Priority = "";
    String issue_Assignee = "";
    String issue_EstimateTime = "";
    String issue_Creator = "";
    String issue_project_ID = "";
    String issue_FinishDate = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IssueDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IssueDetailFragment newInstance(String param1, String param2) {
        IssueDetailFragment fragment = new IssueDetailFragment();
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
        //Set up
        View view = inflater.inflate(R.layout.fragment_issue_detail, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        spinner_process_issue_detail = view.findViewById(R.id.spinner_process_issue_detail);
        spinner_issue_type_detail = view.findViewById(R.id.spinner_issue_type_detail);
        spinner_priority_issue_detail = view.findViewById(R.id.spinner_priority_issue_detail);
        spinner_assignee_issue_detail = view.findViewById(R.id.spinner_assignee_issue_detail);

        img_issue_type_detail = view.findViewById(R.id.img_issue_type_detail);
        tv_name_issue_detail = view.findViewById(R.id.tv_name_issue_detail);
        tv_issue_finish_date = view.findViewById(R.id.tv_issue_finish_date);
        ibtn_back_issue_detail = view.findViewById(R.id.ibtn_back_issue_detail);
        ibtn_confirm_done_issue = view.findViewById(R.id.ibtn_confirm_done_issue);
        calendar_start_date_issue_detail = view.findViewById(R.id.calendar_start_date_issue_detail);

        description_issue_detail_text_input_layout = view.findViewById(R.id.description_issue_detail_text_input_layout);
        start_date_issue_detail_text_input_layout = view.findViewById(R.id.start_date_issue_detail_text_input_layout);
        estimate_time_issue_detail_text_input_layout = view.findViewById(R.id.estimate_time_issue_detail_text_input_layout);

        btn_update_issue_detail = view.findViewById(R.id.btn_update_issue_detail);
        btn_cancel_issue_detail = view.findViewById(R.id.btn_cancel_issue_detail);

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
                "1", "0942920838","","Leader", "", "", ""));

        issueTypeAdapter = new IssueTypeAdapter(getContext(), issueType_list);
        spinner_issue_type_detail.setAdapter(issueTypeAdapter);

        processTypeAdapter = new ProcessTypeAdapter(getContext(), processType_list);
        spinner_process_issue_detail.setAdapter(processTypeAdapter);

        priorityAdapter = new PriorityAdapter(getContext(), priority_list);
        spinner_priority_issue_detail.setAdapter(priorityAdapter);

        assigneeAdapter = new AssigneeAdapter(getContext(), member_list);
        spinner_assignee_issue_detail.setAdapter(assigneeAdapter);

        activity = (AppCompatActivity) getContext();
        sharedPreferences_user = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        sharedPreferences_project = requireContext().getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
        user_ID = sharedPreferences_user.getString("user_ID", "abc");
        project_ID = sharedPreferences_project.getString("project_ID", "abc");
//        member_list.clear();
        databaseReference1 = FirebaseDatabase.getInstance().getReference("User_List_By_Project").child(project_ID);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> list = new ArrayList();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Project_Users user = dataSnapshot.getValue(Project_Users.class);
                    list.add(user.getUser_ID());
                }
                if (list.size() > 0){
                    member_list.clear();
                    databaseReference2 = FirebaseDatabase.getInstance().getReference("Users");
                    for (String s : list){
//                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                        databaseReference2.child(s).addValueEventListener(new ValueEventListener() {
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

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference3 = FirebaseDatabase.getInstance().getReference("Projects").child(project_ID);
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Project project = snapshot.getValue(Project.class);
                if (!project.getProject_Leader().equals(firebaseUser.getUid())){
                    if (!processType_list.get(spinner_process_issue_detail.getSelectedItemPosition()).equals("ToDo")){
                        start_date_issue_detail_text_input_layout.setEnabled(false);
                    }
                    spinner_assignee_issue_detail.setEnabled(false);
                    spinner_issue_type_detail.setEnabled(false);
                    spinner_priority_issue_detail.setEnabled(false);
                    spinner_process_issue_detail.setEnabled(false);
                }else {
                    Toast.makeText(activity, "Leader is here", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        start_date_issue_detail_text_input_layout.getEditText().setText(dateFormat.sdf.format(Calendar.getInstance().getTime()));

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                start_date_issue_detail_text_input_layout.getEditText().setText(dateFormat.sdf.format(myCalendar.getTime()));
                checkRightStartDate(start_date_issue_detail_text_input_layout.getEditText().getText().toString());
            }
        };

        sharedPreferences_issue = requireContext().getSharedPreferences("Issue", Context.MODE_PRIVATE);
        issue_ID = sharedPreferences_issue.getString("issue_ID", "abc");
        if (!issue_ID.equals("abc")){
            reference = FirebaseDatabase.getInstance().getReference("Issues").child(issue_ID);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Issue issue = snapshot.getValue(Issue.class);
                    issue_ID = issue.getIssue_ID();
                    issue_Name = issue.getIssue_Name();
                    issue_ProcessType = issue.getIssue_ProcessType();
                    issue_Description = issue.getIssue_Description();
                    issue_Type = issue.getIssue_Type();
                    issue_StartDate = issue.getIssue_StartDate();
                    issue_Priority = issue.getIssue_Priority();
                    issue_Assignee = issue.getIssue_Assignee();
                    issue_EstimateTime = issue.getIssue_EstimateTime();
                    issue_Creator = issue.getIssue_Creator();
                    issue_project_ID = issue.getIssue_project_ID();
                    issue_FinishDate = issue.getIssue_FinishDate();

                    tv_name_issue_detail.setText(issue_Name);
                    description_issue_detail_text_input_layout.getEditText().setText(issue_Description);
                    start_date_issue_detail_text_input_layout.getEditText().setText(issue_StartDate);
                    estimate_time_issue_detail_text_input_layout.getEditText().setText(issue_EstimateTime);
                    tv_issue_finish_date.setText(issue_FinishDate);

                    switch (issue_ProcessType){
                        case "Todo":
                            spinner_process_issue_detail.setSelection(0);
                            processTypeAdapter.notifyDataSetChanged();
                            break;
                        case "Inprogress":
                            spinner_process_issue_detail.setSelection(1);
                            processTypeAdapter.notifyDataSetChanged();
                            break;
                        case "Done":
                            spinner_process_issue_detail.setSelection(2);
                            processTypeAdapter.notifyDataSetChanged();
                            break;
                    }

                    switch (issue_Type){
                        case "Task":
                            img_issue_type_detail.setImageResource(R.drawable.task);
                            spinner_issue_type_detail.setSelection(0);
                            issueTypeAdapter.notifyDataSetChanged();
                            break;
                        case "Bug":
                            img_issue_type_detail.setImageResource(R.drawable.bug);
                            spinner_issue_type_detail.setSelection(1);
                            issueTypeAdapter.notifyDataSetChanged();
                            break;
                        case "Story":
                            img_issue_type_detail.setImageResource(R.drawable.user_story);
                            spinner_issue_type_detail.setSelection(2);
                            issueTypeAdapter.notifyDataSetChanged();
                            break;
                    }

                    switch (issue_Priority){
                        case "High":
                            spinner_priority_issue_detail.setSelection(0);
                            priorityAdapter.notifyDataSetChanged();
                            break;
                        case "Medium":
                            spinner_priority_issue_detail.setSelection(1);
                            priorityAdapter.notifyDataSetChanged();
                            break;
                        case "Low":
                            spinner_priority_issue_detail.setSelection(2);
                            priorityAdapter.notifyDataSetChanged();
                            break;
                    }

                    databaseReference1 = FirebaseDatabase.getInstance().getReference("User_List_By_Project").child(project_ID);
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<String> list = new ArrayList();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Project_Users user = dataSnapshot.getValue(Project_Users.class);
                                list.add(user.getUser_ID());
                            }
                            if (list.size() > 0){
                                member_list.clear();
                                databaseReference2 = FirebaseDatabase.getInstance().getReference("Users");
                                for (int i = 0; i < list.size(); i++) {
                                    int finalI = i;
                                    databaseReference2.child(list.get(i)).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User user = snapshot.getValue(User.class);
                                            member_list.add(user);
                                            if(issue_Assignee.equals(user.getUser_Name())){
                                                spinner_assignee_issue_detail.setSelection(finalI);
                                                assigneeAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    assigneeAdapter.notifyDataSetChanged();
                                }
                            }
                            assigneeAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    assigneeAdapter.notifyDataSetChanged();
//                    switch (issue_Assignee){
//                        case "Chí Thiện":
//                            spinner_assignee_issue_detail.setSelection(0);
//                            assigneeAdapter.notifyDataSetChanged();
//                            break;
//                        case "Thiện Võ":
//                            spinner_assignee_issue_detail.setSelection(1);
//                            assigneeAdapter.notifyDataSetChanged();
//                            break;
//                        case "Rakitic Võ":
//                            spinner_assignee_issue_detail.setSelection(2);
//                            assigneeAdapter.notifyDataSetChanged();
//                            break;
//                    }
//                    Toast.makeText(getContext(), "" + assigneeAdapter.getItem(spinner_assignee_issue_detail.getSelectedItemPosition()), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //Bắt sự kiện
        //Todo: Xử lý sự kiện rời khỏi Fragment
        ibtn_back_issue_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Todo: Xử lý sự kiện rời khỏi Fragment
        btn_cancel_issue_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Todo: Xử lý sự kiện chọn ngày từ Calendar View
        calendar_start_date_issue_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng Issue Detail Estimate Time
        estimate_time_issue_detail_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (estimate_time_issue_detail_text_input_layout.getEditText().getText().length() == 0){
                        estimate_time_issue_detail_text_input_layout.setError("Wrong time!!!");
                        estimate_time_issue_detail_text_input_layout.setErrorEnabled(true);
                    } else {
                        estimate_time_issue_detail_text_input_layout.setErrorEnabled(false);
                        checkRightEstimateTime(estimate_time_issue_detail_text_input_layout.getEditText().getText().toString());
                    }
                }else {
                    estimate_time_issue_detail_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                estimate_time_issue_detail_text_input_layout.setError("Wrong time!!!");
                                estimate_time_issue_detail_text_input_layout.setErrorEnabled(true);
                            } else {
                                estimate_time_issue_detail_text_input_layout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng Issue Detail Start Date
        start_date_issue_detail_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    issue_StartDate = start_date_issue_detail_text_input_layout.getEditText().getText().toString();
                    if (issue_StartDate.length() == 0){
                        start_date_issue_detail_text_input_layout.setError("Please choose day!!!");
                        start_date_issue_detail_text_input_layout.setErrorEnabled(true);
                    }else {
                        start_date_issue_detail_text_input_layout.setErrorEnabled(false);
                        checkRightStartDate(issue_StartDate);
                    }
                }else {
                    start_date_issue_detail_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0){
                                start_date_issue_detail_text_input_layout.setError("Please choose day!!!");
                                start_date_issue_detail_text_input_layout.setErrorEnabled(true);
                            }else{
                                start_date_issue_detail_text_input_layout.setErrorEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                }
            }
        });

        //Todo: Xử lý sự kiện lấy Issue Detail Issue Type và hiển thị hình ảnh theo Issue Type
        spinner_issue_type_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "" + issueType_list.get(position).getName(), Toast.LENGTH_SHORT).show();
                img_issue_type_detail.setImageResource(issueType_list.get(position).getImage());
                issue_Type = issueType_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện lấy Issue Detail Process Type
        spinner_process_issue_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "" + processType_list.get(position).getName(), Toast.LENGTH_SHORT).show();
                issue_ProcessType = processType_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện lấy Issue Detail Priority
        spinner_priority_issue_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "" + priority_list.get(position).getName(), Toast.LENGTH_SHORT).show();
                issue_Priority = priority_list.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện lấy Issue Detail Assignee
        spinner_assignee_issue_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "" + user_list.get(position).getName(), Toast.LENGTH_SHORT).show();
                issue_Assignee = member_list.get(position).getUser_Name();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Todo: Xử lý sự kiện cập nhật một Issue
        // - Kiểm tra rỗng cho các text ----- (Incomplete)
        // - Lấy các text cần dùng ----- (Done)
        // - Gọi Api Service để cập nhật thông tin Issue trên database ----- (Incomplete)
        // - Gọi một Instance để cập nhật một Issue ----- (Incomplete)
        btn_update_issue_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo: Check permission
                if (start_date_issue_detail_text_input_layout.getEditText().getText().length() == 0) {
                    start_date_issue_detail_text_input_layout.setError("Please choose day!!!");
                    start_date_issue_detail_text_input_layout.setErrorEnabled(true);
                } else {
                    start_date_issue_detail_text_input_layout.setErrorEnabled(false);
                    checkRightStartDate(issue_StartDate);
                }

                if (estimate_time_issue_detail_text_input_layout.getEditText().getText().length() == 0) {
                    estimate_time_issue_detail_text_input_layout.setError("Wrong time!!!");
                    estimate_time_issue_detail_text_input_layout.setErrorEnabled(true);
                } else {
                    estimate_time_issue_detail_text_input_layout.setErrorEnabled(false);
                    checkRightEstimateTime(estimate_time_issue_detail_text_input_layout.getEditText().getText().toString());
                }

                if (start_date_issue_detail_text_input_layout.isErrorEnabled() || estimate_time_issue_detail_text_input_layout.isErrorEnabled()) {
                    Toast.makeText(getContext(), "Please check the error!!!", Toast.LENGTH_SHORT).show();
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    // Todo: remember check date must be >= today
                                    issue_Description = description_issue_detail_text_input_layout.getEditText().getText().toString();
                                    if (issue_Description.length() == 0){
                                        issue_Description = " ";
                                    }
                                    issue_StartDate = start_date_issue_detail_text_input_layout.getEditText().getText().toString();
                                    issue_EstimateTime = estimate_time_issue_detail_text_input_layout.getEditText().getText().toString();

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("issue_ProcessType", issue_ProcessType);
                                    hashMap.put("issue_Description", issue_Description);
                                    hashMap.put("issue_Type", issue_Type);
                                    hashMap.put("issue_StartDate", issue_StartDate);
                                    hashMap.put("issue_Priority", issue_Priority);
                                    hashMap.put("issue_Assignee", issue_Assignee);
                                    hashMap.put("issue_EstimateTime", issue_EstimateTime);

                                    reference = FirebaseDatabase.getInstance().getReference("Issues").child(issue_ID);
                                    AppCompatActivity activity = (AppCompatActivity) getContext();
                                    reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(activity, "Update success", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(activity, "Update failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    Toast.makeText(getContext(), "Update error!!!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you want to save change?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            }
        });

        //Todo: Xử lý sự kiện hoàn thành một Issue
        // - Gọi Api Service để xác nhận hoàn thành một Issue trên database ----- (Incomplete)
        // - Gọi một Instance để xác nhận hoàn thành một Issue ----- (Incomplete)
        ibtn_confirm_done_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update list with process is done and return dashboard
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                for (int i = 0; i < processType_list.size(); i++) {
                                    if (processType_list.get(i).getName().equals("Done")){
                                        spinner_process_issue_detail.setSelection(i);
                                        issue_ProcessType = processType_list.get(i).getName().toString();
                                        processTypeAdapter.notifyDataSetChanged();
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("issue_ProcessType", issue_ProcessType);

                                        reference = FirebaseDatabase.getInstance().getReference("Issues").child(issue_ID);
                                        AppCompatActivity activity = (AppCompatActivity) getContext();
                                        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(activity, "This task is "+issue_ProcessType+"!!!" , Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        break;
                                    }
                                }
                                dismiss();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                Toast.makeText(getContext(), "Cancel" , Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Would you like to confirm this issue is complete?").setPositiveButton("Confirm", dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener).show();
            }
        });

        return view;
    }

    public void checkRightStartDate(String date){
        if (!dateFormat.isValidDate(date)){
            start_date_issue_detail_text_input_layout.setError("Wrong format. Ex: dd/MM/yyy");
            start_date_issue_detail_text_input_layout.setErrorEnabled(true);
        }else{
            try {
                date1 = dateFormat.sdf.parse(date);
                if (date1.getTime() < Calendar.getInstance().getTime().getTime()){
                    start_date_issue_detail_text_input_layout.setError("Wrong start day!!!");
                    start_date_issue_detail_text_input_layout.setErrorEnabled(true);
                }else start_date_issue_detail_text_input_layout.setErrorEnabled(false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkRightEstimateTime(String date){
        String last = date.substring(date.length() - 1);
        if (date.length() < 2 || (!last.equals("d") && !last.equals("m") && !last.equals("w"))) {
            estimate_time_issue_detail_text_input_layout.setError("Wrong format!!! Ex: 1d, 2w, 3m");
            estimate_time_issue_detail_text_input_layout.setErrorEnabled(true);
        } else {
            estimate_time_issue_detail_text_input_layout.setErrorEnabled(false);
        }
    }
}