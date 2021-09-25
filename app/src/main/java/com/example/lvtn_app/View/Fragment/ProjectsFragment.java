package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.GroupChatAdapter;
import com.example.lvtn_app.Adapter.ProjectsAdapter;
import com.example.lvtn_app.Controller.Retrofit.ApiService;
import com.example.lvtn_app.Controller.Retrofit.ApiUtils;
import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsFragment extends Fragment {
    //Khai báo
    ImageButton ibtn_search, ibtn_add;
    TextInputLayout search_project_text_input_layout;
    TextView tvNoresult_project;
    RecyclerView recyclerViewProjects;
    ProjectsAdapter projectsAdapter;
    ArrayList<Project> projects, projects_search;

    SharedPreferences sharedPreferences_user, sharedPreferences_leader;
    SharedPreferences.Editor editor_leader;

    static ProjectsFragment instance;

    public static ProjectsFragment getInstance() {
        return instance;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectsFragment newInstance(String param1, String param2) {
        ProjectsFragment fragment = new ProjectsFragment();
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
        // Ánh xạ
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        ibtn_add = view.findViewById(R.id.ibtn_add_project);
        ibtn_search = view.findViewById(R.id.ibtn_search_project);
        search_project_text_input_layout = view.findViewById(R.id.search_project_text_input_layout);
        recyclerViewProjects = view.findViewById(R.id.recyclerViewProjects);
        tvNoresult_project = view.findViewById(R.id.tvNoresult_project);

        instance = this;

        sharedPreferences_user = Objects.requireNonNull(getContext()).getSharedPreferences("User", Context.MODE_PRIVATE);
        sharedPreferences_leader = Objects.requireNonNull(getContext()).getSharedPreferences("Leader", Context.MODE_PRIVATE);
        editor_leader = sharedPreferences_leader.edit();

        String user = sharedPreferences_user.getString("userName_txt", "");

        // Set data
        projects = new ArrayList<>();
        projects_search = new ArrayList<>();
        projects.add(new Project(1, "Scrum 1","", "20/12/2021", "Scrum", "25/09/2021", user, Color.CYAN));
        projects.add(new Project(2, "Test 1","", "20/12/2021", "Normal", "25/09/2021", user, Color.RED));
//        projects.add(new Project(3, "Test 2","", "20/12/2021", "Normal", "25/09/2021", user, Color.BLACK));
//        projects.add(new Project(4, "Kanban 1","", "20/12/2021", "Kanban", "25/09/2021", user, Color.WHITE));
//        projects.add(new Project(5, "Business 1","", "20/12/2021", "Bussiness", "25/09/2021", user, Color.GREEN));
//        projects.add(new Project(6, "Personal 1", "", "20/12/2021", "Personal", "25/09/2021", user, Color.YELLOW));
        recyclerViewProjects.setLayoutManager(new LinearLayoutManager(getContext()));
        projectsAdapter = new ProjectsAdapter(getContext(), projects);
        recyclerViewProjects.setAdapter(projectsAdapter);

        getProject();

        //Todo: get User is Leader of project to check permission of function
//        editor_leader.putString("name_leader", "Chí Thiện");
//        editor_leader.commit();

        // Bắt sự kiện
        ibtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateProjectFragment dialog = new CreateProjectFragment();
                dialog.show(getFragmentManager(), "CreateProjectFragment");
            }
        });

        search_project_text_input_layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    projects_search.clear();
                    for (Project project : projects){
//                        Toast.makeText(getContext(), "" + groupChat.getName().toLowerCase(), Toast.LENGTH_SHORT).show();
                        if (project.getProjectName().toLowerCase(Locale.ROOT).contains(s)){
                            projects_search.add(project);
                        }
                    }
                    if (projects_search.size()>0){
                        recyclerViewProjects.setVisibility(View.VISIBLE);
                        tvNoresult_project.setVisibility(View.GONE);
                        projectsAdapter = new ProjectsAdapter(getContext(), projects_search);
                        recyclerViewProjects.setAdapter(projectsAdapter);
                    }else {
                        recyclerViewProjects.setVisibility(View.GONE);
                        tvNoresult_project.setVisibility(View.VISIBLE);
                    }
                }else {
                    recyclerViewProjects.setVisibility(View.VISIBLE);
                    tvNoresult_project.setVisibility(View.GONE);
                    projectsAdapter = new ProjectsAdapter(getContext(), projects);
                    recyclerViewProjects.setAdapter(projectsAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public void createProject(String name, String decription, String estimate_finish_date, String type, String date_create, String creator, int background){
        ProjectsFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                int temp = (projects.size()) + 1;
//                projects.add(new Project(temp, name, decription, estimate_finish_date, type, date_create, creator, background));
//                projectsAdapter.notifyDataSetChanged();
//                search_project_text_input_layout.getEditText().setText("");
//                search_project_text_input_layout.getEditText().clearFocus();
//
                ApiService createNewProject = ApiUtils.connectRetrofit();
                createNewProject.isCreateNewProjectSuccess(name, decription, estimate_finish_date, type, date_create, creator, background).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e("TAG", "onResponse: " + call.toString());
                        Toast.makeText(getContext(), "ABC: " + response.body(), Toast.LENGTH_SHORT).show();
                        getProject();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }
    public void getProject(){
        projects.clear();
        ApiService getProject = ApiUtils.connectRetrofit();
        getProject.getProject().enqueue(new Callback<ArrayList<Project>>() {
            @Override
            public void onResponse(Call<ArrayList<Project>> call, Response<ArrayList<Project>> response) {
//                ArrayList<Project> temp = response.body();
//                Toast.makeText(getContext(), "Success" + temp.size(), Toast.LENGTH_SHORT).show();
                for (Project project : response.body()){
                    projects.add(new Project(project.getId_project(), project.getProjectName(), project.getProjectDescription(),
                            project.getProjectFinishDate(), project.getProjectType(), project.getProjectDateCreate(),
                            project.getProjectLeader(), project.getProjectBackground()));
                    Toast.makeText(getContext(),  "*" +project.getProjectName()+ "*", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), "" + project.getId_project() + "\n"
//                            + project.getProjectName() + "\n"
//                            + project.getProjectDescription() + "\n"
//                            + project.getProjectFinishDate() + "\n"
//                            + project.getProjectType() + "\n"
//                            + project.getProjectDateCreate() + "\n"
//                            + project.getProjectLeader() + "\n"
//                            + project.getProjectBackground() + "\n", Toast.LENGTH_SHORT).show();
                }
                projectsAdapter.notifyDataSetChanged();
                recyclerViewProjects.scrollToPosition(projects.size()-1);
                recyclerViewProjects.clearFocus();
            }

            @Override
            public void onFailure(Call<ArrayList<Project>> call, Throwable t) {
                Toast.makeText(getContext(), "" + call +"\n"+ t, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + call +"\n"+ t);
            }
        });
    }
}