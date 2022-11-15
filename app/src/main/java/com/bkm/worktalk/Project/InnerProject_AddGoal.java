package com.bkm.worktalk.Project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bkm.worktalk.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InnerProject_AddGoal  extends AppCompatActivity {

    //파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    //데이터베이스의 특정 위치로 연결
    private DatabaseReference databaseReference = database.getReference();

    public String projectName = ""; //프로젝트 이름 값 받아오는 변수

    private EditText et_addGoalName; //프로젝트 목표명
    private EditText et_addGoalExplain; //프로젝트 목표 내용(설명)
    private Button btn_createGoal; //프로젝트 목표 생성 버튼

    private boolean isGoalSuccessed = false; //프로젝트 목표 달성 여부

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innerproject_addgoal);

        et_addGoalName = findViewById(R.id.et_addGoalName); //프로젝트 목표명
        et_addGoalExplain = findViewById(R.id.et_addGoalExplain); //프로젝트 목표 내용(설명)
        btn_createGoal = findViewById(R.id.btn_createGoal); //프로젝트 목표 생성 버튼

        //프로젝트 이름을 InnerProject에서 받아옴=======================================================
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        projectName = bundle.getString("projectName");

        //버튼 누르면 값을 저장=======================================================================
        btn_createGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //프로젝트 목표명이 비어있을시
                if(et_addGoalName.getText().toString().equals("")) {
                    showAlert("목표명을 정해주세요.", 0);
                    return;
                }
                //에딧 텍스트 값을 문자열로 바꾸어 함수에 넣어줌
                addGoal(et_addGoalName.getText().toString(), et_addGoalName.getText().toString(),
                        et_addGoalExplain.getText().toString(), isGoalSuccessed);

                Toast.makeText(InnerProject_AddGoal.this, "프로젝트 목표가 생성되었습니다!", Toast.LENGTH_SHORT).show();
                finish();

                ((InnerProject)InnerProject.mContext).goalListLoad(); //InnerProject의 목표 리사이클러뷰를 다시 불러옴.
            }
        });
    }
    //값을 파이어베이스 Realtime database로 넘기는 함수=================================================
    public void addGoal(String goalName, String goalNameForChange, String goalExplain, boolean isGoalSuccessed) {
        //DTO에서 선언했던 함수.
        InnerProject_AddGoalDTO goalDTO = new InnerProject_AddGoalDTO(goalName, goalNameForChange, goalExplain, isGoalSuccessed);

        //child는 해당 키 위치로 이동하는 함수입니다.
        //키가 없는데 값을 지정한 경우 자동으로 생성합니다.
        databaseReference.child("projectGoalList").child(projectName).child(goalName).setValue(goalDTO);
    }

    //다이얼로그 창 띄우는 메서드======================================================================
    public void showAlert(String msg, int chk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("").setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(chk == 1) {
                    finish();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
