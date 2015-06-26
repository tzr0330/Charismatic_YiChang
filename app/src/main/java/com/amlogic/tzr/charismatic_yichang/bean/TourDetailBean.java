package com.amlogic.tzr.charismatic_yichang.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2015/6/27.
 */
public class TourDetailBean extends BmobObject {
    private String tour_id;
    private BmobFile tour_img1;
    private BmobFile tour_img2;
    private BmobFile tour_img3;
    private BmobFile tour_img4;
    private String tour_info;
    private String tour_tip;

    public String getTour_id() {
        return tour_id;
    }

    public void setTour_id(String tour_id) {
        this.tour_id = tour_id;
    }

    public BmobFile getTour_img1() {
        return tour_img1;
    }

    public void setTour_img1(BmobFile tour_img1) {
        this.tour_img1 = tour_img1;
    }

    public BmobFile getTour_img2() {
        return tour_img2;
    }

    public void setTour_img2(BmobFile tour_img2) {
        this.tour_img2 = tour_img2;
    }

    public BmobFile getTour_img3() {
        return tour_img3;
    }

    public void setTour_img3(BmobFile tour_img3) {
        this.tour_img3 = tour_img3;
    }

    public BmobFile getTour_img4() {
        return tour_img4;
    }

    public void setTour_img4(BmobFile tour_img4) {
        this.tour_img4 = tour_img4;
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
