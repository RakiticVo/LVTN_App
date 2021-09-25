package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lvtn_app.Model.Member;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Member> members;
    private ItemClickListener mClickListener;


    public MemberAdapter(Context context, ArrayList<Member> members) {
        this.context = context;
        this.members = members;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

            cb_delete_member.setVisibility(View.GONE);

            linearLayout_member_choose.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    // Lấy data từ vị trí đc click
    public Member getItem(int id) {
        return members.get(id);
    }

    // Cho phép bắt các sự kiện nhấp chuột
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // Phương thức này để phản hồi các sự kiện nhấp chuột
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
