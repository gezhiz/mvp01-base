package com.mvp01.common.exception;


import com.mvp01.common.bean.ResultBean;

/**
 * Created by wenjie on 16/1/6.
 */
public class ErrcodeException extends CustomException {
    private ResultBean obj = new ResultBean();

    public ErrcodeException(String message) {
        this(ResultBean.COMMON_ERROR_CODE, message);
    }

    public ErrcodeException(int errcode, String message) {
        super(message);
        obj.setErrno(errcode);
        obj.setErrmsg(message);
    }

    public ErrcodeException(String message, Throwable cause) {
        super(message, cause);
        obj.setErrno(ResultBean.COMMON_ERROR_CODE);
        obj.setErrmsg(message);
    }

    public ErrcodeException(int errcode,String message, Throwable cause) {
        this(message,cause);
        obj.setErrno(errcode);
    }

    public int getErrcode() {
        return obj.getErrno();
    }

    public void setErrcode(int errcode) {
        obj.setErrno(errcode);
    }

    public Object getData() {
        return obj.getData();
    }

    public void setData(Object data) {
        this.obj.setData(data);
    }

    public ResultBean getObj() {
        return obj;
    }
}
