package com.example.lvtn_app.View.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lvtn_app.Adapter.CalendarTaskAdapter;
import com.example.lvtn_app.Adapter.TaskAdapter;
import com.example.lvtn_app.Controller.Method.DateFormat;
import com.example.lvtn_app.Model.Task;
import com.example.lvtn_app.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTasksFragment extends Fragment {
    //Khai báo
    ImageButton ibtn_add_tasks;
    TextView tv_day_number_calendar, tv_day_of_week_calendar;
    CompactCalendarView calendarView;
    RecyclerView recyclerViewTasks;
    TaskAdapter calendarTaskAdapter;
    ArrayList<Task> tasks, task_date;
    ArrayList<Event> events;
    String selectedDate = "";

    SharedPreferences sharedPreferences;

    DateFormat dateFormat;

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
        recyclerViewTasks = view.findViewById(R.id.recyclerViewTasks);

        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getContext()));

        dateFormat = new DateFormat();

        selectedDate = dateFormat.formatDate(Calendar.getInstance().getTime());

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("User", Context.MODE_PRIVATE);

        calendarView.setLocale(TimeZone.getDefault(), Locale.getDefault());
        calendarView.setUseThreeLetterAbbreviation(true);
        calendarView.shouldDrawIndicatorsBelowSelectedDays(true);

        //Set data
        tasks = new ArrayList<>();
        task_date = new ArrayList<>();
        events = new ArrayList<>();
        tasks.add(new Task(1, "Làm mô tả chi tiết", selectedDate));
        tasks.add(new Task(2, "Phân tích các chức năng và phi chức năng", "19/09/2021"));
        tasks.add(new Task(3, "Báo cáo tiến độ", "18/09/2021"));
        tasks.add(new Task(4, "Demo thử ứng dụng", selectedDate));

        setDay(Calendar.getInstance().getTime());

        for (Task task : tasks){
            if (task.getStart_date().equals(dateFormat.formatDate(Calendar.getInstance().getTime()))){
                events.add(new Event(R.color.purple, dateFormat.checkFormatDate(dateFormat.formatDate(Calendar.getInstance().getTime())).getTime(), task.getName()));
                task_date.add(task);
            }else{
                events.add(new Event(R.color.purple, dateFormat.checkFormatDate(task.getStart_date()).getTime(), task.getName()));
            }
        }
        if (events!=null){
            for (Event event : events){
                calendarView.removeEvent(event);
                calendarView.addEvent(event);
            }
        }

        calendarTaskAdapter = new TaskAdapter(getContext(), task_date);
        recyclerViewTasks.setAdapter(calendarTaskAdapter);

        //Bắt sự kiện
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
                setDay(dateClicked);
                selectedDate = dateFormat.formatDate(dateClicked);
//                setTaskInCalendar(selectedDate, tasks, task_date, events);
                task_date.clear();
                events.clear();
                for (Task task : tasks){
                    if (task.getStart_date().equals(selectedDate)){
                        events.add(new Event(R.color.purple, dateFormat.checkFormatDate(selectedDate).getTime(), task.getName()));
                        task_date.add(task);
                    }
                }
                if (events!=null){
                    for (Event event : events){
                        if (event.getTimeInMillis() == dateFormat.checkFormatDate(selectedDate).getTime()){
                            calendarView.removeEvent(event);
                            calendarView.addEvent(event);
                        }
                    }
                }
                calendarTaskAdapter.notifyDataSetChanged();
                recyclerViewTasks.setAdapter(calendarTaskAdapter);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });

        return view;
    }

    public void createTask(String name, String decription, String start_date, String assignee, String creator){
        calendarView.setCurrentDate(dateFormat.checkFormatDate(start_date));
        setDay(dateFormat.checkFormatDate(start_date));
        tasks.add(new Task(task_date.size()+1, name, decription, "Task", start_date, assignee, creator));
//        setTaskInCalendar(start_date, tasks, task_date, events);
        task_date.clear();
        events.clear();
        for (Task task : tasks){
            if (task.getStart_date().equals(start_date)){
                events.add(new Event(R.color.purple, dateFormat.checkFormatDate(start_date).getTime(), task.getName()));
                task_date.add(task);
            }
        }
        if (events!=null){
            for (Event event : events){
                if (event.getTimeInMillis() == dateFormat.checkFormatDate(start_date).getTime()){
                    calendarView.removeEvent(event);
                    calendarView.addEvent(event);
                }
            }
        }
        calendarTaskAdapter.notifyDataSetChanged();
        recyclerViewTasks.setAdapter(calendarTaskAdapter);
    }

    public void setDay(Date date){
        String dow = new SimpleDateFormat("EE", Locale.getDefault()).format(date);
        String day = new SimpleDateFormat("dd", Locale.getDefault()).format(date);
        tv_day_number_calendar.setText(day);
        tv_day_of_week_calendar.setText(dow);
//        Toast.makeText(getContext(), ""+ tv_day_of_week_calendar.getText().toString() +"-"+tv_day_number_calendar.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}