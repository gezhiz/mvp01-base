package com.mvp01.common.bean;

import java.util.Date;

/**
 * 用于生成accesstoken的bean
 * Created by gezhizheng on 16/6/16
 */
public class UserAccess {
    private String userId;
    private String source;
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
