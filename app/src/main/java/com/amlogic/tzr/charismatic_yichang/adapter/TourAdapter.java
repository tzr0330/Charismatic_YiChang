package com.amlogic.tzr.charismatic_yichang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.VolleyImageCache;
import com.amlogic.tzr.charismatic_yichang.bean.TourListBean;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;


public class TourAdapter extends BaseAdapter {
    private Context context;
    private List<TourListBean> list;

    public TourAdapter(Context context, List<TourListBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<TourListBean> list) {
        this.list = list;
    }

    public List<TourListBean> getData() {
        return list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new viewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tour_list, null);
            viewHolder.imageView = (NetworkImageView) convertView.findViewById(R.id.iv_itl_img);
            viewHolder.titletTextView = (TextView) convertView.findViewById(R.id.tv_itl_name);
            viewHolder.scoreTextView= (TextView) convertView.findViewById(R.id.tv_itl_sore);
            viewHolder.addTextView= (TextView) convertView.findViewById(R.id.tv_itl_address);
            viewHolder.priceTextView= (TextView) convertView.findViewById(R.id.tv_itl_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (viewHolder) convertView.getTag();
        }
        if (list.size() > 0) {
            TourListBean bean = list.get(position);
            if (bean.getTour_thumb()!=null){
                BmobFile icon=bean.getTour_thumb();
                String url=icon.getFileUrl(context);
                VolleyImageCache.networkImageViewUse(viewHolder.imageView,url);
            }else{
                viewHolder.imageView.setImageResource(R.mipmap.pic_default);
            }
            viewHolder.titletTextView.setText(bean.getTour_name());
            viewHolder.scoreTextView.setText(bean.getTour_score());
            viewHolder.addTextView.setText(bean.getTour_address());
            if (bean.getTour_price()!=null){
                viewHolder.priceTextView.setText(bean.getTour_price());
            }else {
                viewHolder.priceTextView.setText("");
            }

        }

        return convertView;
    }

    static class viewHolder {
        NetworkImageView imageView;
        TextView titletTextView,scoreTextView,addTextView,priceTextView;
    }
}
