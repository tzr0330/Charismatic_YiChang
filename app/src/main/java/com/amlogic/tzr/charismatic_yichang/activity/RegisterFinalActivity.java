package com.amlogic.tzr.charismatic_yichang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amlogic.tzr.charismatic_yichang.BaseActivity;
import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.bean.User;

import cn.bmob.v3.listener.SaveListener;

public class RegisterFinalActivity extends BaseActivity {
    private Context mContext;

    private Toolbar mToolbar;

    private EditText et_account,et_pw;

    private Button btn_complete;

    private TextInputLayout til_account,til_pw;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_final);
        mContext=RegisterFinalActivity.this;
        phoneNumber=getIntent().getStringExtra("user_phone");
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.tl_arf_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(R.string.title_activity_register_final);
        et_account= (EditText) findViewById(R.id.et_arf_account);
        et_pw= (EditText) findViewById(R.id.et_arf_password);
        til_account= (TextInputLayout) findViewById(R.id.til_arf_account);
        til_account.setHint(getResources().getString(R.string.input_name));
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length()>0){
                    btn_complete.setEnabled(true);
                    til_account.setErrorEnabled(false);
                }else {
                    btn_complete.setEnabled(false);
                    til_account.setErrorEnabled(true);
                    til_account.setError(getResources().getString(R.string.error_empty_account));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        til_pw= (TextInputLayout) findViewById(R.id.til_arf_password);
        til_pw.setHint(getResources().getString(R.string.input_passWord));
        et_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 if (s.length()>0){
                     btn_complete.setEnabled(true);
                     til_pw.setErrorEnabled(false);
                 }else {
                     btn_complete.setEnabled(false);
                     til_pw.setErrorEnabled(true);
                     til_pw.setError(getResources().getString(R.string.error_empty_passWord));
                 }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_complete= (Button) findViewById(R.id.btn_arf_next);
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=et_account.getText().toString();
                String passWord=et_pw.getText().toString();
                User mUser = new User();
                mUser.setUsername(name);
                mUser.setPassword(passWord);
                mUser.setMobilePhoneNumber(phoneNumber);
                mUser.signUp(mContext, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(mContext, "注册成功", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(mContext,LoginActivity.class));
                        finish();
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(mContext, "注册失败:" + s, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
