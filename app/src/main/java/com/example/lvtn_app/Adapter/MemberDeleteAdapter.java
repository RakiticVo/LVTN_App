package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lvtn_app.Model.Group_Chat_Users;
import com.example.lvtn_app.Model.User;
import com.example.lvtn_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberDeleteAdapter extends RecyclerView.Adapter<MemberDeleteAdapter.ViewHolder> {
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<User> members, members_checked;

    public ArrayList<User> getMembers_checked() {
        return members_checked;
    }

    public void setMembers_checked(ArrayList<User> members_checked) {
        this.members_checked = members_checked;
    }

    public MemberDeleteAdapter(Context context, ArrayList<User> members) {
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
        if (position == 0){
            holder.cb_delete_member.setVisibility(View.GONE);
        }
        if (members != null && members.get(position) != null){
            members_checked = new ArrayList<>();
            if (members.get(position).getUser_Status().toLowerCase().equals("online")){
                Glide.with(context).load(R.drawable.circle_blue).into(holder.imgStatusMember);
            } else {
                Glide.with(context).load(R.drawable.circle_grey).into(holder.imgStatusMember);
            }
            String avatar = members.get(position).getUser_Avatar();
            if (avatar.length() > 0){
                Glide.with(context).load(avatar)
                        .onlyRetrieveFromCache(true)
                        .override(50)
                        .centerCrop()
                        .into(holder.imgAvatarMember);
            }else {
                Glide.with(context).load(members.get(position).getUser_Avatar()).centerCrop().into(holder.imgAvatarMember);
            }
            holder.tvNameMember.setText(members.get(position).getUser_Name());
            String group_ID = holder.sharedPreferences.getString("group_ID","token");
            String s = members.get(position).getUser_ID();
            if (!group_ID.equals("token")){
                holder.reference.child(group_ID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Group_Chat_Users users = dataSnapshot.getValue(Group_Chat_Users.class);
//                        Toast.makeText(context, "" + users.getGroup_ID(), Toast.LENGTH_SHORT).show();
                            if (s.equals(users.getUser_ID())){
                                holder.tvPositionMember.setText(users.getPosition());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            holder.cb_delete_member.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        Toast.makeText(context, "Checkbox " + (holder.getAdapterPosition() + 1) + " is checked", Toast.LENGTH_SHORT).show();
                        members_checked.add(members.get(holder.getAdapterPosition()));
                    }else {
                        Toast.makeText(context, "Checkbox " + (holder.getAdapterPosition() + 1) + " is unchecked", Toast.LENGTH_SHORT).show();
                        String member_name = members.get(holder.getAdapterPosition()).getUser_Name();
                        for (int i = 0; i < members_checked.size(); i++) {
                            if (members_checked.get(i).getUser_Name().equals(member_name)){
                                members_checked.remove(i);
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (members.size() > 0){
            return members.size();
        }else  return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView imgStatusMember, imgAvatarMember;
        TextView tvNameMember, tvPositionMember;
        public CheckBox cb_delete_member;
        LinearLayout linearLayout_member_choose;

        SharedPreferences sharedPreferences;
        DatabaseReference reference;

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

            sharedPreferences = context.getSharedPreferences("Chat", Context.MODE_PRIVATE);
            reference = FirebaseDatabase.getInstance().getReference("User_List_By_Group_Chat");
        }

        @Override
        public void onClick(View v) {
            cb_delete_member.setChecked(cb_delete_member.isChecked());
        }
    }
}
