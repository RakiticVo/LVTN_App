package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lvtn_app.Model.Group_Chat_Users;
import com.example.lvtn_app.Model.Project_Users;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberProjectAdapter extends RecyclerView.Adapter<MemberProjectAdapter.ViewHolder> {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<User> members;
    private MemberAdapter.ItemClickListener mClickListener;

    public MemberProjectAdapter(Context context, ArrayList<User> members) {
        this.context = context;
        this.members = members;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MemberProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_member, parent, false);
        return new MemberProjectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberProjectAdapter.ViewHolder holder, int position) {
        if (members.get(position).getUser_Status().toLowerCase().equals("online")){
            Glide.with(context).load(R.drawable.circle_blue).into(holder.imgStatusMember);
        } else {
            Glide.with(context).load(R.drawable.circle_grey).into(holder.imgStatusMember);
        }
        String avatar = members.get(position).getUser_Avatar();
        if (avatar == null || avatar.length() == 0){
            holder.imgAvatarMember.setImageResource(R.drawable.profile_1);
        }else{
            Glide.with(context).load(avatar).centerCrop().into(holder.imgAvatarMember);
        }
        holder.tvNameMember.setText(members.get(position).getUser_Name());
        String project_ID = holder.sharedPreferences.getString("project_ID","token");
        String userid = members.get(position).getUser_ID();
        if (!project_ID.equals("token")){
            holder.reference.child(project_ID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Project_Users users = dataSnapshot.getValue(Project_Users.class);
//                        Toast.makeText(context, "" + users.getGroup_ID(), Toast.LENGTH_SHORT).show();
                        if (userid.equals(users.getUser_ID())){
                            holder.tvPositionMember.setText(users.getPosition());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView imgStatusMember, imgAvatarMember;
        TextView tvNameMember, tvPositionMember;
        CheckBox cb_delete_member;
        LinearLayout linearLayout_member_choose;
        SharedPreferences sharedPreferences;
        DatabaseReference reference, reference2;

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

            sharedPreferences = context.getSharedPreferences("ProjectDetail", Context.MODE_PRIVATE);
            reference = FirebaseDatabase.getInstance().getReference("User_List_By_Project");
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    // Lấy data từ vị trí đc click
    public User getItem(int id) {
        return members.get(id);
    }

    // Cho phép bắt các sự kiện nhấp chuột
    public void setClickListener(MemberAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // Phương thức này để phản hồi các sự kiện nhấp chuột
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
