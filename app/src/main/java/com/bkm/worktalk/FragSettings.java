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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.FileNotFoundException;
import java.io.IOException;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_settings, container, false);

        mAuth = FirebaseAuth.getInstance();

        btn_settings_profile_photo_change = view.findViewById(R.id.btn_settings_profile_photo_change);
        btn_settings_user_edit = view.findViewById(R.id.btn_settings_user_edit);
        btn_settings_user_alarm = view.findViewById(R.id.btn_settings_user_alarm);
        btn_settings_user_wtf = view.findViewById(R.id.btn_settings_user_wtf);
        btn_settings_user_help = view.findViewById(R.id.btn_settings_user_help);
        btn_settings_user_signout = view.findViewById(R.id.btn_settings_user_signout);
        btn_settings_user_delete = view.findViewById(R.id.btn_settings_user_delete);

        iv_settings_profile_photo_change = view.findViewById(R.id.iv_settings_profile_photo_change);
        view.findViewById(R.id.btn_settings_profile_photo_change).setOnClickListener(mClick);

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
    // 프사변경 기능을 버튼에 할당
    View.OnClickListener mClick = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_settings_profile_photo_change:
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    profile_photo_change.launch(intent);
                    break;
            }
        }
    };
    ActivityResultLauncher<Intent> profile_photo_change = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if( result.getResultCode() == RESULT_OK && result.getData() != null){

                    uri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                        iv_settings_profile_photo_change.setImageBitmap(bitmap);

                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
}