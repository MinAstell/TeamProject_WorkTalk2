package com.bkm.worktalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Join_Find extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private String myUid;

    EditText et_mail_login, // 로그인 창 메일(ID겸) 입력하는 텍스트
             et_pw_login,   // 로그인 창 비밀번호 입력하는 텍스트
             et_mail,       // 회원가입 창 메일(ID겸) 입력하는 텍스트
             et_pw,         // 회원가입 창 비밀번호 입력하는 텍스트
             et_pw2,        // 회원가입 창 비밀번호 확인 입력하는 텍스트
             et_name,       // 회원가입 창 이름 입력하는 텍스트
             et_emp,        // 회원가입 창 사원번호 입력하는 텍스트
             et_phone,      // 회원가입 창 전화번호 입력하는 텍스트
             et_dept,       // 회원가입 창 부서이름 입력하는 텍스트
             et_job,        // 회원가입 창 직책 입력하는 텍스트
             et_inpEmp,     // 계정 찾기 창 사원번호 입력하는 텍스트
             et_inpEmail,   // 비밀번호 찾기 창 메일(ID겸) 입력하는 텍스트
             et_newPw,      // 비밀번호 찾기 창 비밀번호 재설정 입력하는 텍스트
             et_newRePw;    // 비밀번호 찾기 창 비밀번호 재설정 확인 입력하는 텍스트

    ImageButton ib_all_del1_login, // 로그인 창 메일(ID겸) 한번에 삭제하는 이미지버튼
                ib_all_del2_login, // 로그인 창 비밀번호 한번에 삭제하는 이미지버튼
                ib_all_dell_1,     // 회원가입 창 메일(ID겸) 한번에 삭제하는 이미지버튼
                ib_all_dell_2,     // 회원가입 창 비밀번호 한번에 삭제하는 이미지버튼
                ib_all_dell_3,     // 회원가입 창 비밀번호 확인 한번에 삭제하는 이미지버튼
                ib_all_dell_4,     // 회원가입 창 이름 한번에 삭제하는 이미지버튼
                ib_all_dell_5,     // 회원가입 창 사원번호 한번에 삭제하는 이미지버튼
                ib_all_dell_6,     // 회원가입 창 전화번호 한번에 삭제하는 이미지버튼
                ib_all_dell_7,     // 회원가입 창 부서이름 한번에 삭제하는 이미지버튼
                ib_all_dell_8;     // 회원가입 창 직책 한번에 삭제하는 이미지버튼

    Button btn_login,               // 로그인 창 로그인 버튼
           btn_join,                // 회원가입 창 회원가입 버튼
           btn_sendToDbFindYourAct, // 계정 찾기 창 계정 찾기 버튼
           btn_sendToDbFindYourPw,  // 비밀번호 찾기 창 비밀번호 변경 버튼
           btn_modifyPw;            // 비밀번호 찾기 창 비밀번호 재설정 버튼

    LinearLayout loginPage2, // 회원가입 가리는 로고있는 백그라운드 레이아웃
                 selAccount, // 계정 찾기 레이아웃
                 selPw,      // 비밀번호 찾기 레이아웃
                 modifyPw;   // 비밀번호를 찾은 뒤 비밀번호가 있을경우 재설정을 하게 뜨는 레이아웃

    TextView tv_sucFindYourId, // 계정주인이름 의 계정은 다음과 같습니다 라고 뜨는 텍스트뷰
             tv_viewId;        // 찾은계정이 뜨는 텍스트뷰

    ArrayList<JoinDTO> userInfo = new ArrayList<>(); // DB 유저 이름과 메일(ID)을 담는 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_find);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // 액티비티가 넘어갈때 원하는 레이아웃이 뜨도록 하는 PutExtra를 받는 GetExtra
        Intent intent = getIntent();
        String text = intent.getStringExtra("0");

        mDatabase = FirebaseDatabase.getInstance().getReference("UserInfo");
        mAuth = FirebaseAuth.getInstance();

        et_mail = (EditText) findViewById(R.id.et_mail);
        et_pw = (EditText) findViewById(R.id.et_pw);
        et_pw2 = (EditText) findViewById(R.id.et_pw2);
        et_name = (EditText) findViewById(R.id.et_name);
        et_emp = (EditText) findViewById(R.id.et_emp);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_dept = (EditText) findViewById(R.id.et_dept);
        et_job = (EditText) findViewById(R.id.et_job);
        et_mail_login = (EditText) findViewById(R.id.et_mail_login);
        et_pw_login = (EditText) findViewById(R.id.et_pw_login);
        et_inpEmp = (EditText) findViewById(R.id.et_inpEmp);
        et_inpEmail = (EditText) findViewById(R.id.et_inpEmail);
        et_newPw = (EditText) findViewById(R.id.et_newPw);
        et_newRePw = (EditText) findViewById(R.id.et_newRePw);

        tv_sucFindYourId = (TextView) findViewById(R.id.tv_sucFindYourId);
        tv_viewId = (TextView) findViewById(R.id.tv_viewId);

        btn_join = (Button) findViewById(R.id.btn_join);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_sendToDbFindYourAct = (Button) findViewById(R.id.btn_sendToDbFindYourAct);
        btn_sendToDbFindYourPw = (Button) findViewById(R.id.btn_sendToDbFindYourPw);
        btn_modifyPw = (Button) findViewById(R.id.btn_modifyPw);

        ib_all_dell_1 = (ImageButton) findViewById(R.id.ib_all_del1);
        ib_all_dell_2 = (ImageButton) findViewById(R.id.ib_all_del2);
        ib_all_dell_3 = (ImageButton) findViewById(R.id.ib_all_del3);
        ib_all_dell_4 = (ImageButton) findViewById(R.id.ib_all_del4);
        ib_all_dell_5 = (ImageButton) findViewById(R.id.ib_all_del5);
        ib_all_dell_6 = (ImageButton) findViewById(R.id.ib_all_del6);
        ib_all_dell_7 = (ImageButton) findViewById(R.id.ib_all_del7);
        ib_all_dell_8 = (ImageButton) findViewById(R.id.ib_all_del8);
        ib_all_del1_login = (ImageButton) findViewById(R.id.ib_all_del1_login);
        ib_all_del2_login = (ImageButton) findViewById(R.id.ib_all_del2_login);

        loginPage2 = (LinearLayout) findViewById(R.id.loginPage2);
        selAccount = (LinearLayout) findViewById(R.id.selAccount);
        selPw = (LinearLayout) findViewById(R.id.selPw);
        modifyPw = (LinearLayout) findViewById(R.id.modifyPw);

        // 액티비티가 넘어갈때 원하는 레이아웃이 뜨도록 하는 PutExtra를 받는 GetExtra
        if(text.equals("0")) {
            selAccount.setVisibility(View.INVISIBLE);
            selPw.setVisibility(View.INVISIBLE);
            loginPage2.setVisibility(View.INVISIBLE);
        } else if(text.equals("1")) {
            selAccount.setVisibility(View.VISIBLE);
            selPw.setVisibility(View.INVISIBLE);
            loginPage2.setVisibility(View.VISIBLE);
        } else if(text.equals("2")) {
            selAccount.setVisibility(View.INVISIBLE);
            selPw.setVisibility(View.VISIBLE);
            loginPage2.setVisibility(View.VISIBLE);
        }
        // 비밀번호 찾기의 비밀번호 변경 버튼 (메일 링크로 비번 재설정)
        btn_sendToDbFindYourPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_inpEmail.getText().toString().trim();

                if(email.equals("")) {
                    showAlert("이메일을 입력해주세요.", 0);
                    return;
                }
                mDatabase.orderByChild("emailId").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                myUid = snapshot.getKey();
                            }

                            String myId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            if (myId != null) {
                                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            showAlert("가입된 이메일로 비밀번호 재설정 링크가 전송되었습니다.", 1);
                                        }
                                        else {
                                            showAlert("요청한 작업을 수행할 수 없습니다.", 0);
                                        }
                                    }
                                });
                            }
                            else {
                                showAlert("요청한 작업을 수행할 수 없습니다.", 0);
                            }
                        }
                        else {
                            showAlert("등록되지 않은 이메일 입니다. 다시 시도해주세요.", 0);
                            et_inpEmail.setText("");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        // 계정 찾기의 사원번호로 메일 찾기
        btn_sendToDbFindYourAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String empno = et_inpEmp.getText().toString().trim();
                if(empno.equals("")) {
                    showAlert("사원번호를 입력해주세요.", 0);
                    return;
                }
                mDatabase.orderByChild("empno").equalTo(empno).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            userInfo.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                JoinDTO joinDTO = snapshot.getValue(JoinDTO.class);
                                userInfo.add(joinDTO);
                            }
                            Log.d("listSize", String.valueOf(userInfo.size()));
                            tv_sucFindYourId.setText("" + userInfo.get(0).emailId);
                            tv_viewId.setText(userInfo.get(0).name + "님의 계정은 다음과 같습니다.");
                        } else {
                            showAlert("등록된 사원이 아닙니다. 다시 시도해주세요.", 0);
                            et_inpEmp.setText("");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        // 회원가입 레이아웃
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = et_mail.getText().toString().trim();
                String pw = et_pw.getText().toString().trim();
                String rePw = et_pw2.getText().toString().trim();
                String name = et_name.getText().toString().trim();
                String emp = et_emp.getText().toString().trim();
                String hp = et_phone.getText().toString().trim();
                String dept = et_dept.getText().toString().trim();
                String job = et_job.getText().toString().trim();

                if(email.equals("")) {
                    showAlert("이메일 아이디를 입력해주세요.", 0);
                    return;
                }
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
                mAuth.createUserWithEmailAndPassword(email, rePw)
                    .addOnCompleteListener(Join_Find.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                FirebaseMessaging.getInstance().getToken()
                                        .addOnCompleteListener(new OnCompleteListener<String>() {
                                            @Override
                                            public void onComplete(@NonNull Task<String> task) {
                                                if (!task.isSuccessful()) {
                                                    return;
                                                }

                                                // Get new FCM registration token
                                                String token = task.getResult();

                                                JoinDTO joinDTO = new JoinDTO(email, rePw, name, emp, hp, dept, job, token);

                                                mDatabase.child(myUid).setValue(joinDTO);
                                            }
                                        });

                                showAlert("가입이 완료되었습니다.", 1);

                                et_mail.setText("");
                                et_pw.setText("");
                                et_pw2.setText("");
                                et_name.setText("");
                                et_emp.setText("");
                                et_phone.setText("");
                                et_dept.setText("");
                                et_job.setText("");
                            } else {
                                showAlert("가입에 실패했습니다. 다시 시도해주세요.", 0);
                            }
                        }
                    });
            }
        });
        // 각각 텍스트를 한번에 지우는 버튼 모음
        ib_all_dell_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_mail.setText("");
            }
        });
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
}