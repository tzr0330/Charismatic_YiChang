package com.amlogic.tzr.charismatic_yichang.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.amlogic.tzr.charismatic_yichang.BaseActivity;
import com.amlogic.tzr.charismatic_yichang.R;
import com.amlogic.tzr.charismatic_yichang.Tool.DensityUtil;
import com.amlogic.tzr.charismatic_yichang.adapter.VideoListAdapter;
import com.amlogic.tzr.charismatic_yichang.bean.VideoListBean;
import com.amlogic.tzr.charismatic_yichang.view.FullVideoView;

import java.util.ArrayList;

/**
 * 视频播放界面
 * 
 * @author WangYuWen
 * @version 1.0
 * @date 2015年3月5日
 * @Copyright: Copyright (c) 2014 Shenzhen Utoow Technology Co., Ltd. All rights
 *             reserved.
 */
public class VideoPlayActivity extends BaseActivity {

	/** 右边列表控件 */
	private ListView listview;
	/** 右边缩放控件 */
	private RelativeLayout right_relative;
	/** 上一首 */
	private ImageView img_last;
	/** 下一首 */
	private ImageView img_next;
	/** 播放 */
	private ImageView img_start;
	/** 全屏播放 */
	private ImageView img_full;
	/** 加减声音 */
	private ImageView img_voice;
	/** 列表适配器 */
	private VideoListAdapter adapter;
	/** 列表集合 */
	private ArrayList<VideoListBean> list;
	/** 播放视频的背景图片 */
	private ImageView video_img;

	/** 亮度或者声音的图片 */
	private ImageView img_center;
	/** 调节进度 */
	private ImageView img_pres_white;
	/** 底部视图栏 */
	private RelativeLayout buttom_relative;

	private View view;

	private FullVideoView video_VideoView;
	/** 当前播放时间 */
	private TextView txt_current_time;
	/** 总时间 */
	private TextView txt_max_time;
	/** 当前播放进度 */
	private ImageView img_white;
	/** 播放进度背景 */
	private ImageView img_bg;
	/** 拖放进度 */
	private ImageView img_center_speed;

	private AudioManager mAudioManager;
	/** 最大音量 */
	private int mMaxVolume;
	/** 当前声音 */
	private int mVolume = -1;
	/** 当前亮度 */
	private float mBrightness = -1f;
	/** 屏幕宽度 */
	private int width;
	/** 屏幕高度 */
	private int height;
	/** 手势 */
	private GestureDetector mGestureDetector;
	/** 视频总长度 */
	private long mVideo_total_length;
	/** 视频当前长度 */
	private long mVideo_current_length;
	/** 按下手势时的X点 */
	private float downX = 0;
	/** 按下手势时的Y点 */
	private float downY = 0;
	/** 移动手势时的Y点 */
	private float moveY = 0;
	/** 移动手势时的X点 */
	private float moveX = 0;
	/** 记录上一次移动手势时的X点 */
	private float OldMoveX = 0;
	/** 记录上一次移动手势时的y点 */
	private float OldMoveY = 0;
	/** 双数才执行（在用到的地方有详细的解释） */
	private int evens = 0;

	/** 是否显示全屏 */
	private boolean isVH = true;
	/** 是否快进了 */
	private boolean isVideo = false;
	/** 是否调节了音量 */
	private boolean isVoice = false;
	/** 是否调节了亮度 */
	private boolean isBright = false;
	/** 是否点击了开始播放 */
	private boolean isStart = false;
	/** 当前播放位置 */
	private int index_position = 0;

	private static final int handKey = 123;

	/** 视频路径 这是视频本地路径，资源的话你们自己去找，文件夹有创建，*/
	private String Path = Environment.getExternalStorageDirectory() + "/wywVideo/";


	private String url;

	private Uri mUri;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_list);
		Bundle bundle=getIntent().getExtras();
		if (bundle!=null){
			url=bundle.getString("video_url");
			mUri=Uri.parse(url);
		}
