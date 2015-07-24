package com.amlogic.tzr.charismatic_yichang.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.amlogic.tzr.charismatic_yichang.ApplicationController;
import com.amlogic.tzr.charismatic_yichang.BaseActivity;
import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.BitmapCache;
import com.amlogic.tzr.charismatic_yichang.Tool.BitmapUtil;
import com.amlogic.tzr.charismatic_yichang.Tool.ConfigUtil;
import com.amlogic.tzr.charismatic_yichang.Tool.SPUtils;
import com.amlogic.tzr.charismatic_yichang.adapter.FragmentAdapter;
import com.amlogic.tzr.charismatic_yichang.bean.User;
import com.amlogic.tzr.charismatic_yichang.event.LoginEvent;
import com.amlogic.tzr.charismatic_yichang.fragment.UserInfoFragment;
import com.amlogic.tzr.charismatic_yichang.fragment.ZoneFragment;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.greenrobot.event.EventBus;

public class UserProfileActivity extends BaseActivity {
    private static final String TAG = "UserProfileActivity";
    private static final int PICK_FROM_CAMERA = 0x000000;
    private static final int PICK_FROM_FILE = 0x000001;
    private static final int CROP_FROM_CAMERA = 0x000002;
    private static final int CROP_FROM_FILE = 0x000003;
    private Context mContext;
    private List<String> titles=new ArrayList<String>();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private ImageView user_icon;
    private Uri imgUri;

    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    private FragmentAdapter fragmentAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private String user_id = "123";
    private String bitmapUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mContext = UserProfileActivity.this;
        mQueue = ApplicationController.getInstance().getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
        initViews();
        initDatas();
    }




    private void initDatas() {
        UserInfoFragment mUserInfoFragment = UserInfoFragment.newInstance(user_id);
        ZoneFragment mZoneFragment = ZoneFragment.newInstance(user_id);
        mFragments.add(mUserInfoFragment);
        mFragments.add(mZoneFragment);
        titles.add("主页");
        titles.add("动态");
        fragmentAdapter=new FragmentAdapter(getSupportFragmentManager(),mFragments,titles);
        String icon_url = (String) SPUtils.get(mContext, ConfigUtil.USER_ICON, "123");

        if (!icon_url.equals("123")) {
            ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(user_icon, R.mipmap.pic_default, R.mipmap.pic_default);
            mImageLoader.get(icon_url, imageListener);
        } else {
            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_user);
            Bitmap circle_bitmap = BitmapUtil.transform(mBitmap);
            mBitmap.recycle();
            user_icon.setImageBitmap(circle_bitmap);
        }
        mViewPager.setAdapter(fragmentAdapter);
        mViewPager.setCurrentItem(0);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(fragmentAdapter);

    }

    private void initViews() {
        titles.add("主页");
        titles.add("动态");
        user_icon = (ImageView) findViewById(R.id.iv_vup_userProfilePhoto);
        mTabLayout= (TabLayout) findViewById(R.id.tl_aup_header);
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mViewPager = (ViewPager) findViewById(R.id.vp_aup_content);
        mToolbar = (Toolbar) findViewById(R.id.tl_aup_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("用户详情");
        user_icon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getPicFromContent();
        }
    });
}

    private void getPicFromContent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_FROM_FILE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
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


    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
//            case PICK_FROM_CAMERA:
//                cropImageUri(imgUri, 250, 250, CROP_FROM_CAMERA);
//                break;
            case PICK_FROM_FILE:
                imgUri = data.getData();
                doCrop();
                break;
//            case CROP_FROM_CAMERA:
//                if (imgUri != null) {
//                    ImageRoundUtil imgUtil = new ImageRoundUtil();
//                    image = imgUtil.toRoundBitmap(decodeUriAsBitmap(imgUri));
//                    headView.setImageBitmap(image);
//                }
//                break;
            case CROP_FROM_FILE:
                if (null != data) {
                    setCropImg(data);
                }
                break;
        }

    }

    private void doCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imgUri, "image/*");
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_FROM_FILE);
    }

    private void setCropImg(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (null != bundle) {
            Bitmap mBitmap = BitmapUtil.transform((Bitmap) bundle.getParcelable("data"));
//            user_icon.setImageBitmap(mBitmap);

            String BitmapPath = BitmapUtil.saveBitmap(mBitmap, mContext);
            final BmobFile file = new BmobFile(new File(BitmapPath));
            file.upload(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    Log.e(TAG, "file.upload is ok");
                    final User currentUser = BmobUser.getCurrentUser(mContext, User.class);
                    currentUser.setHead_thumb(file);
                    updateHead_thumb(file.getFileUrl(mContext));
                    currentUser.update(mContext, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            EventBus.getDefault().post(new LoginEvent(true, currentUser));
                            Log.e(TAG, " currentUser.update is ok");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.e(TAG, " currentUser.update is onFailure---" + i + s);
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.e(TAG, "file.upload is onFailure---" + i + s);
                }
            });


        }

    }

    private void updateHead_thumb(String url) {
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(user_icon, R.mipmap.pic_default, R.mipmap.pic_default);
        mImageLoader.get(url, imageListener);
    }

}
