package com.amlogic.tzr.charismatic_yichang.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/6/22.
 */
public class NewsDetailBean extends BmobObject{
    private String news_time;
    private String news_source;
    private String news_content;
    private String news_id;

    public String getNews_time() {
        return news_time;
    }

    public String getNews_source() {
        return news_source;
    }

    public String getNews_content() {
        return news_content;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_time(String news_time) {
        this.news_time = news_time;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }

    public void setNews_source(String news_source) {
        this.news_source = news_source;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }
}
