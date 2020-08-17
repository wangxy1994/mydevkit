/**
  * Copyright 2020 bejson.com 
  */
package com.wangxy.exoskeleton.api.tushare.vo;
import java.util.List;

/**
 * Auto-generated: 2020-08-17 9:6:9
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data {

    private List<String> fields;
    private List<List<String>> items;
    private boolean has_more;
    public void setFields(List<String> fields) {
         this.fields = fields;
     }
     public List<String> getFields() {
         return fields;
     }

    public void setItems(List<List<String>> items) {
         this.items = items;
     }
     public List<List<String>> getItems() {
         return items;
     }

    public void setHas_more(boolean has_more) {
         this.has_more = has_more;
     }
     public boolean getHas_more() {
         return has_more;
     }

}