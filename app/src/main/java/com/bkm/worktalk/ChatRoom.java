package com.bkm.worktalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkm.worktalk.Adapter.ChatRoom_Adapter;
import com.bkm.worktalk.BeginApp.Login;
import com.bkm.worktalk.DTO.ChatRoom_DTO;
import com.bkm.worktalk.DTO.JoinDTO;
import com.bkm.worktalk.DTO.NotificationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatRoom extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private ValueEventListener valueEventListener;

    public String friendName;
    public String myProfileImageUrl;

    private ArrayList<ChatRoom_DTO.Comment2> arrayList;
    private ChatRoom_Adapter chatRoom_adapter;
    private RecyclerView recyclerView;
    public  RecyclerView.Adapter mAdapter;
    private LinearLayoutManager linearLayoutManager;

    ImageView btnSend;
    TextView tv_friendName;
    EditText etComments;

    public String myName = "";
    public String myUid = "";
    private String chatRoomPath = "";
    public String myComment;
    public String friendToken = "";
    public String friendUid = "";
    private String chatRoomPath2 = "";
    private int chkProject = 0;
    public String myToken = "";

    ArrayList<JoinDTO> friendTokenList = new ArrayList<>();
    ArrayList<String> userTokens = new ArrayList<>();
    ArrayList<String> userEmail = new ArrayList<>();
    ArrayList<String> userName = new ArrayList<>();
    ArrayList<String> userUid = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();   // ChatRoom_DTO ????????? ?????? ?????????

        mDatabase = FirebaseDatabase.getInstance().getReference("chatRoom");

        btnSend = (ImageView) findViewById(R.id.iv_btnSend);
        etComments = (EditText) findViewById(R.id.etComments);
        tv_friendName = (TextView) findViewById(R.id.tv_friendName);

//        Log.d("????????????", Login.appData.getString("??????????????????", ""));

        // ??????????????? / ????????? ???????????? ????????? ????????? ???????????? ??????!
       if(Login.appData.getString("projectCheck", "").equals("n")) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            chatRoomPath2 = bundle.getString("chatRoomPath");
            myName = bundle.getString("myName");
            friendName = bundle.getString("friendName");
            myUid = bundle.getString("myUid");

            tv_friendName.setText(friendName);
           Log.d("chatRoom2", chatRoomPath2);
        }
        else if(Login.appData.getString("projectCheck", "").equals("y")) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            chatRoomPath2 = bundle.getString("chatRoomPath");
            myUid = bundle.getString("myUid");
            myName = Login.appData.getString("myName", "");
            Log.d("chatRoom2", chatRoomPath2);
            Log.d("myUid", myUid);
            Log.d("myName", myName);

            tv_friendName.setText(chatRoomPath2);
            chkProject = 1;
            getProjectUserCnt();
        }

        getMyToken();

        Log.d("???????????????2", chatRoomPath2);
        Log.d("chkProject", String.valueOf(chkProject));

//        getMyProfileImage();

        valueEventListener = mDatabase.child(chatRoomPath2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() > 0) {

                    getLead();

                    arrayList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ChatRoom_DTO.Comment2 comment2 = snapshot.getValue(ChatRoom_DTO.Comment2.class);
//                        ChatRoom_DTO.Comment comment = snapshot.getValue(ChatRoom_DTO.Comment.class);

                        if(chkProject == 1) {
                            if (comment2.userEmail.equals("")) {
                                arrayList.add(comment2);
                            }
                        }
                        else if(chkProject == 0) {
                            if(comment2.createdTime.equals("")) {
                                arrayList.add(comment2);
                            }
                        }
                    }

                    mAdapter = new ChatRoom_Adapter(arrayList, getApplication(), myName);
                    recyclerView.setAdapter(mAdapter);

                    recyclerView.scrollToPosition(arrayList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM??? dd??? hh:mm");
                String getTime = dateFormat.format(date);

                myComment = etComments.getText().toString();

                ChatRoom_DTO.Comment2 comment = new ChatRoom_DTO.Comment2();
                comment.userName = myName;
                comment.userContents = myComment;
                comment.readUsers.put(myName, true);
//                comment.userProfileImageUrl = myProfileImageUrl;
                comment.timestamp = getTime;

                mDatabase.child(chatRoomPath2).push().setValue(comment);

                pushMsg();

                etComments.setText("");
            }
        });
    }

    public void getLead() {
        Log.d("chatRoomPath", chatRoomPath2);
        mDatabase.child(chatRoomPath2).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() > 0) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ChatRoom_DTO.Comment2 comment2 = snapshot.getValue(ChatRoom_DTO.Comment2.class);

