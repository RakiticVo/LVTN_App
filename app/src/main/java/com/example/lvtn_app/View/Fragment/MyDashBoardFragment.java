package com.example.lvtn_app.View.Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.lvtn_app.Adapter.ProcessTypeAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.ProcessType;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.User_Issue_List;
import com.example.lvtn_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
 * Use the {@link MyDashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDashBoardFragment extends Fragment {
    //Khai báo
    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;
    ArrayList<User_Issue_List> issue_list, final_list, temp_list;
    AppCompatActivity activity;
    Spinner spinner_issue_process_personal_dashoard;
    ArrayList<ProcessType> processType_list;
    ProcessTypeAdapter processTypeAdapter;

    DateFormat dateFormat;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ValueEventListener valueEventListener;

    static MyDashBoardFragment instance;

    public static MyDashBoardFragment getInstance() {
        return instance;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyDashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TableDashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyDashBoardFragment newInstance(String param1, String param2) {
        MyDashBoardFragment fragment = new MyDashBoardFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_dash_board, container, false);
        mTableLayout = view.findViewById(R.id.tableLayout);
        spinner_issue_process_personal_dashoard = view.findViewById(R.id.spinner_issue_process_personal_dashoard);
        activity = (AppCompatActivity) getContext();
        dateFormat = new DateFormat();

        instance = this;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //For spinner use
        processType_list = new ArrayList<>();
        processType_list.add(new ProcessType(R.drawable.all_issues, "My All Issues"));
        processType_list.add(new ProcessType(R.drawable.todo, "ToDo"));
        processType_list.add(new ProcessType(R.drawable.inprogress, "InProgress"));
        processType_list.add(new ProcessType(R.drawable.done, "Done"));

        processTypeAdapter = new ProcessTypeAdapter(getContext(), processType_list);
        spinner_issue_process_personal_dashoard.setAdapter(processTypeAdapter);

        issue_list = new ArrayList<>();
        temp_list = new ArrayList<>();
        final_list = new ArrayList<>();
        issue_list.add(new User_Issue_List(" ", " ", " ", " ", " ", ""));
        final_list.add(new User_Issue_List(" ", "Issue Name", "Process", "Issue Type", "Project", ""));


        issue_list.clear();
        final_list.clear();
        mProgressBar = new ProgressDialog(activity);
        mTableLayout.setStretchAllColumns(true);
        startLoadData();
//        getAllIssue();

        spinner_issue_process_personal_dashoard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
//                        Toast.makeText(getContext(), "" + processType_list.get(0).getName(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), "All issues: " + issue_list.size(), Toast.LENGTH_SHORT).show();
                        final_list.clear();
                        showIssuesByProcessType(temp_list, processType_list.get(0).getName());
                        break;
                    case 1:
//                        Toast.makeText(getContext(), "" + processType_list.get(1).getName(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), "Todo List: " + toDo_list.size(), Toast.LENGTH_SHORT).show();
                        final_list.clear();
                        showIssuesByProcessType(temp_list, processType_list.get(1).getName());
                        break;
                    case 2:
//                        Toast.makeText(getContext(), "" + processType_list.get(2).getName(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), "InProgress List: " + inProgress_list.size(), Toast.LENGTH_SHORT).show();
                        final_list.clear();
                        showIssuesByProcessType(temp_list, processType_list.get(2).getName());
                        break;
                    case 3:
//                        Toast.makeText(getContext(), "" + processType_list.get(3).getName(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), "Done List: " + done_list.size(), Toast.LENGTH_SHORT).show();
                        final_list.clear();
                        showIssuesByProcessType(temp_list, processType_list.get(3).getName());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void startLoadData() {
        mProgressBar.setCancelable(false);
        mProgressBar.setMessage("Fetching Data...");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();
        createHeaderForTable();
        getAllIssue();
    }

    public void getAllIssue(){
        issue_list.clear();
        reference = FirebaseDatabase.getInstance().getReference("Issue_List_By_User").child(firebaseUser.getUid());
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User_Issue_List issue = dataSnapshot.getValue(User_Issue_List.class);
                    issue_list.add(issue);
                }
                showIssues(issue_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addValueEventListener(valueEventListener);
        reference.onDisconnect();
    }

    public void showIssues(ArrayList<User_Issue_List> issues){
        final_list.clear();
//        final_list.add(new User_Issue_List(" ", "Issue Name", "Process", "Issue Type", "Project", ""));
        final_list.addAll(issues);
//        Toast.makeText(activity, "" + final_list.size(), Toast.LENGTH_SHORT).show();
        for (User_Issue_List issue : final_list){
            addRowIntoTable(issue);
        }
        mProgressBar.dismiss();
        updateIssue(issues);
        temp_list.addAll(issues);
    }

    public void showIssuesByProcessType(ArrayList<User_Issue_List> issues, String type){
        ArrayList<User_Issue_List> list = new ArrayList<>();
        final_list.clear();
        mTableLayout.removeAllViews();
        createHeaderForTable();
        switch (type) {
            case "My All Issues":
                final_list.clear();
//                final_list.add(new User_Issue_List(" ", "Issue Name", "Process", "Issue Type", "Project", ""));
                final_list.addAll(issues);
                for (User_Issue_List issue : final_list){
                    addRowIntoTable(issue);
                }
                break;
            case "ToDo":
                final_list.clear();
//                final_list.add(new User_Issue_List(" ", "Issue Name", "Process", "Issue Type", "Project", ""));
                list.clear();
                for (User_Issue_List userIssueList : issues) {
                    if (userIssueList.getIssue_ProcessType().equals("ToDo")) {
                        list.add(userIssueList);
                    }
                }
                final_list.addAll(list);
                for (User_Issue_List issue : final_list){
                    addRowIntoTable(issue);
                }
                break;
            case "InProgress":
                final_list.clear();
//                final_list.add(new User_Issue_List(" ", "Issue Name", "Process", "Issue Type", "Project", ""));
                list.clear();
                for (User_Issue_List userIssueList : issues) {
                    if (userIssueList.getIssue_ProcessType().equals("InProgress")) {
                        list.add(userIssueList);
                    }
                }
                final_list.addAll(list);
                for (User_Issue_List issue : final_list){
                    addRowIntoTable(issue);
                }
                break;
            case "Done":
                final_list.clear();
//                final_list.add(new User_Issue_List(" ", "Issue Name", "Process", "Issue Type", "Project", ""));
                list.clear();
                for (User_Issue_List userIssueList : issues) {
                    if (userIssueList.getIssue_ProcessType().equals("Done")) {
                        list.add(userIssueList);
                    }
                }
                final_list.addAll(list);
                for (User_Issue_List issue : final_list){
                    addRowIntoTable(issue);
                }
//                }
                break;
        }
    }

    public void createHeaderForTable(){
        TableRow row0 = new TableRow(activity);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        row0.setLayoutParams(layoutParams);
        row0.setWeightSum(12f);
        TextView tv0 = new TextView(activity);
        TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(50, TableRow.LayoutParams.WRAP_CONTENT, 3);
        tv0.setText("Issue Type");
        tv0.setTextColor(Color.BLACK);
        tv0.setTextSize(16f);
        tv0.setTypeface(tv0.getTypeface(), Typeface.BOLD);
        tv0.setGravity(Gravity.CENTER);
        tv0.setBackgroundResource(R.drawable.custom_heading_gridview);
        row0.addView(tv0, 0, layoutParams1);

        TextView tv1 = new TextView(activity);
        TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 4);
        layoutParams2.setMargins(-3,0,0,0);
        tv1.setText("Issue Name");
        tv1.setTextColor(Color.BLACK);
        tv1.setTextSize(16f);
        tv1.setTypeface(tv0.getTypeface(), Typeface.BOLD);
        tv1.setGravity(Gravity.CENTER);
        tv1.setBackgroundResource(R.drawable.custom_heading_gridview);
        row0.addView(tv1, 1, layoutParams2);

        TextView tv2 = new TextView(activity);
        TableRow.LayoutParams layoutParams3 = new TableRow.LayoutParams(50, TableRow.LayoutParams.MATCH_PARENT, 2);
        layoutParams3.setMargins(-3,0,0,0);
        tv2.setText("Project Name");
        tv2.setTextColor(Color.BLACK);
        tv2.setTextSize(16f);
        tv2.setTypeface(tv0.getTypeface(), Typeface.BOLD);
        tv2.setGravity(Gravity.CENTER);
        tv2.setBackgroundResource(R.drawable.custom_heading_gridview);
        row0.addView(tv2, 2, layoutParams3);

        TextView tv3 = new TextView(activity);
        TableRow.LayoutParams layoutParams4 = new TableRow.LayoutParams(50, TableRow.LayoutParams.MATCH_PARENT, 3);
        layoutParams4.setMargins(-3,0,0,0);
        tv3.setText("Process");
        tv3.setTextColor(Color.BLACK);
        tv3.setTextSize(16f);
        tv3.setTypeface(tv0.getTypeface(), Typeface.BOLD);
        tv3.setGravity(Gravity.CENTER);
        tv3.setBackgroundResource(R.drawable.custom_heading_gridview);
        row0.addView(tv3, 3, layoutParams4);
        row0.setBackgroundResource(R.drawable.custom_heading_gridview);
        mTableLayout.addView(row0);
    }

    public void addRowIntoTable(User_Issue_List issue_list){
        TableRow row = new TableRow(activity);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(layoutParams);
        row.setGravity(Gravity.CENTER);
        row.setWeightSum(12f);
        ImageView imageView = new ImageView(activity);
        TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(50, 150, 3);
        layoutParams1.setMargins(0,-3,0,0);
        imageView.setPadding(8,12,12,8);
        switch (issue_list.getIssue_Type()){
            case "Task":
                imageView.setImageResource(R.drawable.task);
                break;
            case "Bug":
                imageView.setImageResource(R.drawable.bug);
                break;
            case "Story":
                imageView.setImageResource(R.drawable.story);
                break;
        }
        imageView.setBackgroundResource(R.drawable.custome_data_gridview);
        row.addView(imageView, 0, layoutParams1);

        TextView tv1 = new TextView(activity);
        TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(350, TableRow.LayoutParams.MATCH_PARENT, 4);
        layoutParams2.setMargins(-3,-3,0,0);
        tv1.setPadding(12,0,0,0);
        tv1.setText(issue_list.getIssue_Name());
        tv1.setTextColor(Color.BLACK);
        tv1.setTextSize(16f);
        tv1.setGravity(Gravity.CENTER_VERTICAL);
        tv1.setBackgroundResource(R.drawable.custome_data_gridview);
        row.addView(tv1, 1, layoutParams2);

        TextView tv2 = new TextView(activity);
        TableRow.LayoutParams layoutParams3 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 2);
        layoutParams3.setMargins(-3,-3,0,0);
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Projects").child(issue_list.getProject_ID());
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Project project = snapshot.getValue(Project.class);
                tv2.setText(project.getProject_Name());
                tv2.setTextColor(Color.BLACK);
                tv2.setTextSize(16f);
                tv2.setGravity(Gravity.CENTER);
                tv2.setBackgroundResource(R.drawable.custome_data_gridview);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        row.addView(tv2, 2, layoutParams3);

        ImageView imageView1 = new ImageView(activity);
        TableRow.LayoutParams layoutParams4 = new TableRow.LayoutParams(50, TableRow.LayoutParams.MATCH_PARENT, 3);
        layoutParams4.setMargins(-3,-3,0,0);
        imageView1.setPadding(8,12,12,8);
        switch (issue_list.getIssue_ProcessType()){
            case "ToDo":
                imageView1.setImageResource(R.drawable.todo);
                imageView1.setPadding(14,14,14,14);
                break;
            case "InProgress":
                imageView1.setImageResource(R.drawable.inprogress);
                imageView1.setPadding(14,14,14,14);
                break;
            case "Done":
                imageView1.setImageResource(R.drawable.done);
                break;
        }
        imageView1.setBackgroundResource(R.drawable.custome_data_gridview);
        row.addView(imageView1, 3, layoutParams4);
        mTableLayout.addView(row);
    }

    public void updateIssue(ArrayList<User_Issue_List> issues){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Issues");
        for (User_Issue_List user_issue_list : issues) {
            if (user_issue_list.getIssue_ProcessType().equals("ToDo")) {
                reference.child(user_issue_list.getProject_ID()).child(user_issue_list.getIssue_ID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Issue issue = snapshot.getValue(Issue.class);
                        try {
                            Date date = dateFormat.sdf.parse(issue.getIssue_StartDate());
                            if (date.getTime() < Calendar.getInstance().getTime().getTime()){
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Issue_List_By_User")
                                        .child(firebaseUser.getUid()).child(issue.getIssue_ID());
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("issue_ProcessType", "InProgress");
                                databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                           getAllIssue();
                                        }
                                    }
                                });
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }
}