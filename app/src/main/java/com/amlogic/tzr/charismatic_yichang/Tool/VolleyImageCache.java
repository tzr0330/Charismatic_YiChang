package com.amlogic.tzr.charismatic_yichang.Tool;

import com.amlogic.tzr.charismatic_yichang.ApplicationController;
import com.amlogic.tzr.charismatic_yichang.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by Administrator on 2015/6/21.
 */
public class VolleyImageCache {

    public static void networkImageViewUse(NetworkImageView iv, String url) {
        ImageLoader imLoader = new ImageLoader(ApplicationController.getInstance().getRequestQueue(), new BitmapCache());
        iv.setDefaultImageResId(R.mipmap.pic_default);
        iv.setErrorImageResId(R.mipmap.pic_error);
        iv.setImageUrl(url, imLoader);
    }
}
