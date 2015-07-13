package com.amlogic.tzr.charismatic_yichang.event;

import com.amlogic.tzr.charismatic_yichang.bean.User;

/**
 * Created by Administrator on 2015/7/13.
 */
public class LoginEvent {
    private boolean isLogin=false;
    private User mUser;

    public LoginEvent(boolean isLogin,User mUser) {
        this.isLogin = isLogin;
        this.mUser=mUser;
    }
    public boolean isLogin(){
        return  isLogin;
    }
    public User getLoginUser(){
        return mUser;
    }
    public void setIsLogin(boolean isLogin){
        this.isLogin = isLogin;
    }
    public void setloginUser(User mUser){
        this.mUser=mUser;
    }
}
