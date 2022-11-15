package com.bkm.worktalk.Project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bkm.worktalk.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class InnerProject_UpdateGoal extends AppCompatActivity {

    private TextView tv_updateGoalTitle; //프로젝트 목표 수정창 제목
    private EditText et_updateGoalName; //프로젝트 목표 수정될 이름
    private EditText et_updateGoalExplain; //프로젝트 목표 수정될 설명

    private String goalName; //프로젝트 목표 이름 받아오는 변수
    private String goalNameForChange; //프로젝트 목표 이름(수정용) 받아오는 변수
    private String goalExplain; //프로젝트 목표 설명 받아오는 변수

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innerproject_updategoal);

        tv_updateGoalTitle = findViewById(R.id.tv_updateGoalTitle); //프로젝트 목표 수정창 제목
        et_updateGoalName = findViewById(R.id.et_updateGoalName); //프로젝트 목표 수정될 이름
        et_updateGoalExplain = findViewById(R.id.et_updateGoalExplain); //프로젝트 목표 수정될 설명

        findViewById(R.id.ib_clearUpdateGoalName).setOnClickListener(mClickListener); //프로젝트 목표 이름 지우기 버튼
        findViewById(R.id.ib_clearUpdateGoalExplain).setOnClickListener(mClickListener); //프로젝트 목표 설명 지우기 버튼
        findViewById(R.id.btn_updateGoal).setOnClickListener(mClickListener); //프로젝트 목표 수정완료 버튼

        //프로젝트 목표의 이름, 설명을 가져옴============================================================
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        goalName = bundle.getString("goalName");
        goalNameForChange = bundle.getString("goalNameForChange");
        goalExplain = bundle.getString("goalExplain");
        tv_updateGoalTitle.setText(goalNameForChange + "의 정보 수정");
        et_updateGoalName.setText(goalNameForChange);
        et_updateGoalExplain.setText(goalExplain);
    }
    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_clearProjectName: //프로젝트 목표 제목 옆의 이미지 버튼을 누르면 해당 칸이 비워짐.
                    et_updateGoalName.setText("");
                    break;
                case R.id.ib_clearProjectExplain: //프로젝트 목표 설명 옆의 이미지 버튼을 누르면 해당 칸이 비워짐.
                    et_updateGoalExplain.setText("");
                    break;
                case R.id.btn_updateGoal: //목표 수정을 누르면 데이터가 업데이트됨.
                    String uProjectGoalName = et_updateGoalName.getText().toString();
                    String uProjectGoalExplain = et_updateGoalExplain.getText().toString();

                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("goalNameForChange", uProjectGoalName); //프로젝트 목표 이름 변경
                    taskMap.put("goalExplain", uProjectGoalExplain); //프로젝트 목표 설명 변경

                    database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                    databaseReference = database.getReference("projectGoalList"); // DB 테이블 연결

                    String projectName = ((InnerProject)InnerProject.mContext).projectName; //InnerProject에서 projectName의 값을 가져옴.
                    databaseReference.child(projectName).child(goalName).updateChildren(taskMap); //프로젝트 (변경용)이름, 설명 업데이트

                    Toast.makeText(InnerProject_UpdateGoal.this, "해당 목표의 정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();

                    ((InnerProject)InnerProject.mContext).goalListLoad(); //InnerProject의 목표 리사이클러뷰를 다시 불러옴.
                    break;
            }
        }
    };

}
