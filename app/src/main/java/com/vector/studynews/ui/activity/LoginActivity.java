package com.vector.studynews.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.vector.studynews.R;
import com.vector.studynews.db.SharedHelper;
import com.vector.studynews.utils.EMHelper;

/**
 * Created by zhang on 2016/8/18.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG ="LoginActivity";
    private EditText username,password;
    private String currentUsername,currentPassword;
    private Button login_btn;
    private boolean progressShow ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login_btn = (Button)findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPassword = password.getText().toString().trim();
                currentUsername = username.getText().toString().trim();
                if(TextUtils.isEmpty(currentUsername)){
                    Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(currentPassword)){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);

                progressShow = true;
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Log.d(TAG,"取消登录");
                        progressShow = false;
                    }
                });
                progressDialog.setMessage(getString(R.string.logining));
                progressDialog.show();
                EMHelper.getInstance().setCurrentUsername(currentUsername);
                EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.i("登录成功","===");
                        if(!LoginActivity.this.isFinishing()&&progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        //初始化列表
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();

                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        if(progressShow){
                            progressDialog.dismiss();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"账号或者密码错误",Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });

    }
}
