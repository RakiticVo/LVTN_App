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
        if (project_list.get(position) != null && project_list != null){
            String s = project_list.get(position).getProject_Background();
            holder.CardView_background.setCardBackgroundColor(Integer.parseInt(s));
            switch (project_list.get(position).getProject_Type().toString()){
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
            holder.tv_project_name.setText(project_list.get(position).getProject_Name().toString().trim());
        }
    }

    @Override
    public int getItemCount() {
        if (project_list != null){
            return project_list.size();
        }else return 0;
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
            sharedPreferences = Objects.requireNonNull(context).getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("project_ID", project_list.get(getAdapterPosition()).getProject_ID());
            editor.putString("project_finish_date", project_list.get(getAdapterPosition()).getProject_FinishDate());
            editor.commit();
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().addToBackStack("ProjectDetail return Projects").replace(R.id.frame_main, new ProjectDetailFragment()).commit();
        }
    }
}
