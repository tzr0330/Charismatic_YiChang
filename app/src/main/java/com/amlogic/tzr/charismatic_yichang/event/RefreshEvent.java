package com.amlogic.tzr.charismatic_yichang.event;

/**
 * Created by Administrator on 2015/7/23.
 */
public class RefreshEvent {
    private boolean isRefresh=false;

    public RefreshEvent(boolean isRefresh){
        this.isRefresh=isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }
    public boolean getIsRefresh(){
      return isRefresh;
    }
}
