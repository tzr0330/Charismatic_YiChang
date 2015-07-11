package com.amlogic.tzr.charismatic_yichang.view;


import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.R;

public class TextURLView extends LinearLayout {

	private Context mContext;
	private TextView url;
	public TextURLView(Context context) {
		super(context);
		mContext=context;
		initView();
	}
	
	public TextURLView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
	}
	
	private void initView(){
		LayoutInflater.from(mContext).inflate(R.layout.common_url_textview, this);
		url=(TextView) findViewById(R.id.tv_url_view);
	};
	
	public void setText(int txtRes){
		url.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		url.setText(txtRes);
	}
	
	public void setUrlOnclickListener(OnClickListener listener){
		url.setOnClickListener(listener);
	}

}
