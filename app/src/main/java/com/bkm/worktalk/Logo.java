package com.bkm.worktalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class Logo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        new Handler().postDelayed(new Runnable() { // 로고 전체 레이아웃 없애는 핸들러
            @Override
            public void run() {
                //Intent intent = new Intent(Logo.this, MainActivity.class);
                Intent intent = new Intent(Logo.this, Login.class);
                startActivity(intent); // 메인 액티비티 시작
                finish();
            }
        }, 1000);
    }
}