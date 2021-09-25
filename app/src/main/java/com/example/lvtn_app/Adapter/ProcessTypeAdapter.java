package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lvtn_app.Model.IssueType;
import com.example.lvtn_app.Model.ProcessType;
import com.example.lvtn_app.R;

import java.util.ArrayList;

public class ProcessTypeAdapter extends BaseAdapter {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<ProcessType> processType_list;

    public ProcessTypeAdapter(Context context, ArrayList<ProcessType> processType_list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.processType_list = processType_list;
    }

    @Override
    public int getCount() {
        return processType_list.size();
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
        convertView = mInflater.inflate(R.layout.item_process, null);

        ImageView img_process_type = convertView.findViewById(R.id.img_process_type);
        TextView tv_name_process_type = convertView.findViewById(R.id.tv_name_process_type);

        img_process_type.setImageResource(processType_list.get(position).getImage());
        tv_name_process_type.setText(processType_list.get(position).getName());

        return convertView;
    }
}
