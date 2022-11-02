package com.bkm.worktalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Project_Adapter extends RecyclerView.Adapter<Project_Adapter.CustomViewHolder> {

    private ArrayList<ProjectDTO> arrayList;
    private Context context;
    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    public Project_Adapter(ArrayList<ProjectDTO> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_projectlist, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tv_ProjectName.setText(arrayList.get(position).getProjectName());
        holder.tv_projectExplain.setText(String.valueOf(arrayList.get(position).getProjectExplain()));
    }

    @Override
    public int getItemCount() {
        //삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ProjectName;
        TextView tv_projectExplain;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_ProjectName = itemView.findViewById(R.id.tv_ProjectName);
            this.tv_projectExplain = itemView.findViewById(R.id.tv_projectExplain);

            //리싸이클뷰 아이템 클릭으로 프래그먼트 넘어가기===============================================
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context.getApplicationContext(), "아이템 클릭!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
