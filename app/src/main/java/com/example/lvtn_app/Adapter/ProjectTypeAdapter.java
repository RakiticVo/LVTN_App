package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lvtn_app.Model.ProcessType;
import com.example.lvtn_app.Model.ProjectType;
import com.example.lvtn_app.R;

import java.util.ArrayList;

public class ProjectTypeAdapter extends BaseAdapter {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<ProjectType> projectType_list;

    public ProjectTypeAdapter(Context context, ArrayList<ProjectType> projectType_list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.projectType_list = projectType_list;
    }

    @Override
    public int getCount() {
        return projectType_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Khai báo
        convertView = mInflater.inflate(R.layout.item_project_type, null);

        ImageView img_project_type = convertView.findViewById(R.id.img_project_type);
        TextView tv_name_project_type = convertView.findViewById(R.id.tv_name_project_type);

        img_project_type.setImageResource(projectType_list.get(position).getImage());
        tv_name_project_type.setText(projectType_list.get(position).getName());

        return convertView;
    }
}
