package com.mvp01.common.bean.ret;

/**
 * Created by wenjie on 16/4/7.
 */
public class AccessToken {
    private String access_token;
    private Boolean new_user;
    private String username;
    private String headimg;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Boolean getNew_user() {
        return new_user;
    }

    public void setNew_user(Boolean new_user) {
        this.new_user = new_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }
}
