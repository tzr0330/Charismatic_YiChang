package com.amlogic.tzr.charismatic_yichang.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.bean.User;

import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    private Context mContext;

    private Toolbar mToolbar;

    private EditText et_account,et_passWord;

    private Button btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext = RegisterActivity.this;
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.tl_ra_top);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.action_register));
        et_account= (EditText) findViewById(R.id.et_ra_account);
        et_passWord= (EditText) findViewById(R.id.et_ra_password);
        btn_register= (Button) findViewById(R.id.btn_ra_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User mUser = new User();
                String account=et_account.getText().toString();
                String passWord=et_passWord.getText().toString();
                mUser.setUsername(account);
                mUser.setPassword(passWord);
                mUser.setPhone("13143458479");
                mUser.setNick("jim");
                mUser.signUp(mContext, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(mContext,"注册成功:",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(mContext,"zhuce失败:"+s,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


}