//		mUri=Uri.parse("http://vod.yichang.gov.cn/2009/%E6%97%85%E6%B8%B8%E7%AF%87%E7%BD%91%E7%BB%9C%E7%89%88.wmv");
		findViews();
		init();

	}





	private void findViews() {
		img_last = (ImageView) findViewById(R.id.video_img_last);
		img_start = (ImageView) findViewById(R.id.video_img_start);
		img_next = (ImageView) findViewById(R.id.video_img_next);
		img_full = (ImageView) findViewById(R.id.video_img_full);
		img_voice = (ImageView) findViewById(R.id.video_img_voice);
		video_img = (ImageView) findViewById(R.id.video_img);

		img_center = (ImageView) findViewById(R.id.video_img_center);
		img_pres_white = (ImageView) findViewById(R.id.video_img_pres_front);

		txt_current_time = (TextView) findViewById(R.id.video_txt_current_time);
		txt_max_time = (TextView) findViewById(R.id.video_txt_max_time);
		img_white = (ImageView) findViewById(R.id.video_videoview_pres_front);
		img_bg = (ImageView) findViewById(R.id.video_videoview_pres_bg);
		img_center_speed = (ImageView) findViewById(R.id.video_img_center_speed);

		video_VideoView = (FullVideoView) findViewById(R.id.video_VideoView);

		view = findViewById(R.id.video_frame);
		// 获取屏幕宽度
		width = getWindowManager().getDefaultDisplay().getWidth();
		// 获取屏幕高度
		height = getWindowManager().getDefaultDisplay().getHeight();

		right_relative = (RelativeLayout) findViewById(R.id.video_relative_right);
		right_relative.setVisibility(View.INVISIBLE);
		buttom_relative = (RelativeLayout) findViewById(R.id.video_relative_buttom);
		// 右边试图宽度为屏幕宽度的3分之1
		right_relative.getLayoutParams().width = width / 3;

		listview = (ListView) findViewById(R.id.video_listview);

		widgetListener();
	}


	private void init() {

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

//		File file = new File(Path);
//		if (!file.exists()) {
//			file.mkdirs();
//		}

		list = new ArrayList<VideoListBean>();

		mGestureDetector = new GestureDetector(this, new MyGestureListener());

		adapter = new VideoListAdapter(VideoPlayActivity.this, list);
		listview.setAdapter(adapter);

//		new Thread(runnable2).start();
		mVideo_total_length = video_VideoView.getDuration();// 获取视频总长度
		// 设置总时长（总长度换算成时间）
		txt_max_time.setText(length2time(mVideo_total_length));
		isStart = true;
		video_VideoView.setVideoURI(mUri);
		video_VideoView.start();
		img_start.setImageResource(R.drawable.pause_video_df);
		handler.postAtTime(runnable, 0);
	}


	@Override
	protected void onStart() {
		super.onStart();

	}

	/** 点击事件 */
	private void widgetListener() {
		/** 列表点击事件 */
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				index_position = position;
				txt_max_time.setText(R.string.play_time);
				img_start.setImageResource(R.drawable.start_video_df);
				video_VideoView.stopPlayback();
//				setVideo(position);
			}
		});

		/** 上一首 */
		img_last.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				index_position--;
				if (index_position>=0) {
					txt_max_time.setText(R.string.play_time);
					img_start.setImageResource(R.drawable.start_video_df);
					video_VideoView.stopPlayback();
//					setVideo(index_position);
				}else{
					index_position=0;
				}
			}
		});
		/** 下一首 */
		img_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				index_position++;
				img_start.setImageResource(R.drawable.start_video_df);
				txt_max_time.setText(R.string.play_time);
				video_VideoView.stopPlayback();
//				setVideo(index_position);
			}
		});

		/** 播放 */
		img_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				video_img.setVisibility(View.GONE);
				if (video_VideoView.isPlaying()) {
					video_VideoView.pause();
					img_start.setImageResource(R.drawable.start_video_df);
				} else {
					mVideo_total_length = video_VideoView.getDuration();// 获取视频总长度
					// 设置总时长（总长度换算成时间）
					txt_max_time.setText(length2time(mVideo_total_length));
					isStart = true;
					video_VideoView.start();
					img_start.setImageResource(R.drawable.pause_video_df);
					handler.postAtTime(runnable, 0);
				}
			}
		});

		/** 全屏 */
		img_full.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isVH) {
					WindowManager.LayoutParams params = getWindow().getAttributes();
					params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
					getWindow().setAttributes(params);
					getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

					right_relative.setVisibility(View.GONE);
					buttom_relative.setVisibility(View.GONE);

					isVH = false;
				} else {
					WindowManager.LayoutParams params = getWindow().getAttributes();
					params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
					getWindow().setAttributes(params);
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

					buttom_relative.setVisibility(View.VISIBLE);
					isVH = true;
				}
			}
		});
		/** 是否显示右边视图点击事件 */
		img_voice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (right_relative.isShown()) {
					right_relative.setVisibility(View.GONE);
				} else {
					right_relative.setVisibility(View.VISIBLE);
				}
			}
		});
		/** 右边视图 */
		right_relative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				right_relative.setVisibility(View.GONE);
			}
		});
	}

	private Runnable runnable2 = new Runnable() {

		@Override
		public void run() {
//			doSearch(Path);
			Message message = handler.obtainMessage();
			message.what = handKey;
			handler.sendMessage(message);
		}
	};

	/**
	 * 搜索该路径下面的所有文件
	 *
	 * @version 1.0
	 * @createTime 2015年3月12日,下午2:11:47
	 * @updateTime 2015年3月12日,下午2:11:47
	 * @createAuthor WangYuWen
	 * @updateAuthor WangYuWen
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 * @param
	 */
