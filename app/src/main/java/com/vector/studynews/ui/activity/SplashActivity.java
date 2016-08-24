package com.vector.studynews.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.hyphenate.chat.EMClient;
import com.vector.studynews.R;
import com.vector.studynews.utils.EMHelper;

/**
 * Created by zhang on 2016/8/19.
 */
public class SplashActivity extends AppCompatActivity {
    private LinearLayout ll_splash;
    private static final int sleeptime = 2000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ll_splash = (LinearLayout) findViewById(R.id.ll_splash);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        ll_splash.startAnimation(animation);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(EMHelper.getInstance().isLoggedIn()){
                    long starttime = System.currentTimeMillis();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    long costtime = System.currentTimeMillis()-starttime;
                    //保证两秒后启动
                    if(sleeptime-costtime>0){
                        try {
                            Thread.sleep(sleeptime-costtime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent i = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    try {
                        Thread.sleep(sleeptime);
                        Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("splash","=====Onresume");
    }
}
