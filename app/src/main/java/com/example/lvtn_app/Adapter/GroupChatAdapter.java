package com.example.lvtn_app.Adapter;

import android.content.Context;
import android.content.Intent;
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

import com.example.lvtn_app.Model.GroupChat;
import com.example.lvtn_app.R;
import com.example.lvtn_app.View.Activity.ChatActivity;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder>{
    //Khai báo
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<GroupChat> groupChats;
//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;

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
        if (groupChats.get(position).getUriImage() != null){
//            Glide.with(context).load(groupChats.get(position).getImage1()).centerCrop().into(holder.imgAvatarChat);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), groupChats.get(position).getUriImage());
                holder.imgAvatarChat.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else holder.imgAvatarChat.setImageResource(R.drawable.blueprint);

//        else if (!groupChats.get(position).getImage1().equals("") || groupChats.get(position).getImage1() != null){
//            Glide.with(context).load(groupChats.get(position).getImage1()).centerCrop().into(holder.imgAvatarChat);
//        }

        if (groupChats.get(position).getLast_message().equals("This group has been created")){
            holder.tvReceiverLatest.setVisibility(View.GONE);
        }else {
            holder.tvReceiverLatest.setText(groupChats.get(position).getLast_sender());
        }
        holder.tvNameGroupChat.setText(groupChats.get(position).getName());
        holder.tvLatestMessageChat.setText(groupChats.get(position).getLast_message());
    }

    @Override
    public int getItemCount() {
        return groupChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Khai báo
        CircleImageView imgAvatarChat;
        TextView tvNameGroupChat, tvReceiverLatest, tvLatestMessageChat;
        LinearLayout linearLayout_group_chat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatarChat = itemView.findViewById(R.id.imgAvatarChat);
            tvNameGroupChat = itemView.findViewById(R.id.tvNameGroupChat);
            tvReceiverLatest  = itemView.findViewById(R.id.tvReceiverLatest);
            tvLatestMessageChat = itemView.findViewById(R.id.tvLatestMessageChat);
            linearLayout_group_chat = itemView.findViewById(R.id.linearLayout_group_chat);

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
                    intent.putExtra("groupChat_id", groupChats.get(getAdapterPosition()).getId());
                    intent.putExtra("groupChat_name", groupChats.get(getAdapterPosition()).getName());
                    activity.startActivity(intent);
                }
            });
        }
    }
}
