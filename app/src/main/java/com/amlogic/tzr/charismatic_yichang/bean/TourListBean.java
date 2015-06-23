package com.amlogic.tzr.charismatic_yichang.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2015/6/22.
 */
public class TourListBean extends BmobObject{
    private BmobFile tour_thumb;
    private String tour_name;
    private String tour_score;
    private String tour_address;
    private String tour_price;

    public BmobFile getTour_thumb() {
        return tour_thumb;
    }

    public String getTour_name() {
        return tour_name;
    }

    public String getTour_score() {
        return tour_score;
    }

    public String getTour_address() {
        return tour_address;
    }

    public String getTour_price() {
        return tour_price;
    }

    public void setTour_name(String tour_name) {
        this.tour_name = tour_name;
    }

    public void setTour_thumb(BmobFile tour_thumb) {
        this.tour_thumb = tour_thumb;
    }

    public void setTour_score(String tour_score) {
        this.tour_score = tour_score;
    }

    public void setTour_address(String tour_address) {
        this.tour_address = tour_address;
    }

    public void setTour_price(String tour_price) {
        this.tour_price = tour_price;
    }
}
