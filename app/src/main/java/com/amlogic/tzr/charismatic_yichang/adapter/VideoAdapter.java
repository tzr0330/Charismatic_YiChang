package com.amlogic.tzr.charismatic_yichang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.ApplicationController;
import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.BitmapCache;
import com.amlogic.tzr.charismatic_yichang.Tool.ScreenUtil;
import com.amlogic.tzr.charismatic_yichang.bean.VideoListBean;
import com.amlogic.tzr.charismatic_yichang.view.ShowMaxImageView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2015/7/4.
 */
public class VideoAdapter extends RecyclerView.Adapter {
    private static final int ANIMATED_ITEMS_COUNT = 2;

    private Context mContext;

    private int lastAnimatedPosition = -1;

    private List<VideoListBean> list;

    private RequestQueue mQueue;

    private ImageLoader mImageLoader;

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }



    public VideoAdapter(Context mContext, List<VideoListBean> list) {
        this.mContext = mContext;
        this.list = list;
        mQueue = ApplicationController.getInstance().getRequestQueue();
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
    }

    private void runEnterAnimation(View view,int position){
//        if (position >=ANIMATED_ITEMS_COUNT-1){
//           return;
//        }

        if (position>lastAnimatedPosition){
            lastAnimatedPosition=position;
            view.setTranslationY(ScreenUtil.getScreenHeight(mContext));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.0f))
                    .setDuration(800)
                    .start();

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final  View view= LayoutInflater.from(mContext).inflate(R.layout.item_video_list,parent,false);
        return new CellVideoHoder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        CellVideoHoder videoHoder= (CellVideoHoder) holder;
        if (list.size()>0){
            VideoListBean bean=list.get(position);
//            Log.e("VideoAdapter", "------ >>>++++  bean.getVideo_title()="+bean.getVideo_title());
            videoHoder.titleView.setText(bean.getVideo_title());
            videoHoder.mImageView.setImageResource(R.mipmap.ic_class_icon);
            if (bean.getVideo_thumb()!=null){
                BmobFile icon=bean.getVideo_thumb();
                String url=icon.getFileUrl(mContext);
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(videoHoder.mImageView, R.mipmap.pic_default, R.mipmap.pic_default);
                mImageLoader.get(url, imageListener);
            }
            videoHoder.clockView.setText(bean.getVideo_time());
            videoHoder.dateView.setText(bean.getVideo_date());
        }
        runEnterAnimation(holder.itemView,position);

        if (mOnItemClickLitener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    static class CellVideoHoder extends RecyclerView.ViewHolder{

        ShowMaxImageView mImageView;
        TextView titleView,clockView,dateView;

        public CellVideoHoder(View itemView) {
            super(itemView);
            mImageView= (ShowMaxImageView) itemView.findViewById(R.id.iv_vp_video);
            titleView= (TextView) itemView.findViewById(R.id.tv_vp_title);
            clockView= (TextView) itemView.findViewById(R.id.tv_vp_clock);
            dateView= (TextView) itemView.findViewById(R.id.tv_vp_date);
        }
    }
}
