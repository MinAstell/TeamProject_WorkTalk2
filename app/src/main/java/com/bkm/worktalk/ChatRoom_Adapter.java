package com.bkm.worktalk;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatRoom_Adapter extends RecyclerView.Adapter<ChatRoom_Adapter.CustomViewHolder> {

    private String myName;
    private Context context;

    private ArrayList<ChatRoom_DTO.Comment> arrayList;

    public ChatRoom_Adapter(ArrayList<ChatRoom_DTO.Comment> arrayList, Context context, String myName) {
        this.arrayList = arrayList;
        this.context = context;
        this.myName = myName;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        if(arrayList.get(position).readUsers.size() == 2) {
            holder.readChecked.setText("");
        }

        ChatRoom_DTO.Comment comment = arrayList.get(position);

        holder.tv_comments.setText(comment.userContents);

        if(comment.userName.equals(myName)) {

            holder.tv_nick.setText("");
            holder.tv_timestamp.setText(comment.timestamp);
            holder.tv_comments.setBackgroundResource(R.drawable.rightbubble_2);
            holder.tv_comments.setTextSize(22);

            holder.LinearLayoutMessage.setGravity(Gravity.RIGHT);
            holder.LinearLayoutUserName.setGravity(Gravity.RIGHT);
            holder.LinearLayoutTimestamp.setGravity(Gravity.RIGHT);
        }
        else {

            holder.tv_nick.setText(comment.userName);
            holder.tv_timestamp.setText(comment.timestamp);
            Glide.with(context).load(R.drawable.profile_simple).apply(new RequestOptions().circleCrop()).into(holder.iv_profile);
            holder.tv_comments.setBackgroundResource(R.drawable.leftbubble_2);
            holder.tv_comments.setTextSize(22);

            holder.LinearLayoutMessage.setGravity(Gravity.LEFT);
            holder.LinearLayoutUserName.setGravity(Gravity.LEFT);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);  // 새로고침
        }catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_nick;
        protected TextView tv_comments;
        protected TextView readChecked;
        protected LinearLayout LinearLayoutMessage;
        protected LinearLayout LinearLayoutUserName;
        protected LinearLayout LinearLayoutTimestamp;
        protected ImageView iv_profile;
        protected TextView tv_timestamp;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_nick = (TextView) itemView.findViewById(R.id.tv_nick);
            this.tv_comments = (TextView) itemView.findViewById(R.id.tv_comments);
            this.readChecked = (TextView) itemView.findViewById(R.id.readChecked);
            this.LinearLayoutMessage = (LinearLayout) itemView.findViewById(R.id.LinearLayoutMessage);
            this.LinearLayoutUserName = (LinearLayout) itemView.findViewById(R.id.LinearLayoutUserName);
            this.iv_profile = (ImageView) itemView.findViewById(R.id.iv_profile);
            this.tv_timestamp = (TextView) itemView.findViewById(R.id.tv_timestamp);
            this.LinearLayoutTimestamp = (LinearLayout) itemView.findViewById(R.id.LinearLayoutTimestamp);
        }
    }
}