package com.bkm.worktalk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bkm.worktalk.Project.InnerProject;
import com.bkm.worktalk.DTO.ProjectDTO;
import com.bkm.worktalk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragProject_Adapter extends RecyclerView.Adapter<FragProject_Adapter.CustomViewHolder> {

    private ArrayList<ProjectDTO> arrayList;
    private Context context;
    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    private String projectName;
    private String projectExplain;

    public DatabaseReference mDatabase;

    public FragProject_Adapter(ArrayList<ProjectDTO> arrayList, String projectName, String projectExplain, Context context) {
        this.arrayList = arrayList;
        this.projectName = projectName;
        this.projectExplain = projectExplain;
        this.context = context;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_projectlist, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        mDatabase = FirebaseDatabase.getInstance().getReference("innerProject");

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tv_ProjectName.setText(arrayList.get(position).getProjectName());
        holder.tv_projectExplain.setText(arrayList.get(position).getProjectExplain());

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectName = holder.tv_ProjectName.getText().toString();
                projectExplain = holder.tv_projectExplain.getText().toString();

                Log.d("projectName, projectExplain", projectName + ", " + projectExplain);
                clickToInnerProject();
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
            notifyItemRemoved(position);  // 새로고침
        }catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ProjectName;
        TextView tv_projectExplain;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_ProjectName = itemView.findViewById(R.id.tv_ProjectName);
            this.tv_projectExplain = itemView.findViewById(R.id.tv_projectExplain);
        }
    }

    //리사이클러뷰 클릭시 InnerProject로 이동===========================================================
    public void clickToInnerProject() {

        mDatabase.child(projectName).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Intent intent = new Intent(context, InnerProject.class);
                intent.putExtra("projectName", projectName);
                intent.putExtra("projectExplain", projectExplain);
                context.startActivity(intent);

                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
