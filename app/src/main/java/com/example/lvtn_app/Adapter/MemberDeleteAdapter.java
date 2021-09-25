package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lvtn_app.Model.Member;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberDeleteAdapter extends RecyclerView.Adapter<MemberDeleteAdapter.ViewHolder> {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Member> members, members_checked;

    public ArrayList<Member> getMembers_checked() {
        return members_checked;
    }

    public void setMembers_checked(ArrayList<Member> members_checked) {
        this.members_checked = members_checked;
    }

    public MemberDeleteAdapter(Context context, ArrayList<Member> members) {
        this.context = context;
        this.members = members;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MemberDeleteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_member, parent, false);
        return new MemberDeleteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberDeleteAdapter.ViewHolder holder, int position) {
        members_checked = new ArrayList<>();
        if (members.get(position).isStatus()){
            Glide.with(context).load(R.drawable.circle_blue).into(holder.imgStatusMember);
        } else {
            Glide.with(context).load(R.drawable.circle_grey).into(holder.imgStatusMember);
        }
        if (members.get(position).getAvatar().length() == 0){
            holder.imgAvatarMember.setImageResource(R.drawable.profile_1);
        }else {
            Glide.with(context).load(members.get(position).getAvatar()).centerCrop().into(holder.imgAvatarMember);
        }
        holder.tvNameMember.setText(members.get(position).getName());
        holder.tvPositionMember.setText(members.get(position).getPosition());

        holder.cb_delete_member.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(context, "Checkbox " + (holder.getAdapterPosition() + 1) + " is checked", Toast.LENGTH_SHORT).show();
                    members_checked.add(members.get(holder.getAdapterPosition()));
                }else {
                    Toast.makeText(context, "Checkbox " + (holder.getAdapterPosition() + 1) + " is unchecked", Toast.LENGTH_SHORT).show();
                    String member_name = members.get(holder.getAdapterPosition()).getName();
                    for (int i = 0; i < members_checked.size(); i++) {
                        if (members_checked.get(i).getName().equals(member_name)){
                            members_checked.remove(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView imgAvatarMember;
        ImageView imgStatusMember;
        TextView tvNameMember, tvPositionMember;
        public CheckBox cb_delete_member;
        LinearLayout linearLayout_member_choose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatarMember = itemView.findViewById(R.id.imgAvatarMember);
            imgStatusMember = itemView.findViewById(R.id.imgStatusMember);
            tvNameMember = itemView.findViewById(R.id.tvNameMember);
            tvPositionMember = itemView.findViewById(R.id.tvPositionMember);
            cb_delete_member = itemView.findViewById(R.id.cb_delete_member);
            linearLayout_member_choose = itemView.findViewById(R.id.linearLayout_member_choose);

            cb_delete_member.setVisibility(View.VISIBLE);
            cb_delete_member.setChecked(false);
            cb_delete_member.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            cb_delete_member.setChecked(cb_delete_member.isChecked());
        }
    }
}
