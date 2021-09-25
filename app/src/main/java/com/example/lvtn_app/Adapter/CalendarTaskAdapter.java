package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.Task;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.MyTasksFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarTaskAdapter extends RecyclerView.Adapter<CalendarTaskAdapter.ViewHolder>{
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Task> calendar_tasks_list;
    private TaskAdapter taskAdapter;

    public CalendarTaskAdapter(Context context, ArrayList<Task> calendar_tasks_list) {
        this.context = context;
        this.calendar_tasks_list = calendar_tasks_list;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CalendarTaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_calendar_task, parent, false);
        return new CalendarTaskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarTaskAdapter.ViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = Calendar.getInstance().getTime();
        if (calendar_tasks_list.get(position).getStart_date() != null){
            try {
                date = sdf.parse(calendar_tasks_list.get(position).getStart_date());
                String dow = new SimpleDateFormat("EE", Locale.US).format(date);
                String day = new SimpleDateFormat("dd", Locale.US).format(date);
                holder.tv_day_number.setText(day);
                holder.tv_day_of_week.setText(dow);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

//        holder.recyclerViewTasks.setLayoutManager(new LinearLayoutManager(context));
//        taskAdapter = new TaskAdapter(context, calendar_tasks_list);
//        holder.recyclerViewTasks.setAdapter(taskAdapter);
    }

    @Override
    public int getItemCount() {
        return calendar_tasks_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Khai báo
        TextView tv_day_number, tv_day_of_week;
        RecyclerView recyclerViewTasks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Khai báo
            tv_day_number = itemView.findViewById(R.id.tv_day_number);
            tv_day_of_week = itemView.findViewById(R.id.tv_day_of_week);
            recyclerViewTasks = itemView.findViewById(R.id.recyclerViewTasks);

            //Set up
            recyclerViewTasks.setLayoutManager(new LinearLayoutManager(context));
            taskAdapter = new TaskAdapter(context, calendar_tasks_list);
            recyclerViewTasks.setAdapter(taskAdapter);
        }
    }
}
