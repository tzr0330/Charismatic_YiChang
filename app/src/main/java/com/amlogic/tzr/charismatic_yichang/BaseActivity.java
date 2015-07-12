package com.amlogic.tzr.charismatic_yichang;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 应用程序Activity的基类
 */
public class BaseActivity extends AppCompatActivity {

    public Context context;
    public boolean stopActivity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	// 添加Activity到堆栈

	context = this;
	AppManager.getAppManager().addActivity(this);
	stopActivity = false;
    }

    @Override
    protected void onResume() {
	super.onResume();

    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	stopActivity = true;
	// 结束Activity并从堆栈中移除
	AppManager.getAppManager().finishActivity(this);
    }

}
