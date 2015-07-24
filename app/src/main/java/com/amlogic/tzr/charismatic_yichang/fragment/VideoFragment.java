package com.amlogic.tzr.charismatic_yichang.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.LoadFinishCallBack;
import com.amlogic.tzr.charismatic_yichang.activity.VideoPlayActivity;
import com.amlogic.tzr.charismatic_yichang.adapter.OnItemClickLitener;
import com.amlogic.tzr.charismatic_yichang.adapter.VideoAdapter;
import com.amlogic.tzr.charismatic_yichang.bean.VideoListBean;
import com.amlogic.tzr.charismatic_yichang.view.AutoLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {
    private static final String TAG = "VideoFragment";
    private static final int STATE_REFRESH = 0;
    private static final int STATE_MORE = 1;
    private int limit = 10;
    private int curPage = 0;

    private View fragmentView;
    private Context context;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AutoLoadRecyclerView mRecyclerView;
    private VideoAdapter mAdapter;
    private List<VideoListBean> list;
    private BmobQuery<VideoListBean> bmobQuery;
    private LoadFinishCallBack mLoadFinisCallBack;


    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
     public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        list=new ArrayList<VideoListBean>();
        queryData(0, STATE_REFRESH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_video, null);
        if (fragmentView!=null){
          initView();
        }
        return fragmentView;
    }

    private void initView() {
        mSwipeRefreshLayout= (SwipeRefreshLayout) fragmentView.findViewById(R.id.srl_fv_refresh);
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
        mRecyclerView= (AutoLoadRecyclerView) fragmentView.findViewById(R.id.rv_video);
        mRecyclerView.setHasFixedSize(false);
        mLoadFinisCallBack = mRecyclerView;
        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                queryData(curPage, STATE_MORE);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter=new VideoAdapter(context,list);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle=new Bundle();
                bundle.putCharSequence("video_url",list.get(position).getVideo_url());
                Intent intent=new Intent(getActivity(), VideoPlayActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }


    private void queryData(final int page,final int actionType){
        bmobQuery =new BmobQuery<VideoListBean>();
        bmobQuery.setLimit(limit);
        bmobQuery.setSkip(page * limit);
        bmobQuery.findObjects(context, new FindListener<VideoListBean>() {
            @Override
            public void onSuccess(List<VideoListBean> queryList) {
                if (queryList.size()>0){
                    if (actionType == STATE_REFRESH) {
                        curPage=0;
                        list.clear();
                    }
                    for (VideoListBean bean : queryList) {
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
