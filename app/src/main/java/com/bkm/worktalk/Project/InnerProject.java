package com.bkm.worktalk.Project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkm.worktalk.Adapter.InnerProject_MemberList_Adapter;
import com.bkm.worktalk.BeginApp.Login;
import com.bkm.worktalk.DTO.InnerProject_AddMemberDTO;
import com.bkm.worktalk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class InnerProject extends AppCompatActivity {

    private String memberListName;
    private String memberListEmail;
    private String memberListHP;

    public String getMemberListName() {
        return memberListName;
    }

    public void setMemberListName(String memberListName) {
        this.memberListName = memberListName;
    }

    public String getMemberListEmail() {
        return memberListEmail;
    }

    public void setMemberListEmail(String memberListEmail) {
        this.memberListEmail = memberListEmail;
    }

    public String getMemberListHP() {
        return memberListHP;
    }

    public void setMemberListHP(String memberListHP) {
        this.memberListHP = memberListHP;
    }

    private TextView tv_innerProjectName; //프로젝트 이름
    private TextView tv_innerProjectExplainOpened; //열린 프로젝트 설명
    private TextView tv_innerProjectExplainClosed; //닫힌 프로젝트 설명

    private ImageButton ib_addGoal; //누르면 목표생성 창이 뜸.
    private ImageButton ib_addMember; //누르면 멤버추가 창이 뜸.

    private Button btn_connChatroom;

    private RecyclerView rv_goalList; //목표 리스트 리사이클러뷰
    private RecyclerView rv_memberList; //멤버 리스트 리사이클러뷰

    private InnerProject_MemberList_Adapter memberAdapter;
    private ArrayList<InnerProject_AddMemberDTO> memberArrayList;
    private ArrayList<String> memberListToChatRoom;
    private RecyclerView.LayoutManager layoutManager;

    public String projectName = ""; //프로젝트 이름 값 받아오는 변수
    public String projectExplain = ""; //프로젝트 설명 값 받아오는 변수

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innerproject);

        tv_innerProjectName = findViewById(R.id.tv_innerProjectName); //프로젝트 이름
        tv_innerProjectExplainOpened = findViewById(R.id.tv_innerProjectExplainOpened); //열린 프로젝트 설명
        tv_innerProjectExplainClosed = findViewById(R.id.tv_innerProjectExplainClosed); //닫힌 프로젝트 설명
        btn_connChatroom = findViewById(R.id.btn_connChatroom);

        findViewById(R.id.ib_addGoal).setOnClickListener(mClickListener);//누르면 목표생성 창이 뜸.
        findViewById(R.id.ib_addMember).setOnClickListener(mClickListener);//누르면 멤버추가 창이 뜸.

        rv_goalList = findViewById(R.id.rv_goalList); //목표 리스트 리사이클러뷰
        rv_memberList = findViewById(R.id.rv_memberList); //멤버 리스트 리사이클러뷰
        rv_goalList.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        rv_memberList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_memberList.setLayoutManager(layoutManager);
        memberArrayList = new ArrayList<>();

        tv_innerProjectExplainOpened.setVisibility(View.INVISIBLE); //프로젝트 설명은 기본적으로 닫혀있음.

        //DB에 저장된 프로젝트 이름, 설명을 가져옴=======================================================
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        projectName = bundle.getString("projectName");
        projectExplain = bundle.getString("projectExplain");
        tv_innerProjectName.setText(projectName);
        tv_innerProjectExplainOpened.setText(projectExplain);
        tv_innerProjectExplainClosed.setText(projectExplain);

        // 채팅방으로 버튼 작업 (누르면 채팅방 생성 및 이동기능) : 백경민 작업
        btn_connChatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("버튼", "버튼눌림");
                enterToChatRoom();
            }
        });

        //닫힌 프로젝트 설명 클릭시 프로젝트가 열림=======================================================
        tv_innerProjectExplainClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_innerProjectExplainOpened.setVisibility(View.VISIBLE);
                tv_innerProjectExplainClosed.setVisibility(View.INVISIBLE);
            }
        });
        //열린 프로젝트 설명 클릭시 프로젝트가 닫힘=======================================================
        tv_innerProjectExplainOpened.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_innerProjectExplainOpened.setVisibility(View.INVISIBLE);
                tv_innerProjectExplainClosed.setVisibility(View.VISIBLE);
            }
        });

        //프로젝트 멤버 리사이클러뷰====================================================================
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("projectMemberList"); // DB 테이블 연결
        Query myProjectMemberQuery = databaseReference.child(projectName); //연결된 DB의 child
        myProjectMemberQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                memberArrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 List를 추출해냄
                    InnerProject_AddMemberDTO memberDTO = snapshot.getValue(InnerProject_AddMemberDTO.class); // 만들어뒀던 객체에 데이터를 담는다.
                    memberArrayList.add(memberDTO); //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                memberAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침해야 반영이 됨
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        memberAdapter = new InnerProject_MemberList_Adapter(memberArrayList, getMemberListName(),
                getMemberListEmail(), getMemberListHP(), getApplicationContext());
        rv_memberList.setAdapter(memberAdapter); //리사이클러뷰에 어댑터 연결
    }
    //버튼 이벤트====================================================================================
    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_addGoal: //프로젝트 목표 옆의 이미지 버튼을 누르면 목표생성 창이 뜸.
                    Intent goal = new Intent(InnerProject.this, InnerProject_AddGoal.class);
                    goal.putExtra("projectName", projectName); //DB에서 프로젝트 구분을 하기 위해 이름을 보냄.
                    startActivity(goal);
                    break;
                case R.id.ib_addMember: //프로젝트 멤버 옆의 이미지 버튼을 누르면 멤버추가 창이 뜸.
                    Intent member = new Intent(InnerProject.this, InnerProject_AddMember.class);
                    member.putExtra("projectName", projectName);
                    startActivity(member);
                    break;
            }
        }
    };

    // 채팅방 생성 및 이동기능을 가진 메소드 : 백경민 작업
    // 현재 리사이클러뷰 리스트가 갱신이 되지 않는 관계로 디비데이터 다시 끌어오도록 작업했습니다.
    public void enterToChatRoom() {
        databaseReference.child(projectName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    databaseReference2 = database.getReference("chatRoom");
                    databaseReference2.child(projectName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            if (dataSnapshot2.getChildrenCount() > 0) {
                                return;
                            }
                            HashMap<String, Object> map = new HashMap<>();
                            memberArrayList.clear();
                            Log.d("리스너", "들어옴");
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.d("리스너 안 for문", "들어옴");
                                InnerProject_AddMemberDTO memberDTO = snapshot.getValue(InnerProject_AddMemberDTO.class);
                                Log.d("memberDTO", memberDTO.email);
                                memberArrayList.add(memberDTO);
                            }
                            databaseReference2 = database.getReference("chatRoom");
                            String myUid = Login.appData.getString("myUid", "");
                            databaseReference3 = database.getReference("eachUserChatRoomInfo");
                            int count = 0;
                            while (count != memberArrayList.size()) {
                                Log.d("count", String.valueOf(count));
                                Log.d("memberArrayList", memberArrayList.get(count).getName());
                                map.clear();
                                map.put("userEmail", memberArrayList.get(count).getEmail());
                                Log.d("map", memberArrayList.get(count).getEmail());
                                Log.d("map", memberArrayList.get(count).getName());
                                databaseReference2.child(projectName).push().updateChildren(map);
                                Log.d("제발", "들어옴");
                                ++count;
                            }
                            map.clear();
                            map.put("chatRoomPath", projectName);
                            databaseReference3.child(myUid).push().updateChildren(map);
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
