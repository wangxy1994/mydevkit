/********************************************
* 文件名称: BulkComPrice.java
* 系统名称: 资金风险管理系统
* 模块名称: tbbulkcomprice商品报价表
* 软件版权: 
* 功能说明: 
* 系统版本: 
* 开发人员: 资金风险管理系统
* 开发时间:  
* 审核人员:
* 相关文档:
* 修改记录: 修改日期    修改人员    修改说明
*********************************************/
package com.wangxy.exoskeleton.risk.vo;


public class BulkComPrice{
	//日期
	private int transDate = 0;
	public int getTransDate(){
		return transDate;
	}
	public void setTransDate(int transDate){
		this.transDate = transDate;
	}

	//大宗商品市场
	private String bulkComMarket = " ";
	public String getBulkComMarket(){
		return bulkComMarket;
	}
	public void setBulkComMarket(String bulkComMarket){
		this.bulkComMarket = bulkComMarket;
	}

	//商品品种编号
	private String bulkComType = " ";
	public String getBulkComType(){
		return bulkComType;
	}
	public void setBulkComType(String bulkComType){
		this.bulkComType = bulkComType;
	}

	//报价类型
	private String priceType = " ";
	public String getPriceType(){
		return priceType;
	}
	public void setPriceType(String priceType){
		this.priceType = priceType;
	}

	//期限
	private String priceLimit = " ";
	public String getPriceLimit(){
		return priceLimit;
	}
	public void setPriceLimit(String priceLimit){
		this.priceLimit = priceLimit;
	}

	//收盘价
	private double closingPrice = 0.0;
	public double getClosingPrice(){
		return closingPrice;
	}
	public void setClosingPrice(double closingPrice){
		this.closingPrice = closingPrice;
	}

	//优先级别
	private int priorityLevel = 0;
	public int getPriorityLevel(){
		return priorityLevel;
	}
	public void setPriorityLevel(int priorityLevel){
		this.priorityLevel = priorityLevel;
	}

	//备用字段1
	private String reserve1 = " ";
	public String getReserve1(){
		return reserve1;
	}
	public void setReserve1(String reserve1){
		this.reserve1 = reserve1;
	}

	//备用字段2
	private String reserve2 = " ";
	public String getReserve2(){
		return reserve2;
	}
	public void setReserve2(String reserve2){
		this.reserve2 = reserve2;
	}

	//备用字段3
	private String reserve3 = " ";
	public String getReserve3(){
		return reserve3;
	}
	public void setReserve3(String reserve3){
		this.reserve3 = reserve3;
	}


}
