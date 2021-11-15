package com.example.lvtn_app.View.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.GridViewAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.Model.User_Issue_List;
import com.example.lvtn_app.R;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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
    RecyclerView recyclerViewIssuesCompleted;
    ImageButton ibtn_back_issue_complete;
    GridViewAdapter gridViewAdapter;
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

        recyclerViewIssuesCompleted = view.findViewById(R.id.recyclerViewIssuesCompleted);
        ibtn_back_issue_complete = view.findViewById(R.id.ibtn_back_issue_complete);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        dateFormat = new DateFormat();
        activity = (AppCompatActivity) getContext();

        final_list = new ArrayList<>();
        final_list.add(new User_Issue_List(" ", "Issue Name", "Process", "Issue Type", "Project", ""));
        GridLayoutManager manager = new GridLayoutManager(activity, 16);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 4 == 0){
                    return 3;
                }else if (position % 4 == 1){
                    return 6;
                }else if (position % 4 == 2){
                    return 3;
                }else return 4;
            }
        });
        recyclerViewIssuesCompleted.setLayoutManager(manager);
//        gridViewAdapter = new GridViewAdapter(activity, final_list);
////        recyclerViewIssues.setLayoutManager(manager);
//        recyclerViewIssuesCompleted.setAdapter(gridViewAdapter);

        Bundle bundle = getArguments();
        type = bundle.getString("type");

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

        ibtn_back_issue_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
        switch (type){
            case "Perfect Issues":
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
                gridViewAdapter = new GridViewAdapter(activity, final_list);
                recyclerViewIssuesCompleted.setAdapter(gridViewAdapter);
                recyclerViewIssuesCompleted.smoothScrollToPosition(final_list.size()*4 - 1);
                break;
            case "Delay Issues":
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
                gridViewAdapter = new GridViewAdapter(activity, final_list);
                recyclerViewIssuesCompleted.setAdapter(gridViewAdapter);
                recyclerViewIssuesCompleted.smoothScrollToPosition(final_list.size()*4 - 1);
                break;
        }
    }
}