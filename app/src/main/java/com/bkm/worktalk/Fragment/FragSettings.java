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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bkm.worktalk.BeginApp.Login;
import com.bkm.worktalk.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

public class FragSettings extends Fragment {

    private FirebaseAuth mAuth;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_settings, container, false);

        mAuth = FirebaseAuth.getInstance();

        btn_settings_profile_photo_change = view.findViewById(R.id.btn_settings_profile_photo_change);
        btn_settings_user_edit = view.findViewById(R.id.btn_settings_user_edit);
        btn_settings_user_alarm = view.findViewById(R.id.btn_settings_user_alarm);
        btn_settings_user_wtf = view.findViewById(R.id.btn_settings_user_wtf);
        btn_settings_user_signout = view.findViewById(R.id.btn_settings_user_signout);
        btn_settings_user_delete = view.findViewById(R.id.btn_settings_user_delete);

        iv_settings_profile_photo_change = (ImageView) view.findViewById(R.id.iv_settings_profile_photo_change);

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
            iv_settings_profile_photo_change.setImageURI(data.getData());  // 프로필 뷰를 바꿈
            Glide.with(this).load(data.getData()).apply(new RequestOptions().circleCrop().diskCacheStrategy(DiskCacheStrategy.NONE)).into(iv_settings_profile_photo_change);
            //Glide.with(this).load(img).diskCacheStrategy(DiskCacheStrategy.NONE).into(product_img_imageview);
            uri = data.getData();  // 이미지 경로원본
        }
    }
}



















