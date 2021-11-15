package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.User_Issue_List;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.IssueDetailFragment;
import com.example.lvtn_app.View.Fragment.TaskDetailFragment;
import com.example.lvtn_app.View.NotificationMessage.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<User_Issue_List> user_issue_lists;

    public GridViewAdapter(Context context, ArrayList<User_Issue_List> user_issue_lists) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.user_issue_lists = user_issue_lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_gridview, parent, false);
        return new GridViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int p = (position/4) - 1;
        if (position == 0){
            holder.textView.setBackgroundResource(R.drawable.custom_heading_gridview);
            holder.textView.setText("Issue Type");
            holder.textView.setPadding(5, 5, 5, 5);
            holder.textView.setGravity(Gravity.CENTER);
            holder.textView.setTypeface(holder.textView.getTypeface(), Typeface.BOLD);
            holder.imageView.setVisibility(View.GONE);
        }else if (position == 1){
            holder.textView.setBackgroundResource(R.drawable.custom_heading_gridview);
            holder.textView.setText("Issue Name");
            holder.textView.setPadding(5, 5, 5, 5);
            holder.textView.setGravity(Gravity.CENTER);
            holder.textView.setTypeface(holder.textView.getTypeface(), Typeface.BOLD);
            holder.imageView.setVisibility(View.GONE);
        }else if (position == 2){
            holder.textView.setBackgroundResource(R.drawable.custom_heading_gridview);
            holder.textView.setText("Project");
            holder.textView.setPadding(5, 5, 5, 5);
            holder.textView.setGravity(Gravity.CENTER);
            holder.textView.setTypeface(holder.textView.getTypeface(), Typeface.BOLD);
            holder.imageView.setVisibility(View.GONE);
        }else if (position == 3){
            holder.textView.setBackgroundResource(R.drawable.custom_heading_gridview);
            holder.textView.setText("Process");
            holder.textView.setPadding(5, 5, 5, 5);
            holder.textView.setGravity(Gravity.CENTER);
            holder.textView.setTypeface(holder.textView.getTypeface(), Typeface.BOLD);
            holder.imageView.setVisibility(View.GONE);
        }else if (position >= 4){
            if (position % 4 == 0){
                String issueType = user_issue_lists.get(p).getIssue_Type();
                switch (issueType){
                    case "Task":
                        holder.imageView.setImageResource(R.drawable.task);
                        break;
                    case "Bug":
                        holder.imageView.setImageResource(R.drawable.bug);
                        break;
                    case "Story":
                        holder.imageView.setImageResource(R.drawable.user_story);
                        break;
                }
                holder.imageView.setPadding(5, 5, 5, 5);
                holder.textView.setVisibility(View.GONE);
            }else if (position % 4 == 1){
                holder.textView.setText(user_issue_lists.get(p).getIssue_Name());
                holder.textView.setGravity(Gravity.CENTER_VERTICAL);
                holder.textView.setPadding(8, 5, 5, 5);
                holder.imageView.setVisibility(View.GONE);
            }else if (position % 4 == 2){
                String s = user_issue_lists.get(p).getProject_ID();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Projects").child(s);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Project project = snapshot.getValue(Project.class);
                        holder.textView.setText(project.getProject_Name());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                holder.textView.setPadding(5, 5, 5, 5);
                holder.textView.setGravity(Gravity.CENTER);
                holder.imageView.setVisibility(View.GONE);
            }else if (position % 4 == 3) {
                String process = user_issue_lists.get(p).getIssue_ProcessType();
                switch (process){
                    case "ToDo":
                        holder.imageView.setImageResource(R.drawable.todo);
                        break;
                    case "InProgress":
                        holder.imageView.setImageResource(R.drawable.inprogress);
                        break;
                    case "Done":
                        holder.imageView.setImageResource(R.drawable.done);
                        break;
                }
                holder.textView.setVisibility(View.GONE);
            }
        }
        if (p >= 0){
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = Objects.requireNonNull(context).getSharedPreferences("Task", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Issues")
                            .child(user_issue_lists.get(p).getProject_ID())
                            .child(user_issue_lists.get(p).getIssue_ID());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Issue issue = snapshot.getValue(Issue.class);
                            editor.putString("task_id", issue.getIssue_ID());
                            editor.putString("task_name", issue.getIssue_Name());
                            editor.putString("task_type", issue.getIssue_Type());
                            editor.putString("task_description", issue.getIssue_Description());
                            editor.putString("task_start_date", issue.getIssue_StartDate());
                            editor.commit();
                            TaskDetailFragment dialog = new TaskDetailFragment();
                            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "TaskDetailFragment");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (user_issue_lists != null){
            return (user_issue_lists.size()+1) * 4;
        }else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_detail);
            textView = itemView.findViewById(R.id.tv_name);
        }
    }
}
