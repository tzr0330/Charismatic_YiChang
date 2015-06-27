package com.amlogic.tzr.charismatic_yichang.bean;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/6/27.
 */
public class TourDetailBean extends BmobObject {
    private String tour_id;
    private ArrayList<String> tour_banner;
    private String tour_info;
    private String tour_tip;

    public String getTour_id() {
        return tour_id;
    }

    public void setTour_id(String tour_id) {
        this.tour_id = tour_id;
    }

    public ArrayList<String> getTour_banner() {
        return tour_banner;
    }

    public void setTour_banner(ArrayList<String> tour_banner) {
        this.tour_banner = tour_banner;
    }

    public String getTour_info() {
        return tour_info;
    }

    public void setTour_info(String tour_info) {
        this.tour_info = tour_info;
    }

    public String getTour_tip() {
        return tour_tip;
    }

    public void setTour_tip(String tour_tip) {
        this.tour_tip = tour_tip;
    }
}
