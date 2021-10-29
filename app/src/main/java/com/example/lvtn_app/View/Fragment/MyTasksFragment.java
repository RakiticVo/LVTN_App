package com.example.lvtn_app.View.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.TaskAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.Task;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTasksFragment extends Fragment {
    //Khai báo
    ImageButton ibtn_add_tasks;
    TextView tv_day_number_calendar, tv_day_of_week_calendar, tv_today;
    CompactCalendarView calendarView;
    RecyclerView recyclerViewTasks;
    TaskAdapter taskAdapter;
    ArrayList<Task> tasks, daily_tasks;
    ArrayList<Event> events;
    ArrayList<Issue> issue_list;

    String selectedDate = "";
    Date date = new Date();
    String today = "";

    AppCompatActivity activity;
    SharedPreferences sharedPreferences_user;
    String username = "";
    SharedPreferences sharedPreferences;
    String project_ID;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    DateFormat dateFormat;
    Calendar myCalendar = Calendar.getInstance();

    static MyTasksFragment instance;

    public static MyTasksFragment getInstance() {
        return instance;
    }


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
     * @return A new instance of fragment PersonalTaskFragment.
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
       //Set up
        View view = inflater.inflate(R.layout.fragment_my_tasks, container, false);

        instance = this;

        ibtn_add_tasks = view.findViewById(R.id.ibtn_add_task);
        calendarView = view.findViewById(R.id.calendarView);
        tv_day_number_calendar = view.findViewById(R.id.tv_day_number_calendar);
        tv_day_of_week_calendar = view.findViewById(R.id.tv_day_of_week_calendar);
        tv_today = view.findViewById(R.id.tv_today);
        recyclerViewTasks = view.findViewById(R.id.recyclerViewTasks);

        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getContext()));

        dateFormat = new DateFormat();

        selectedDate = dateFormat.formatDate(Calendar.getInstance().getTime());

        sharedPreferences_user = requireContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        username = sharedPreferences_user.getString("userName_txt", "abc");

        sharedPreferences = requireContext().getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
        project_ID = sharedPreferences.getString("project_ID", "token");

        calendarView.setLocale(TimeZone.getDefault(), Locale.getDefault());
        calendarView.setUseThreeLetterAbbreviation(true);
        calendarView.shouldDrawIndicatorsBelowSelectedDays(true);

        activity = (AppCompatActivity) getContext();
        issue_list = new ArrayList<>();
        tasks = new ArrayList<>();
        daily_tasks = new ArrayList<>();
        events = new ArrayList<>();
        tasks.add(new Task("1", "Làm mô tả chi tiết", "", selectedDate, username, "Tasks"));
        tasks.add(new Task("2", "Phân tích các chức năng và phi chức năng", "", "19/09/2021", username, "Tasks"));
        tasks.add(new Task("3", "Báo cáo tiến độ", "", "18/09/2021", username, "Tasks"));
        tasks.add(new Task("4", "Demo thử ứng dụng", "", selectedDate, username, "Tasks"));

        today = new SimpleDateFormat("EE, dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        tv_today.setText(today);
        setDay(Calendar.getInstance().getTime());

        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                today = new SimpleDateFormat("EE, dd/MM/yyyy", Locale.getDefault()).format(myCalendar.getTime());
                tv_today.setText(today);
                try {
                    date = dateFormat.sdf.parse(tv_today.getText().toString());
                    calendarView.setCurrentDate(date);
                    setDay(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        taskAdapter = new TaskAdapter(getContext(), daily_tasks);
        recyclerViewTasks.setAdapter(taskAdapter);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        getTaskByDate(selectedDate);

        //Bắt sự kiện
        tv_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ibtn_add_tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewPersonalTaskFragment createNewPersonalTaskFragment = new CreateNewPersonalTaskFragment();
                Bundle bundle = new Bundle();
                bundle.putString("selectedDate", selectedDate);
                createNewPersonalTaskFragment.setArguments(bundle);
                createNewPersonalTaskFragment.show(getFragmentManager(), "CreateNewPersonalTaskFragment");
            }
        });

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                today = new SimpleDateFormat("EE, dd/MM/yyyy", Locale.getDefault()).format(dateClicked);
                tv_today.setText(today);
                daily_tasks.clear();
                events.clear();
                setDay(dateClicked);
                selectedDate = dateFormat.formatDate(dateClicked);
                try {
                    date = dateFormat.sdf.parse(selectedDate);
                    //                setTaskInCalendar(selectedDate, tasks, task_date, events);
                    for (Task issue : tasks){
                        if (issue.getTask_StartDate().equals(selectedDate)){
                            events.add(new Event(Color.RED, date.getTime(), issue.getTask_Name()));
                            daily_tasks.add(issue);
                            taskAdapter.notifyDataSetChanged();
                        }
                    }

                    if (events!=null){
                        calendarView.getEvents(date).clear();
                        for (Event event : events){
                            if (event.getTimeInMillis() == date.getTime()){
                                calendarView.addEvent(event);
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });

        return view;
    }

    public void setDay(Date date){
        String dow = new SimpleDateFormat("EE", Locale.getDefault()).format(date);
        String day = new SimpleDateFormat("dd", Locale.getDefault()).format(date);
        tv_day_number_calendar.setText(day);
        tv_day_of_week_calendar.setText(dow);
//        Toast.makeText(getContext(), ""+ tv_day_of_week_calendar.getText().toString() +"-"+tv_day_number_calendar.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    public void getTaskByDate(String today){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Waiting!!!");
        reference = FirebaseDatabase.getInstance().getReference("Tasks").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.show();
                tasks.clear();
                daily_tasks.clear();
                events.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Task task = dataSnapshot.getValue(Task.class);
//                    Toast.makeText(getContext(), "" + task.getTask_ID(), Toast.LENGTH_SHORT).show();
                    if (task.getTask_Creator().equals(firebaseUser.getUid())){
                        tasks.add(task);
                    }
                }
                getProject(tasks, today);
                setEventsInCalendar(tasks, today);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getProject(ArrayList<Task> tasks, String today){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Issues");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String key = dataSnapshot.getKey();
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Projects").child(key);
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Project project = snapshot.getValue(Project.class);
                            getIssueList(tasks, today, project.getProject_Name(), project.getProject_ID());
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

    public void getIssueList(ArrayList<Task> tasks, String today, String projectName, String IDproject){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
//                Toast.makeText(activity, "" + project_ID, Toast.LENGTH_SHORT).show();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Issues").child(IDproject);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        issue_list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Issue issue = dataSnapshot.getValue(Issue.class);
                            if (issue.getIssue_Assignee().equals(user.getUser_Name())
                                    && issue.getIssue_ProcessType().toLowerCase().equals("todo")){
                                issue_list.add(issue);
                            }
                        }
                        if (issue_list.size() > 0){
                            for (Issue issue : issue_list){
                                String task_issue_name = issue.getIssue_Name() + " - " + projectName;
//                                Toast.makeText(activity, "" + issueName, Toast.LENGTH_SHORT).show();
                                tasks.add(new Task(issue.getIssue_ID(),
                                        task_issue_name,
                                        issue.getIssue_Description(),
                                        issue.getIssue_StartDate(),
                                        issue.getIssue_Creator(),
                                        issue.getIssue_Type()));
                            }
                            setEventsInCalendar(tasks, today);
                        }else {
                            issue_list.clear();
                        }
//                        Toast.makeText(activity, "" + tasks.size(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setEventsInCalendar(ArrayList<Task> tasks, String today){
        daily_tasks.clear();
        events.clear();
        for (Task task : tasks){
            try {
                date = dateFormat.sdf.parse(task.getTask_StartDate());
                events.add(new Event(Color.RED, date.getTime(), task.getTask_Name()));
                if (task.getTask_StartDate().equals(today)){
                    daily_tasks.add(task);
                }
                taskAdapter.notifyDataSetChanged();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (events!=null){
            calendarView.getEvents(date).clear();
            for (Event event : events){
                calendarView.removeEvent(event);
                calendarView.addEvent(event);
            }
        }
    }
}