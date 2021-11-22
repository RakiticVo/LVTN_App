package com.example.lvtn_app.View.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    ImageButton ibtn_back_issue_detail, ibtn_confirm_done_issue, ibtn_delete_issue_detail,
            calendar_start_date_issue_detail, calendar_estimate_date_finish_issue_detail;
    TextInputLayout description_issue_detail_text_input_layout, start_date_issue_detail_text_input_layout,
            estimate_date_finish_issue_detail_text_input_layout;
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
    ProgressDialog progressDialog;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference, databaseReference1, databaseReference2, databaseReference3, databaseReference4;
    //New issue information
    String issue_ID;
    String issue_Name = "";
    String issue_ProcessType = "";
    String issue_Description = "";
    String issue_Type = "";
    String issue_StartDate = "";
    String issue_Priority = "";
    String issue_Assignee = "";
    String issue_EstimateFinishDate = "";
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
        ibtn_delete_issue_detail = view.findViewById(R.id.ibtn_delete_issue_detail);
        calendar_start_date_issue_detail = view.findViewById(R.id.calendar_start_date_issue_detail);
        calendar_estimate_date_finish_issue_detail = view.findViewById(R.id.calendar_estimate_date_finish_issue_detail);

        description_issue_detail_text_input_layout = view.findViewById(R.id.description_issue_detail_text_input_layout);
        start_date_issue_detail_text_input_layout = view.findViewById(R.id.start_date_issue_detail_text_input_layout);
        estimate_date_finish_issue_detail_text_input_layout = view.findViewById(R.id.estimate_date_finish_issue_detail_text_input_layout);

        btn_update_issue_detail = view.findViewById(R.id.btn_update_issue_detail);
        btn_cancel_issue_detail = view.findViewById(R.id.btn_cancel_issue_detail);

        start_date_issue_detail_text_input_layout.setEnabled(false);

        issueType_list = new ArrayList<>();
        issueType_list.add(new IssueType(R.drawable.task, getString(R.string.task)));
        issueType_list.add(new IssueType(R.drawable.bug, getString(R.string.bug)));
        issueType_list.add(new IssueType(R.drawable.user_story, getString(R.string.story)));

        processType_list = new ArrayList<>();
        processType_list.add(new ProcessType(R.drawable.todo, getString(R.string.todo)));
        processType_list.add(new ProcessType(R.drawable.inprogress, getString(R.string.inprogress)));
        processType_list.add(new ProcessType(R.drawable.done, getString(R.string.done)));

        priority_list = new ArrayList<>();
        priority_list.add(new Priority(R.drawable.high, getString(R.string.high)));
        priority_list.add(new Priority(R.drawable.medium, getString(R.string.medium)));
        priority_list.add(new Priority(R.drawable.low, getString(R.string.low)));

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

        sharedPreferences_issue = requireContext().getSharedPreferences("Issue", Context.MODE_PRIVATE);
        sharedPreferences_user = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        sharedPreferences_project = requireContext().getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);

        issue_ID = sharedPreferences_issue.getString("issue_ID", "abc");
        user_ID = sharedPreferences_user.getString("user_ID", "abc");
        project_ID = sharedPreferences_project.getString("project_ID", "abc");

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
                    if (!processType_list.get(spinner_process_issue_detail.getSelectedItemPosition()).equals(activity.getString(R.string.todo))){
                        start_date_issue_detail_text_input_layout.setEnabled(false);
                    }
                    spinner_assignee_issue_detail.setEnabled(false);
                    spinner_issue_type_detail.setEnabled(false);
                    spinner_priority_issue_detail.setEnabled(false);
                }else {
                    if (!processType_list.get(spinner_process_issue_detail.getSelectedItemPosition()).equals(activity.getString(R.string.done))){
                        ibtn_delete_issue_detail.setVisibility(View.VISIBLE);
                        ibtn_delete_issue_detail.setEnabled(true);
                    }
//                    Toast.makeText(activity, "Leader is here", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        start_date_issue_detail_text_input_layout.getEditText().setText(dateFormat.sdf.format(Calendar.getInstance().getTime()));

        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                estimate_date_finish_issue_detail_text_input_layout.getEditText().setText(dateFormat.sdf.format(myCalendar.getTime()));
                checkDate(estimate_date_finish_issue_detail_text_input_layout);
                checkRightDate(estimate_date_finish_issue_detail_text_input_layout, start_date_issue_detail_text_input_layout);
            }
        };

        if (!issue_ID.equals("abc")){
            reference = FirebaseDatabase.getInstance().getReference("Issues").child(project_ID).child(issue_ID);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Issue issue = snapshot.getValue(Issue.class);
                    if (issue != null){
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                if (!issue.getIssue_Assignee().equals(user.getUser_Name())){
                                    ibtn_confirm_done_issue.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        issue_ID = issue.getIssue_ID();
                        issue_Name = issue.getIssue_Name();
                        issue_ProcessType = issue.getIssue_ProcessType();
                        issue_Description = issue.getIssue_Description();
                        issue_Type = issue.getIssue_Type();
                        issue_StartDate = issue.getIssue_StartDate();
                        issue_Priority = issue.getIssue_Priority();
                        issue_Assignee = issue.getIssue_Assignee();
                        issue_EstimateFinishDate = issue.getIssue_EstimateFinishDate();
                        issue_Creator = issue.getIssue_Creator();
                        issue_project_ID = issue.getIssue_project_ID();
                        issue_FinishDate = issue.getIssue_FinishDate();

                        tv_name_issue_detail.setText(issue_Name);
                        description_issue_detail_text_input_layout.getEditText().setText(issue_Description);
                        start_date_issue_detail_text_input_layout.getEditText().setText(issue_StartDate);
                        estimate_date_finish_issue_detail_text_input_layout.getEditText().setText(issue_EstimateFinishDate);
                        tv_issue_finish_date.setText(issue_FinishDate);

                        switch (issue.getIssue_ProcessType()){
                            case "Todo":
                                spinner_process_issue_detail.setSelection(0);
                                processTypeAdapter.notifyDataSetChanged();
                                break;
                            case "InProgress":
                                spinner_process_issue_detail.setSelection(1);
                                processTypeAdapter.notifyDataSetChanged();
                                break;
                            case "Done":
                                spinner_process_issue_detail.setSelection(2);
                                processTypeAdapter.notifyDataSetChanged();
                                break;
                        }

                        switch (issue.getIssue_Type()){
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

                        switch (issue.getIssue_Priority()){
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
                                member_list.clear();
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
                        spinner_assignee_issue_detail.setEnabled(false);

                        if (issue.getIssue_ProcessType().toLowerCase().equals("inprogress")){
                            ibtn_confirm_done_issue.setVisibility(View.VISIBLE);
                        }else {
                            ibtn_confirm_done_issue.setVisibility(View.GONE);
                        }

                        if (issue.getIssue_ProcessType().toLowerCase().equals("done")){
                            spinner_process_issue_detail.setEnabled(false);
                            spinner_issue_type_detail.setEnabled(false);
                            spinner_priority_issue_detail.setEnabled(false);
                            description_issue_detail_text_input_layout.setEnabled(false);
                            start_date_issue_detail_text_input_layout.setEnabled(false);
                            estimate_date_finish_issue_detail_text_input_layout.setEnabled(false);
                            btn_update_issue_detail.setVisibility(View.GONE);
                            btn_cancel_issue_detail.setVisibility(View.GONE);
                        }else {
                            spinner_process_issue_detail.setEnabled(true);
                            spinner_issue_type_detail.setEnabled(true);
                            spinner_priority_issue_detail.setEnabled(true);
                            description_issue_detail_text_input_layout.setEnabled(true);
                            estimate_date_finish_issue_detail_text_input_layout.setEnabled(true);
                            btn_update_issue_detail.setVisibility(View.VISIBLE);
                            btn_cancel_issue_detail.setVisibility(View.VISIBLE);
                        }
                    }
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

        calendar_estimate_date_finish_issue_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Todo: Xử lý sự kiện nhập và kiểm tra rỗng Issue Detail Estimate Finish Date
        estimate_date_finish_issue_detail_text_input_layout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (estimate_date_finish_issue_detail_text_input_layout.getEditText().getText().length() == 0){
                        estimate_date_finish_issue_detail_text_input_layout.setError("Please enter estimate finish date!!!");
                        estimate_date_finish_issue_detail_text_input_layout.setErrorEnabled(true);
                    }else {
                        estimate_date_finish_issue_detail_text_input_layout.setErrorEnabled(false);
                        checkDate(estimate_date_finish_issue_detail_text_input_layout);
                    }
                }else {
                    estimate_date_finish_issue_detail_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (estimate_date_finish_issue_detail_text_input_layout.getEditText().getText().length() == 0){
                                estimate_date_finish_issue_detail_text_input_layout.setError("Please enter estimate finish date!!!");
                                estimate_date_finish_issue_detail_text_input_layout.setErrorEnabled(true);
                            }else {
                                estimate_date_finish_issue_detail_text_input_layout.setErrorEnabled(false);
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
                switch (position){
                    case 0:
                        issue_Type = "Task";
                        break;
                    case 1:
                        issue_Type = "Bug";
                        break;
                    case 2:
                        issue_Type = "Story";
                        break;
                }
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
                switch (position){
                    case 0:
                        issue_ProcessType = "ToDo";
                        break;
                    case 1:
                        issue_ProcessType = "InProgress";
                        break;
                    case 2:
                        issue_ProcessType = "Done";
                        break;
                }
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
                switch (position){
                    case 0:
                        issue_Priority = "High";
                        break;
                    case 1:
                        issue_Priority = "Medium";
                        break;
                    case 2:
                        issue_Priority = "Low";
                        break;
                }
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
//                if (start_date_issue_detail_text_input_layout.getEditText().getText().length() == 0) {
//                    start_date_issue_detail_text_input_layout.setError("Please choose day!!!");
//                    start_date_issue_detail_text_input_layout.setErrorEnabled(true);
//                } else {
//                    start_date_issue_detail_text_input_layout.setErrorEnabled(false);
//                    checkDate(issue_StartDate);
//                }
                if (estimate_date_finish_issue_detail_text_input_layout.getEditText().getText().length() > 0){
                    checkDate(estimate_date_finish_issue_detail_text_input_layout);
                    checkRightDate(estimate_date_finish_issue_detail_text_input_layout, start_date_issue_detail_text_input_layout);
                }

                if (estimate_date_finish_issue_detail_text_input_layout.isErrorEnabled()) {
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
                                    issue_EstimateFinishDate = estimate_date_finish_issue_detail_text_input_layout.getEditText().getText().toString();

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("issue_ProcessType", issue_ProcessType);
                                    hashMap.put("issue_Description", issue_Description);
                                    hashMap.put("issue_Type", issue_Type);
                                    hashMap.put("issue_StartDate", issue_StartDate);
                                    hashMap.put("issue_Priority", issue_Priority);
                                    hashMap.put("issue_Assignee", issue_Assignee);
                                    hashMap.put("issue_EstimateFinishDate", issue_EstimateFinishDate);

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Issues").child(project_ID).child(issue_ID);
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
                                spinner_process_issue_detail.setSelection(2);
                                issue_ProcessType = "Done";
                                DateFormat dateFormat = new DateFormat();
                                String currentDate = dateFormat.formatDate(Calendar.getInstance().getTime());
                                processTypeAdapter.notifyDataSetChanged();
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("issue_ProcessType", issue_ProcessType);
                                hashMap.put("issue_FinishDate", currentDate);

//                                Toast.makeText(activity, project_ID + "\n" + issue_ID, Toast.LENGTH_LONG).show();

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Issues").child(project_ID).child(issue_ID);
                                reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
//                                            Toast.makeText(activity, "Update Issue is done success!!!" , Toast.LENGTH_SHORT).show();
//                                            Toast.makeText(activity, "This task is "+issue_ProcessType+"!!!" , Toast.LENGTH_SHORT).show();
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Issue_List_By_User");
                                            HashMap<String, Object> hashMap2 = new HashMap<>();
                                            hashMap2.put("issue_ProcessType", issue_ProcessType);
                                            databaseReference.child(firebaseUser.getUid()).child(issue_ID).updateChildren(hashMap2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(activity, "Update Issue is done success!!!" , Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                });
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

        ibtn_delete_issue_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                deleteIssueAll(issue_ID, project_ID);
                                dismiss();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
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

    public void deleteIssueAll(String issue_ID, String project_ID){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();
        deleteIssueInIssueUserList(issue_ID, project_ID);
    }

    private void deleteIssueInIssueUserList(String issue_ID, String project_ID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Issue_List_By_User").child(firebaseUser.getUid());
        databaseReference.child(issue_ID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(SettingChatActivity.this, "Delete chat list success", Toast.LENGTH_SHORT).show();
                    deleteIssueNotification(issue_ID, project_ID);
                }else {
//                    Toast.makeText(SettingChatActivity.this, "Delete chat list failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteIssueNotification(String issue_ID, String project_ID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications").child("Project_Issue_Request");
        databaseReference.child(project_ID).child(firebaseUser.getUid()).child(issue_ID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(SettingChatActivity.this, "Delete chat list success", Toast.LENGTH_SHORT).show();
                    deleteIssueInIssues(issue_ID, project_ID);
                }else {
//                    Toast.makeText(SettingChatActivity.this, "Delete chat list failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteIssueInIssues(String issue_ID, String project_ID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Issues").child(project_ID);
        databaseReference.child(issue_ID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    Toast.makeText(SettingChatActivity.this, "Delete chat list success", Toast.LENGTH_SHORT).show();
                    Toast.makeText(activity, "Delete Issue success" , Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    dismiss();
                }else {
//                    Toast.makeText(SettingChatActivity.this, "Delete chat list failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void checkDate(TextInputLayout textInputLayout){
        String date = textInputLayout.getEditText().getText().toString();
        if (!dateFormat.isValidDate(date)){
            textInputLayout.setError("Wrong format. Ex: dd/MM/yyy");
            textInputLayout.setErrorEnabled(true);
        }else{
            try {
                date1 = dateFormat.sdf.parse(date);
                if (date1.getTime() < Calendar.getInstance().getTime().getTime()){
                    textInputLayout.setError("Wrong day!!!");
                    textInputLayout.setErrorEnabled(true);
                }else textInputLayout.setErrorEnabled(false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkRightDate(TextInputLayout textInputLayout1, TextInputLayout textInputLayout2){
        try {
            Date day1 = dateFormat.sdf.
                    parse(textInputLayout1.getEditText().getText().toString());

            Date day2 = dateFormat.sdf.
                    parse(textInputLayout2.getEditText().getText().toString());
            if (day2.getTime() > day1.getTime()){
                estimate_date_finish_issue_detail_text_input_layout.setError("Wrong day!!!");
                estimate_date_finish_issue_detail_text_input_layout.setErrorEnabled(true);
            }else estimate_date_finish_issue_detail_text_input_layout.setErrorEnabled(false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}