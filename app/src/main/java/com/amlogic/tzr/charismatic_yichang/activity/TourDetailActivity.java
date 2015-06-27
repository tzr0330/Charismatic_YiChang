package com.amlogic.tzr.charismatic_yichang.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amlogic.tzr.charismatic_yichang.ApplicationController;
import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.BitmapCache;
import com.amlogic.tzr.charismatic_yichang.bean.TourDetailBean;
import com.amlogic.tzr.charismatic_yichang.view.ImageCycleView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class TourDetailActivity extends AppCompatActivity {
    private TextView tv_info, tv_tip;

    private Context mContext;

    private Toolbar mToolbar;

    private FloatingActionButton fab;

    private String tour_title;

    private String tour_id;

    private ImageCycleView mImageCycleView;

    private BmobQuery<TourDetailBean> bmobQuery;

    private List<String> mImageUrl = new ArrayList<String>();

    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        mContext = TourDetailActivity.this;
        mQueue = ApplicationController.getInstance().getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tour_id = bundle.getString("tour_id");
            tour_title = bundle.getString("tour_title");

        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mToolbar.setTitle(tour_title);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(tour_title);

        fab = (FloatingActionButton) findViewById(R.id.fab_more_img);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.tour_morePic), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.tour_look), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(
                                        mContext,
                                        "Toast comes out",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        mImageCycleView = (ImageCycleView) findViewById(R.id.icv_img);
        tv_info = (TextView) findViewById(R.id.tv_content_info);
        tv_tip = (TextView) findViewById(R.id.tv_content_tip);

        initData();

    }

    private void initData() {
        bmobQuery = new BmobQuery<TourDetailBean>();
        bmobQuery.addWhereEqualTo("tour_id", tour_id);
        bmobQuery.findObjects(mContext, new FindListener<TourDetailBean>() {
            @Override
            public void onSuccess(List<TourDetailBean> list) {
                if (list.size() > 0) {
                    mImageUrl=list.get(0).getTour_banner();
                    mImageCycleView.setImageResources(mImageUrl, mAdCycleViewListener);
                    tv_info.setText(Html.fromHtml(list.get(0).getTour_info()));
                    tv_tip.setText(Html.fromHtml(list.get(0).getTour_tip()));
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

        @Override
        public void onImageClick(int position, View imageView) {
            // TODO 单击图片处理事件

        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.mipmap.pic_default, R.mipmap.pic_default);
            mImageLoader.get(imageURL, imageListener);
        }
    };


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
