package com.amlogic.tzr.charismatic_yichang.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2015/7/4.
 */
public class VideoListBean  extends BmobObject {
    private BmobFile video_thumb;
    private String video_title;
    private String video_time;
    private String video_date;
    private String video_url;

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public BmobFile getVideo_thumb() {
        return video_thumb;
    }

    public void setVideo_thumb(BmobFile video_thumb) {
        this.video_thumb = video_thumb;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_time() {
        return video_time;
    }

    public void setVideo_time(String video_time) {
        this.video_time = video_time;
    }

    public String getVideo_date() {
        return video_date;
    }

    public void setVideo_date(String video_date) {
        this.video_date = video_date;
    }
}
