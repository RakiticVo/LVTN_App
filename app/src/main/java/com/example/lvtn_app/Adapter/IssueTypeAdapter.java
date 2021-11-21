package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lvtn_app.Model.IssueType;
import com.example.lvtn_app.R;

import java.util.ArrayList;

public class IssueTypeAdapter extends BaseAdapter {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<IssueType> issueType_list;

    public IssueTypeAdapter(Context context, ArrayList<IssueType> issueType_list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.issueType_list = issueType_list;
    }

    @Override
    public int getCount() {
        if (issueType_list.size() > 0){
            return issueType_list.size();
        }else return 0;
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
        convertView = mInflater.inflate(R.layout.item_issue_type, null);

        ImageView img_issue_type = convertView.findViewById(R.id.img_issue_type);
        TextView tv_name_issue_type = convertView.findViewById(R.id.tv_name_issue_type);

        img_issue_type.setImageResource(issueType_list.get(position).getImage());
        tv_name_issue_type.setText(issueType_list.get(position).getName());

        return convertView;
    }
}
