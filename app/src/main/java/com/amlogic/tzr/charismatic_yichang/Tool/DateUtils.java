package com.amlogic.tzr.charismatic_yichang.Tool;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author WangYuWen
 * @version 1.0
 * @date 2015年1月30日
 * @Copyright: Copyright (c) 2014 Shenzhen Utoow Technology Co., Ltd. All rights
 *             reserved.
 */
public class DateUtils {
	/**
	 * 获取系统当前时间
	 *
	 * @version 1.0
	 * @createTime 2015年1月30日,下午1:50:53
	 * @updateTime 2015年1月30日,下午1:50:53
	 * @createAuthor WangYuWen
	 * @updateAuthor WangYuWen
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 
	 * 获取系统当前日期
	 *
	 * @version 1.0
	 * @createTime 2015年3月10日,下午3:40:49
	 * @updateTime 2015年3月10日,下午3:40:49
	 * @createAuthor WangYuWen
	 * @updateAuthor WangYuWen
	 * @updateInfo (此处输入修改内容,若无修改可不写.)
	 *
	 @SuppressLint("SimpleDateFormat")
	* @return
	 */
	public static String getDay() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
}
