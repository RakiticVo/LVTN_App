package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lvtn_app.Model.Process;
import com.example.lvtn_app.Model.Task;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.CreateGroupChatFragment;
import com.example.lvtn_app.View.Fragment.CreateIssueFragment;
import com.example.lvtn_app.View.Fragment.IssueDetailFragment;

import java.util.ArrayList;

public class DashboardItemAdapter  extends RecyclerView.Adapter<DashboardItemAdapter.ViewHolder> {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Process> process_list;
    private IssueAdapter issueAdapter;

    public DashboardItemAdapter(Context context, ArrayList<Process> process_list) {
        this.context = context;
        this.process_list = process_list;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DashboardItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_dashboard, parent, false);
        return new DashboardItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardItemAdapter.ViewHolder holder, int position) {
        holder.list.clear();
        holder.tv_process.setText(process_list.get(position).getName());
        if (process_list.get(position).getName().equals("InProgress")
            || process_list.get(position).getName().equals("Done")){
            holder.linear_create_task.setVisibility(View.GONE);
        }
        if (holder.tv_process.getText().equals("ToDo")){
            holder.list.addAll(process_list.get(0).getList());
        }
        if (holder.tv_process.getText().equals("InProgress")){
            holder.list.addAll(process_list.get(1).getList());
        }
        if(holder.tv_process.getText().equals("Done")){
            holder.list.addAll(process_list.get(2).getList());
        }
        holder.recyclerView_Task_Process.setLayoutManager(new LinearLayoutManager(context));
        issueAdapter = new IssueAdapter(context, holder.list);
        holder.recyclerView_Task_Process.setAdapter(issueAdapter);
    }

    @Override
    public int getItemCount() {
        return process_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Khai báo
        TextView tv_process;
        RecyclerView recyclerView_Task_Process;
        ArrayList<Task> list = new ArrayList<>();
        LinearLayout linear_create_task;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Khai báo
            tv_process = itemView.findViewById(R.id.tv_process);
            recyclerView_Task_Process = itemView.findViewById(R.id.recyclerView_Task_Process);
            linear_create_task = itemView.findViewById(R.id.linear_create_task);

            linear_create_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateIssueFragment dialog = new CreateIssueFragment();
                    dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "CreateGroupChatFragment");
                }
            });
        }
    }
}
