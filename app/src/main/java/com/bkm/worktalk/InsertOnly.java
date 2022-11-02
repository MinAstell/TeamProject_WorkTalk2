package com.bkm.worktalk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class InsertOnly extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_test);

        mDatabase = FirebaseDatabase.getInstance().getReference("dept");

        InsertToDb();
    }
    public void showAlert(String msg) {    // 다이얼로그 창 띄우는 메서드
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("").setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void InsertToDb() {
        String email[] = {"aaa@daum.net", "bbb@daum.net", "ccc@daum.net", "ddd@daum.net"};
        String hp[] = {"01012341234", "01043214321", "01012345678", "01056785678"};
        String name[] = {"홍길동", "아무개", "짱구", "둘리"};

        for(int i=0; i<4; i++) {
            String i_s = String.valueOf(i+1);

            Map<String, Object> map = new HashMap<>();
            map.put("email", email[i]);
            map.put("hp", hp[i]);
            map.put("name", name[i]);

            mDatabase.child("서버개발팀").child(i_s).setValue(map);
        }

        showAlert("INSERT 완료!");
    }
}