package com.amlogic.tzr.charismatic_yichang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.Tool.BitmapCache;
import com.amlogic.tzr.charismatic_yichang.Tool.ConfigUtil;
import com.amlogic.tzr.charismatic_yichang.Tool.DensityUtil;
import com.amlogic.tzr.charismatic_yichang.Tool.SPUtils;
import com.amlogic.tzr.charismatic_yichang.activity.LoginActivity;
import com.amlogic.tzr.charismatic_yichang.activity.UserProfileActivity;
import com.amlogic.tzr.charismatic_yichang.bean.User;
import com.amlogic.tzr.charismatic_yichang.event.LoginEvent;
import com.amlogic.tzr.charismatic_yichang.fragment.FeedFragment;
import com.amlogic.tzr.charismatic_yichang.fragment.NewsMainFragment;
import com.amlogic.tzr.charismatic_yichang.fragment.TourFragment;
import com.amlogic.tzr.charismatic_yichang.fragment.VideoFragment;
import com.amlogic.tzr.charismatic_yichang.view.CircleTransformation;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import de.greenrobot.event.EventBus;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private Context mContext;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private boolean pendingIntroAnimation=false;
    private TextView mUserName;
    private ImageView mUserHead;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    /*
    fragment
     */
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private NewsMainFragment mNewsMainFragment;
    private VideoFragment mFoodFragment;
    private TourFragment mTourFragment;
    private FeedFragment mFeedFragment;

    private FragmentTransaction  transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;
        mQueue = ApplicationController.getInstance().getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
        if (savedInstanceState==null){
            pendingIntroAnimation=true;
        }
        Bmob.initialize(mContext,"d3ec31d05d5dd780ced301c5c81431ee");
        initView();
        if (pendingIntroAnimation){
//            startIntroAnimation();
        }
        setTabSelection(0);
        EventBus.getDefault().register(this);
    }

    private void initView() {
        mUserHead= (ImageView) findViewById(R.id.iv_user_head);
        User mUser= BmobUser.getCurrentUser(mContext,User.class);
        if (mUser!=null){
            if (mUser.getHead_thumb()!=null) {
                Picasso.with(mContext).load(mUser.getHead_thumb().getFileUrl(mContext)).transform(new CircleTransformation()).into(mUserHead);
            }else {
                Picasso.with(context).load(R.mipmap.ic_user).transform(new CircleTransformation()).into(mUserHead);
            }

        }else{
//            Bitmap bitmap= BitmapUtil.transform(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_user));
//            mUserHead.setImageBitmap(bitmap);
            Picasso.with(context).load(R.mipmap.ic_user).transform(new CircleTransformation()).into(mUserHead);
        }
        mUserName= (TextView) findViewById(R.id.tv_user_name);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nv_main_navigation);
        mNavigationView.getMenu().getItem(0).setChecked(true);
        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }
        mUserHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( BmobUser.getCurrentUser(mContext,User.class)==null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }else{
                     Intent intent=new Intent(mContext, UserProfileActivity.class);
                     intent.putExtra(UserProfileActivity.CURRENT_USER,BmobUser.getCurrentUser(mContext,User.class));
                     startActivity(intent);
                }
            }
        });

    }

    private void startIntroAnimation() {
        int actionbarSize = DensityUtil.dp2px(MainActivity.this, 56);
        mToolbar.setTranslationY(-actionbarSize);
        mToolbar.animate()
                .translationY(0)
                .setDuration(700)
                .setStartDelay(300)
                .start();

    }

    private void setTabSelection(int index) {
         transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (mNewsMainFragment==null){
                    mNewsMainFragment = new NewsMainFragment();
                    transaction.add(R.id.fl_main_content,mNewsMainFragment);
                }else {
                    transaction.show(mNewsMainFragment);
                }
                break;
            case 1:
                if (mTourFragment==null){
                    mTourFragment = new TourFragment();
                    transaction.add(R.id.fl_main_content,mTourFragment);

                }else {
                    transaction.show(mTourFragment);
                }
                break;
            case 2:
                if (mFoodFragment==null){
                    mFoodFragment = new VideoFragment();
                    transaction.add(R.id.fl_main_content,mFoodFragment);
                }else{
                    transaction.show(mFoodFragment);
                }

                break;
            case 3:
                if (mFeedFragment==null){
                    mFeedFragment = new FeedFragment();
                    transaction.add(R.id.fl_main_content,mFeedFragment);
                }else {
                    transaction.show(mFeedFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mNewsMainFragment != null) {
            transaction.hide(mNewsMainFragment);
        }
        if (mTourFragment != null) {
            transaction.hide(mTourFragment);
        }
        if (mFoodFragment != null) {
            transaction.hide(mFoodFragment);
        }
        if (mFeedFragment != null) {
            transaction.hide(mFeedFragment);
        }
    }


    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.news_yc:
                                setTabSelection(0);
                                mToolbar.setTitle(menuItem.getTitle());
                                menuItem.setChecked(true);
                                break;
                            case R.id.tour_yc:
                                setTabSelection(1);
                                mToolbar.setTitle(menuItem.getTitle());
                                menuItem.setChecked(true);
                                break;
                            case R.id.food_yc:
                                setTabSelection(2);
                                mToolbar.setTitle(menuItem.getTitle());
                                menuItem.setChecked(true);
                                break;
                            case R.id.info_yc:
                                setTabSelection(3);
                                mToolbar.setTitle(menuItem.getTitle());
                                menuItem.setChecked(true);
                                break;
                            case R.id.setting:

                                break;
                            case R.id.about:

                                break;
                        }

                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(LoginEvent event){
        User mUser= event.getmUser();
        if (mUser!=null){
            if (mUser.getUsername()!=null) {
                mUserName.setText(mUser.getUsername());
            }
            BmobFile icon=mUser.getHead_thumb();
            if (icon!=null) {
                Picasso.with(mContext).load(mUser.getHead_thumb().getFileUrl(mContext)).into(mUserHead);
            }else {
                Picasso.with(context).load(R.mipmap.ic_user).transform(new CircleTransformation()).into(mUserHead);
            }

        }
/*
        if (mFeedFragment!=null){
            LogManager.e(TAG, "transaction.replace(R.id.fl_main_content,mFeedFragment); is success!----");
            mFeedFragment = new FeedFragment();
            transaction.replace(R.id.fl_main_content,mFeedFragment);
            transaction.commit();
        }*/

    }

    @Override
    protected void onDestroy() {
        BmobUser.logOut(this);   //清除缓存用户对象
        super.onDestroy();
        SPUtils.put(mContext, ConfigUtil.IS_LOGIN, false);
        EventBus.getDefault().unregister(this);

    }

}
