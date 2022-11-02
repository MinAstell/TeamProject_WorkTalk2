package com.bkm.worktalk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragUsers extends Fragment {

    private static String myName;
    private static String myUid;
    private static String myDept;

    public String getMyUid() {
        return myUid;
    }

    public void setMyUid(String myUid) {
        this.myUid = myUid;
    }

    public String getMyDept() {
        return myDept;
    }

    public void setMyDept(String myDept) {
        this.myDept = myDept;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    private DatabaseReference mDatabase;
    private FloatingActionButton fab_project;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rv_userList;

    ArrayList<UserListsDTO> userList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_users, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference("dept");

        fab_project = view.findViewById(R.id.fab_project);
        rv_userList = view.findViewById(R.id.rv_usersList);

        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_userList.setLayoutManager(linearLayoutManager);
        rv_userList.setHasFixedSize(true);

        selUserList();

        fab_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("플로팅 버튼 눌림!");
            }
        });

        return view;
    }

    // 다이얼로그 창 띄우는 메서드
    public void showAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("").setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void selUserList() {
        mDatabase.child(getMyDept()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    userList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        UserListsDTO userListsDTO = snapshot.getValue(UserListsDTO.class);

                        if(!userListsDTO.name.equals(getMyName())) {
                            userList.add(userListsDTO);
                        }
                    }

                    Log.d("userList", userList.get(0).name);

                    UsersList_Adapter usersListAdapter = new UsersList_Adapter(userList, getMyUid(), getMyName(), getContext());
                    rv_userList.setAdapter(usersListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}