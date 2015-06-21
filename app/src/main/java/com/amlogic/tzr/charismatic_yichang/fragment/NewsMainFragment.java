package com.amlogic.tzr.charismatic_yichang.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsMainFragment extends Fragment {
    private Context mContext;
    private View fm_view;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> fragments;

    private NewsFragment new_fm,hot_fm,recommand_fm;

    private FragmentAdapter fragmentAdapter;


    public NewsMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fm_view=inflater.inflate(R.layout.fragment_news_main, container, false);
        if (fm_view!=null){
           initView();
        }

        return fm_view;
    }

    private void initView() {
        mTabLayout= (TabLayout) fm_view.findViewById(R.id.tl_news);
        mViewPager= (ViewPager) fm_view.findViewById(R.id.vp_news);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.recommand_news));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.new_news));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.hot_news));
        fragments=new ArrayList<Fragment>();
        new_fm=NewsFragment.newInstance(NewsFragment.FLAG_NEW);
        hot_fm=NewsFragment.newInstance(NewsFragment.FLAG_HOT);
        recommand_fm=NewsFragment.newInstance(NewsFragment.FLAG_RECOMMAND);
        fragments.add(recommand_fm);
        fragments.add(new_fm);
        fragments.add(hot_fm);
        List<String> titles=new ArrayList<String>();
        titles.add(getResources().getString(R.string.recommand_news));
        titles.add(getResources().getString(R.string.new_news));
        titles.add(getResources().getString(R.string.hot_news));
        fragmentAdapter=new FragmentAdapter(getActivity().getSupportFragmentManager(),fragments,titles);
        mViewPager.setAdapter(fragmentAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(fragmentAdapter);
    }


}
