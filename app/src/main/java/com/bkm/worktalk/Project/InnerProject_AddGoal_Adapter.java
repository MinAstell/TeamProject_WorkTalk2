package com.bkm.worktalk.Project;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkm.worktalk.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InnerProject_AddGoal_Adapter extends RecyclerView.Adapter<InnerProject_AddGoal_Adapter.CustomViewHolder> {

    private ArrayList<InnerProject_AddGoalDTO> arrayList;
    private Context context;
    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    private String goalName;
    private String goalNameForChange;
    private String goalExplain;
    private boolean isGoalSuccessed;

    private String projectName; //프로젝트 이름 값 받아오는 변수

    private FirebaseDatabase database;
    public DatabaseReference mDatabase;

    public InnerProject_AddGoal_Adapter(ArrayList<InnerProject_AddGoalDTO> arrayList, String goalName, String goalNameForChange, String goalExplain,
                                        boolean isGoalSuccessed, Context context) {
        this.arrayList = arrayList;
        this.goalName = goalName;
        this.goalNameForChange = goalNameForChange;
        this.goalExplain = goalExplain;
        this.isGoalSuccessed = isGoalSuccessed;
        this.context = context;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_innerproject_addgoal, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        mDatabase = FirebaseDatabase.getInstance().getReference("projectGoalList"); //해당 DB와 연결

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tv_goalListName.setText(arrayList.get(position).getGoalNameForChange());
        holder.tv_goalListId.setText(arrayList.get(position).getGoalName());
        holder.tv_goalListExplain.setText(arrayList.get(position).getGoalExplain());

        //리사이클러뷰 아이템 클릭 이벤트===============================================================
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goalNameForChange = holder.tv_goalListName.getText().toString();
                goalExplain = holder.tv_goalListExplain.getText().toString();

                clickToInnerWork();
            }
        });

        //리사이클러뷰 아이템 롱클릭 이벤트==============================================================
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(),view);

                goalNameForChange = holder.tv_goalListName.getText().toString();
                goalName = holder.tv_goalListId.getText().toString();
                goalExplain = holder.tv_goalListExplain.getText().toString();

                popupMenu.getMenuInflater().inflate(R.menu.innerproject_goalmenu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_goalUpdate: //수정 버튼
                                Intent update = new Intent(context, InnerProject_UpdateGoal.class);
                                update.putExtra("goalName", goalName);
                                update.putExtra("goalNameForChange", goalNameForChange);
                                update.putExtra("goalExplain", goalExplain);
                                context.startActivity(update.addFlags(FLAG_ACTIVITY_NEW_TASK));
                                break;
                            case R.id.action_goalDelete: //삭제 버튼 클릭시
                                onClickShowAlert(goalName);
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

        //리사이클러뷰 내부의 체크박스 클릭 이벤트========================================================
        final InnerProject_AddGoalDTO goalDTO = arrayList.get(position); //final로 선언해야 체크박스의 체크 상태값(T/F)이 바뀌지 않는다

        holder.ck_goalSuccess.setOnCheckedChangeListener(null); //스크롤 시 체크값이 초기화되는 것을 방지

        holder.ck_goalSuccess.setChecked(goalDTO.getGoalSuccessed());
        //모델 클래스의 getter로 체크 상태값을 가져온 다음, setter를 통해 이 값을 아이템 안의 체크박스에 set함.

        projectName = ((InnerProject) InnerProject.mContext).projectName; //프로젝트 이름을 가져옴.
        goalName = holder.tv_goalListId.getText().toString(); //목표 이름을 가져옴.
        mDatabase = FirebaseDatabase.getInstance().getReference("projectGoalList"); //DB와 연동
        Query mySuccessQuery = mDatabase.child(projectName).child(goalName).child("goalSuccessed");

        mySuccessQuery.addListenerForSingleValueEvent(new ValueEventListener() { //DB에서 goalSuccessed의 값을 가져옴.
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); //기존 배열리스트가 존재하지않게 초기화

                String goalSuccess = dataSnapshot.getValue().toString(); //DB에서 goalSuccessed의 값(T/F)을 가져옴.

                if(goalSuccess.equals("true")) { //DB의 값으로 체크박스의 체크상태를 결정함.
                    holder.ck_goalSuccess.setChecked(true);
                }else if(goalSuccess.equals("false")) {
                    holder.ck_goalSuccess.setChecked(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        holder.ck_goalSuccess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                ((DatabaseReference) mySuccessQuery).setValue(holder.ck_goalSuccess.isChecked());

                if(isChecked) {
                    InnerProject_AddGoalDTO.setGoalSuccessed(true);
                }else {
                    InnerProject_AddGoalDTO.setGoalSuccessed(false);
                }
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
            notifyItemRemoved(position);//새로고침
        }catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_goalListName;
        TextView tv_goalListId;
        TextView tv_goalListExplain;
        CheckBox ck_goalSuccess;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_goalListName = itemView.findViewById(R.id.tv_goalListName);
            this.tv_goalListId = itemView.findViewById(R.id.tv_goalListId);
            this.tv_goalListExplain = itemView.findViewById(R.id.tv_goalListExplain);
            this.ck_goalSuccess = itemView.findViewById(R.id.ck_goalSuccess);
        }
    }

    //리사이클러뷰 클릭시 InnerGoal로 이동==============================================================
    public void clickToInnerWork() {

        projectName = ((InnerProject)InnerProject.mContext).projectName; //액티비티에 저장된 변수를 가져옴.

        mDatabase.child(projectName).child(goalName).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Intent intent = new Intent(context, InnerProject_InnerGoal.class);
                intent.putExtra("goalNameForChange", goalNameForChange);
                intent.putExtra("goalExplain", goalExplain);
                context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                //서비스는 태스크가 없기 때문에 액티비티를 시작하려면 new task 플래그를 줘야 한다.

                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //삭제버튼 클릭시 뜨는 경고창=======================================================================
    public void onClickShowAlert(String goalName) {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(InnerProject.mContext);
        //alert의 title과 Messege 세팅
        myAlertBuilder.setTitle("프로젝트 목표 삭제");
        myAlertBuilder.setMessage("정말로 해당 목표를 삭제하시겠습니까?");

        //버튼 추가
        myAlertBuilder.setPositiveButton("예",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                //예 버튼을 눌렸을 경우
                onDeleteContent(goalName);
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

    //프로젝트 목표 삭제==============================================================================
    private void onDeleteContent(String goalName) {
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("projectGoalList").child(projectName).child(goalName);

        mDatabase.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) { //삭제 성공시
                Toast.makeText(context, "프로젝트 목표가 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                ((InnerProject)InnerProject.mContext).goalListLoad(); //프로젝트 목표 리사이클러뷰를 다시 불러옴
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