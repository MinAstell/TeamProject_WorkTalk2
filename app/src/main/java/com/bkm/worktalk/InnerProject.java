package com.bkm.worktalk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InnerProject extends AppCompatActivity {

    private TextView tv_innerProjectName; //프로젝트 이름
    private TextView tv_innerProjectExplainOpened; //열린 프로젝트 설명
    private TextView tv_innerProjectExplainClosed; //닫힌 프로젝트 설명

    private ImageButton ib_addGoal; //누르면 목표생성 창이 뜸.
    private ImageButton ib_addMember; //누르면 멤버추가 창이 뜸.

    private RecyclerView rv_goalList; //목표 리스트 리사이클러뷰
    private RecyclerView rv_memberList; //멤버 리스트 리사이클러뷰

    private InnerProject_AddMember_Adapter memberAdapter;
    private ArrayList<InnerProject_AddMemberDTO> memberArrayList;
    private RecyclerView.LayoutManager layoutManager;

    public String projectName = ""; //프로젝트 이름 값 받아오는 변수
    public String projectExplain = ""; //프로젝트 설명 값 받아오는 변수

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innerproject);

        tv_innerProjectName = findViewById(R.id.tv_innerProjectName); //프로젝트 이름
        tv_innerProjectExplainOpened = findViewById(R.id.tv_innerProjectExplainOpened); //열린 프로젝트 설명
        tv_innerProjectExplainClosed = findViewById(R.id.tv_innerProjectExplainClosed); //닫힌 프로젝트 설명

        findViewById(R.id.ib_addGoal).setOnClickListener(mClickListener);; //누르면 목표생성 창이 뜸.
        findViewById(R.id.ib_addMember).setOnClickListener(mClickListener);; //누르면 멤버추가 창이 뜸.

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

    }
    //버튼 이벤트====================================================================================
    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_addGoal: //프로젝트 목표 옆의 이미지 버튼을 누르면 목표생성 창이 뜸.
                    Intent goal = new Intent(InnerProject.this, InnerProject_AddGoal.class);
                    startActivity(goal);
                    break;
                case R.id.ib_addMember: //프로젝트 멤버 옆의 이미지 버튼을 누르면 멤버추가 창이 뜸.
                    Intent member = new Intent(InnerProject.this, InnerProject_AddMember.class);
                    startActivity(member);
                    break;
            }
        }
    };

}