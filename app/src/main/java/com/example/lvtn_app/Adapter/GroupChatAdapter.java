package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Activity.ChatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder>{
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<GroupChat> groupChats;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public GroupChatAdapter(Context context, ArrayList<GroupChat> groupChats) {
        this.context = context;
        this.groupChats = groupChats;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public GroupChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_group_chat, parent, false);
        return new GroupChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatAdapter.ViewHolder holder, int position) {
        if (groupChats.get(position).getGroupImage() != null){
            Glide.with(context).load(groupChats.get(position).getGroupImage()).centerCrop().into(holder.imgAvatarChat);
        }else holder.imgAvatarChat.setImageResource(R.drawable.blueprint);

        if (groupChats.get(position).getGroupLastSender() == null
                || groupChats.get(position).getGroupLastSender().equals(" ")
                || groupChats.get(position).getGroupLastSender().length() == 0){
            holder.tvSenderLatest.setText("");
        }else {
            holder.tvSenderLatest.setText(groupChats.get(position).getGroupLastSender() + ": ");
        }
        holder.tvNameGroupChat.setText(groupChats.get(position).getGroupName());
        holder.tvLatestMessageChat.setText(groupChats.get(position).getGroupLastMess());
    }

    @Override
    public int getItemCount() {
        return groupChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Khai báo
        CircleImageView imgAvatarChat;
        TextView tvNameGroupChat, tvSenderLatest, tvLatestMessageChat;
        LinearLayout linearLayout_group_chat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatarChat = itemView.findViewById(R.id.imgAvatarChat);
            tvNameGroupChat = itemView.findViewById(R.id.tvNameGroupChat);
            tvSenderLatest  = itemView.findViewById(R.id.tvSenderLatest);
            tvLatestMessageChat = itemView.findViewById(R.id.tvLatestMessageChat);
            linearLayout_group_chat = itemView.findViewById(R.id.linearLayout_group_chat);

            sharedPreferences = Objects.requireNonNull(context).getSharedPreferences("Chat", Context.MODE_PRIVATE);

            linearLayout_group_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sharedPreferences = Objects.requireNonNull(context).getSharedPreferences("GroupChat", Context.MODE_PRIVATE);
//                    editor = sharedPreferences.edit();
//                    editor.putInt("groupchat_id", groupChats.get(getAdapterPosition()).getId());
//                    editor.putString("groupchat_name", (groupChats.get(getAdapterPosition()).getName()));
//                    editor.commit();
//                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                    activity.getSupportFragmentManager().beginTransaction().addToBackStack("MessageFragment return ChatFragment").replace(R.id.frame_main, new MessageFragment()).commit();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Intent intent = new Intent(activity, ChatActivity.class);
                    editor = sharedPreferences.edit();
                    editor.putInt("groupChat_id", groupChats.get(getAdapterPosition()).getId_Group());
                    editor.putString("groupChat_name", groupChats.get(getAdapterPosition()).getGroupName());
                    editor.putString("groupChat_img", groupChats.get(getAdapterPosition()).getGroupImage());
                    editor.putString("groupChat_creator", groupChats.get(getAdapterPosition()).getGroupCreator());
                    editor.putString("groupChat_lastmess", groupChats.get(getAdapterPosition()).getGroupLastMess());
                    editor.putString("groupChat_lastsender", groupChats.get(getAdapterPosition()).getGroupLastSender());
                    editor.commit();
                    activity.startActivity(intent);
                }
            });
        }
    }
}
