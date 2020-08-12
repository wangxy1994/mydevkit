/**
  * Copyright 2020 bejson.com 
  */
package com.wangxy.exoskeleton.vo;
import java.util.List;

/**
 * Auto-generated: 2020-08-12 9:19:33
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JslListResponseData {

    private int page;
    private List<Row> rows;
    public void setPage(int page) {
         this.page = page;
     }
     public int getPage() {
         return page;
     }

    public void setRows(List<Row> rows) {
         this.rows = rows;
     }
     public List<Row> getRows() {
         return rows;
     }

}