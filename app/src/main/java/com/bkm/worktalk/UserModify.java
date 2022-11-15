package com.bkm.worktalk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bkm.worktalk.BeginApp.Login;
import com.bkm.worktalk.DTO.JoinDTO;
import com.bkm.worktalk.DTO.TalkListsDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

public class UserModify extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private String myUid;

    String deptno = "";
    String emailId = "";
    String empno = "";
    String hp = "";
    String job = "";
    String name = "";
    String pw = "";

    EditText
             et_pw,         // 회원가입 창 비밀번호 입력하는 텍스트
             et_pw2,        // 회원가입 창 비밀번호 확인 입력하는 텍스트
             et_name,       // 회원가입 창 이름 입력하는 텍스트
             et_emp,        // 회원가입 창 사원번호 입력하는 텍스트
             et_phone,      // 회원가입 창 전화번호 입력하는 텍스트
             et_dept,       // 회원가입 창 부서이름 입력하는 텍스트
             et_job;        // 회원가입 창 직책 입력하는 텍스트

    ImageButton
                ib_all_dell_2,     // 회원가입 창 비밀번호 한번에 삭제하는 이미지버튼
                ib_all_dell_3,     // 회원가입 창 비밀번호 확인 한번에 삭제하는 이미지버튼
                ib_all_dell_4,     // 회원가입 창 이름 한번에 삭제하는 이미지버튼
                ib_all_dell_5,     // 회원가입 창 사원번호 한번에 삭제하는 이미지버튼
                ib_all_dell_6,     // 회원가입 창 전화번호 한번에 삭제하는 이미지버튼
                ib_all_dell_7,     // 회원가입 창 부서이름 한번에 삭제하는 이미지버튼
                ib_all_dell_8;     // 회원가입 창 직책 한번에 삭제하는 이미지버튼

    Button btn_join;          // 회원가입 창 회원가입 버튼

    ArrayList<JoinDTO> userInfo = new ArrayList<>(); // DB 유저 이름과 메일(ID)을 담는 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_modify);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // 액티비티가 넘어갈때 원하는 레이아웃이 뜨도록 하는 PutExtra를 받는 GetExtra
        Intent intent = getIntent();
        String text = intent.getStringExtra("0");

        mDatabase = FirebaseDatabase.getInstance().getReference("UserInfo");
        mAuth = FirebaseAuth.getInstance();

        et_pw = (EditText) findViewById(R.id.et_pw);
        et_pw2 = (EditText) findViewById(R.id.et_pw2);
        et_name = (EditText) findViewById(R.id.et_name);
        et_emp = (EditText) findViewById(R.id.et_emp);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_dept = (EditText) findViewById(R.id.et_dept);
        et_job = (EditText) findViewById(R.id.et_job);

        btn_join = (Button) findViewById(R.id.btn_join);

        ib_all_dell_2 = (ImageButton) findViewById(R.id.ib_all_del2);
        ib_all_dell_3 = (ImageButton) findViewById(R.id.ib_all_del3);
        ib_all_dell_4 = (ImageButton) findViewById(R.id.ib_all_del4);
        ib_all_dell_5 = (ImageButton) findViewById(R.id.ib_all_del5);
        ib_all_dell_6 = (ImageButton) findViewById(R.id.ib_all_del6);
        ib_all_dell_7 = (ImageButton) findViewById(R.id.ib_all_del7);
        ib_all_dell_8 = (ImageButton) findViewById(R.id.ib_all_del8);

        myInfoSet();

        // 회원가입 레이아웃
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pw = et_pw.getText().toString().trim();
                String rePw = et_pw2.getText().toString().trim();
                String name = et_name.getText().toString().trim();
                String emp = et_emp.getText().toString().trim();
                String hp = et_phone.getText().toString().trim();
                String dept = et_dept.getText().toString().trim();
                String job = et_job.getText().toString().trim();

                if(pw.equals("")) {
                    showAlert("비밀번호를 입력해주세요.", 0);
                    return;
                }
                if(rePw.equals("")) {
                    showAlert("비밀번호를 확인해주세요.", 0);
                    return;
                }
                if(name.equals("")) {
                    showAlert("이름을 입력해주세요.", 0);
                    return;
                }
                if(emp.equals("")) {
                    showAlert("사원번호를 입력해주세요.", 0);
                    return;
                }
                if(hp.equals("")) {
                    showAlert("전화번호를 입력해주세요.", 0);
                    return;
                }
                if(dept.equals("")) {
                    showAlert("부서를 입력해주세요.", 0);
                    return;
                }
                if(job.equals("")) {
                    showAlert("직급을 입력해주세요.", 0);
                    return;
                }
                if(!pw.equals(rePw)) {
                    showAlert("비밀번호가 일치하지 않습니다.", 0);
                    return;
                }

                String myUid = Login.appData.getString("myUid", "");
                HashMap<String, Object> map = new HashMap<>();
                map.put("pw", pw);
                map.put("name", name);
                map.put("empno", emp);
                map.put("hp", hp);
                map.put("deptno", dept);
                map.put("job", job);
                mDatabase.child(myUid).updateChildren(map);
                showAlert("수정완료", 1);
            }
        });

        // 각각 텍스트를 한번에 지우는 버튼 모음
        ib_all_dell_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_pw.setText("");
            }
        });
        ib_all_dell_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_pw2.setText("");
            }
        });
        ib_all_dell_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_name.setText("");
            }
        });
        ib_all_dell_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_emp.setText("");
            }
        });
        ib_all_dell_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_phone.setText("");
            }
        });
        ib_all_dell_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_dept.setText("");
            }
        });
        ib_all_dell_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_job.setText("");
            }
        });
    }
    // 다이얼로그 창 띄우는 메서드
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
    public void myInfoSet() {
        String myUid = Login.appData.getString("myUid", "");
        mDatabase.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                        JoinDTO joinDTO = dataSnapshot.getValue(JoinDTO.class);
                        pw = joinDTO.pw;
                        name = joinDTO.name;
                        empno = joinDTO.empno;
                        hp = joinDTO.hp;
                        deptno = joinDTO.deptno;
                        job = joinDTO.job;

                        et_pw.setText(pw);
                        et_name.setText(name);
                        et_emp.setText(empno);
                        et_phone.setText(hp);
                        et_dept.setText(deptno);
                        et_job.setText(job);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}