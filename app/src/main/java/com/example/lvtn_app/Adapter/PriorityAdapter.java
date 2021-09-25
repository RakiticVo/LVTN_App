package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lvtn_app.R;
import com.example.lvtn_app.Model.Priority;

import java.util.ArrayList;

public class PriorityAdapter extends BaseAdapter {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Priority> priority_list;

    public PriorityAdapter(Context context, ArrayList<Priority> priority_list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.priority_list = priority_list;
    }

    @Override
    public int getCount() {
        return priority_list.size();
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
        convertView = mInflater.inflate(R.layout.item_priority, null);

        ImageView img_priority = convertView.findViewById(R.id.img_priority_create);
        TextView tv_name_priority = convertView.findViewById(R.id.tv_name_priority_create);

        img_priority.setImageResource(priority_list.get(position).getImage());
        tv_name_priority.setText(priority_list.get(position).getName());

        return convertView;
    }
}
