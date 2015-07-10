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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.Tool.DensityUtil;
import com.amlogic.tzr.charismatic_yichang.activity.LoginActivity;
import com.amlogic.tzr.charismatic_yichang.fragment.InfoFragment;
import com.amlogic.tzr.charismatic_yichang.fragment.NewsMainFragment;
import com.amlogic.tzr.charismatic_yichang.fragment.TourFragment;
import com.amlogic.tzr.charismatic_yichang.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;


public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private boolean pendingIntroAnimation=false;
    private TextView mUserName;
    private ImageView mUserHead;

    /*
    fragment
     */
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private NewsMainFragment mNewsMainFragment;
    private VideoFragment mFoodFragment;
    private TourFragment mTourFragment;
    private InfoFragment mInfoFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;
        if (savedInstanceState==null){
            pendingIntroAnimation=true;
        }
        Bmob.initialize(mContext,"d3ec31d05d5dd780ced301c5c81431ee");
        initView();
        if (pendingIntroAnimation){
//            startIntroAnimation();
        }
        setTabSelection(0);
    }

    private void initView() {
        mUserHead= (ImageView) findViewById(R.id.iv_user_head);
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
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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
                if (mInfoFragment==null){
                    mInfoFragment = new InfoFragment();
                    transaction.add(R.id.fl_main_content,mInfoFragment);
                }else {
                    transaction.show(mInfoFragment);
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
        if (mInfoFragment != null) {
            transaction.hide(mInfoFragment);
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
}