//                        Log.d("chatRoom", comment.createdTime);
                        Log.d("userEmail", comment2.userEmail);

                        if(chkProject == 1) {
                            if (comment2.userEmail.equals("")) {
                                Log.d("userEmail", comment2.userEmail);
                                Log.d("chk", "?????????");
                                Map<String, Object> map = new HashMap<>();
                                map.clear();
                                map.put(myName, true);

                                mDatabase.child(chatRoomPath2).child(snapshot.getKey()).child("readUsers").updateChildren(map);
                            }
                        }
                        else if(chkProject == 0) {
                            Log.d("createdTime", comment2.createdTime);
                            if(comment2.createdTime.equals("")) {
                                Log.d("createdTime2", comment2.createdTime);
                                Log.d("readusers", "?????????");
                                Map<String, Object> map = new HashMap<>();
                                map.clear();
                                map.put(myName, true);

                                mDatabase.child(chatRoomPath2).child(snapshot.getKey()).child("readUsers").updateChildren(map);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed(){

        mDatabase.child(chatRoomPath).removeEventListener(valueEventListener);
        chkProject = 0;
        finish();
    }

    public void pushMsg() {

        mDatabase2 = FirebaseDatabase.getInstance().getReference("UserInfo");

        if(Login.appData.getString("projectCheck", "").equals("y")) {
            mDatabase.child(chatRoomPath2).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0) {
                        userEmail.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ChatRoom_DTO.Comment2 comment2 = snapshot.getValue(ChatRoom_DTO.Comment2.class);
                            if(!comment2.userEmail.equals("")) {
                                userEmail.add(comment2.userEmail);
                            }
                        }

                        mDatabase2 = FirebaseDatabase.getInstance().getReference("UserInfo");
                        userTokens.clear();
                        userName.clear();
                        userUid.clear();
                        for(int i=0; i<userEmail.size(); i++) {
                            mDatabase2.orderByChild("emailId").equalTo(userEmail.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getChildrenCount() > 0) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            JoinDTO joinDTO = snapshot.getValue(JoinDTO.class);
                                            Log.d("token", joinDTO.token);
                                            userTokens.add(joinDTO.token);
                                            userName.add(joinDTO.name);
                                            userUid.add(snapshot.getKey());
                                        }

                                        for(int i=0; i<userTokens.size(); i++) {
                                            if(!myToken.equals(userTokens.get(i))) {
                                                Log.d("userTokens", userTokens.get(i));
                                                Gson gson = new Gson();
                                                NotificationModel notificationModel = new NotificationModel();
                                                notificationModel.to = userTokens.get(i);
                                                notificationModel.data.title = "?????? ?????? : " + myName;
                                                notificationModel.data.body = myComment;
                                                notificationModel.data.sendingUser = myName;
                                                notificationModel.data.chatRoomPath = chatRoomPath2;
                                                notificationModel.data.receiver = userName.get(i);
                                                notificationModel.data.receiverUid = userUid.get(i);

                                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationModel));
                                                Request request = new Request.Builder()
                                                        .header("Content-Type", "application/json")
                                                        .addHeader("Authorization", "key=AAAAT31oZAs:APA91bFxIff62TJ_bv0amJJ-I6LuYdNL13ATpy8EAY-3MyzljI2DVymZbmWuU1VuDCpClzDXO3_xEDG5dkRolTSlddvTnsEVqDETrXHURNUeTd-1q3Uz5iMxLMv-mt63ICyd8AnItUmB")
                                                        .url("https://fcm.googleapis.com/fcm/send")
                                                        .post(requestBody)
                                                        .build();

                                                OkHttpClient okHttpClient = new OkHttpClient();
                                                okHttpClient.newCall(request).enqueue(new Callback() {
                                                    @Override
                                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                        Log.d("post", "error");
                                                    }

                                                    @Override
                                                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                        Log.d("post", "success");
                                                    }
                                                });
                                            }
                                        }
                                        return;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
//                        Log.d("userTokens?????????", String.valueOf(userTokens.size()));

