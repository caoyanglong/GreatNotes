package com.zhefang.assignment2.login;

import android.view.View;
import android.widget.Toast;

import com.zhefang.assignment2.R;
import com.zhefang.assignment2.base.BaseActivity;


public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_login_layout);
        setTitle("login");
    }

    public void login(View v){
        Toast.makeText(this,"success",Toast.LENGTH_LONG).show();
        finish();
    }
}
