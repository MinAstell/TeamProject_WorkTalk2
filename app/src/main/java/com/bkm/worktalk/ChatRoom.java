package com.bkm.worktalk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
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

    private ArrayList<ChatRoom_DTO.Comment> arrayList;
    private ChatRoom_Adapter chatRoom_adapter;
    private RecyclerView recyclerView;
    public  RecyclerView.Adapter mAdapter;
    private LinearLayoutManager linearLayoutManager;

    ImageView btnSend;
    TextView tv_friendName;
    EditText etComments;

    public String myName = "";
    public String myUid = "";
    public String chatRoomPath = "";
    public String myComment;
    public String friendToken = "";
    public String friendUid = "";

    ArrayList<JoinDTO> friendTokenList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();   // ChatRoom_DTO 객체를 담을 리스트

        mDatabase = FirebaseDatabase.getInstance().getReference("chatRoom");

        btnSend = (ImageView) findViewById(R.id.iv_btnSend);
        etComments = (EditText) findViewById(R.id.etComments);
        tv_friendName = (TextView) findViewById(R.id.tv_friendName);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        chatRoomPath = bundle.getString("chatRoomPath");
        myName = bundle.getString("myName");
        friendName = bundle.getString("friendName");
        myUid = bundle.getString("myUid");

        tv_friendName.setText(friendName);

//        getMyProfileImage();

        valueEventListener = mDatabase.child(chatRoomPath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() > 0) {

                    getLead();

                    arrayList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ChatRoom_DTO.Comment comment = snapshot.getValue(ChatRoom_DTO.Comment.class);

                        if (comment.createdTime.equals("")) {
                            arrayList.add(comment);
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일 hh:mm");
                String getTime = dateFormat.format(date);

                myComment = etComments.getText().toString();

                ChatRoom_DTO.Comment comment = new ChatRoom_DTO.Comment();
                comment.userName = myName;
                comment.userContents = myComment;
                comment.readUsers.put(myName, true);
//                comment.userProfileImageUrl = myProfileImageUrl;
                comment.timestamp = getTime;

                mDatabase.child(chatRoomPath).push().setValue(comment);

                pushMsg();

                etComments.setText("");
            }
        });
    }

    public void getLead() {

        mDatabase.child(chatRoomPath).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() > 0) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        ChatRoom_DTO.Comment comment = snapshot.getValue(ChatRoom_DTO.Comment.class);

                        Log.d("chatRoom", comment.createdTime);

                        if (comment.createdTime.equals("")) {

                            Map<String, Object> map = new HashMap<>();
                            map.clear();
                            map.put(myName, true);

                            mDatabase.child(chatRoomPath).child(snapshot.getKey()).child("readUsers").updateChildren(map);
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
        finish();
    }

    public void pushMsg() {

        mDatabase2 = FirebaseDatabase.getInstance().getReference("UserInfo");

        mDatabase2.orderByChild("name").equalTo(friendName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() > 0) {

                    Log.d("메시지", "들어옴");

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
                    notificationModel.data.title = "보낸 사람 : " + myName;
                    notificationModel.data.body = myComment;
                    notificationModel.data.sendingUser = myName;
                    notificationModel.data.chatRoomPath = chatRoomPath;
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

    public void getMyProfileImage() {  // 나중에 수정

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