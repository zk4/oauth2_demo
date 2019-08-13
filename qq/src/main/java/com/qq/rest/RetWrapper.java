package com.qq.rest;

public class RetWrapper {
    Integer code;
    Object data;
    String msg;

    public RetWrapper() {
    }

    public RetWrapper(Integer code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }


    public static RetWrapper ok(Object data){
        return new RetWrapper(0,data,null);
    }
    public static RetWrapper error(String msg,Object data){
        return new RetWrapper(-1,data,msg);
    }
    public static RetWrapper custom(Integer code ,String msg,Object data){
        return new RetWrapper(code,data,msg);
    }
    public Integer getCode() {
        return code;
    }

    public RetWrapper setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RetWrapper setData(Object data) {
        this.data = data;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RetWrapper setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
