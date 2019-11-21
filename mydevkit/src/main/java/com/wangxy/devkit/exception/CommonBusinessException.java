package com.wangxy.devkit.exception;

import java.io.Serializable;

public class CommonBusinessException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;
    private String msg;
    private String code;
    
    public CommonBusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }
    
    public CommonBusinessException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }
    
    public CommonBusinessException(String code,String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
    
    public CommonBusinessException(String msg, String code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}