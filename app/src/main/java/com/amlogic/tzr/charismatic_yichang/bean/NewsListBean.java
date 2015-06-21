package com.amlogic.tzr.charismatic_yichang.bean;

import java.io.File;

import cn.bmob.v3.BmobObject;

public class NewsListBean extends BmobObject {
    private String news_title;
    private File news_thumb;
    private String news_type;

    public String getNews_title() {
        return news_title;
    }

    public File getNews_thumb() {
        return news_thumb;
    }

    public String getNews_type() {
        return news_type;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public void setNews_thumb(File news_thumb) {
        this.news_thumb = news_thumb;
    }

    public void setNews_type(String news_type) {
        this.news_type = news_type;
    }
}
