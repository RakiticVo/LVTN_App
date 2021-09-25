package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lvtn_app.Model.Task;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.CreateIssueFragment;
import com.example.lvtn_app.View.Fragment.IssueDetailFragment;

import java.util.ArrayList;
import java.util.Objects;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder>{
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Task> task_list;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public IssueAdapter(Context context, ArrayList<Task> task_list) {
        this.context = context;
        this.task_list = task_list;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public IssueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_issue, parent, false);
        return new IssueAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueAdapter.ViewHolder holder, int position) {
        holder.tv_name_issue.setText(task_list.get(position).getName());
        holder.tv_name_assignee.setText(task_list.get(position).getAssignee());
        holder.tv_start_date_issue.setText(task_list.get(position).getStart_date());
        switch (task_list.get(position).getIssue_type()){
            case "Task":
                holder.img_issue_type.setImageResource(R.drawable.task);
                break;
            case "Bug":
                holder.img_issue_type.setImageResource(R.drawable.bug);
                break;
            case "Story":
                holder.img_issue_type.setImageResource(R.drawable.user_story);
                break;
        }
        switch (task_list.get(position).getPriority()){
            case "High":
                holder.img_priority_issue.setImageResource(R.drawable.high);
                break;
            case "Medium":
                holder.img_priority_issue.setImageResource(R.drawable.medium);
                break;
            case "Low":
                holder.img_priority_issue.setImageResource(R.drawable.low);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return task_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Khai báo
        TextView tv_name_issue, tv_name_assignee, tv_start_date_issue;
        ImageView img_issue_type, img_priority_issue;
        ConstraintLayout item_issue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name_issue = itemView.findViewById(R.id.tv_name_issue);
            tv_name_assignee = itemView.findViewById(R.id.tv_name_assignee);
            tv_start_date_issue = itemView.findViewById(R.id.tv_start_date_issue);
            img_issue_type = itemView.findViewById(R.id.img_issue_type);
            img_priority_issue = itemView.findViewById(R.id.img_priority_issue);
            item_issue = itemView.findViewById(R.id.item_issue);

            item_issue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferences = Objects.requireNonNull(context).getSharedPreferences("Task", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putInt("task_id", task_list.get(getAdapterPosition()).getId());
                    editor.putString("task_name", task_list.get(getAdapterPosition()).getName());
                    editor.putString("task_name_assignee", task_list.get(getAdapterPosition()).getAssignee());
                    editor.putString("task_start_date", task_list.get(getAdapterPosition()).getStart_date());
                    editor.putString("task_type", task_list.get(getAdapterPosition()).getIssue_type());
                    editor.putString("task_priority", task_list.get(getAdapterPosition()).getPriority());
                    editor.commit();
                    IssueDetailFragment dialog = new IssueDetailFragment();
                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "IssueDetailFragment");
                }
            });
        }
    }
}
