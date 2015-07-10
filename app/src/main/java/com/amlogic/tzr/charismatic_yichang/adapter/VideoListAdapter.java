package com.amlogic.tzr.charismatic_yichang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.DateUtils;
import com.amlogic.tzr.charismatic_yichang.bean.VideoListBean;

import java.util.ArrayList;

public class VideoListAdapter extends BaseAdapter {

	private Context context;
	
	private ArrayList<VideoListBean> list;
	
	public VideoListAdapter(Context context,ArrayList<VideoListBean> list){
		this.context=context;
		this.list=list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		final VideoListBean bean = list.get(position);
		if (convertView==null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_video_listview, null);
			viewHolder.img_video = (ImageView) convertView.findViewById(R.id.item_video_img);
			viewHolder.txt_name = (TextView) convertView.findViewById(R.id.item_video_txt_name);
			viewHolder.txt_time = (TextView) convertView.findViewById(R.id.item_video_txt_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.txt_name.setText(bean.getVideo_title());
//		viewHolder.img_video.setImageBitmap(bean.getVideo_imgbg());
		
		viewHolder.txt_time.setText(DateUtils.getDay());
		
		return convertView;
	}
	
	
	private class ViewHolder{
		/**视频图片*/
		private ImageView img_video;
		/**视频名字*/
		private TextView txt_name;
		/**视频时间*/
		private TextView txt_time;
	}

}