//                        for(int i=0; i<userTokens.size(); i++) {
//                            Log.d("userTokens", userTokens.get(i));
//                            Gson gson = new Gson();
//                            NotificationModel notificationModel = new NotificationModel();
//                            notificationModel.to = userTokens.get(i);
//                            notificationModel.data.title = "?????? ?????? : " + myName;
//                            notificationModel.data.body = myComment;
//                            notificationModel.data.sendingUser = myName;
//                            notificationModel.data.chatRoomPath = chatRoomPath2;
//                            notificationModel.data.receiver = userName.get(i);
//                            notificationModel.data.receiverUid = userUid.get(i);
//
//                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationModel));
//                            Request request = new Request.Builder()
//                                    .header("Content-Type", "application/json")
//                                    .addHeader("Authorization", "key=AAAAT31oZAs:APA91bFxIff62TJ_bv0amJJ-I6LuYdNL13ATpy8EAY-3MyzljI2DVymZbmWuU1VuDCpClzDXO3_xEDG5dkRolTSlddvTnsEVqDETrXHURNUeTd-1q3Uz5iMxLMv-mt63ICyd8AnItUmB")
//                                    .url("https://fcm.googleapis.com/fcm/send")
//                                    .post(requestBody)
//                                    .build();
//
//                            OkHttpClient okHttpClient = new OkHttpClient();
//                            okHttpClient.newCall(request).enqueue(new Callback() {
//                                @Override
//                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                                    Log.d("post", "error");
//                                }
//
//                                @Override
//                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                                    Log.d("post", "success");
//                                }
//                            });
//                        }
//                        return;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        // ???????????? ??? ??????
        mDatabase2.orderByChild("name").equalTo(friendName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() > 0) {

                    Log.d("?????????", "?????????");

                    friendTokenList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        friendUid = snapshot.getKey();
                        JoinDTO joinDTO = snapshot.getValue(JoinDTO.class);
                        friendTokenList.add(joinDTO);
                    }

                    friendToken = friendTokenList.get(0).token;

                    Log.d("token", friendToken);

                    Gson gson = new Gson();

                    NotificationModel notificationModel = new NotificationModel();

                    notificationModel.to = friendToken;
                    notificationModel.data.title = "?????? ?????? : " + myName;
                    notificationModel.data.body = myComment;
                    notificationModel.data.sendingUser = myName;
                    notificationModel.data.chatRoomPath = chatRoomPath2;
                    notificationModel.data.receiver = friendName;
                    notificationModel.data.receiverUid = friendUid;

                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationModel));

                    Request request = new Request.Builder()
                            .header("Content-Type", "application/json")
                            .addHeader("Authorization", "key=AAAAT31oZAs:APA91bFxIff62TJ_bv0amJJ-I6LuYdNL13ATpy8EAY-3MyzljI2DVymZbmWuU1VuDCpClzDXO3_xEDG5dkRolTSlddvTnsEVqDETrXHURNUeTd-1q3Uz5iMxLMv-mt63ICyd8AnItUmB")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(requestBody)
                            .build();

                    OkHttpClient okHttpClient = new OkHttpClient();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Log.d("post", "error");
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            Log.d("post", "success");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    int userCnt = 0;
    public void getProjectUserCnt() {
        mDatabase.child(chatRoomPath2).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() > 0) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ChatRoom_DTO.Comment2 comment2 = snapshot.getValue(ChatRoom_DTO.Comment2.class);
                        if(!comment2.userEmail.equals("")) {
                            ++userCnt;
                        }
                    }
                    SharedPreferences.Editor editor = Login.appData.edit();
                    editor.putString("userCnt", String.valueOf(userCnt));
                    editor.apply();
                    Log.d("userCnt", String.valueOf(userCnt));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getMyToken() {
        mDatabase2 = FirebaseDatabase.getInstance().getReference("UserInfo");
        mDatabase2.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    myToken = dataSnapshot.child("token").getValue().toString();
                    Log.d("myToken", myToken);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getForUserTokenToAlarm() {
        mDatabase2 = FirebaseDatabase.getInstance().getReference("UserInfo");
        userTokens.clear();
        userName.clear();
        userUid.clear();
        for(int i=0; i<userEmail.size(); i++) {
            mDatabase2.orderByChild("emailId").equalTo(userEmail.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            JoinDTO joinDTO = snapshot.getValue(JoinDTO.class);
                            userTokens.add(joinDTO.token);
                            userName.add(joinDTO.name);
                            userUid.add(snapshot.getKey());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        for(int i=0; i<userTokens.size(); i++) {
            Gson gson = new Gson();
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.to = userTokens.get(i);
            notificationModel.data.title = "?????? ?????? : " + myName;
            notificationModel.data.body = myComment;
            notificationModel.data.sendingUser = myName;
            notificationModel.data.chatRoomPath = chatRoomPath2;
            notificationModel.data.receiver = userName.get(i);
            notificationModel.data.receiverUid = userUid.get(i);

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationModel));
            Request request = new Request.Builder()
                    .header("Content-Type", "application/json")
                    .addHeader("Authorization", "key=AAAAT31oZAs:APA91bFxIff62TJ_bv0amJJ-I6LuYdNL13ATpy8EAY-3MyzljI2DVymZbmWuU1VuDCpClzDXO3_xEDG5dkRolTSlddvTnsEVqDETrXHURNUeTd-1q3Uz5iMxLMv-mt63ICyd8AnItUmB")
                    .url("https://fcm.googleapis.com/fcm/send")
                    .post(requestBody)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("post", "error");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.d("post", "success");
                }
            });
        }
    }

    public void getMyProfileImage() {  // ????????? ??????

        mDatabase = FirebaseDatabase.getInstance().getReference("UserInfo");

        mDatabase.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() > 0) {

                    myProfileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}