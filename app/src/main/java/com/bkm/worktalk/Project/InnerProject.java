package com.bkm.worktalk.Project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bkm.worktalk.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InnerProject extends AppCompatActivity {

    //목표리스트 리사이클러뷰 getter, setter===========================================================
    private String goalName;
    private String goalNameForChange;
    private String goalExplain;
    private boolean isGoalSuccessed;

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getGoalNameForChange() {
        return goalNameForChange;
    }

    public void setGoalNameForChange(String goalNameForChange) {
        this.goalNameForChange = goalNameForChange;
    }

    public String getGoalExplain() {
        return goalExplain;
    }

    public void setGoalExplain(String goalExplain) {
        this.goalExplain = goalExplain;
    }

    public boolean getGoalSuccessed() {
        return isGoalSuccessed;
    }

    public void setGoalSuccessed(boolean goalSuccessed) {
        isGoalSuccessed = goalSuccessed;
    }

    //멤버리스트 리사이클러뷰 getter, setter===========================================================
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

    private RecyclerView rv_goalList; //목표 리스트 리사이클러뷰
    private RecyclerView rv_memberList; //멤버 리스트 리사이클러뷰

    private InnerProject_AddGoal_Adapter goalAdapter;
    private InnerProject_MemberList_Adapter memberAdapter;
    private ArrayList<InnerProject_AddGoalDTO> goalArrayList;
    private ArrayList<InnerProject_AddMemberDTO> memberArrayList;
    private RecyclerView.LayoutManager layoutManager1;
    private RecyclerView.LayoutManager layoutManager;

    public String projectName = ""; //프로젝트 이름 값 받아오는 변수
    public String projectExplain = ""; //프로젝트 설명 값 받아오는 변수
    public String projectNameForChange = ""; //프로젝트 이름 변경용(업데이트용) 값 받아오는 변수

    public static Context mContext; //프로젝트 이름 값 보내주는 context

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReferenceProject;
    private DatabaseReference databaseReferenceGoal;
    private DatabaseReference databaseReferenceMember;

    private SwipeRefreshLayout srl_goalList; //프로젝트 목표 리사이클러뷰 레이아웃 새로고침
    private SwipeRefreshLayout srl_memberList; //프로젝트 멤버 리사이클러뷰 레이아웃 새로고침

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innerproject);

        tv_innerProjectName = findViewById(R.id.tv_innerProjectName); //프로젝트 이름
        tv_innerProjectExplainOpened = findViewById(R.id.tv_innerProjectExplainOpened); //열린 프로젝트 설명
        tv_innerProjectExplainClosed = findViewById(R.id.tv_innerProjectExplainClosed); //닫힌 프로젝트 설명

        findViewById(R.id.ib_innerProjectExit).setOnClickListener(mClickListener); //프로젝트 나가기 버튼
        findViewById(R.id.btn_deleteProject).setOnClickListener(mClickListener); //프로젝트 삭제 버튼
        findViewById(R.id.ib_addGoal).setOnClickListener(mClickListener); //누르면 목표생성 창이 뜸.
        findViewById(R.id.ib_addMember).setOnClickListener(mClickListener); //누르면 멤버추가 창이 뜸.

        rv_goalList = findViewById(R.id.rv_goalList); //목표 리스트 리사이클러뷰
        rv_memberList = findViewById(R.id.rv_memberList); //멤버 리스트 리사이클러뷰
        rv_goalList.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        rv_memberList.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(getApplicationContext());
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_goalList.setLayoutManager(layoutManager1);
        rv_memberList.setLayoutManager(layoutManager);
        memberArrayList = new ArrayList<>();
        goalArrayList = new ArrayList<>();

        tv_innerProjectExplainOpened.setVisibility(View.INVISIBLE); //프로젝트 설명은 기본적으로 닫혀있음.

        mContext = this; //oncreate 에 this(=액티비티 클래스 자체를 의미) 할당

        //프로젝트 이름, 설명의 값을 가져옴=============================================================
        getData();

        //프로젝트 이름 터치시 이름 및 설명 변경 창이 뜸.=================================================
        tv_innerProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InnerProject.this, InnerProject_TitleOption.class);
                intent.putExtra("projectName", projectName);
                intent.putExtra("projectExplain", projectExplain);
                intent.putExtra("projectNameForChange", projectNameForChange);
                startActivity(intent);
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

        //프로젝트 목표 리사이클러뷰====================================================================
        goalListLoad();

        //프로젝트 목표 스와이프 리플레쉬 레이아웃========================================================
        srl_goalList = findViewById(R.id.srl_goalList);
        srl_goalList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                goalListLoad();
                srl_goalList.setRefreshing(false); //새로고침을 멈추는 용도
            }
        });

        //프로젝트 멤버 리사이클러뷰====================================================================
        memberListLoad();

        //프로젝트 목표 스와이프 리플레쉬 레이아웃========================================================
        srl_memberList = findViewById(R.id.srl_memberList);
        srl_memberList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                memberListLoad();
                srl_memberList.setRefreshing(false); //새로고침을 멈추는 용도
            }
        });

    }
    //버튼 이벤트====================================================================================
    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_innerProjectExit: //프로젝트 닫기 버튼
                    finish();
                    break;
                case R.id.btn_deleteProject: //프로젝트 삭제 버튼
                    onClickShowAlert();
                    break;
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

    //삭제버튼 클릭시 뜨는 경고창=======================================================================
    public void onClickShowAlert() {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(InnerProject.this);
        //alert의 title과 Messege 세팅
        myAlertBuilder.setTitle("프로젝트 삭제");
        myAlertBuilder.setMessage("정말로 해당 프로젝트를 삭제하시겠습니까?");

        //버튼 추가
        myAlertBuilder.setPositiveButton("예",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                //예 버튼을 눌렸을 경우
                onDeleteGoal(); //목표 삭제
                onDeleteMember(); //멤버 삭제
                onDeleteProject(); //프로젝트 삭제
            }
        });
        myAlertBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //아니오 버튼을 눌렸을 경우
            }
        });

        // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
        myAlertBuilder.show();
    }

    //프로젝트 삭제===================================================================================
    private void onDeleteProject() {
        database = FirebaseDatabase.getInstance();
        databaseReferenceProject = database.getReference("projectList").child(projectName);

        databaseReferenceProject.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) { //삭제 성공시
                Toast.makeText(InnerProject.this, "프로젝트가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { //삭제 실패시
                System.out.println("error: "+e.getMessage());
                Toast.makeText(InnerProject.this, "삭제에 실패했습니다. 다시 시도해 주십시오.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //프로젝트 삭제시 해당 프로젝트에 저장된 목표도 DB에서 삭제됨============================================
    private void onDeleteGoal() {
        database = FirebaseDatabase.getInstance();
        databaseReferenceGoal = database.getReference("projectGoalList").child(projectName);

        databaseReferenceGoal.removeValue();
    }
    //프로젝트 삭제시 해당 프로젝트에 저장된 멤버도 DB에서 삭제됨============================================
    private void onDeleteMember() {
        database = FirebaseDatabase.getInstance();
        databaseReferenceMember = database.getReference("projectMemberList").child(projectName);

        databaseReferenceMember.removeValue();
    }

    //FragProject에서 데이터를 가져옴==================================================================
    public void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        projectName = bundle.getString("projectName");
        projectExplain = bundle.getString("projectExplain");
        projectNameForChange = bundle.getString("projectNameForChange");
        tv_innerProjectName.setText(projectNameForChange);
        tv_innerProjectExplainOpened.setText(projectExplain);
        tv_innerProjectExplainClosed.setText(projectExplain);
    }

    //프로젝트 목표를 파이어베이스 DB에서 가져옴==========================================================
    public void goalListLoad() {
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference1 = database.getReference("projectGoalList"); // DB 테이블 연결
        Query myProjectGoalQuery = databaseReference1.child(projectName); //연결된 DB의 child
        myProjectGoalQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                goalArrayList.clear(); //기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 List를 추출해냄
                    InnerProject_AddGoalDTO goalDTO = snapshot.getValue(InnerProject_AddGoalDTO.class); // 만들어뒀던 객체에 데이터를 담는다.
                    goalArrayList.add(goalDTO); //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                goalAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침해야 반영이 됨
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(databaseError.toException())); //에러문 출력
            }
        });
        goalAdapter = new InnerProject_AddGoal_Adapter(goalArrayList, getGoalName(), getGoalNameForChange(),
                getGoalExplain(), getGoalSuccessed(), getApplicationContext());
        rv_goalList.setAdapter(goalAdapter); //리사이클러뷰에 어댑터 연결
    }

    //프로젝트 멤버를 파이어베이스 DB에서 가져옴==========================================================
    public void memberListLoad() {
        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("projectMemberList"); //DB 테이블 연결
        Query myProjectMemberQuery = databaseReference.child(projectName); //연결된 DB의 child
        myProjectMemberQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                memberArrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 List를 추출해냄
                    InnerProject_AddMemberDTO memberDTO = snapshot.getValue(InnerProject_AddMemberDTO.class); //만들어뒀던 객체에 데이터를 담는다.
                    memberArrayList.add(memberDTO); //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                memberAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침해야 반영이 됨
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(databaseError.toException())); //에러문 출력
            }
        });
        memberAdapter = new InnerProject_MemberList_Adapter(memberArrayList, getMemberListName(),
                getMemberListEmail(), getMemberListHP(), getApplicationContext());
        rv_memberList.setAdapter(memberAdapter); //리사이클러뷰에 어댑터 연결
    }

}