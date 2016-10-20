package com.mvp01.common.bean;

import org.apache.commons.lang.StringUtils;

/**
 * result bean
 *
 * @author by wenjie on 16/1/7.
 */
public class ResultBean {
    public static final String COMMON_OK_MSG = "OK";
    public static final int COMMON_OK_CODE = 0;
    public static final int COMMON_ERROR_CODE = 200;
    private static final ResultBean COMMON_OK_RESULT = new CommonOKResult();

    private int errno;
    private Object data;
    private String errmsg;

    public ResultBean() {
    }

    public ResultBean(int errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }

    public static ResultBean buildOKResult() {
        return COMMON_OK_RESULT;
    }

    public static ResultBean buildOKResult(String msg) {
        return buildOKResult(msg, null);
    }

    public static ResultBean buildOKResult(String msg, Object data) {
        return buildResult(COMMON_OK_CODE, msg, data);
    }

    public static ResultBean buildResult(int errcode, String msg, Object data) {
        ResultBean resultBean = new ResultBean();
        resultBean.setErrno(errcode);
        resultBean.setErrmsg(msg);
        resultBean.setData(data);
        return resultBean;
    }

    public static ResultBean buildOKResultWithObj(Object obj) {
        return buildOKResult(COMMON_OK_MSG, obj);
    }

    public boolean isOK() {
        return COMMON_OK_CODE == this.errno;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String msg) {
        this.errmsg = msg;
    }

    private static class CommonOKResult extends ResultBean {

        public CommonOKResult() {
            setErrno(COMMON_OK_CODE);
            setErrmsg(COMMON_OK_MSG);
        }

        @Override
        public void setErrno(int errno) {
            if (COMMON_OK_CODE != errno) {
                throw new IllegalArgumentException("通用结果Bean,不能设置errcode");
            }
            super.setErrno(errno);
        }

        @Override
        public void setData(Object data) {
            throw new IllegalArgumentException("通用结果Bean,不能设置data");
        }

        @Override
        public void setErrmsg(String msg) {
            if (StringUtils.isNotBlank(this.getErrmsg())) {
                throw new IllegalArgumentException("通用结果Bean,不能设置msg");
            }
            super.setErrmsg(msg);
        }
    }
}
