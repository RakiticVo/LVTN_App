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
import com.example.lvtn_app.Controller.Retrofit.ApiService;
import com.example.lvtn_app.Controller.Retrofit.ApiUtils;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.Process;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.R;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends Fragment {
    //Khai báo
    RecyclerView recyclerViewDashBoard;
    DashboardItemAdapter dashboardItemAdapter;
    ArrayList<Issue> toDo_list, inProgress_list, done_list;
    ArrayList<Process> processes;
    AppCompatActivity activity;

    static DashBoardFragment instance;

    public static DashBoardFragment getInstance() {
        return instance;
    }

    SharedPreferences sharedPreferences;
    int project_id;

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

        sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("ProjectDetail",Context.MODE_PRIVATE);

        recyclerViewDashBoard = view.findViewById(R.id.recyclerViewDashBoard);

        toDo_list = new ArrayList<>();
        inProgress_list = new ArrayList<>();
        done_list = new ArrayList<>();
        processes = new ArrayList<>();
        project_id = sharedPreferences.getInt("id_project", -1);

        //ToDo_Task_List
        toDo_list.add(new Issue(1, "Demo ứng dụng", "Todo", "", "Task", "27/09/2021", "Medium", "Chí Thiện", "1w", "Chí Thiện", project_id, ""));
        toDo_list.add(new Issue(2, "Viết tài liệu thiết kế", "Todo", "", "Story", "15/09/2021", "High", "Thiện Võ", "1w", "Chí Thiện", project_id, ""));

        //InProgress_Task_List
        inProgress_list.add(new Issue(1, "Thiết kế giao diện ứng dụng", "InProgress", "", "Task", "12/09/2021", "Medium", "Rakitic Võ", "1w", "Chí Thiện", project_id, ""));

        //Done_Task_List
        done_list.add(new Issue(1, "Viết mô tả ứng dụng", "Done", "", "Story", "26/08/2021", "Medium", "Chí Thiện", "1w", "Chí Thiện", project_id, ""));
        done_list.add(new Issue(2, "Viết lại mô tả ứng dụng", "Done", "", "Bug", "01/09/2021", "Low", "Võ Rakitic", "1w", "Chí Thiện", project_id, ""));
        done_list.add(new Issue(3, "Thiết kế mô hình dữ liệu", "Done", "",  "Story", "09/09/2021", "High", "Thiện Võ", "1w", "Chí Thiện", project_id, ""));

        //ToDo_List
        processes.add(new Process(1, sharedPreferences.getString("project_name_txt", "Project_name"),"ToDo", toDo_list));

        //InProgress_List
        processes.add(new Process(2, sharedPreferences.getString("project_name_txt", "Project_name"),"InProgress", inProgress_list));

        //Done_List
        processes.add(new Process(3, sharedPreferences.getString("project_name_txt", "Project_name"),"Done", done_list));

        recyclerViewDashBoard.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        dashboardItemAdapter = new DashboardItemAdapter(getContext(), processes);
        recyclerViewDashBoard.setAdapter(dashboardItemAdapter);

        getIssueListByProject();

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

    //Todo: Hàm tạo một Issue theo Process
    // - Gọi Api Serivice để thêm một Issue trên databse ----- (Incomplete)
    // - Khi tạo thành công một Issue sẽ lưu vào process list
    // tương ứng vs process type của Issue đó(mâc định là todo_list) ----- (Done)

    public void createIssue(String issueName, String issueProjectType, String issueDecription, String issueType, String issueStartDate, String issuePriority, String issueAssignee, String issueEstimateTime, String issueCreator, int issueProjectID, String issueFinishDate){
        DashBoardFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                int temp = (projects.size()) + 1;
//                projects.add(new Project(temp, name, decription, estimate_finish_date, type, date_create, creator, background));
//                projectsAdapter.notifyDataSetChanged();
//                search_project_text_input_layout.getEditText().setText("");
//                search_project_text_input_layout.getEditText().clearFocus();
//
                ApiService createNewIssue = ApiUtils.connectRetrofit();
                createNewIssue.isCreateNewIssueSuccess(issueName, issueProjectType, issueDecription, issueType, issueStartDate, issuePriority,
                                                        issueAssignee, issueEstimateTime, issueCreator, issueProjectID, issueFinishDate)
                        .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e("TAG1", "onResponse: " + response.body().toString());
                        Toast.makeText(getContext(), "Create " + response.body().toLowerCase().toString(), Toast.LENGTH_SHORT).show();
                        AddIssueToProcess(issueName, issueProjectType, issueDecription, issueType, issueStartDate, issuePriority,
                                issueAssignee, issueEstimateTime, issueCreator, issueProjectID, issueFinishDate);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("TAG1", "onResponse: " + t.getMessage());
                    }
                });
            }
        });
    }

    public void getIssueListByProject(){
        toDo_list.clear();
        inProgress_list.clear();
        done_list.clear();
        ApiService getIssueListByProject = ApiUtils.connectRetrofit();
        if (project_id > 0) {
//            Toast.makeText(getContext(), "" + sharedPreferences_user.getString("userName_txt", ""), Toast.LENGTH_SHORT).show();
            getIssueListByProject.getIssueListbyProject(project_id).enqueue(new Callback<ArrayList<Issue>>() {
                @Override
                public void onResponse(Call<ArrayList<Issue>> call, Response<ArrayList<Issue>> response) {
                    for (Issue issue : response.body()) {
                        AddIssueToProcess(issue.getIssueName(), issue.getIssueProjectType(), issue.getIssueDecription(), issue.getIssueType(),
                                issue.getIssueStartDate(), issue.getIssuePriority(), issue.getIssueAssignee(), issue.getIssueEstimateTime(),
                                issue.getIssueCreator(), issue.getIssueProjectID(), issue.getIssueFinishDate());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Issue>> call, Throwable t) {
                    toDo_list.clear();
                    inProgress_list.clear();
                    done_list.clear();
                    dashboardItemAdapter.notifyDataSetChanged();
//                    Toast.makeText(getContext(), "Saiiiiiiiiiiiiiii" + call + "\n" + t, Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "onFailure: " + call + "\n" + t);
                }
            });
        }
    }

    public void AddIssueToProcess(String issueName, String issueProjectType, String issueDecription, String issueType, String issueStartDate, String issuePriority, String issueAssignee, String issueEstimateTime, String issueCreator, int issueProjectID, String issueFinishDate){
        int temp = 0;
        switch (issueProjectType.toString()){
            case "ToDo":
                temp = (toDo_list.size() + 1);
                toDo_list.add(new Issue(temp, issueName, issueProjectType, issueDecription, issueType, issueStartDate, issuePriority, issueAssignee, issueEstimateTime, issueCreator, issueProjectID, issueFinishDate));
                processes.get(0).setList(toDo_list);
//                Toast.makeText(getContext(), "Add success Todo - " + processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                break;
            case "InProgress":
                temp = (inProgress_list.size() + 1);
                inProgress_list.add(new Issue(temp, issueName, issueProjectType, issueDecription, issueType, issueStartDate, issuePriority, issueAssignee, issueEstimateTime, issueCreator, issueProjectID, issueFinishDate));
                processes.get(1).setList(inProgress_list);
//                Toast.makeText(getContext(), "Add success InProgress - "+ processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                break;
            case "Done":
                temp = (done_list.size() + 1);
                done_list.add(new Issue(temp, issueName, issueProjectType, issueDecription, issueType, issueStartDate, issuePriority, issueAssignee, issueEstimateTime, issueCreator, issueProjectID, issueFinishDate));
                processes.get(2).setList(done_list);
//                Toast.makeText(getContext(), "Add success Done - "+ processes.get(0).getProject_name(), Toast.LENGTH_SHORT).show();
                break;
        }
        dashboardItemAdapter.notifyDataSetChanged();
        recyclerViewDashBoard.scrollToPosition(0);
        recyclerViewDashBoard.clearFocus();
    }
}