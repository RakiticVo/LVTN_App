package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.IssueDetailFragment;

import java.util.ArrayList;
import java.util.Objects;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder>{
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Issue> issue_list;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public IssueAdapter(Context context, ArrayList<Issue> issue_list) {
        this.context = context;
        this.issue_list = issue_list;
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
        holder.tv_name_issue.setText(issue_list.get(position).getIssue_Name());
        holder.tv_name_assignee.setText(issue_list.get(position).getIssue_Assignee());
        holder.tv_start_date_issue.setText(issue_list.get(position).getIssue_StartDate());
        switch (issue_list.get(position).getIssue_Type()){
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
        switch (issue_list.get(position).getIssue_Priority()){
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
        return issue_list.size();
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
                    sharedPreferences = Objects.requireNonNull(context).getSharedPreferences("Issue", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("issue_ID", issue_list.get(getAdapterPosition()).getIssue_ID());
                    editor.commit();
                    IssueDetailFragment dialog = new IssueDetailFragment();
                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "IssueDetailFragment");
                }
            });
        }
    }
}
