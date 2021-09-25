package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import com.example.lvtn_app.Model.Process;
import com.example.lvtn_app.Model.Task;
import com.example.lvtn_app.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends Fragment {
    //Khai báo
    RecyclerView recyclerViewDashBoard;
    DashboardItemAdapter dashboardItemAdapter;
    ArrayList<Task> toDo_list, inProgress_list, done_list;
    ArrayList<Process> processes;
    AppCompatActivity activity;

    static DashBoardFragment instance;

    public static DashBoardFragment getInstance() {
        return instance;
    }

    SharedPreferences sharedPreferences;
    String projectname;

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
        //Khai báo
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);

        instance = this;

        activity = (AppCompatActivity) view.getContext();

        sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("ProjectDetail",Context.MODE_PRIVATE);

        recyclerViewDashBoard = view.findViewById(R.id.recyclerViewDashBoard);

        //Tạo dữ liệu
        toDo_list = new ArrayList<>();
        inProgress_list = new ArrayList<>();
        done_list = new ArrayList<>();
        processes = new ArrayList<>();
        projectname = sharedPreferences.getString("project_name", "ABC");

        //ToDo_Task_List
        toDo_list.add(new Task(1, "Demo ứng dụng", "Todo", "", "Task", "27/09/2021", "Medium", "Chí Thiện", projectname));
        toDo_list.add(new Task(2, "Viết tài liệu thiết kế", "Todo", "", "Story", "15/09/2021", "High", "Thiện Võ", projectname));

        //InProgress_Task_List
        inProgress_list.add(new Task(1, "Thiết kế giao diện ứng dụng", "InProgress", "", "Task", "12/09/2021", "Medium", "Rakitic Võ", projectname));

        //Done_Task_List
        done_list.add(new Task(1, "Viết mô tả ứng dụng", "Done", "", "Story", "26/08/2021", "Medium", "Chí Thiện", projectname));
        done_list.add(new Task(2, "Viết lại mô tả ứng dụng", "Done", "", "Bug", "01/09/2021", "Low", "Võ Rakitic", projectname));
        done_list.add(new Task(3, "Thiết kế mô hình dữ liệu", "Done", "",  "Story", "09/09/2021", "High", "Thiện Võ", projectname));

        //ToDo_List
        processes.add(new Process(1, sharedPreferences.getString("project_name_txt", "Project_name"),"ToDo", toDo_list));

        //InProgress_List
        processes.add(new Process(2, sharedPreferences.getString("project_name_txt", "Project_name"),"InProgress", inProgress_list));

        //Done_List
        processes.add(new Process(3, sharedPreferences.getString("project_name_txt", "Project_name"),"Done", done_list));

        //set up
        recyclerViewDashBoard.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        dashboardItemAdapter = new DashboardItemAdapter(getContext(), processes);
        recyclerViewDashBoard.setAdapter(dashboardItemAdapter);

        recyclerViewDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().addToBackStack("IssueDetailFragment return Dashboard Fragment").replace(R.id.frame_main, new IssueDetailFragment()).commit();
            }
        });

        return view;
    }

    public void createIssue(String name, String process_type, String decription, String issue_type, String start_date, String priority, String assignee, String estimate_time, String creator){
        DashBoardFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int temp = 0;
                switch (process_type.toString()){
                    case "ToDo":
                        temp = (toDo_list.size() + 1);
                        toDo_list.add(new Task(temp, name, process_type, decription, issue_type, start_date, priority, assignee, estimate_time, creator, projectname, ""));
                        processes.get(0).setList(toDo_list);
                        Toast.makeText(getContext(), "Add success Todo - " + processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                        break;
                    case "InProgress":
                        temp = (inProgress_list.size() + 1);
                        inProgress_list.add(new Task(temp, name, process_type, decription, issue_type, start_date, priority, assignee, estimate_time, creator, projectname, ""));
                        processes.get(1).setList(inProgress_list);
                        Toast.makeText(getContext(), "Add success InProgress - "+ processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                        break;
                    case "Done":
                        temp = (done_list.size() + 1);
                        done_list.add(new Task(temp, name, process_type, decription, issue_type, start_date, priority, assignee, estimate_time, creator, projectname, ""));
                        processes.get(2).setList(done_list);
                        Toast.makeText(getContext(), "Add success Done - "+ processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                        break;
                }
                dashboardItemAdapter.notifyDataSetChanged();
            }
        });
    }
}