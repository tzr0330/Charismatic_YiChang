package com.amlogic.tzr.charismatic_yichang.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.adapter.VideoAdapter;
import com.amlogic.tzr.charismatic_yichang.bean.VideoListBean;

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
    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;
    private List<VideoListBean> list;
    private BmobQuery<VideoListBean> bmobQuery;


    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        list=new ArrayList<VideoListBean>();
        queryData();
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
        mRecyclerView= (RecyclerView) fragmentView.findViewById(R.id.rv_video);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter=new VideoAdapter(context,list);
        mRecyclerView.setAdapter(mAdapter);

    }


    private void queryData(){
        bmobQuery =new BmobQuery<VideoListBean>();
        bmobQuery.findObjects(context, new FindListener<VideoListBean>() {
            @Override
            public void onSuccess(List<VideoListBean> queryList) {
                if (queryList.size()>0){
                    for (VideoListBean bean : queryList) {
                        list.add(bean);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });

    }

}
