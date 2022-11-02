package com.bkm.worktalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Fragment extends AppCompatActivity {

    private String myName, myDept, myUid;
    private static boolean chk_finish = false;

    private FragmentManager fragmentManager;
    private FragProject fragProject;
    private FragTalk fragTalk;
    private FragSettings fragSettings;
    private FragUsers fragUsers;
    private FragmentTransaction transaction;

    ImageView iv_project, iv_talk, iv_settings;
    public static ImageView iv_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Intent intent = getIntent();
        myName = intent.getStringExtra("myName");
        myDept = intent.getStringExtra("myDept");
        myUid = intent.getStringExtra("myUid");

        fragmentManager = getSupportFragmentManager();

        fragProject = new FragProject();
        fragTalk = new FragTalk();
        fragSettings = new FragSettings();
        fragUsers = new FragUsers();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout, fragProject).commitAllowingStateLoss();

        iv_project = (ImageView)findViewById(R.id.iv_project);
        iv_talk = (ImageView)findViewById(R.id.iv_talk);
        iv_settings = (ImageView)findViewById(R.id.iv_settings);
        iv_users = (ImageView)findViewById(R.id.iv_users);

        iv_project.setImageResource(R.drawable.project_press);

        findViewById(R.id.iv_project).setOnClickListener(mClick);
        findViewById(R.id.iv_talk).setOnClickListener(mClick);
        findViewById(R.id.iv_settings).setOnClickListener(mClick);
        findViewById(R.id.iv_users).setOnClickListener(mClick);
    }
    View.OnClickListener mClick = new View.OnClickListener() {
        public void onClick(View v) {
            transaction = fragmentManager.beginTransaction();
            switch(v.getId()) {
                case R.id.iv_project:
                    transaction.replace(R.id.framelayout, fragProject).commitAllowingStateLoss();
                    iv_users.setImageResource(R.drawable.users);
                    iv_project.setImageResource(R.drawable.project_press);
                    iv_talk.setImageResource(R.drawable.talk);
                    iv_settings.setImageResource(R.drawable.settings);
                    break;
                case R.id.iv_talk:
                    transaction.replace(R.id.framelayout, fragTalk).commitAllowingStateLoss();
                    iv_users.setImageResource(R.drawable.users);
                    iv_project.setImageResource(R.drawable.project);
                    iv_talk.setImageResource(R.drawable.talk_press);
                    iv_settings.setImageResource(R.drawable.settings);
                    break;
                case R.id.iv_settings:
                    transaction.replace(R.id.framelayout, fragSettings).commitAllowingStateLoss();
                    iv_users.setImageResource(R.drawable.users);
                    iv_project.setImageResource(R.drawable.project);
                    iv_talk.setImageResource(R.drawable.talk);
                    iv_settings.setImageResource(R.drawable.settings_press);
                    break;
                case R.id.iv_users:
                    transaction.replace(R.id.framelayout, fragUsers).commitAllowingStateLoss();
                    iv_users.setImageResource(R.drawable.users_press);
                    iv_project.setImageResource(R.drawable.project);
                    iv_talk.setImageResource(R.drawable.talk);
                    iv_settings.setImageResource(R.drawable.settings);
                    FragUsers fragUsers = new FragUsers();
                    fragUsers.setMyName(myName);
                    fragUsers.setMyDept(myDept);
                    fragUsers.setMyUid(myUid);
                    break;
            }
        }
    };
}