/*	private void doSearch(String path) {
		File file = new File(path);

		if (file.exists()) {
			if (file.isDirectory()) {
				File[] fileArray = file.listFiles();
				for (File f : fileArray) {
					if (f.isDirectory()) {// 判断是否文件夹
								// 文件夹为true
						doSearch(f.getPath());
					} else {
						if (f.getName().endsWith("mp4") || f.getName().endsWith("3gp")) {
							VideoListBean bean = new VideoListBean();
							bean.setVideo_name(f.getName());
							bean.setVideo_path(f.getAbsolutePath());
							Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(f.getAbsolutePath(), Thumbnails.MINI_KIND);
							if (bitmap != null) {
								bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 300 / bitmap.getHeight(), 300, true);
								bean.setVideo_imgbg(bitmap);
							}
							list.add(bean);
						}
					}
				}
			}
		}
	}*/

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event))
			return true;

		// 处理手势结束
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			endGesture();
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 手势结束
	 *
	 * @version 1.0
	 * @createTime 2015年3月5日,上午11:36:43
	 * @updateTime 2015年3月5日,上午11:36:43
	 * @createAuthor WangYuWen
	 * @updateAuthor WangYuWen
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 */
	private void endGesture() {
		if (!isVideo && !isVoice && !isBright) {
			if (right_relative.isShown()) {
				right_relative.setVisibility(View.GONE);
			}
			if (buttom_relative.isShown()) {
				buttom_relative.setVisibility(View.GONE);
			} else {
				buttom_relative.setVisibility(View.VISIBLE);
			}
		}

		isVideo = false;
		isVoice = false;
		isBright = false;

		OldMoveX = 0;
		OldMoveY = 0;

		// 隐藏
		img_center.setVisibility(View.GONE);
		img_center_speed.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == handKey) {
//				setVideo(index_position);
				adapter.notifyDataSetChanged();
			}
		};
	};

	/**
	 * 设置Video该播放视频的准备和视频的背景图片
	 *
	 * @version 1.0
	 * @createTime 2015年3月12日,下午2:17:55
	 * @updateTime 2015年3月12日,下午2:17:55
	 * @createAuthor WangYuWen
	 * @updateAuthor WangYuWen
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 * @param position
	 */
	@SuppressWarnings("deprecation")
