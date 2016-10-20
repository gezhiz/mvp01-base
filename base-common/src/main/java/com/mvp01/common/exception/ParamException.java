package com.mvp01.common.exception;

/**
 * Created by wenjie on 16/1/7.
 */
public class ParamException extends CustomException{
    public ParamException() {
        this("参数错误");
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(Throwable cause) {
        super(cause);
    }

    public ParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
