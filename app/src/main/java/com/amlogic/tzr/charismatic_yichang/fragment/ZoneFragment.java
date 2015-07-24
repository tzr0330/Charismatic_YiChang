package com.amlogic.tzr.charismatic_yichang.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.amlogic.tzr.charismatic_yichang.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoneFragment extends Fragment {
    public static final String TITLE = "title";

    private String mTitle = "Defaut Value";

    public static final String USER_ID = "user_id";

    private ListView mListView;

    private List<String> mDatas = new ArrayList<String>();

    private View view;
    public ZoneFragment() {
        // Required empty public constructor
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
        mListView= (ListView) view.findViewById(R.id.lv_fz_list);
        for (int i = 0; i < 20; i++)
        {
            mDatas.add(mTitle+" -> " + i);
        }
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.item, R.id.id_info, mDatas)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                Log.e("tag", "convertView = " + convertView);
                return super.getView(position, convertView, parent);
            }
        });
    }

    public static ZoneFragment newInstance(String userId){
       ZoneFragment fragment = new ZoneFragment();
       Bundle bundle = new Bundle();
       bundle.putString(USER_ID, userId);
       fragment.setArguments(bundle);
       return fragment;
   }

}
