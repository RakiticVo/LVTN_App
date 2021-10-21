package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.lvtn_app.Model.Issue;
import com.example.lvtn_app.Model.Process;
import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.CreateIssueFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardItemAdapter  extends RecyclerView.Adapter<DashboardItemAdapter.ViewHolder> {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Process> process_list;
    private IssueAdapter issueAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

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
        holder.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Project project = snapshot.getValue(Project.class);
                if (project != null){
                    if (project.getProject_Leader().equals(firebaseUser.getUid()) &&
                            process_list.get(position).getName().equals("ToDo")){
                        holder.linear_create_task.setVisibility(View.VISIBLE);
                    }else {
                        holder.linear_create_task.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        if (process_list != null){
            return process_list.size();
        }else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Khai báo
        TextView tv_process;
        RecyclerView recyclerView_Task_Process;
        ArrayList<Issue> list = new ArrayList<>();
        LinearLayout linear_create_task;
        DatabaseReference reference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Khai báo
            tv_process = itemView.findViewById(R.id.tv_process);
            recyclerView_Task_Process = itemView.findViewById(R.id.recyclerView_Task_Process);
            linear_create_task = itemView.findViewById(R.id.linear_create_task);

            sharedPreferences = context.getSharedPreferences("ProjectDetail",Context.MODE_PRIVATE);
            String project_ID = sharedPreferences.getString("project_ID", "token");
            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Projects").child(project_ID);

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
