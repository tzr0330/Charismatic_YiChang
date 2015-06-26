package com.amlogic.tzr.charismatic_yichang.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.bean.NewsDetailBean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String TAG = "NewsDetailActivity";
    private Context mContext;
    private Toolbar mToolbar;
    private TextView tv_title,tv_time,tv_source;
    private WebView contentView;
    private String news_id;
    private String news_title;
    private BmobQuery<NewsDetailBean> bmobQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        mContext=NewsDetailActivity.this;
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            news_id=bundle.getString("news_id");
            news_title=bundle.getString("news_title");
            Log.e(TAG,"news_title="+news_title);
        }
        initView();
        getData();
    }

    private void initView() {

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tl_nda_top);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tv_title= (TextView) findViewById(R.id.tv_nda_newsTitle);
        tv_title.setText(news_title);
        tv_source= (TextView) findViewById(R.id.tv_nda_source);
        tv_time= (TextView) findViewById(R.id.tv_nda_time);
        contentView= (WebView) findViewById(R.id.tv_nda_content);
        WebSettings webSettings = contentView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(false);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);


    }


    public void getData() {
        bmobQuery=new BmobQuery<NewsDetailBean>();
        bmobQuery.addWhereEqualTo("news_id",news_id);
        bmobQuery.findObjects(mContext, new FindListener<NewsDetailBean>() {
            @Override
            public void onSuccess(List<NewsDetailBean> list) {
                if (list.size()>0){
                    tv_time.setText("发表时间："+list.get(0).getNews_time());
                    tv_source.setText("来源："+list.get(0).getNews_source());
                    contentView.loadDataWithBaseURL(null,list.get(0).getNews_content(),"text/html", "utf-8", null);
                }
            }

            @Override
            public void onError(int i, String s) {

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
