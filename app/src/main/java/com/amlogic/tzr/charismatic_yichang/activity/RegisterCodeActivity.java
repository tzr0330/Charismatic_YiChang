package com.amlogic.tzr.charismatic_yichang.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amlogic.tzr.charismatic_yichang.AppManager;
import com.amlogic.tzr.charismatic_yichang.BaseActivity;
import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.ConfigUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterCodeActivity extends BaseActivity {
    private Toolbar mToolbar;
    private EditText et_verifyCode;
    private TextInputLayout til_verifyCode;
    private Button btn_next;
    private String phString;
    private String codeString;
    private Context mContext;
    private TextView phoneNumberText;
    private TextView changeTimeText;
    private TextView restartText;
    private TextView titleTextView;
    private RelativeLayout receiveNormaLayout, receiveRestartLayout;
    private Timer timer;// 计时器
    private int seconds = 60;

    private EventHandler eh2;

    private Handler handler2 = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("CodeCheckActivity_event", "event=" + event);

            if (msg.what == 1) {
                receiveNormaLayout.setVisibility(View.GONE);
                receiveRestartLayout.setVisibility(View.VISIBLE);
                timer.cancel();
            } else if (msg.what >= 0) {
                changeTimeText.setText(msg.what + "");
            }
            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                // 提交验证码
                if (result == SMSSDK.RESULT_COMPLETE) {
                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                    Intent codeIntent = new Intent(mContext, RegisterFinalActivity.class);
                    codeIntent.putExtra("user_phone", phString);
                    startActivity(codeIntent);
                    AppManager.getAppManager().finishActivity(RegisterPhoneActivity.class);
                    AppManager.getAppManager().finishActivity();
                } else {
                    ((Throwable) data).printStackTrace();
                    // 验证码不正确
                    Toast.makeText(mContext, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                // 获取验证码成功后的执行动作
                afterGet(result, data);
            }
	    if (msg.what==-10) {
		    SMSSDK.getVerificationCode("86", phString);
	    }

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_code);
        mContext = RegisterCodeActivity.this;
        phString = getIntent().getStringExtra("user_phone");
        initView();
        SMSSDK.initSDK(this, ConfigUtil.APPKEY,ConfigUtil.APPSECRET);
        eh2 = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = Message.obtain();
                msg.what = -1;
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler2.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh2);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.tl_arc_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(getResources().getString(R.string.action_register));
        til_verifyCode = (TextInputLayout) findViewById(R.id.til_arc_verify);
        et_verifyCode = (EditText) findViewById(R.id.et_arc_verify);
        til_verifyCode.setHint(getResources().getString(R.string.input_verifyCode));
        et_verifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==4) {
                    til_verifyCode.setErrorEnabled(false);
                    btn_next.setEnabled(true);
                } else {
                    til_verifyCode.setError(getResources().getString(R.string.error_invalid_verifyCode));
                    til_verifyCode.setErrorEnabled(true);
                    btn_next.setEnabled(false);
                }
            }
        });
        btn_next = (Button) findViewById(R.id.btn_arc_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_verifyCode.getText().toString().length()==4) {
                    SMSSDK.submitVerificationCode("86", phString, et_verifyCode.getText().toString());
                } else {
                    til_verifyCode.setErrorEnabled(true);
                }
            }
        });
        phoneNumberText = (TextView) findViewById(R.id.tv_arc_phoneNumber);
        phoneNumberText.setText(phString);
        changeTimeText = (TextView) findViewById(R.id.tv_arc_time);
        restartText = (TextView) findViewById(R.id.tv_arc_restart);
        receiveNormaLayout = (RelativeLayout) findViewById(R.id.rl_arc_receive_normal);
        receiveRestartLayout = (RelativeLayout) findViewById(R.id.rl_arc_receive_restart);
        receiveRestartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSSDK.getVerificationCode("86", phString);
                receiveRestartLayout.setVisibility(View.GONE);
                receiveNormaLayout.setVisibility(View.VISIBLE);
                startTime();
                startSendMSM();
            }
        });
        startTime();
    }

    private void startTime() {

        // TODO Auto-generated method stub
        seconds = 60;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                handler2.sendEmptyMessage(seconds--);
            }
        }, 0, 1000);
    }

    private void afterGet(final int result, final Object data) {

        if (result == SMSSDK.RESULT_COMPLETE) {
            Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
        } else {
            ((Throwable) data).printStackTrace();
            Throwable throwable = (Throwable) data;
            // 根据服务器返回的网络错误，给toast提示
            try {
                JSONObject object = new JSONObject(throwable.getMessage());
                String des = object.optString("detail");
                if (!TextUtils.isEmpty(des)) {
                    Toast.makeText(mContext, des, Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 如果木有找到资源，默认提示
            Toast.makeText(mContext, "smssdk_network_error", Toast.LENGTH_SHORT).show();

        }
    }

    private void startSendMSM() {
                Message message = Message.obtain();
                message.what = -10;
                handler2.sendEmptyMessage(message.what);
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               showNotifyDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SMSSDK.registerEventHandler(eh2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(eh2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh2);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            showNotifyDialog();
            return true;
        } else {
            return false;
        }
    }



    private void showNotifyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.confirm_back);
        builder.setNegativeButton(getString(R.string.wait), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               finish();
            }
        });
        builder.show();
    }

}
