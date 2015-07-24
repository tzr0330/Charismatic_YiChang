package com.amlogic.tzr.charismatic_yichang.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;

import com.amlogic.tzr.charismatic_yichang.BaseActivity;
import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.BitmapUtil;
import com.amlogic.tzr.charismatic_yichang.bean.Feed;
import com.amlogic.tzr.charismatic_yichang.bean.User;
import com.amlogic.tzr.charismatic_yichang.event.RefreshEvent;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.greenrobot.event.EventBus;

public class PublishActivity extends BaseActivity {
    private static final String TAG = "PublishActivity";
    public static final String ARG_TAKEN_PHOTO_URI = "image";

    private Context mContext;

    private Uri photoUri;

    private int photoSize;

    private Toolbar mToolbar;

    private ImageView photoView;

    private EditText contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        mContext=PublishActivity.this;
        if (savedInstanceState == null) {
            photoUri = getIntent().getParcelableExtra(ARG_TAKEN_PHOTO_URI);
        } else {
            photoUri = savedInstanceState.getParcelable(ARG_TAKEN_PHOTO_URI);
        }
        photoSize = getResources().getDimensionPixelSize(R.dimen.publish_photo_thumbnail_size);
        initVIew();
    }

    private void initVIew() {
        mToolbar = (Toolbar) findViewById(R.id.tl_ap_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("分享感受");
        photoView= (ImageView) findViewById(R.id.iv_ap_Photo);
        contentText= (EditText) findViewById(R.id.et_ap_Description);
        photoView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                loadThumbnailPhoto();
                return true;
            }
        });

    }
    private void loadThumbnailPhoto() {
        photoView.setScaleX(0);
        photoView.setScaleY(0);
        Picasso.with(this)
                .load(photoUri)
                .centerCrop()
                .resize(photoSize, photoSize)
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
                        photoView.animate()
                                .scaleX(1.f).scaleY(1.f)
                                .setInterpolator(new OvershootInterpolator())
                                .setDuration(400)
                                .setStartDelay(200)
                                .start();
                    }

                    @Override
                    public void onError() {
                    }
                });
    }

    private void publishContent() {
        final BmobFile file = new BmobFile(new File(BitmapUtil.getRealFilePath(mContext,photoUri)));
        file.upload(mContext, new UploadFileListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, " feed upload is onSuccess");
                publish(file);

            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(TAG, " feed upload is onFailure");
            }
        });
    }

    private void publish(BmobFile file) {
        User user = BmobUser.getCurrentUser(mContext, User.class);
//        Log.e(TAG, " feed publish user=="+user.toString());
        Feed feed = new Feed();
        if (file != null) {
            feed.setPhoto(file);
        }
        feed.setContent(contentText.getText().toString());
        feed.setUser(user);
        feed.setComment(0);
        feed.setIsLove(false);
        feed.setLove(0);
        feed.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().post(new RefreshEvent(true));
                Log.e(TAG, " feed publish is onSuccess");
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(TAG, " feed publish is onFailure");
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_TAKEN_PHOTO_URI, photoUri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

      switch (id) {
          case R.id.home:
              finish();
              break;
          case R.id.action_publish:
              publishContent();
              break;

      }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
