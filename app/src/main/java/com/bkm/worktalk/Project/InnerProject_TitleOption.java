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

public class InnerProject_TitleOption extends AppCompatActivity {

    private TextView tv_titleOption; //옵션 제목
    private EditText et_changeProjectName; //프로젝트 제목 변경
    private EditText et_changeProjectExplain; //프로젝트 설명 변경

    public String projectName = ""; //프로젝트 이름 값 받아오는 변수
    public String projectExplain = ""; //프로젝트 설명 값 받아오는 변수
    public String projectNameForChange = ""; //프로젝트 이름 업데이트용 값 받아오는 변수

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innerproject_titleoption);

        tv_titleOption = findViewById(R.id.tv_titleOption); //옵션 제목
        et_changeProjectName = findViewById(R.id.et_changeProjectName); //프로젝트 제목 변경 칸
        et_changeProjectExplain = findViewById(R.id.et_changeProjectExplain); //프로젝트 설명 변경 칸

        findViewById(R.id.ib_clearProjectName).setOnClickListener(mClickListener); //프로젝트 설명 지우는 버튼
        findViewById(R.id.ib_clearProjectExplain).setOnClickListener(mClickListener); //프로젝트 설명 지우는 버튼
        findViewById(R.id.btn_changeProject).setOnClickListener(mClickListener); //변경내용 저장 버튼

        //프로젝트 이름, 설명의 값을 가져옴=============================================================
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        projectName = bundle.getString("projectName");
        projectExplain = bundle.getString("projectExplain");
        projectNameForChange = bundle.getString("projectNameForChange");
        tv_titleOption.setText(projectNameForChange + "의 설정");
        et_changeProjectName.setText(projectNameForChange);
        et_changeProjectExplain.setText(projectExplain);
    }
    //버튼 이벤트====================================================================================
    Button.OnClickListener mClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_clearProjectName: //프로젝트 제목 옆의 이미지 버튼을 누르면 해당 칸이 비워짐.
                    et_changeProjectName.setText("");
                    break;
                case R.id.ib_clearProjectExplain: //프로젝트 설명 옆의 이미지 버튼을 누르면 해당 칸이 비워짐.
                    et_changeProjectExplain.setText("");
                    break;
                case R.id.btn_changeProject: //프로젝트 변경을 누르면 데이터가 업데이트됨.
                    String uProjectName = et_changeProjectName.getText().toString();
                    String uProjectExplain = et_changeProjectExplain.getText().toString();

                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("projectNameForChange", uProjectName); //프로젝트 이름 변경
                    taskMap.put("projectExplain", uProjectExplain); //프로젝트 설명 변경

                    database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                    databaseReference = database.getReference("projectList"); // DB 테이블 연결

                    databaseReference.child(projectName).updateChildren(taskMap); //프로젝트 (변경용)이름, 설명 업데이트

                    Toast.makeText(InnerProject_TitleOption.this, "프로젝트 정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();

                    ((InnerProject)InnerProject.mContext).getData(); //InnerProject의 이름, 설명의 값을 다시 불러옴.

                    break;
            }
        }
    };

}
