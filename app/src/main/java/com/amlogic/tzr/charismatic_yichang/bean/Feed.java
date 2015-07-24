package com.amlogic.tzr.charismatic_yichang.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2015/7/22.
 */
public class Feed extends BmobObject {
    private BmobFile photo;
    private String content;
    private int comment;
    private int love;
    private boolean isLove;
    private User user;
    private BmobRelation likes;

    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public boolean isLove() {
        return isLove;
    }

    public void setIsLove(boolean isLove) {
        this.isLove = isLove;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "photo=" + photo +
                ", content='" + content + '\'' +
                ", comment=" + comment +
                ", love=" + love +
                ", isLove=" + isLove +
                ", user=" + user +
                ", likes=" + likes +
                '}';
    }
}
