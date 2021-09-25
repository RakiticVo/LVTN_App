package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lvtn_app.Model.Member;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssigneeAdapter extends BaseAdapter {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Member> members;

    public AssigneeAdapter(Context context, ArrayList<Member> members) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.members = members;
    }

    @Override
    public int getCount() {
        return members.size();
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
        convertView = mInflater.inflate(R.layout.item_assignee, null);

        CircleImageView img_assignee_create = convertView.findViewById(R.id.img_assignee_create);
        TextView tv_name_assignee_create = convertView.findViewById(R.id.tv_name_assignee_create);

        if (!members.get(position).getAvatar().equals("")){
            Glide.with(convertView).load(members.get(position).getAvatar()).centerCrop().into(img_assignee_create);
        }else {
            img_assignee_create.setImageResource(R.drawable.profile_1);
        }
        tv_name_assignee_create.setText(members.get(position).getName());

        return convertView;
    }
}
