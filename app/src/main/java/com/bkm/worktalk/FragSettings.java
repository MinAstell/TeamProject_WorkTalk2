package com.bkm.worktalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
}