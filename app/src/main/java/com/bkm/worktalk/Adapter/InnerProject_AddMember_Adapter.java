package com.bkm.worktalk.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkm.worktalk.Project.InnerProject_AddMember;
import com.bkm.worktalk.DTO.InnerProject_AddMemberDTO;
import com.bkm.worktalk.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InnerProject_AddMember_Adapter extends RecyclerView.Adapter<InnerProject_AddMember_Adapter.CustomViewHolder> {

    private ArrayList<InnerProject_AddMemberDTO> arrayList;
    private Context context;
    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    private String memberListName = "";
    private String memberListEmail;
    private String memberListHP;

    private FirebaseDatabase database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
    private DatabaseReference mDatabase = database.getReference(); //데이터베이스의 특정 위치로 연결

    public InnerProject_AddMember_Adapter(ArrayList<InnerProject_AddMemberDTO> arrayList, String memberListEmail, String memberListHP, String memberListName, Context context) {
        this.arrayList = arrayList;
        this.memberListEmail = memberListEmail;
        this.memberListHP = memberListHP;
        this.memberListName = memberListName;
        this.context = context;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public InnerProject_AddMember_Adapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_innerproject_addmember, parent, false);
        InnerProject_AddMember_Adapter.CustomViewHolder holder = new InnerProject_AddMember_Adapter.CustomViewHolder(view);

//        mDatabase = FirebaseDatabase.getInstance().getReference("innerProject");

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerProject_AddMember_Adapter.CustomViewHolder holder, int position) {
        holder.tv_memberName.setText(arrayList.get(position).getName());
        holder.tv_memberEmail.setText(arrayList.get(position).getEmail());
        holder.tv_memberHP.setText(arrayList.get(position).getHp());

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() { //아이템 클릭 이벤트
            @Override
            public void onClick(View view) {
                memberListName = holder.tv_memberName.getText().toString();
                memberListEmail = holder.tv_memberEmail.getText().toString();
                memberListHP = holder.tv_memberHP.getText().toString();

                //값을 함수에 넣어줌
                addMember(memberListEmail, memberListHP, memberListName);

                Toast.makeText(context, "프로젝트의 새로운 멤버 "+memberListName+"!", Toast.LENGTH_SHORT).show();

                ((InnerProject_AddMember)InnerProject_AddMember.mContext).finish(); //어댑터에서 액티비티 창을 닫음.
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        //삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public void remove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);  //새로고침
        }catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_memberName;
        TextView tv_memberEmail;
        TextView tv_memberHP;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_memberName = itemView.findViewById(R.id.tv_memberName);
            this.tv_memberEmail = itemView.findViewById(R.id.tv_memberEmail);
            this.tv_memberHP = itemView.findViewById(R.id.tv_memberHP);
        }
    }
    //값을 파이어베이스 Realtime database로 넘기는 함수=================================================
    public void addMember(String memberListEmail, String memberListHP, String memberListName) {
        String projectName = ((InnerProject_AddMember)InnerProject_AddMember.mContext).projectName; //액티비티에 저장된 변수를 가져옴.

        //DTO에서 선언했던 함수.
        InnerProject_AddMemberDTO addMemberDTO = new InnerProject_AddMemberDTO(memberListEmail, memberListHP, memberListName);
        Log.d("memberDTO", memberListEmail+memberListHP+memberListName);

        //child는 해당 키 위치로 이동하는 함수입니다.
        //키가 없는데 값을 지정한 경우 자동으로 생성합니다.
        mDatabase.child("projectMemberList").child(projectName).child(memberListName).setValue(addMemberDTO);
    }

}
