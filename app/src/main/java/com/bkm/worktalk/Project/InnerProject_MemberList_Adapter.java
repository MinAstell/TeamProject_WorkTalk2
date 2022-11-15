package com.bkm.worktalk.Project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkm.worktalk.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InnerProject_MemberList_Adapter extends RecyclerView.Adapter<InnerProject_MemberList_Adapter.CustomViewHolder> {

    private ArrayList<InnerProject_AddMemberDTO> arrayList;
    private Context context;
    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    private String memberListName;
    private String memberListEmail;
    private String memberListHP;

    private FirebaseDatabase database;
    public DatabaseReference mDatabase;
    public DatabaseReference mDatabase1;

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
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_innerproject_addmember, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tv_memberName.setText(arrayList.get(position).getName());
        holder.tv_memberEmail.setText(arrayList.get(position).getEmail());
        holder.tv_memberHP.setText(arrayList.get(position).getHp());

        //리사이클러뷰 아이템 롱클릭 이벤트==============================================================
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(),view);

                memberListName = holder.tv_memberName.getText().toString();

                popupMenu.getMenuInflater().inflate(R.menu.innerproject_membermenu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_memberDelete: //삭제 버튼 클릭시
                                onClickShowAlert(memberListName);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();

                return true;
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

    //삭제버튼 클릭시 뜨는 경고창=======================================================================
    public void onClickShowAlert(String memberListName) {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(InnerProject.mContext);
        //alert의 title과 Messege 세팅
        myAlertBuilder.setTitle("프로젝트 멤버 삭제");
        myAlertBuilder.setMessage("정말로 해당 멤버를 삭제하시겠습니까?");

        //버튼 추가
        myAlertBuilder.setPositiveButton("예",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                //예 버튼을 눌렸을 경우
                onDeleteContent(memberListName);
            }
        });
        myAlertBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //아니오 버튼을 눌렸을 경우
            }
        });

        // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
        myAlertBuilder.show();
    }

    //프로젝트 멤버 삭제==============================================================================
    private void onDeleteContent(String memberListName) {
        String projectName = ((InnerProject) InnerProject.mContext).projectName; //InnerProject의 projectName을 가져옴.

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("projectMemberList").child(projectName).child(memberListName);

        mDatabase.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) { //삭제 성공시
                Toast.makeText(context, "해당 멤버가 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                ((InnerProject)InnerProject.mContext).memberListLoad(); //프로젝트 멤버 리사이클러뷰를 다시 불러옴
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { //삭제 실패시
                System.out.println("error: "+e.getMessage());
                Toast.makeText(context, "삭제에 실패했습니다. 다시 시도해 주십시오.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
