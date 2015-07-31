package com.amlogic.tzr.charismatic_yichang.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.LoadFinishCallBack;
import com.amlogic.tzr.charismatic_yichang.Tool.LogManager;
import com.amlogic.tzr.charismatic_yichang.activity.LoginActivity;
import com.amlogic.tzr.charismatic_yichang.activity.PublishActivity;
import com.amlogic.tzr.charismatic_yichang.adapter.FeedAdapter;
import com.amlogic.tzr.charismatic_yichang.bean.Feed;
import com.amlogic.tzr.charismatic_yichang.bean.User;
import com.amlogic.tzr.charismatic_yichang.event.RefreshEvent;
import com.amlogic.tzr.charismatic_yichang.view.AutoLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {
    private static final int PICK_FROM_CAMERA = 0x000000;
    private static final int PICK_FROM_FILE = 0x000001;
    private static final int CROP_FROM_CAMERA = 0x000002;
    private static final int CROP_FROM_FILE = 0x000003;

    private static final String TAG = "FeedFragment";
    private static final int STATE_REFRESH = 0;
    private static final int STATE_MORE = 1;
    private int limit = 10;
    private int curPage = 0;
    private FloatingActionButton fab;
    private View fragmentView;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AutoLoadRecyclerView mRecyclerView;
    private FeedAdapter mAdapter;
    private List<Feed> list;
    private BmobQuery<Feed> bmobQuery;
    private LoadFinishCallBack mLoadFinisCallBack;
    private RelativeLayout mProgressBar;
    private CoordinatorLayout mainContent;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        list = new ArrayList<Feed>();
        queryData(0, STATE_REFRESH);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView =inflater.inflate(R.layout.fragment_feed, container, false);
        if (fragmentView!=null){
            initView();
        }
        return fragmentView;
    }

    private void initView() {
        mSwipeRefreshLayout= (SwipeRefreshLayout) fragmentView.findViewById(R.id.srl_ff_refresh);
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
        mRecyclerView=(AutoLoadRecyclerView) fragmentView.findViewById(R.id.rv_ff_feed);
        mRecyclerView.setHasFixedSize(false);
        mLoadFinisCallBack = mRecyclerView;
        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                queryData(curPage, STATE_MORE);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new FeedAdapter(mContext,list);
        mRecyclerView.setAdapter(mAdapter);

        fab = (FloatingActionButton) fragmentView.findViewById(R.id.fab_ff_photo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User mUser= BmobUser.getCurrentUser(mContext,User.class);
                if (mUser!=null) {
                    View dialog_publish = LayoutInflater.from(mContext).inflate(
                            R.layout.dialog_publish_photo, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setView(dialog_publish);
                    builder.create();
                    final AlertDialog dialog = builder.show();
                    ImageView choiceCamera = (ImageView) dialog_publish
                            .findViewById(R.id.img_choice_from_camera);
                    ImageView choicePhoto = (ImageView) dialog_publish
                            .findViewById(R.id.img_choice_from_photo);
                    ImageView choiceCancle = (ImageView) dialog_publish.findViewById(R.id.img_choice_cancale);
                    choiceCamera.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
//                        getPicFromCapture();

                        }
                    });

                    choicePhoto.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                            getPicFromContent();
                        }
                    });

                    choiceCancle.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });
                }else{
                  startActivity(new Intent(mContext, LoginActivity.class));
                }

            }
        });
        mProgressBar= (RelativeLayout) fragmentView.findViewById(R.id.rl_ff_progress);
        mainContent= (CoordinatorLayout) fragmentView.findViewById(R.id.cl_ff_main);
    }

    private void getPicFromContent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_FROM_FILE);
    }

    private void queryData(final int page,final int actionType){
        bmobQuery =new BmobQuery<Feed>();
        bmobQuery.order("-createdAt");
        bmobQuery.include("user");
//        bmobQuery.addWhereRelatedTo()
        bmobQuery.setLimit(limit);
        bmobQuery.setSkip(page * limit);
        bmobQuery.findObjects(mContext, new FindListener<Feed>() {
            @Override
            public void onSuccess(List<Feed> queryList) {
                mProgressBar.setVisibility(View.GONE);
                mainContent.setVisibility(View.VISIBLE);
                if (queryList.size() > 0) {
                    if (actionType == STATE_REFRESH) {
                        curPage = 0;
                        list.clear();
                    }
                    for (Feed bean : queryList) {
                        list.add(bean);
                    }
                    curPage++;
                    mRecyclerView.loadFinish();
                }
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
                mRecyclerView.loadFinish();
                mSwipeRefreshLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);
                mainContent.setVisibility(View.VISIBLE);
            }
        });

    }

    public void onEventMainThread(RefreshEvent event)
    {
        LogManager.e(TAG, "queryData(0, STATE_REFRESH) is success!");

        queryData(0, STATE_REFRESH);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode !=getActivity().RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_FILE:
                Uri mUri=data.getData();
                Intent pictureIntent=new Intent(getActivity(), PublishActivity.class);
                pictureIntent.putExtra("image",mUri);
                startActivity(pictureIntent);
                break;
        }
    }
//    public void onEventMainThread(LoginEvent event){
//        if (event.isLogin()){
//            LogManager.e(TAG, "queryData(0, STATE_REFRESH) is success!");
//            queryData(0, STATE_REFRESH);
//        }
//
//    }
}
