package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.LoginAdapter;
import com.example.lvtn_app.Adapter.ProjectDetailAdapter;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectDetailFragment extends Fragment {
    //Khai báo
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView tv_project_name_details;
    ImageButton ibtn_back_project_detail, ibtn_info_project;
    float v=0;

    SharedPreferences sharedPreferences;

    DatabaseReference reference;
    //Project infomation
    String project_ID;
    String project_Name;
    String project_Description;
    String project_FinishDate;
    String project_Type;
    String project_DateCreate;
    String project_Leader;
    String project_Background;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProjectDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectDetailFragment newInstance(String param1, String param2) {
        ProjectDetailFragment fragment = new ProjectDetailFragment();
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
        //Declare
        View view = inflater.inflate(R.layout.fragment_project_detail, container, false);
        tabLayout = view.findViewById(R.id.project_detail_tabLayout);
        viewPager = view.findViewById(R.id.project_detail_viewPager);
        tv_project_name_details = view.findViewById(R.id.tv_project_name_details);
        ibtn_back_project_detail = view.findViewById(R.id.ibtn_back_project_detail);
        ibtn_info_project = view.findViewById(R.id.ibtn_info_project);

        //Set data
        sharedPreferences = requireContext().getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
        project_ID = sharedPreferences.getString("project_ID", "token");
//        Toast.makeText(getContext(), "" + project_ID, Toast.LENGTH_SHORT).show();
        if (!project_ID.equals("token")){
            reference = FirebaseDatabase.getInstance().getReference("Projects").child(project_ID);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Project project = snapshot.getValue(Project.class);
//                    Toast.makeText(getContext(), "" + (project == null), Toast.LENGTH_SHORT).show();
                    project_Name = project.getProject_Name();
                    project_Description = project.getProject_Description();
                    project_FinishDate = project.getProject_FinishDate();
                    project_Type = project.getProject_Type();
                    project_DateCreate = project.getProject_DateCreate();
                    project_Leader = project.getProject_Leader();
                    project_Background = project.getProject_Background();

                    tv_project_name_details.setText(project_Name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //Set up
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabTextColors(Color.BLACK, Color.BLACK);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ProjectDetailAdapter projectDetailAdapter = new ProjectDetailAdapter(getChildFragmentManager(), getContext(), tabLayout.getTabCount());
        viewPager.setAdapter(projectDetailAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Event Handling
        ibtn_back_project_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        ibtn_info_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectInfoFragment dialog = new ProjectInfoFragment();
                dialog.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "ProjectInfoFragment");
            }
        });

        return view;
    }
}