package com.amlogic.tzr.charismatic_yichang.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.bean.NewsDetailBean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String TAG = "NewsDetailActivity";
    private Context mContext;
    private Toolbar mToolbar;
//    private TextView tv_title, tv_time, tv_source;
    private WebView contentView;
    private String news_id;
    private String news_title;
    private BmobQuery<NewsDetailBean> bmobQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        mContext = NewsDetailActivity.this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            news_id = bundle.getString("news_id");
            news_title = bundle.getString("news_title");
            Log.e(TAG, "news_title=" + news_title);
        }
        initView();
        getData();
    }

    private void initView() {

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tl_nda_top);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        tv_title = (TextView) findViewById(R.id.tv_nda_newsTitle);
//        tv_title.setText(news_title);
//        tv_source = (TextView) findViewById(R.id.tv_nda_source);
//        tv_time = (TextView) findViewById(R.id.tv_nda_time);
        contentView = (WebView) findViewById(R.id.tv_nda_content);
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


    private static String getHtml(String tv_title, String tv_time, String tv_source, String content) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html dir=\"ltr\" lang=\"zh\">");
        sb.append("<head>");
        sb.append("<meta name=\"viewport\" content=\"width=100%; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\" />");
        sb.append("<link rel=\"stylesheet\" href='file:///android_asset/style.css' type=\"text/css\" media=\"screen\" />");
        sb.append("</head>");
        sb.append("<body style=\"padding:0px 8px 8px 8px;\">");
        sb.append("<div id=\"pagewrapper\">");
        sb.append("<div id=\"mainwrapper\" class=\"clearfix\">");
        sb.append("<div id=\"maincontent\">");
        sb.append("<div class=\"post\">");
        sb.append("<div class=\"posthit\">");
        sb.append("<div class=\"postinfo\">");
        sb.append("<h2 class=\"thetitle\">");
        sb.append("<a>");
        sb.append(tv_title);
        sb.append("</a>");
        sb.append("</h2>");
        sb.append(tv_source + " @ " + tv_time);
        sb.append("</div>");
        sb.append("<div class=\"entry\">");
        sb.append(content);
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }


    public void getData() {
        bmobQuery = new BmobQuery<NewsDetailBean>();
        bmobQuery.addWhereEqualTo("news_id", news_id);
        bmobQuery.findObjects(mContext, new FindListener<NewsDetailBean>() {
            @Override
            public void onSuccess(List<NewsDetailBean> list) {
                if (list.size() > 0) {
//                    tv_time.setText("发表时间：" + list.get(0).getNews_time());
//                    tv_source.setText("来源：" + list.get(0).getNews_source());
                    contentView.loadDataWithBaseURL(null, getHtml(news_title, list.get(0).getNews_time(), list.get(0).getNews_source(),list.get(0).getNews_content()), "text/html", "utf-8", null);
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
