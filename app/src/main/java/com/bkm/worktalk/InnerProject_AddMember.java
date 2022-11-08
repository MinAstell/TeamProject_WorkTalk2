package com.bkm.worktalk;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InnerProject_AddMember extends AppCompatActivity {

    private String memberListName;
    private String memberListEmail;
    private String memberListHP;

    public String getMemberListName() {
        return memberListName;
    }

    public void setMemberListName(String memberListName) {
        this.memberListName = memberListName;
    }

    public String getMemberListEmail() {
        return memberListEmail;
    }

    public void setMemberListEmail(String memberListEmail) {
        this.memberListEmail = memberListEmail;
    }

    public String getMemberListHP() {
        return memberListHP;
    }

    public void setMemberListHP(String memberListHP) {
        this.memberListHP = memberListHP;
    }

    private RecyclerView rv_memberListToAdd;

    private ArrayList<InnerProject_AddMemberDTO> arrayList;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innerproject_addmember);

        rv_memberListToAdd = findViewById(R.id.rv_memberListToAdd);
        rv_memberListToAdd.setHasFixedSize(true); //리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_memberListToAdd.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("dept"); //DB 테이블 연결
        databaseReference.child("서버개발팀").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); //기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 List를 추출해냄
                    InnerProject_AddMemberDTO addMemberDTO = snapshot.getValue(InnerProject_AddMemberDTO.class); //만들어뒀던 객체에 데이터를 담는다.
                    arrayList.add(addMemberDTO); //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    Log.d("memberList", arrayList.get(0).memberListName+"");
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침해야 반영이 됨
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        adapter = new InnerProject_AddMember_Adapter(arrayList, getMemberListName(), getMemberListEmail(), getMemberListHP(), getApplicationContext());
        rv_memberListToAdd.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
    }

}
