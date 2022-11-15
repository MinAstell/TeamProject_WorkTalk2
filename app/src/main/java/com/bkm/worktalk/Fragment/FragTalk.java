package com.bkm.worktalk.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bkm.worktalk.Adapter.TalkList_Adapter;
import com.bkm.worktalk.Adapter.UsersList_Adapter;
import com.bkm.worktalk.BeginApp.Login;
import com.bkm.worktalk.DTO.TalkListsDTO;
import com.bkm.worktalk.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragTalk extends Fragment {
    public DatabaseReference databaseReference;
    private FloatingActionButton fab_talk;
    private RecyclerView rv_talkList;
    private LinearLayoutManager linearLayoutManager;

    ArrayList<String> talkList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_talk, container, false);

        fab_talk = view.findViewById(R.id.fab_talk);
        rv_talkList = view.findViewById(R.id.rv_talkList);

        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_talkList.setLayoutManager(linearLayoutManager);
        rv_talkList.setHasFixedSize(true);

        selUserList();

        return view;
    }

    public void selUserList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("eachUserChatRoomInfo");
        String myUid = Login.appData.getString("myUid", "");
        databaseReference.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    talkList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TalkListsDTO talkListsDTO = snapshot.getValue(TalkListsDTO.class);

                        if(talkListsDTO.chatRoomPath == null && talkListsDTO.opponent != null) {
                            talkList.add(talkListsDTO.opponent);
                        }
                        else if(talkListsDTO.chatRoomPath != null && talkListsDTO.opponent == null) {
                            talkList.add(talkListsDTO.chatRoomPath);
                        }
                        Log.d("값", talkListsDTO.toString());
                    }

                    for(int i=0; i<talkList.size(); i++) {
                        Log.d("talkList", talkList.get(i));
                    }

                    TalkList_Adapter talkListAdapter = new TalkList_Adapter(talkList, myUid, getContext());
                    rv_talkList.setAdapter(talkListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}