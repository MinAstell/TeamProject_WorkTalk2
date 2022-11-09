package com.bkm.worktalk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private SharedPreferences appData;

    private static boolean chk_signOut = false;

    public boolean isChk_signOut() {
        return chk_signOut;
    }

    public void setChk_signOut(boolean chk_signOut) {
        this.chk_signOut = chk_signOut;
    }

    EditText et_mail_login, et_pw_login;
    ImageButton ib_all_del1_login, ib_all_del2_login;
    Button btn_login, btn_transJoin, btn_selAccount, btn_selPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appData = getSharedPreferences("appData", MODE_PRIVATE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mDatabase = FirebaseDatabase.getInstance().getReference("UserInfo");

        mAuth = FirebaseAuth.getInstance();

        et_mail_login = (EditText) findViewById(R.id.et_mail_login);
        et_pw_login = (EditText) findViewById(R.id.et_pw_login);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_transJoin = (Button) findViewById(R.id.btn_transJoin);
        btn_selAccount = (Button) findViewById(R.id.btn_selAccount);
        btn_selPw = (Button) findViewById(R.id.btn_selPw);

        ib_all_del1_login = (ImageButton) findViewById(R.id.ib_all_del1_login);
        ib_all_del2_login = (ImageButton) findViewById(R.id.ib_all_del2_login);

        chkLoginStatus();  // 로그인 가버렷!!

        btn_transJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Join_Find.class);
                intent.putExtra("0", "0"); // 액티비티가 넘어갈때 원하는 레이아웃이 뜨도록 하는 PutExtra를 보냄
                startActivity(intent);
            }
        });
        btn_selAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Join_Find.class);
                intent.putExtra("0", "1"); // 액티비티가 넘어갈때 원하는 레이아웃이 뜨도록 하는 PutExtra를 보냄
                startActivity(intent);
            }
        });
        btn_selPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Join_Find.class);
                intent.putExtra("0", "2"); // 액티비티가 넘어갈때 원하는 레이아웃이 뜨도록 하는 PutExtra를 보냄
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = et_mail_login.getText().toString().trim();
                String pw = et_pw_login.getText().toString().trim();

                if(id.equals("")) {
                    showAlert("아이디를 입력해주세요.");
                    return;
                }
                if(pw.equals("")) {
                    showAlert("비밀번호를 입력해주세요.");
                    return;
                }

                mAuth.signInWithEmailAndPassword(id, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            mDatabase.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getChildrenCount() > 0) {
                                        JoinDTO joinDTO = dataSnapshot.getValue(JoinDTO.class);

                                        Log.d("myName", joinDTO.name);

                                        SharedPreferences.Editor editor = appData.edit();

                                        editor.putString("myUid", myUid);
                                        editor.putString("myName", joinDTO.name);
                                        editor.putString("myDept", joinDTO.deptno);
                                        editor.apply();

                                        FirebaseMessaging.getInstance().getToken()
                                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if (!task.isSuccessful()) {
                                                            return;
                                                        }

                                                        // Get new FCM registration token
                                                        String token = task.getResult();

                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("token", token);

                                                        mDatabase.child(myUid).updateChildren(map);
                                                    }
                                                });

                                        Map<String, Object> map = new HashMap<>();
                                        map.put("pw", pw);

                                        mDatabase.child(myUid).updateChildren(map);

                                        Intent intent = new Intent(getApplication(), Fragment.class);
                                        intent.putExtra("myName", joinDTO.name);
                                        intent.putExtra("myDept", joinDTO.deptno);
                                        intent.putExtra("myUid", myUid);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else {
                            showAlert("로그인에 실패했습니다. 다시 시도해주세요.");
                        }
                    }
                });
            }
        });
        ib_all_del1_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_mail_login.setText("");
            }
        });
        ib_all_del2_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_pw_login.setText("");
            }
        });
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
    public void chkLoginStatus() {

        String myUid = appData.getString("myUid", "");
        String myName = appData.getString("myName", "");
        String myDept = appData.getString("myDept", "");

        // else if 숙지하자! if와 엄연히 다름.
        if(chk_signOut) {
            SharedPreferences.Editor editor = appData.edit();

            editor.putString("myUid", "");
            editor.apply();

            chk_signOut = false;
        }
        else if(!myUid.equals("")) {

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();

                            Map<String, Object> map = new HashMap<>();
                            map.put("token", token);

                            mDatabase.child(myUid).updateChildren(map);
                        }
                    });

            Intent intent = new Intent(getApplication(), Fragment.class);
            intent.putExtra("myName", myName);
            intent.putExtra("myDept", myDept);
            intent.putExtra("myUid", myUid);
            startActivity(intent);
            finish();
        }
    }
}
