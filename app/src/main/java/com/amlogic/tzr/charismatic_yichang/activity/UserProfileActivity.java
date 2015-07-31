package com.amlogic.tzr.charismatic_yichang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.BaseActivity;
import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.LoadFinishCallBack;
import com.amlogic.tzr.charismatic_yichang.adapter.FeedAdapter;
import com.amlogic.tzr.charismatic_yichang.bean.Feed;
import com.amlogic.tzr.charismatic_yichang.bean.User;
import com.amlogic.tzr.charismatic_yichang.view.AutoLoadRecyclerView;
import com.amlogic.tzr.charismatic_yichang.view.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;

public class UserProfileActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener{
    private static final String TAG = "UserProfileActivity";
    public static final  String CURRENT_USER="current_user";

    private static final int STATE_REFRESH = 0;
    private static final int STATE_MORE = 1;
    private AppBarLayout appBarLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AutoLoadRecyclerView mRecyclerView;
    private FeedAdapter mAdapter;
    private List<Feed> list=new ArrayList<>();
    private BmobQuery<Feed> bmobQuery;
    private LoadFinishCallBack mLoadFinisCallBack;
    private int limit = 10;
    private int curPage = 0;
    private Context mContext;
    private Toolbar mToolbar;
    private ImageView user_icon;
    private TextView nameView;
    private ImageView sexView;
    private  RelativeLayout rl_toast;
    private User mUser;
    private String user_id = "123";
    private String bitmapUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mContext = UserProfileActivity.this;
        mUser= (User) getIntent().getSerializableExtra(CURRENT_USER);
        initViews();
        initDatas();
        queryData(0, STATE_REFRESH);
    }


    private void initDatas() {
//        User mUser = BmobUser.getCurrentUser(mContext, User.class);
        if (mUser != null) {
            user_id = mUser.getObjectId();
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
        BmobFile file=mUser.getHead_thumb();
        if (file!=null) {
            String icon_url =file.getFileUrl(mContext);
            Picasso.with(mContext).load(icon_url).into(user_icon);
        } else {
            Picasso.with(mContext).load(R.mipmap.ic_user).transform(new CircleTransformation()).into(user_icon);
        }
        nameView.setText(mUser.getUsername());
        boolean sex=mUser.getSex();
        if (sex==false) {
            sexView.setImageResource(R.mipmap.userinfo_icon_male);
        }else{
            sexView.setImageResource(R.mipmap.userinfo_icon_female);
        }
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.tl_aup_toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.ctl_aup_toolbar);
//        collapsingToolbar.setExpandedTitleColor(R.color.white);
//        collapsingToolbar.setCollapsedTitleTextColor(R.color.accent);
        collapsingToolbar.setTitle("用户详情");

        collapsingToolbar.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
                    Log.e(TAG, "DragEvent.ACTION_DRAG_ENDED");
                }
                return false;
            }
        });
        appBarLayout= (AppBarLayout) findViewById(R.id.apl_aup_appbar);
        user_icon = (ImageView) findViewById(R.id.iv_vup_userProfilePhoto);
        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,UserInfoActivity.class);
                intent.putExtra(CURRENT_USER,mUser);
                startActivity(intent);
            }
        });
        nameView= (TextView) findViewById(R.id.tv_vup_userName);
        sexView= (ImageView) findViewById(R.id.iv_vup_sex);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_aup_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryData(0, STATE_REFRESH);
            }
        });
        mRecyclerView = (AutoLoadRecyclerView) findViewById(R.id.rv_aup_feed);
        mRecyclerView.setHasFixedSize(false);
        mLoadFinisCallBack = mRecyclerView;
        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                queryData(curPage, STATE_MORE);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FeedAdapter(mContext, list);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
    }

    private void queryData(final int page, final int actionType) {
        BmobQuery<User> innerQuery = new BmobQuery<User>();
        String[] id = {user_id};
        innerQuery.addWhereContainedIn("objectId", Arrays.asList(id));
        bmobQuery = new BmobQuery<Feed>();
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(limit);
        bmobQuery.setSkip(page * limit);
        bmobQuery.addWhereMatchesQuery("user", "_User", innerQuery);
        bmobQuery.include("user");
        bmobQuery.findObjects(mContext, new FindListener<Feed>() {
            @Override
            public void onSuccess(List<Feed> queryList) {
                if (queryList.size() > 0) {
//                    rl_toast.setVisibility(View.GONE);
                    if (actionType == STATE_REFRESH) {
                        curPage = 0;
                        list.clear();
                    }
                    for (Feed bean : queryList) {
                        list.add(bean);
                    }
                    curPage++;
                    mRecyclerView.loadFinish();
                } else {
//                    rl_toast.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
                mRecyclerView.loadFinish();
                mSwipeRefreshLayout.setRefreshing(false);
//                rl_toast.setVisibility(View.VISIBLE);
            }
        });

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

            case R.id.action_edit:

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }
}
