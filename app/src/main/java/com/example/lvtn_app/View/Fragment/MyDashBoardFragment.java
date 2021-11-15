package com.example.lvtn_app.View.Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.GridViewAdapter;
import com.example.lvtn_app.Adapter.ProcessTypeAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.Process;
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
    RecyclerView recyclerViewIssues;
    ArrayList<User_Issue_List> issue_list, final_list, temp_list;
    AppCompatActivity activity;
    GridViewAdapter gridViewAdapter;
    Spinner spinner_issue_process_personal_dashoard;
    ArrayList<ProcessType> processType_list;
    ProcessTypeAdapter processTypeAdapter;

    DateFormat dateFormat;
    ProgressDialog progressDialog;

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
        recyclerViewIssues = view.findViewById(R.id.recyclerViewIssues);
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
        GridLayoutManager manager = new GridLayoutManager(activity, 15);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 4 == 0){
                    return 2;
                }else if (position % 4 == 1){
                    return 6;
                }else if (position % 4 == 2){
                    return 4;
                }else return 3;
            }
        });
        recyclerViewIssues.setLayoutManager(manager);
        gridViewAdapter = new GridViewAdapter(activity, final_list);
//        recyclerViewIssues.setLayoutManager(manager);
        recyclerViewIssues.setAdapter(gridViewAdapter);

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Waiting!!!");
        progressDialog.show();

        issue_list.clear();
        final_list.clear();
        getAllIssue();

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
        gridViewAdapter = new GridViewAdapter(activity, final_list);
        recyclerViewIssues.setAdapter(gridViewAdapter);
        if (final_list.size() > 0){
            recyclerViewIssues.smoothScrollToPosition(final_list.size()*4 - 1);
        }
        updateIssue(issues);
        temp_list.addAll(issues);
    }

    public void showIssuesByProcessType(ArrayList<User_Issue_List> issues, String type){
        ArrayList<User_Issue_List> list = new ArrayList<>();
        final_list.clear();
        switch (type) {
            case "My All Issues":
                final_list.clear();
//                final_list.add(new User_Issue_List(" ", "Issue Name", "Process", "Issue Type", "Project", ""));
                final_list.addAll(issues);
                gridViewAdapter = new GridViewAdapter(activity, final_list);
                recyclerViewIssues.setAdapter(gridViewAdapter);
                recyclerViewIssues.scrollToPosition(final_list.size()*4 - 1);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewIssues.smoothScrollToPosition(0);
                        progressDialog.dismiss();
                    }
                }, 100L * 2 * final_list.size());
//                Toast.makeText(activity, "" + final_list.get(0).getIssue_Type() + "\n"
//                        + final_list.get(0).getIssue_Name() + "\n"
//                        + final_list.get(0).getProject_ID() + "\n"
//                        + final_list.get(0).getIssue_ProcessType() + "\n", Toast.LENGTH_SHORT).show();
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
                gridViewAdapter = new GridViewAdapter(activity, final_list);
                recyclerViewIssues.setAdapter(gridViewAdapter);
                if (final_list.size() > 0){
                    recyclerViewIssues.smoothScrollToPosition(final_list.size()*4 - 1);
                }
//                Toast.makeText(activity, "" + final_list.get(0).getIssue_Type() + "\n"
//                        + final_list.get(0).getIssue_Name() + "\n"
//                        + final_list.get(0).getProject_ID() + "\n"
//                        + final_list.get(0).getIssue_ProcessType() + "\n", Toast.LENGTH_SHORT).show();
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
                gridViewAdapter = new GridViewAdapter(activity, final_list);
                recyclerViewIssues.setAdapter(gridViewAdapter);
                if (final_list.size() > 0){
                    recyclerViewIssues.smoothScrollToPosition(final_list.size()*4 - 1);
                }
//                Toast.makeText(activity, "" + final_list.get(0).getIssue_Type() + "\n"
//                        + final_list.get(0).getIssue_Name() + "\n"
//                        + final_list.get(0).getProject_ID() + "\n"
//                        + final_list.get(0).getIssue_ProcessType() + "\n", Toast.LENGTH_SHORT).show();
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
                gridViewAdapter = new GridViewAdapter(activity, final_list);
                recyclerViewIssues.setAdapter(gridViewAdapter);
                if (final_list.size() > 0){
                    recyclerViewIssues.smoothScrollToPosition(final_list.size()*4 - 1);
                }
//                Toast.makeText(activity, "" + gridViewAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
//                for (User_Issue_List list1 : final_list){
//                    Toast.makeText(activity, "" + list1.getIssue_Type() + "\n"
//                            + list1.getIssue_Name() + "\n"
//                            + list1.getProject_ID() + "\n"
//                            + list1.getIssue_ProcessType() + "\n", Toast.LENGTH_SHORT).show();
//                }
                break;
        }
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