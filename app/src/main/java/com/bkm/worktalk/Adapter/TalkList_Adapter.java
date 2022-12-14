package com.bkm.worktalk.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkm.worktalk.BeginApp.Login;
import com.bkm.worktalk.ChatRoom;
import com.bkm.worktalk.DTO.ChatRoom_DTO;
import com.bkm.worktalk.DTO.TalkListsDTO;
import com.bkm.worktalk.DTO.UserListsDTO;
import com.bkm.worktalk.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TalkList_Adapter extends RecyclerView.Adapter<TalkList_Adapter.CustomViewHolder> {

    public DatabaseReference mDatabase;
    public DatabaseReference databaseReference;
    public DatabaseReference databaseReference2;

    public ArrayList<String> arrayList;
    public Context context;
    public String myUid;
    public String opponent;
    public String opponent2;
    public String chatRoomPath;
    public int chk = 0;

    public TalkList_Adapter(ArrayList<String> arrayList, String myUid, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.myUid = myUid;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatroomlist, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        mDatabase = FirebaseDatabase.getInstance().getReference("chatRoom");

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext()).load(R.drawable.profile_simple).apply(new RequestOptions().circleCrop()).into(holder.iv_userProfile);

//        holder.iv_userProfile.setColorFilter(null);

        holder.tv_userName.setText(arrayList.get(position));

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opponent = holder.tv_userName.getText().toString();
                chatRoomPathChk(opponent);
            }
        });
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
            notifyItemRemoved(position);  // ????????????
        }catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_userProfile;
        protected TextView tv_userName;
        protected TextView tv_userHp;
        protected TextView tv_userEmail;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_userProfile = (ImageView) itemView.findViewById(R.id.iv_userProfile);
            this.tv_userName = (TextView) itemView.findViewById(R.id.tv_userName);
            this.tv_userHp = (TextView) itemView.findViewById(R.id.tv_userHp);
            this.tv_userEmail = (TextView) itemView.findViewById(R.id.tv_userEmail);
        }
    }

    public void chatRoomPathChk(String opponent) {
        databaseReference2 = FirebaseDatabase.getInstance().getReference("projectList");
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("key", snapshot.getKey());
                        if(opponent.equals(snapshot.getKey())) {
                            chk = 1;
                            SharedPreferences.Editor editor = Login.appData.edit();
                            editor.putString("projectCheck", "y");
                            editor.apply();
                            Log.d("projectCheck2", Login.appData.getString("projectCheck", ""));
                            Log.d("???????????????", opponent);
                            opponent2 = opponent;
                            Intent intent = new Intent(context, ChatRoom.class);
                            intent.putExtra("chatRoomPath", opponent2);
                            intent.putExtra("myUid", myUid);
                            context.startActivity(intent);
                        }
                    }

                    if(chk == 0) {
                        String myName = Login.appData.getString("myName", "");
                        databaseReference = FirebaseDatabase.getInstance().getReference("eachUserChatRoomInfo");

                        mDatabase.child(myName+"_"+opponent).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.getChildrenCount() > 0) {

                                    chatRoomPath = myName+"_"+opponent;

                                    SharedPreferences.Editor editor = Login.appData.edit();
                                    editor.putString("projectCheck", "n");
                                    editor.apply();
                                    Log.d("projectCheck2", Login.appData.getString("projectCheck", ""));
                                    Intent intent = new Intent(context, ChatRoom.class);
                                    intent.putExtra("chatRoomPath", chatRoomPath);
                                    intent.putExtra("myUid", myUid);
                                    intent.putExtra("myName", myName);
                                    intent.putExtra("friendName", opponent);
                                    context.startActivity(intent);

                                    return;
                                }
                                else {

                                    mDatabase.child(opponent+"_"+myName).addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.getChildrenCount() > 0) {

                                                chatRoomPath = opponent+"_"+myName;

                                                SharedPreferences.Editor editor = Login.appData.edit();
                                                editor.putString("projectCheck", "n");
                                                editor.apply();
                                                Log.d("projectCheck2", Login.appData.getString("projectCheck", ""));
                                                Intent intent = new Intent(context, ChatRoom.class);
                                                intent.putExtra("chatRoomPath", chatRoomPath);
                                                intent.putExtra("myUid", myUid);
                                                intent.putExtra("myName", myName);
                                                intent.putExtra("friendName", opponent);
                                                context.startActivity(intent);

                                                return;
                                            }
                                            else {

                                                long now = System.currentTimeMillis();
                                                Date date = new Date(now);
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM??? dd??? hh:mm");
                                                String getTime = dateFormat.format(date);

                                                Map<String, Object> map = new HashMap<>();
                                                Map<String, Object> map2 = new HashMap<>();
                                                map.put("createdTime", getTime);
                                                map2.put("opponent", opponent);

                                                mDatabase.child(myName + "_" + opponent).push().setValue(map);
                                                databaseReference.child(myUid).push().updateChildren(map2);
                                                chatRoomPath = myName + "_" + opponent;

                                                SharedPreferences.Editor editor = Login.appData.edit();
                                                editor.putString("projectCheck", "n");
                                                editor.apply();
                                                Log.d("projectCheck2", Login.appData.getString("projectCheck", ""));
                                                Intent intent = new Intent(context, ChatRoom.class);
                                                intent.putExtra("chatRoomPath", chatRoomPath);
                                                intent.putExtra("myName", myName);
                                                intent.putExtra("myUid", myUid);
                                                intent.putExtra("friendName", opponent);
                                                context.startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showAlert(String msg) {    // ??????????????? ??? ????????? ?????????
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("").setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}