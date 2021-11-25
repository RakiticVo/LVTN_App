package com.example.lvtn_app.View.Fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.User_Issue_List;
import com.example.lvtn_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IssueCompletedTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssueCompletedTableFragment extends DialogFragment{
    private TableLayout mTableLayout;
    ArrayList<User_Issue_List> final_list;
    ArrayList<Issue> perfect_issue, delay_issue;
    DateFormat dateFormat;
    String type;

    AppCompatActivity activity;

    FirebaseUser firebaseUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IssueCompletedTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IssueCompletedTableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IssueCompletedTableFragment newInstance(String param1, String param2) {
        IssueCompletedTableFragment fragment = new IssueCompletedTableFragment();
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
        View view = inflater.inflate(R.layout.fragment_issue_completed_table, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        mTableLayout = view.findViewById(R.id.tableLayout2);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        dateFormat = new DateFormat();
        activity = (AppCompatActivity) getContext();

        final_list = new ArrayList<>();
        final_list.add(new User_Issue_List(" ", "Issue Name", "Process", "Issue Type", "Project", ""));

        Bundle bundle = getArguments();
        type = bundle.getString("type");
        mTableLayout.setStretchAllColumns(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Issue_List_By_User");
        reference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User_Issue_List> user_issue_lists = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User_Issue_List userIssueList = dataSnapshot.getValue(User_Issue_List.class);
                    user_issue_lists.add(userIssueList);
                }
                getIssues(user_issue_lists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    public void getIssues(ArrayList<User_Issue_List> user_issue_lists){
        ArrayList<Issue> temp = new ArrayList<>();
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Issues");
        for (User_Issue_List list : user_issue_lists){
            reference1.child(list.getProject_ID()).child(list.getIssue_ID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Issue issueList = snapshot.getValue(Issue.class);
                    temp.add(issueList);
                    if (temp.size() == user_issue_lists.size()){
//                        Toast.makeText(activity, "" + temp.size(), Toast.LENGTH_SHORT).show();
                        getIssuesByType(temp, type);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void getIssuesByType(ArrayList<Issue> issues, String type){
        perfect_issue = new ArrayList<>();
        delay_issue = new ArrayList<>();
        createHeaderForTable();
        for (Issue issue : issues){
            try {
                Date date1 = dateFormat.sdf.parse(issue.getIssue_EstimateFinishDate().toString());
                Date date2 = dateFormat.sdf.parse(issue.getIssue_FinishDate().toString());
                if (date2.getTime() <= date1.getTime()){
                    perfect_issue.add(issue);
                }else delay_issue.add(issue);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        final_list.clear();
        String s1 = activity.getString(R.string.perfectissues).toString();
        String s2 = activity.getString(R.string.delayissues).toString();
        if (type.equals(s1)){
            final_list.clear();
            for (Issue issue : perfect_issue){
                final_list.add(new User_Issue_List(
                        issue.getIssue_ID(),
                        issue.getIssue_Name(),
                        issue.getIssue_ProcessType(),
                        issue.getIssue_Type(),
                        issue.getIssue_project_ID(),
                        issue.getIssue_Name()));
            }
            for (User_Issue_List issue : final_list){
                addRowIntoTable(issue);
            }
        } else if (type.equals(s2)) {
            final_list.clear();
            for (Issue issue : delay_issue){
                final_list.add(new User_Issue_List(
                        issue.getIssue_ID(),
                        issue.getIssue_Name(),
                        issue.getIssue_ProcessType(),
                        issue.getIssue_Type(),
                        issue.getIssue_project_ID(),
                        issue.getIssue_Name()));
            }
            for (User_Issue_List issue : final_list){
                addRowIntoTable(issue);
            }
        }
    }

    public void createHeaderForTable(){
        TableRow row0 = new TableRow(activity);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        row0.setLayoutParams(layoutParams);
        row0.setWeightSum(12f);
        TextView tv0 = new TextView(activity);
        TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(50, TableRow.LayoutParams.MATCH_PARENT, 3);
        tv0.setText(getString(R.string.issueType));
        tv0.setTextColor(Color.BLACK);
        tv0.setTextSize(16f);
        tv0.setTypeface(tv0.getTypeface(), Typeface.BOLD);
        tv0.setGravity(Gravity.CENTER);
        tv0.setBackgroundResource(R.drawable.custom_heading_gridview);
        row0.addView(tv0, 0, layoutParams1);

        TextView tv1 = new TextView(activity);
        TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 4);
        layoutParams2.setMargins(-3,0,0,0);
        tv1.setText(getString(R.string.issueName));
        tv1.setTextColor(Color.BLACK);
        tv1.setTextSize(16f);
        tv1.setTypeface(tv0.getTypeface(), Typeface.BOLD);
        tv1.setGravity(Gravity.CENTER);
        tv1.setBackgroundResource(R.drawable.custom_heading_gridview);
        row0.addView(tv1, 1, layoutParams2);

        TextView tv2 = new TextView(activity);
        TableRow.LayoutParams layoutParams3 = new TableRow.LayoutParams(50, TableRow.LayoutParams.MATCH_PARENT, 2);
        layoutParams3.setMargins(-3,0,0,0);
        tv2.setText(getString(R.string.project));
        tv2.setTextColor(Color.BLACK);
        tv2.setTextSize(16f);
        tv2.setTypeface(tv0.getTypeface(), Typeface.BOLD);
        tv2.setGravity(Gravity.CENTER);
        tv2.setBackgroundResource(R.drawable.custom_heading_gridview);
        row0.addView(tv2, 2, layoutParams3);

        TextView tv3 = new TextView(activity);
        TableRow.LayoutParams layoutParams4 = new TableRow.LayoutParams(100, TableRow.LayoutParams.MATCH_PARENT, 3);
        layoutParams4.setMargins(-3,0,0,0);
        tv3.setText(getString(R.string.process2));
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
        TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(50, TableRow.LayoutParams.MATCH_PARENT, 3);
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
        TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(270, TableRow.LayoutParams.MATCH_PARENT, 4);
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

    @Override
    public void onStop() {
        super.onStop();
        PersonalStactisticFragment.instance.pieChart_personal_working_efficiency.getOnTouchListener().setLastHighlighted(null);
        PersonalStactisticFragment.instance.pieChart_personal_working_efficiency.highlightValue(null);
    }
}