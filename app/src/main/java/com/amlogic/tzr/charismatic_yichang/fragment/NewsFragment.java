package com.amlogic.tzr.charismatic_yichang.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.adapter.NewsAdapter;
import com.amlogic.tzr.charismatic_yichang.bean.NewsListBean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    public static final String BUNDLE_URL = "oder";
    public static final String FLAG_NEW="new";
    public static final String FLAG_HOT="hot";
    public static final String FLAG_RECOMMAND="recommand";

    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多

    private String type;
    private int page_total = 0;
    private int page = 1;
    private int default_pageSize = 10;
    private int show_id = 0;
    private View fragmentView;
    private Context context;
    private NewsAdapter mAdapter;
    private ListView news_Listview;
    private List<NewsListBean> list = new ArrayList<NewsListBean>();
    private int pageNum = 0;


    private BmobQuery<NewsListBean> bmobQuery;
    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        bmobQuery = new BmobQuery<NewsListBean>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_news, null);

        initAdapter();
        Bundle arguments = getArguments();
        if (arguments != null) {
            list.clear();
            type = arguments.getString(BUNDLE_URL);
            if (type.equals(FLAG_NEW)){
                 bmobQuery.addWhereEqualTo("news_type",FLAG_NEW);
                bmobQuery.findObjects(context, new FindListener<NewsListBean>() {
                    @Override
                    public void onSuccess(List<NewsListBean> queryList) {
                        list=queryList;
                        mAdapter.setData(list);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(context,"查询失败----sorry！",Toast.LENGTH_SHORT).show();
                    }
                });
            }else if (type.equals(FLAG_HOT)){

            }else if (type.equals(FLAG_RECOMMAND)){

            }

        }
        return fragmentView;
    }

    public static NewsFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_URL, type);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    private void initAdapter() {

        news_Listview = (ListView) fragmentView.findViewById(R.id.lv_nf_list);
        mAdapter = new NewsAdapter(context, list);
        news_Listview.setAdapter(mAdapter);
        news_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {


            }
        });

    }

}
