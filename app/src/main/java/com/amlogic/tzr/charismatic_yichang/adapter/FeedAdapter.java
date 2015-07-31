package com.amlogic.tzr.charismatic_yichang.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.ApplicationController;
import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.LogManager;
import com.amlogic.tzr.charismatic_yichang.Tool.ScreenUtil;
import com.amlogic.tzr.charismatic_yichang.activity.LoginActivity;
import com.amlogic.tzr.charismatic_yichang.bean.Feed;
import com.amlogic.tzr.charismatic_yichang.bean.User;
import com.amlogic.tzr.charismatic_yichang.view.CircleTransformation;
import com.amlogic.tzr.charismatic_yichang.view.ImageCompressTransformation;
import com.amlogic.tzr.charismatic_yichang.view.ShowMaxImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2015/7/22.
 */
public class FeedAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    private static final String TAG = "FeedAdapter";
    private static final int VIEW_TYPE_DEFAULT = 1;

    private static final int VIEW_TYPE_LOADER = 2;

    private static final int ANIMATED_ITEMS_COUNT = 2;

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    private Context mContext;

    private List<Feed> list;
    private int loveCount;
    private int lastAnimatedPosition = -1;
    private boolean animateItems = false;
    private final Map<String, String> likeAnimations = new HashMap<String, String>();
    private final ArrayList<Integer> likedPositions = new ArrayList<>();

    private User mUser;

    private OnFeedItemClickListener onFeedItemClickListener;

    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
        this.onFeedItemClickListener = onFeedItemClickListener;
    }

    public FeedAdapter(Context mContext, List<Feed> list) {
        this.mContext = mContext;
        this.list = list;
        mUser= ApplicationController.getInstance().getmUser();
    }

    public void setData(List<Feed> list){
        this.list = list;
    }

    public List<Feed> getData(){
        return this.list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final   View view= LayoutInflater.from(mContext).inflate(R.layout.item_feed_list,parent,false);
        final  CellFeedViewHolder cellFeedViewHolder = new CellFeedViewHolder(view);
        mUser=BmobUser.getCurrentUser(mContext,User.class);
        cellFeedViewHolder.headImage.setOnClickListener(this);
        cellFeedViewHolder.btnLike.setOnClickListener(this);
        cellFeedViewHolder.tsLikesCounter.setOnClickListener(this);
        cellFeedViewHolder.btnComments.setOnClickListener(this);

        return cellFeedViewHolder;
    }

    private void runEnterAnimation(View view, int position) {

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(ScreenUtil.getScreenHeight(mContext));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        CellFeedViewHolder feedViewHolder= (CellFeedViewHolder) holder;
        if (list.size()>0){
            Feed bean=list.get(position);
            User user=bean.getUser();
            feedViewHolder.nameView.setText(bean.getUser().getUsername());
            if (bean.getUser().getHead_thumb()!=null) {
                BmobFile icon = bean.getUser().getHead_thumb();
                String url = icon.getFileUrl(mContext);
                Picasso.with(mContext).load(url).into(feedViewHolder.headImage);
            }else{
                Picasso.with(mContext).load(R.mipmap.ic_user).transform(new CircleTransformation()).into(feedViewHolder.headImage);
            }
            if (bean.getContent()!=null){
                feedViewHolder.contentView.setText(bean.getContent());
            }
            if (bean.getPhoto()!=null){
                BmobFile icon =bean.getPhoto();
                String url = icon.getFileUrl(mContext);
                Picasso.with(mContext).load(url).placeholder(R.drawable.pic_default_large).error(R.drawable.pic_default_large).transform(new ImageCompressTransformation()).into(feedViewHolder.mPhotoView);
            }

            feedViewHolder.headImage.setTag(feedViewHolder);
            feedViewHolder.btnLike.setTag(feedViewHolder);
            feedViewHolder.btnComments.setTag(feedViewHolder);
            feedViewHolder.tsLikesCounter.setTag(feedViewHolder);

            likeAnimations.clear();
            updateHeartButton(feedViewHolder, false);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        switch (viewId){
            case R.id.iv_ifl_head:

                break;
            case R.id.btn_ifl_Like:
                CellFeedViewHolder holder = (CellFeedViewHolder) view.getTag();
                mUser=ApplicationController.getInstance().getmUser();
                if (mUser!=null) {
                    if (!likeAnimations.containsKey(list.get(holder.getPosition()).getObjectId())) {
                        updateHeartButton(holder, true);
                    }
                }else{
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_ifl_Comments:

                break;
        }

    }


    private void updateLikesCounter(CellFeedViewHolder holder, boolean animated) {

        if (animated) {
            int currentLikesCount = loveCount+1;
            String likesCountText=currentLikesCount+"个人赞过";
            holder.tsLikesCounter.setText(likesCountText);

        } else {
            int currentLikesCount = loveCount;
            String likesCountText=currentLikesCount+"个人赞过";
            holder.tsLikesCounter.setCurrentText(likesCountText);
        }

    }

    private void updateHeartButton(final CellFeedViewHolder holder, boolean animated) {
        if (animated) {
            if (!likeAnimations.containsKey(list.get(holder.getPosition()).getObjectId())){
                AnimatorSet animatorSet = new AnimatorSet();
                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.btnLike, "rotation", 0f, 360f);
                rotationAnim.setDuration(300);
                rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.btnLike, "scaleX", 0.2f, 1f);
                bounceAnimX.setDuration(300);
                bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.btnLike, "scaleY", 0.2f, 1f);
                bounceAnimY.setDuration(300);
                bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                bounceAnimY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.btnLike.setImageResource(R.drawable.ic_heart_red);
                    }
                });

                animatorSet.play(rotationAnim);
                animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setCurrentLike(holder);
                    }
                });

                animatorSet.start();
            }
        } else {
            getLikeAnimationState(holder);
//            if (likedPositions.contains(holder.getPosition())) {
//                holder.btnLike.setImageResource(R.drawable.ic_heart_red);
//            } else {
//                holder.btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
//            }
        }
    }

    private void getLikeAnimationState(final CellFeedViewHolder holder) {
        BmobQuery<User> query=new BmobQuery<User>();
        Feed feed=new Feed();
        feed.setObjectId(list.get(holder.getPosition()).getObjectId());
        query.addWhereRelatedTo("likes", new BmobPointer(feed));
            query.findObjects(mContext, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list_user) {
                loveCount = list_user.size();
                if (loveCount > 0&&mUser!=null) {
                    for (User user : list_user) {
                        LogManager.e(TAG, "getLikeAnimationState is success!----mUser=="+mUser.getObjectId());
                        LogManager.e(TAG, "user is ----"+user.getObjectId()+"--");
                        LogManager.e(TAG, "user and mUser is equal= ----"+user.getObjectId().equals(mUser.getObjectId()));
                        if (user.getObjectId().equals(mUser.getObjectId())) {
                            likeAnimations.put(list.get(holder.getPosition()).getObjectId(), list.get(holder.getPosition()).getObjectId());
                            holder.btnLike.setImageResource(R.drawable.ic_heart_red);
                        }
                    }
                } else {
                    holder.btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
                }
                updateLikesCounter(holder, false);
            }

            @Override
            public void onError(int i, String s) {
                loveCount=0;
                holder.btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
            }
        });

    }
