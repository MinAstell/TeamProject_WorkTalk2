package com.bkm.worktalk.Project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bkm.worktalk.R;

public class InnerProject_InnerGoal extends AppCompatActivity {

    private TextView tv_goalName; //프로젝트 목표명
    private TextView tv_goalExplain; //프로젝트 목표 내용(설명)

    public String goalNameForChange = ""; //프로젝트 목표명 값 받아오는 변수
    public String goalExplain = ""; //프로젝트 목표 내용(설명) 값 받아오는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innergoal);

        tv_goalName = findViewById(R.id.tv_goalName); //프로젝트 목표명
        tv_goalExplain = findViewById(R.id.tv_goalExplain); //프로젝트 목표 내용(설명)

        //프로젝트 목표명, 내용(설명)의 값을 가져옴=======================================================
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        goalNameForChange = bundle.getString("goalNameForChange");
        goalExplain = bundle.getString("goalExplain");
        tv_goalName.setText(goalNameForChange);
        tv_goalExplain.setText(goalExplain);
    }

}
