package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.ProjectsAdapter;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.Project_Users;
import com.example.lvtn_app.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

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

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference, reference2;
    AppCompatActivity activity;

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

        projects = new ArrayList<>();
        projects_search = new ArrayList<>();
        projects.add(new Project("1", "Scrum 1","", "20/12/2021", "Scrum", "25/09/2021", "Chí Thiện", Color.CYAN + ""));
        projects.add(new Project("2", "Test 1","", "20/12/2021", "Normal", "25/09/2021", "Chí Thiện", Color.RED + ""));
        recyclerViewProjects.setLayoutManager(new LinearLayoutManager(getContext()));
        projectsAdapter = new ProjectsAdapter(getContext(), projects);
        recyclerViewProjects.setAdapter(projectsAdapter);

        activity = (AppCompatActivity) getContext();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        projects.clear();
        getUserAndshowProjectList();

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
                        if (project.getProject_Name().toLowerCase(Locale.ROOT).contains(s)){
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

    public void getUserAndshowProjectList(){
        reference = FirebaseDatabase.getInstance().getReference("User_List_By_Project");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    reference2 = FirebaseDatabase.getInstance().getReference("User_List_By_Project").child(dataSnapshot.getKey());
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String temp = "";
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                Project_Users users = dataSnapshot1.getValue(Project_Users.class);
                                if (users.getUser_ID().equals(firebaseUser.getUid())){
                                    temp = users.getProject_ID();
                                }
                            }
                            Toast.makeText(activity, "" + temp, Toast.LENGTH_SHORT).show();
                            getProjectByUser(temp);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getProjectByUser(String projectid){
        projects.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Projects").child(projectid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Project project = snapshot.getValue(Project.class);
                projects.add(project);
                projectsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        projectsAdapter.notifyDataSetChanged();
    }
}