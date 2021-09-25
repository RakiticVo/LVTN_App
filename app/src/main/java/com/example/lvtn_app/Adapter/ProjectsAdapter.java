package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lvtn_app.Model.Project;
import com.example.lvtn_app.Model.ProjectType;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Fragment.ProjectDetailFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder>{
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Project> project_list;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ProjectsAdapter(Context context, ArrayList<Project> project_list) {
        this.context = context;
        this.project_list = project_list;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ProjectsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_projects, parent, false);
        return new ProjectsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsAdapter.ViewHolder holder, int position) {
//        Glide.with(context).load(project_list.get(position).getProject_image()).centerCrop().into(holder.img_project);
        holder.CardView_background.setBackgroundColor(project_list.get(position).getProjectBackground());
        switch (project_list.get(position).getProjectType().toString()){

            case "Normal":
                holder.img_project.setImageResource(R.drawable.project_1);
                break;
            case "Kanban":
                holder.img_project.setImageResource(R.drawable.project_kanban);
                break;
            case "Scrum":
                holder.img_project.setImageResource(R.drawable.project_scrum);
                break;
            case "Personal":
                holder.img_project.setImageResource(R.drawable.project_personal);
                break;
            case "Bussiness":
                holder.img_project.setImageResource(R.drawable.project_bussiness);
                break;

        }
        holder.tv_project_name.setText(project_list.get(position).getProjectName().toString().trim());
    }

    @Override
    public int getItemCount() {
        return project_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Khai báo
        ImageView img_project;
        CardView CardView_background;
        TextView tv_project_name;
        ConstraintLayout constraintLayout_projects;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_project = itemView.findViewById(R.id.img_project);
            tv_project_name = itemView.findViewById(R.id.tv_project_name);
            constraintLayout_projects = itemView.findViewById(R.id.constraintLayout_projects);
            CardView_background = itemView.findViewById(R.id.CardView_background);

            constraintLayout_projects.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "" + project_list.get(getAdapterPosition()).getId_project() + "\n"
                    + project_list.get(getAdapterPosition()).getProjectName() + "\n"
                    + project_list.get(getAdapterPosition()).getProjectDescription() + "\n"
                    + project_list.get(getAdapterPosition()).getProjectFinishDate() + "\n"
                    + project_list.get(getAdapterPosition()).getProjectType() + "\n"
                    + project_list.get(getAdapterPosition()).getProjectDateCreate() + "\n"
                    + project_list.get(getAdapterPosition()).getProjectLeader() + "\n"
                    + project_list.get(getAdapterPosition()).getProjectBackground() + "\n", Toast.LENGTH_SHORT).show();

            sharedPreferences = Objects.requireNonNull(context).getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putInt("id_project", (project_list.get(getAdapterPosition()).getId_project()));
            editor.putString("projectName_txt", (project_list.get(getAdapterPosition()).getProjectName()));
            editor.putString("projectDescription_txt", (project_list.get(getAdapterPosition()).getProjectDescription()));
            editor.putString("projectFinishDate_txt", (project_list.get(getAdapterPosition()).getProjectFinishDate()));
            editor.putString("projectType_txt", (project_list.get(getAdapterPosition()).getProjectType()));
            editor.putString("projectDateCreate_txt", (project_list.get(getAdapterPosition()).getProjectDateCreate()));
            editor.putString("projectLeader_txt", (project_list.get(getAdapterPosition()).getProjectLeader()));
            editor.putInt("projectBackground", (project_list.get(getAdapterPosition()).getProjectBackground()));
            editor.commit();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().addToBackStack("ProjectDetail return Projects").replace(R.id.frame_main, new ProjectDetailFragment()).commit();
        }
    }
}