//
//    private void resetLikeAnimationState(final CellFeedViewHolder holder) {
//        Feed feed=new Feed();
//        feed.setLove(getCurrentLove(holder));
//        feed.update(mContext, list.get(holder.getPosition()).getObjectId(), new UpdateListener() {
//            @Override
//            public void onSuccess() {
//
//                holder.btnLike.setImageResource(R.drawable.ic_heart_red);
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                holder.btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
//            }
//        });
//
//    }

    private void setCurrentLike(final CellFeedViewHolder holder){
        User mUser= BmobUser.getCurrentUser(mContext, User.class);
        Feed feed=list.get(holder.getPosition());
        BmobRelation relation = new BmobRelation();
        relation.add(mUser);
        feed.setLikes(relation);
        feed.update(mContext, new UpdateListener() {
            @Override
            public void onSuccess() {
                LogManager.e(TAG, "setCurrentLike is success!");
                updateLikesCounter(holder, true);
                likeAnimations.put(list.get(holder.getPosition()).getObjectId(), list.get(holder.getPosition()).getObjectId());
                holder.btnLike.setImageResource(R.drawable.ic_heart_red);
            }

            @Override
            public void onFailure(int i, String s) {
                LogManager.e(TAG, "setCurrentLike is onFailure!--" + s);
                holder.btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
            }
        });

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
