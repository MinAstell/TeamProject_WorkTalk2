package com.bkm.worktalk.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkm.worktalk.DTO.InnerProject_AddMemberDTO;
import com.bkm.worktalk.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class InnerProject_MemberList_Adapter extends RecyclerView.Adapter<InnerProject_MemberList_Adapter.CustomViewHolder> {

    private ArrayList<InnerProject_AddMemberDTO> arrayList;
    private Context context;
    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    private String memberListName;
    private String memberListEmail;
    private String memberListHP;

    public DatabaseReference mDatabase;

    public InnerProject_MemberList_Adapter(ArrayList<InnerProject_AddMemberDTO> arrayList, String memberListName,
                                           String memberListEmail, String memberListHP, Context context) {
        this.arrayList = arrayList;
        this.memberListName = memberListName;
        this.memberListEmail = memberListEmail;
        this.memberListHP = memberListHP;
        this.context = context;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public InnerProject_MemberList_Adapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_innerproject_addmember, parent, false);
        InnerProject_MemberList_Adapter.CustomViewHolder holder = new InnerProject_MemberList_Adapter.CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerProject_MemberList_Adapter.CustomViewHolder holder, int position) {
        holder.tv_memberName.setText(arrayList.get(position).getName());
        holder.tv_memberEmail.setText(arrayList.get(position).getEmail());
        holder.tv_memberHP.setText(arrayList.get(position).getHp());
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

}
