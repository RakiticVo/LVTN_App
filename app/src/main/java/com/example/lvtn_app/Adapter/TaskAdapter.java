package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.Task;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.IssueDetailFragment;
import com.example.lvtn_app.View.Fragment.TaskDetailFragment;

import java.util.ArrayList;
import java.util.Objects;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Task> tasks_list;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public TaskAdapter(Context context, ArrayList<Task> tasks_list) {
        this.context = context;
        this.tasks_list = tasks_list;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_tasks, parent, false);
        return new TaskAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        holder.tv_task_name.setText(tasks_list.get(position).getTask_Name());
    }

    @Override
    public int getItemCount() {
        return tasks_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Khai báo
        TextView tv_task_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_task_name = itemView.findViewById(R.id.tv_task_name);

            tv_task_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferences = Objects.requireNonNull(context).getSharedPreferences("Task", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("task_id", tasks_list.get(getAdapterPosition()).getTask_ID() + "");
                    editor.putString("task_name", (tasks_list.get(getAdapterPosition()).getTask_Name()));
                    editor.putString("task_type", tasks_list.get(getAdapterPosition()).getTask_Type() + "");
                    editor.putString("task_description", (tasks_list.get(getAdapterPosition()).getTask_Description()));
                    editor.putString("task_start_date", (tasks_list.get(getAdapterPosition()).getTask_StartDate()));
                    editor.commit();
                    TaskDetailFragment dialog = new TaskDetailFragment();
                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "TaskDetailFragment");
                }
            });
        }
    }
}
