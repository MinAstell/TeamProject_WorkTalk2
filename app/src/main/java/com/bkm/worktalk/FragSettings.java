package com.bkm.worktalk;

import static android.app.appsearch.AppSearchResult.RESULT_OK;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FragSettings extends Fragment {

    private FirebaseAuth mAuth;
    private String myUid = "";
    private String myProfileImage = "";

    Button btn_settings_profile_photo_change,
           btn_settings_user_edit,
           btn_settings_user_alarm,
           btn_settings_user_wtf,
           btn_settings_user_help,
           btn_settings_user_signout,
           btn_settings_user_delete;

    Uri uri;
    ImageView iv_settings_profile_photo_change;

    private static final int PICK_FROM_ALBUM = 1;

    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_settings, container, false);

        mAuth = FirebaseAuth.getInstance();
        myUid = mAuth.getCurrentUser().getUid();


        btn_settings_profile_photo_change = view.findViewById(R.id.btn_settings_profile_photo_change);
        btn_settings_user_edit = view.findViewById(R.id.btn_settings_user_edit);
        btn_settings_user_alarm = view.findViewById(R.id.btn_settings_user_alarm);
        btn_settings_user_wtf = view.findViewById(R.id.btn_settings_user_wtf);
        btn_settings_user_help = view.findViewById(R.id.btn_settings_user_help);
        btn_settings_user_signout = view.findViewById(R.id.btn_settings_user_signout);
        btn_settings_user_delete = view.findViewById(R.id.btn_settings_user_delete);

        iv_settings_profile_photo_change = (ImageView) view.findViewById(R.id.iv_settings_profile_photo_change);

        mDatabase = FirebaseDatabase.getInstance().getReference("UserInfo");

        loadProfile();

        btn_settings_user_alarm.setText(Login.appData.getString("AlarmChkMsg", "방해금지모드 설정"));

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
        btn_settings_user_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = Login.appData.edit();
                int AlarmChk = Login.appData.getInt("AlarmChk", 999);

                Log.d("AlarmChk", String.valueOf(AlarmChk));

                if(AlarmChk == 999) {
                    Log.d("들어옴", "버튼눌림1");
                    editor.putInt("AlarmChk", 1);
                    editor.putString("AlarmChkMsg", "알림끄기");
                    editor.apply();
                    btn_settings_user_alarm.setText("알림끄기");
                }
                else if(AlarmChk == 1) {
                    Log.d("들어옴", "버튼눌림2");
                    editor.putInt("AlarmChk", 999);
                    editor.putString("AlarmChkMsg", "알림켜기");
                    editor.apply();
                    btn_settings_user_alarm.setText("알림켜기");
                }
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

        Log.d("리절트오케이", String.valueOf(resultCode));

        if (requestCode == PICK_FROM_ALBUM) {
                Log.d("getData", String.valueOf(data.getData()));
//                iv_settings_profile_photo_change.setImageURI(data.getData());  // 프로필 뷰를 바꿈
                Glide.with(this).load(data.getData()).apply(new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.NONE)).into(iv_settings_profile_photo_change);
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
}
