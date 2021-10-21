package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.DashboardItemAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.Process;
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
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends Fragment {
    //Khai báo
    RecyclerView recyclerViewDashBoard;
    DashboardItemAdapter dashboardItemAdapter;
    ArrayList<Issue> issue_list, toDo_list, inProgress_list, done_list;
    ArrayList<Process> processes;
    AppCompatActivity activity;

    static DashBoardFragment instance;

    public static DashBoardFragment getInstance() {
        return instance;
    }

    DateFormat dateFormat = new DateFormat();
    SharedPreferences sharedPreferences;
    String project_ID;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference,reference2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);

        instance = this;

        activity = (AppCompatActivity) view.getContext();

        sharedPreferences = requireContext().getSharedPreferences("ProjectDetail",Context.MODE_PRIVATE);
        project_ID = sharedPreferences.getString("project_ID", "token");

        recyclerViewDashBoard = view.findViewById(R.id.recyclerViewDashBoard);

        issue_list = new ArrayList<>();
        toDo_list = new ArrayList<>();
        inProgress_list = new ArrayList<>();
        done_list = new ArrayList<>();
        processes = new ArrayList<>();

        //ToDo_Task_List
        toDo_list.add(new Issue("1", "Demo ứng dụng", "Todo", "", "Task", "27/09/2021", "Medium", "Chí Thiện", "1w", "Chí Thiện", project_ID, ""));
        toDo_list.add(new Issue("2", "Viết tài liệu thiết kế", "Todo", "", "Story", "15/09/2021", "High", "Thiện Võ", "1w", "Chí Thiện", project_ID, ""));

        //InProgress_Task_List
        inProgress_list.add(new Issue("1", "Thiết kế giao diện ứng dụng", "InProgress", "", "Task", "12/09/2021", "Medium", "Rakitic Võ", "1w", "Chí Thiện", project_ID, ""));

        //Done_Task_List
        done_list.add(new Issue("1", "Viết mô tả ứng dụng", "Done", "", "Story", "26/08/2021", "Medium", "Chí Thiện", "1w", "Chí Thiện", project_ID, ""));
        done_list.add(new Issue("2", "Viết lại mô tả ứng dụng", "Done", "", "Bug", "01/09/2021", "Low", "Võ Rakitic", "1w", "Chí Thiện", project_ID, ""));
        done_list.add(new Issue("3", "Thiết kế mô hình dữ liệu", "Done", "",  "Story", "09/09/2021", "High", "Thiện Võ", "1w", "Chí Thiện", project_ID, ""));

        //ToDo_List
        processes.add(new Process(1, sharedPreferences.getString("project_name_txt", "Project_name"),"ToDo", toDo_list));

        //InProgress_List
        processes.add(new Process(2, sharedPreferences.getString("project_name_txt", "Project_name"),"InProgress", inProgress_list));

        //Done_List
        processes.add(new Process(3, sharedPreferences.getString("project_name_txt", "Project_name"),"Done", done_list));

        recyclerViewDashBoard.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        dashboardItemAdapter = new DashboardItemAdapter(getContext(), processes);
        recyclerViewDashBoard.setAdapter(dashboardItemAdapter);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        getIssueList();

        //Bắt sự kiện
        // Todo: Xử lý sự kiện chuyển đến Issue Detail
        recyclerViewDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().addToBackStack("IssueDetailFragment return Dashboard Fragment").replace(R.id.frame_main, new IssueDetailFragment()).commit();
            }
        });

        return view;
    }

    public void getIssueList(){
        reference = FirebaseDatabase.getInstance().getReference("Issues").child(project_ID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                issue_list.clear();
                toDo_list.clear();
                inProgress_list.clear();
                done_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Issue issue = dataSnapshot.getValue(Issue.class);
                    if (issue.getIssue_project_ID().equals(project_ID)){
                        issue_list.add(issue);
                    }
                }
                if (issue_list.size() > 0){
                    addIssueintoProcess(issue_list);
                }else {
                    issue_list.clear();
                    toDo_list.clear();
                    inProgress_list.clear();
                    done_list.clear();
                    dashboardItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addIssueintoProcess(ArrayList<Issue> issue_list){
        toDo_list.clear();
        inProgress_list.clear();
        done_list.clear();
        for (Issue issue : issue_list){
            switch (issue.getIssue_ProcessType().toString()){
                case "ToDo":
                    toDo_list.add(issue);
                    processes.get(0).setList(toDo_list);
//                Toast.makeText(getContext(), "Add success Todo - " + processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                    break;
                case "InProgress":
                    inProgress_list.add(issue);
                    processes.get(1).setList(inProgress_list);
//                Toast.makeText(getContext(), "Add success InProgress - "+ processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                    break;
                case "Done":
                    done_list.add(issue);
                    processes.get(2).setList(done_list);
//                Toast.makeText(getContext(), "Add success Done - "+ processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                    break;
            }
            dashboardItemAdapter.notifyDataSetChanged();
            recyclerViewDashBoard.scrollToPosition(0);
            recyclerViewDashBoard.clearFocus();
            if (toDo_list.size() > 0){
                for (int i = 0; i < toDo_list.size(); i++) {
                    try {
                        Date date = dateFormat.sdf.parse(toDo_list.get(i).getIssue_StartDate());
                        String s = toDo_list.get(i).getIssue_ID();
                        if (date.getTime() < Calendar.getInstance().getTime().getTime()){
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Issues").child(project_ID).child(s);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("issue_ProcessType", "InProgress");
                            databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        getIssueList();
                                        dashboardItemAdapter.notifyDataSetChanged();
                                        recyclerViewDashBoard.scrollToPosition(0);
                                        recyclerViewDashBoard.clearFocus();
                                    }
                                }
                            });
                            toDo_list.remove(i);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            dashboardItemAdapter.notifyDataSetChanged();
            recyclerViewDashBoard.scrollToPosition(0);
            recyclerViewDashBoard.clearFocus();
        }
        dashboardItemAdapter.notifyDataSetChanged();
        recyclerViewDashBoard.scrollToPosition(0);
        recyclerViewDashBoard.clearFocus();
    }
}