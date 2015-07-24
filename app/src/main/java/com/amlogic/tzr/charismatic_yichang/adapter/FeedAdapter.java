package com.amlogic.tzr.charismatic_yichang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.ApplicationController;
import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.BitmapCache;
import com.amlogic.tzr.charismatic_yichang.bean.Feed;
import com.amlogic.tzr.charismatic_yichang.bean.User;
import com.amlogic.tzr.charismatic_yichang.view.ShowMaxImageView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2015/7/22.
 */
public class FeedAdapter extends RecyclerView.Adapter {
    private Context mContext;

    private List<Feed> list;
    private RequestQueue mQueue;

    private ImageLoader mImageLoader;

    private OnFeedItemClickListener onFeedItemClickListener;

    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
        this.onFeedItemClickListener = onFeedItemClickListener;
    }

    public FeedAdapter(Context mContext, List<Feed> list) {
        this.mContext = mContext;
        this.list = list;
        mQueue = ApplicationController.getInstance().getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final  View view= LayoutInflater.from(mContext).inflate(R.layout.item_feed_list,parent,false);
        return new CellFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CellFeedViewHolder feedViewHolder= (CellFeedViewHolder) holder;
        if (list.size()>0){
            Feed bean=list.get(position);
            User user=bean.getUser();

            feedViewHolder.nameView.setText(bean.getUser().getUsername());
            if (bean.getUser().getHead_thumb()!=null) {
                BmobFile icon = bean.getUser().getHead_thumb();
                String url = icon.getFileUrl(mContext);
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(feedViewHolder.headImage, R.mipmap.pic_default, R.mipmap.pic_error);
                mImageLoader.get(url, imageListener);
            }
            if (bean.getContent()!=null){
                feedViewHolder.contentView.setText(bean.getContent());
            }
            if (bean.getPhoto()!=null){
                BmobFile icon =bean.getPhoto();
                String url = icon.getFileUrl(mContext);
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(feedViewHolder.mPhotoView, R.mipmap.pic_default, R.mipmap.pic_error);
                mImageLoader.get(url, imageListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


   public static class CellFeedViewHolder extends RecyclerView.ViewHolder {
       ImageView headImage;
       ShowMaxImageView mPhotoView;
       TextView nameView,contentView;
       ImageButton btnLike,btnComments;
       TextSwitcher tsLikesCounter;

       public CellFeedViewHolder(View itemView) {
           super(itemView);
           headImage= (ImageView) itemView.findViewById(R.id.iv_ifl_head);
           mPhotoView= (ShowMaxImageView) itemView.findViewById(R.id.iv_ifl_photo);
           nameView= (TextView) itemView.findViewById(R.id.tv_ifl_name);
           contentView= (TextView) itemView.findViewById(R.id.tv_ifl_content);
           btnLike= (ImageButton) itemView.findViewById(R.id.btn_ifl_Like);
           btnComments= (ImageButton) itemView.findViewById(R.id.btn_ifl_Comments);
           tsLikesCounter= (TextSwitcher) itemView.findViewById(R.id.ts_ifl_LikesCounter);
       }
   }

    public interface OnFeedItemClickListener {
        public void onCommentsClick(View v, int position);

        public void onMoreClick(View v, int position);

        public void onProfileClick(View v);
    }
}
