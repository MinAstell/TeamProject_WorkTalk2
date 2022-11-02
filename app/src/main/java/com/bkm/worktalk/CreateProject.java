package com.bkm.worktalk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateProject extends AppCompatActivity {

    //파이어베이스 데이터베이스 연동
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //데이터베이스의 특정 위치로 연결
    private DatabaseReference databaseReference = database.getReference();

    Button btn_createProject; //프로젝트 생성 버튼
    EditText et_createProjectName; //프로젝트 이름
    EditText et_createProjectExplain; //프로젝트 설명

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        btn_createProject = findViewById(R.id.btn_createProject);
        et_createProjectName = findViewById(R.id.et_createProjectName);
        et_createProjectExplain = findViewById(R.id.et_createProjectExplain);

        //버튼 누르면 값을 저장=======================================================================
        btn_createProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //프로젝트 이름이 비어있을시
                if(et_createProjectName.getText().toString().equals("")) {
                    showAlert("프로젝트 이름을 정해주세요.", 0);
                    return;
                }
                //에딧 텍스트 값을 문자열로 바꾸어 함수에 넣어줌
                addProject(et_createProjectName.getText().toString(), et_createProjectExplain.getText().toString());

                showAlert("프로젝트가 생성되었습니다.", 1);
            }
        });
    }
    //값을 파이어베이스 Realtime database로 넘기는 함수=================================================
    public void addProject(String projectName, String ProjectExplain) {
        //DTO에서 선언했던 함수.
        ProjectDTO projectDTO = new ProjectDTO(projectName, ProjectExplain);

        //child는 해당 키 위치로 이동하는 함수입니다.
        //키가 없는데 값을 지정한 경우 자동으로 생성합니다.
        databaseReference.child("projectList").child(projectName).setValue(projectDTO);
    }

    // 다이얼로그 창 띄우는 메서드=====================================================================
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
