package com.amlogic.tzr.charismatic_yichang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.bean.NewsListBean;

import java.util.List;


public class NewsAdapter extends BaseAdapter {
    private Context context;
    private List<NewsListBean> list;

    public NewsAdapter(Context context, List<NewsListBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<NewsListBean> list) {
        this.list = list;
    }

    public List<NewsListBean> getData() {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news_list, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_inl_img);
            viewHolder.titletTextView = (TextView) convertView.findViewById(R.id.tv_inl_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (viewHolder) convertView.getTag();
        }
        if (list.size() > 0) {
            NewsListBean bean = list.get(position);
//	    CacheTool.displayImg( viewHolder.imageView,Config.URL_IMG+bean.getThumb());
            viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
            viewHolder.titletTextView.setText(bean.getNews_title());
        }

        return convertView;
    }

    static class viewHolder {
        ImageView imageView;
        TextView titletTextView;
    }
}
