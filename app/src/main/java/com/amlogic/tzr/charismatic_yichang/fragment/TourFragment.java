package com.amlogic.tzr.charismatic_yichang.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.activity.TourDetailActivity;
import com.amlogic.tzr.charismatic_yichang.adapter.TourAdapter;
import com.amlogic.tzr.charismatic_yichang.bean.TourListBean;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class TourFragment extends Fragment {
    private static final String TAG = "TourFragment";
    private static final int STATE_REFRESH = 0;
    private static final int STATE_MORE = 1;
    private int limit = 10;
    private int curPage = 0;
    private ILoadingLayout mILoadingLayout;
    private PullToRefreshListView mPullToRefreshView;
    private ListView mMsgListView;
    private TourAdapter mAdapter;
    private View fragmentView;
    private Context context;
    private List<TourListBean> list ;
    private BmobQuery<TourListBean> bmobQuery;

    public TourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        list=new ArrayList<TourListBean>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_tour, null);
        initPullToRefresh();
        queryData(0,STATE_REFRESH);
        return fragmentView;
    }

    private void initPullToRefresh() {
        mPullToRefreshView = (PullToRefreshListView)fragmentView.findViewById(R.id.prl_ft_list);
        mILoadingLayout = mPullToRefreshView.getLoadingLayoutProxy();
        mILoadingLayout.setLastUpdatedLabel("");
        mILoadingLayout
                .setPullLabel(getString(R.string.pull_to_refresh_bottom_pull));
        mILoadingLayout
                .setRefreshingLabel(getString(R.string.pull_to_refresh_bottom_refreshing));
        mILoadingLayout
                .setReleaseLabel(getString(R.string.pull_to_refresh_bottom_release));
        // //滑动监听
        mPullToRefreshView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    mILoadingLayout.setLastUpdatedLabel("");
                    mILoadingLayout
                            .setPullLabel(getString(R.string.pull_to_refresh_top_pull));
                    mILoadingLayout
                            .setRefreshingLabel(getString(R.string.pull_to_refresh_top_refreshing));
                    mILoadingLayout
                            .setReleaseLabel(getString(R.string.pull_to_refresh_top_release));
                } else if (firstVisibleItem + visibleItemCount + 1 == totalItemCount) {
                    mILoadingLayout.setLastUpdatedLabel("");
                    mILoadingLayout
                            .setPullLabel(getString(R.string.pull_to_refresh_bottom_pull));
                    mILoadingLayout
                            .setRefreshingLabel(getString(R.string.pull_to_refresh_bottom_refreshing));
                    mILoadingLayout
                            .setReleaseLabel(getString(R.string.pull_to_refresh_bottom_release));
                }
            }
        });

        // 下拉刷新监听
        mPullToRefreshView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        // 下拉刷新(从第一页开始装载数据)
                        queryData(0, STATE_REFRESH);
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        // 上拉加载更多(加载下一页数据)
                        queryData(curPage, STATE_MORE);
                    }
                });
        mPullToRefreshView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent tourId_intent=new Intent(context, TourDetailActivity.class);
                String tour_id=list.get(position-1).getObjectId();
                String tour_title=list.get(position-1).getTour_name();
                Bundle bundle=new Bundle();
                bundle.putCharSequence("tour_id",tour_id);
                bundle.putCharSequence("tour_title",tour_title);
                tourId_intent.putExtras(bundle);
                startActivity(tourId_intent);
            }
        });

        mMsgListView = mPullToRefreshView.getRefreshableView();
        mAdapter=new TourAdapter(context,list);
        mMsgListView.setAdapter(mAdapter);
    }

    private void queryData(final int page, final int actionType){
        bmobQuery =new BmobQuery<TourListBean>();
        bmobQuery.setLimit(limit);
        bmobQuery.setSkip(page * limit);
        bmobQuery.findObjects(context, new FindListener<TourListBean>() {
            @Override
            public void onSuccess(List<TourListBean> queryList) {
                if (queryList.size() > 0) {
                    if (actionType == STATE_REFRESH) {

                        curPage = 0;
                        list.clear();
                    }

                    for (TourListBean bean : queryList) {
                        list.add(bean);
                    }
                    curPage++;
                } else if (actionType == STATE_MORE) {
                    showToast("没有更多数据了");
                } else if (actionType == STATE_REFRESH) {

                }
                mPullToRefreshView.onRefreshComplete();
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
