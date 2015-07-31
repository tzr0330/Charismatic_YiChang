package com.amlogic.tzr.charismatic_yichang.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.LoadFinishCallBack;
import com.amlogic.tzr.charismatic_yichang.adapter.FeedAdapter;
import com.amlogic.tzr.charismatic_yichang.bean.Feed;
import com.amlogic.tzr.charismatic_yichang.bean.User;
import com.amlogic.tzr.charismatic_yichang.view.AutoLoadRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoneFragment extends Fragment {
    public static final String TITLE = "title";

    private String mTitle = "Defaut Value";

    public static final String USER_ID = "user_id";

    private static final int STATE_REFRESH = 0;
    private static final int STATE_MORE = 1;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AutoLoadRecyclerView mRecyclerView;
    private FeedAdapter mAdapter;
    private List<Feed> list;
    private BmobQuery<Feed> bmobQuery;
    private LoadFinishCallBack mLoadFinisCallBack;

    private int limit = 10;
    private int curPage = 0;

    private String user_id;

    private View view;
    public ZoneFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        list = new ArrayList<Feed>();
        queryData(0, STATE_REFRESH);
//        EventBus.getDefault().register(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_zone, container, false);
        if (view!=null){
            initView();
        }
        return view;
    }

    private void initView() {
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.srl_fz_refresh);
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
        mRecyclerView=(AutoLoadRecyclerView) view.findViewById(R.id.rv_fz_feed);
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


    }

    public static ZoneFragment newInstance(String userId){
       ZoneFragment fragment = new ZoneFragment();
       Bundle bundle = new Bundle();
       bundle.putString(USER_ID, userId);
       fragment.setArguments(bundle);
       return fragment;
   }
    private void queryData(final int page,final int actionType){
        BmobQuery<User> innerQuery = new BmobQuery<User>();
        String[] id={user_id};
        innerQuery.addWhereContainedIn("objectId", Arrays.asList(id));
        bmobQuery =new BmobQuery<Feed>();
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(limit);
        bmobQuery.setSkip(page * limit);
        bmobQuery.findObjects(mContext, new FindListener<Feed>() {
            @Override
            public void onSuccess(List<Feed> queryList) {
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
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
                mRecyclerView.loadFinish();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}
