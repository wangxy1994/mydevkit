/**
  * Copyright 2020 bejson.com 
  */
package com.wangxy.exoskeleton.api.tushare.vo;

/**
 * Auto-generated: 2020-08-17 9:6:9
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TushareApiResponseData {

    private String request_id;
    private int code;
    private String msg;
    private Data data;
    public void setRequest_id(String request_id) {
         this.request_id = request_id;
     }
     public String getRequest_id() {
         return request_id;
     }

    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

    public void setData(Data data) {
         this.data = data;
     }
     public Data getData() {
         return data;
     }

}