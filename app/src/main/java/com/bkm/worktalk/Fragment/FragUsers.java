package com.bkm.worktalk.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bkm.worktalk.Adapter.UsersList_Adapter;
import com.bkm.worktalk.DTO.JoinDTO;
import com.bkm.worktalk.DTO.UserListsDTO;
import com.bkm.worktalk.R;
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
    public String profileImageUrl;

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
    private DatabaseReference mDatabase2;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rv_userList;

    public static ArrayList<UserListsDTO> userList = new ArrayList<>();
    public static ArrayList<JoinDTO> user_profile = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_users, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference("dept");
        mDatabase2 = FirebaseDatabase.getInstance().getReference("UserInfo");

        rv_userList = view.findViewById(R.id.rv_usersList);

        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_userList.setLayoutManager(linearLayoutManager);
        rv_userList.setHasFixedSize(true);

        selUserList();

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
        userList.clear();
        user_profile.clear();
        mDatabase.child(getMyDept()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserListsDTO userListsDTO = snapshot.getValue(UserListsDTO.class);
                        if(!userListsDTO.name.equals(getMyName())) {
                            userList.add(userListsDTO);

                            mDatabase2.orderByChild("emailId").equalTo(userListsDTO.email).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    if(dataSnapshot2.getChildrenCount() > 0) {
                                        for (DataSnapshot snapshot : dataSnapshot2.getChildren()) {
                                            JoinDTO joinDTO = snapshot.getValue(JoinDTO.class);
                                            user_profile.add(joinDTO);
                                            Log.d("profileImageUrl", joinDTO.profileImageUrl);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                    setUdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setUdapter() {
        UsersList_Adapter usersListAdapter = new UsersList_Adapter(FragUsers.userList, FragUsers.user_profile, getMyUid(), getMyName(), getContext());
        rv_userList.setAdapter(usersListAdapter);
    }
}