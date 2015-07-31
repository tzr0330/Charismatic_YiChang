package com.amlogic.tzr.charismatic_yichang.event;

import com.amlogic.tzr.charismatic_yichang.bean.User;

/**
 * Created by Administrator on 2015/7/13.
 */
public class LoginEvent {
    private boolean isLogin = false;

    public LoginEvent(boolean isLogin, User mUser) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }
}
