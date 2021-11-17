package com.example.lvtn_app.View.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.MyTasksAdapter;
import com.example.lvtn_app.Adapter.ProjectDetailAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTasksFragment extends Fragment {
    //Khai báo
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageButton ibtn_add_task;
    String selectedDate;
    DateFormat dateFormat;
    float v=0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyTasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyTasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyTasksFragment newInstance(String param1, String param2) {
        MyTasksFragment fragment = new MyTasksFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_tasks, container, false);
        tabLayout = view.findViewById(R.id.my_tasks_tabLayout);
        viewPager = view.findViewById(R.id.my_tasks_viewPager);
        ibtn_add_task = view.findViewById(R.id.ibtn_add_task);

        dateFormat = new DateFormat();
        selectedDate = dateFormat.formatDate(Calendar.getInstance().getTime());

        Bundle bundle = getArguments();
        int tab = 0;
        if (bundle != null){
            tab = bundle.getInt("schedule");
        }

        //Set up
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabTextColors(Color.BLACK, Color.BLACK);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final MyTasksAdapter myTasksAdapter = new MyTasksAdapter(getChildFragmentManager(), getContext(), tabLayout.getTabCount());
        viewPager.setAdapter(myTasksAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        if (tab == 1){
            viewPager.setCurrentItem(tab);
            tab = 0;
        }

        ibtn_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewPersonalTaskFragment createNewPersonalTaskFragment = new CreateNewPersonalTaskFragment();
                Bundle bundle = new Bundle();
                bundle.putString("selectedDate", selectedDate);
                createNewPersonalTaskFragment.setArguments(bundle);
                createNewPersonalTaskFragment.show(getFragmentManager(), "CreateNewPersonalTaskFragment");
            }
        });

        return view;
    }
}