/*	private void setVideo(int position) {
		if (list.size() > position && position>-1) {
			Drawable drawable = new BitmapDrawable(list.get(position).getVideo_imgbg());
			video_VideoView.setVideoPath(list.get(position).getVideo_path());
			video_VideoView.requestFocus();
			video_img.setVisibility(View.VISIBLE);
			video_img.setBackgroundDrawable(drawable);
		}
	}*/

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			mVideo_current_length = video_VideoView.getCurrentPosition();

			if (mVideo_current_length >= mVideo_total_length) {
				mVideo_current_length = mVideo_total_length;
			}
			/** 设置当前时长 */
			txt_current_time.setText(length2time(mVideo_current_length));
			/** 设置视频进度 */
			LayoutParams layoutParams = (LayoutParams) img_white.getLayoutParams();
			layoutParams.width = (int) (((float) img_bg.getWidth()) / mVideo_total_length * mVideo_current_length);
			img_white.setLayoutParams(layoutParams);

			handler.postDelayed(runnable, 1000);

			if (mVideo_current_length >= mVideo_total_length) {
				handler.removeCallbacks(runnable);
			}

		}
	};

	/**
	 * 调节音量
	 *
	 * @version 1.0
	 * @createTime 2015年3月5日,上午11:16:35
	 * @updateTime 2015年3月5日,上午11:16:35
	 * @createAuthor WangYuWen
	 * @updateAuthor WangYuWen
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 * @param num
	 */
	private void setVoiceNum(float num) {
		evens++;//这里为什么要用个双数执行呢，因为手势滑动的太快音量只有15，控制了下音量增减速度，不然稍微滑动一下就到了最高或者最低这是我们用户不喜欢看到的。
		mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

		if (mVolume < 0) {
			mVolume = 0;
		}
		if (num < 0 && evens % 2 == 0) {
			mVolume -= 1;
		} else if (num > 0 && evens % 2 == 0) {
			mVolume += 1;
		}

		if (mVolume > mMaxVolume) {
			mVolume = mMaxVolume;
		} else if (mVolume < 0) {
			mVolume = 0;
		}

		img_center.setImageResource(R.drawable.video_voice_bg);
		img_center.setVisibility(View.VISIBLE);
		view.setVisibility(View.VISIBLE);

		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, 0);

		// 变更进度条
		ViewGroup.LayoutParams lp = img_pres_white.getLayoutParams();
		lp.width =DensityUtil.dp2px(VideoPlayActivity.this, 100) * mVolume / mMaxVolume;
		img_pres_white.setLayoutParams(lp);
	}

	/**
	 * 调节视频进度
	 *
	 * @version 1.0
	 * @createTime 2015年3月5日,下午4:48:07
	 * @updateTime 2015年3月5日,下午4:48:07
	 * @createAuthor WangYuWen
	 * @updateAuthor WangYuWen
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 * @param distanceX
	 */
	private void onVideoSpeed(float distanceX) {
		mVideo_current_length = video_VideoView.getCurrentPosition();// 当前播放长度

		if (distanceX > 0) {// 往左滑动 --
			img_center_speed.setVisibility(View.VISIBLE);
			img_center_speed.setImageResource(R.drawable.retreat_video);
			mVideo_current_length -= 1000;// 快进之后长度
		} else if (distanceX < 0) {// 往右滑动 ++
			img_center_speed.setVisibility(View.VISIBLE);
			img_center_speed.setImageResource(R.drawable.speed_video);
			mVideo_current_length += 1000;// 快进之后长度
		}

		if (mVideo_current_length >= mVideo_total_length) {
			mVideo_current_length = mVideo_total_length;
		} else if (mVideo_current_length <= 0) {
			mVideo_current_length = 0;
		}
		video_VideoView.seekTo((int) mVideo_current_length);
		// //定位播放在哪个位置
		handler.postDelayed(runnable, 0);
	}

	/**
	 * 将进度长度转变为进度时间
	 *
	 * @version 1.0
	 * @createTime 2015年3月5日,下午4:25:05
	 * @updateTime 2015年3月5日,下午4:25:05
	 * @createAuthor WangYuWen
	 * @updateAuthor WangYuWen
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 * @param length
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	private String length2time(long length) {
		length /= 1000L;
		long minute = length / 60L;
		long hour = minute / 60L;
		long second = length % 60L;
		minute %= 60L;
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	/**
	 * 调节亮度
	 *
	 * @version 1.0
	 * @createTime 2015年3月5日,上午11:16:44
	 * @updateTime 2015年3月5日,上午11:16:44
	 * @createAuthor WangYuWen
	 * @updateAuthor WangYuWen
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 * @param percent
	 */
	private void onBrightnessSlide(float percent) {
		WindowManager.LayoutParams lpa = getWindow().getAttributes();
		mBrightness = lpa.screenBrightness;

		img_center.setImageResource(R.drawable.video_brightness_bg);
		img_center.setVisibility(View.VISIBLE);
		view.setVisibility(View.VISIBLE);

		lpa.screenBrightness = mBrightness + percent;
		if (lpa.screenBrightness > 1.0f) {
			lpa.screenBrightness = 1.0f;
		} else if (lpa.screenBrightness < 0.01f) {
			lpa.screenBrightness = 0.01f;
		}
		getWindow().setAttributes(lpa);
		// 变更亮度进度条
		ViewGroup.LayoutParams lp = img_pres_white.getLayoutParams();
		lp.width = (int) (DensityUtil.dp2px(VideoPlayActivity.this, 100) * lpa.screenBrightness);
		img_pres_white.setLayoutParams(lp);
	}

	private class MyGestureListener extends SimpleOnGestureListener {
		/** 双击 */
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			return true;
		}

		/** 滑动 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// e1==按下的点 e2==移动的点
			downX = e1.getX();
			downY = e1.getY();
			moveX = e2.getX();
			moveY = e2.getY();
			if (OldMoveX == 0) {
				OldMoveX = downX;
				OldMoveY = downY;
			}
			if (Math.abs(moveY - downY) < Math.abs(moveX - downX) && !isVoice && !isBright && isStart) {
				handler.removeCallbacks(runnable);
				onVideoSpeed((OldMoveX - moveX) / width);
				OldMoveX = moveX;
				isVideo = true;
			} else {
				if (downX > width * 4 / 5 && !isVideo && !isBright) {// 右边滑动
					setVoiceNum((OldMoveY - moveY) / height);
					OldMoveY = moveY;
					isVoice = true;
				} else if (downX < width / 5 && !isVideo && !isVoice) {// 左边滑动
					onBrightnessSlide((OldMoveY - moveY) / height);
					OldMoveY = moveY;
					isBright = true;
				}
			}

			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}
}
