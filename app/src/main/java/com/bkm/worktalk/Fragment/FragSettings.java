package com.bkm.worktalk.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkm.worktalk.BeginApp.Login;
import com.bkm.worktalk.R;
import com.bkm.worktalk.UserModify;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class FragSettings extends Fragment {

    private FirebaseAuth mAuth;
    private String myUid;
    private DatabaseReference mDatabase;
    private String myProfileImage = "";
    private String myEmail = "";
    private String myName = "";

    Button btn_settings_profile_photo_change,
           btn_settings_user_edit,
           btn_settings_user_alarm,
           btn_settings_user_signout,
           btn_settings_user_delete,
            btn_settings_user_wtf;

    Uri uri;
    ImageView iv_settings_profile_photo_change;
    TextView tv_settings_profile_mail, tv_settings_profile_name;

    private static final int PICK_FROM_ALBUM = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_settings, container, false);

        mAuth = FirebaseAuth.getInstance();
        myUid = mAuth.getCurrentUser().getUid();

        btn_settings_profile_photo_change = view.findViewById(R.id.btn_settings_profile_photo_change);
        btn_settings_user_edit = view.findViewById(R.id.btn_settings_user_edit);
        btn_settings_user_alarm = view.findViewById(R.id.btn_settings_user_alarm);
        btn_settings_user_signout = view.findViewById(R.id.btn_settings_user_signout);
        btn_settings_user_delete = view.findViewById(R.id.btn_settings_user_delete);
        tv_settings_profile_mail = view.findViewById(R.id.tv_settings_profile_mail);
        tv_settings_profile_name = view.findViewById(R.id.tv_settings_profile_name);
        btn_settings_user_wtf = view.findViewById(R.id.btn_settings_user_wtf);

        iv_settings_profile_photo_change = (ImageView) view.findViewById(R.id.iv_settings_profile_photo_change);

        mDatabase = FirebaseDatabase.getInstance().getReference("UserInfo");

        getMyInfo();
        loadProfile();

        btn_settings_user_wtf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("홈페이지 공사중!...");
            }
        });

        btn_settings_user_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();

                Login login = new Login();
                login.setChk_signOut(true);

                Intent i = new Intent(getActivity(), Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        btn_settings_profile_photo_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);  // 사진 가져오기(앨범)
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
        btn_settings_user_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserModify.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        return view;
    }
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {
//            iv_settings_profile_photo_change.setImageURI(data.getData());  // 프로필 뷰를 바꿈
            Glide.with(this).load(data.getData()).apply(new RequestOptions().skipMemoryCache(true).circleCrop().diskCacheStrategy(DiskCacheStrategy.NONE)).into(iv_settings_profile_photo_change);
            //Glide.with(this).load(img).diskCacheStrategy(DiskCacheStrategy.NONE).into(product_img_imageview);
            uri = data.getData();  // 이미지 경로원본
            pushToProfile();
        }
    }

    public void pushToProfile() {
        FirebaseStorage.getInstance().getReference().child("userProfile").child(myUid).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                String imageUrl = "https://firebasestorage.googleapis.com/v0/b/worktalk-c2e1d.appspot.com/o/userProfile%2F"+ myUid + "?alt=media";

                Map<String, Object> map = new HashMap<>();
                map.put("profileImageUrl", imageUrl);

                mDatabase.child(myUid).updateChildren(map);
            }
        });
    }

    public void loadProfile() {
        mDatabase.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    Log.d("프로필 경로", dataSnapshot.child("profileImageUrl").getValue().toString());
                    myProfileImage = dataSnapshot.child("profileImageUrl").getValue().toString();
                    Glide.with(FragSettings.this).load(myProfileImage).apply(new RequestOptions().circleCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)).into(iv_settings_profile_photo_change);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getMyInfo() {
        mDatabase.child(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    myEmail = dataSnapshot.child("emailId").getValue().toString();
                    myName = dataSnapshot.child("name").getValue().toString();

                    tv_settings_profile_mail.setText(myEmail);
                    tv_settings_profile_name.setText(myName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
