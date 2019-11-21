/********************************************
* 文件名称: CurrencyPair.java
* 系统名称: 资金交易风险管理系统
* 模块名称:tbcurrencypair货币对表
* 软件版权:  
* 功能说明: 
* 系统版本: 2.5.0.1
* 开发人员: 资金管理项目组
* 开发时间:  
* 审核人员:
* 相关文档:
* 修改记录: 修改日期    修改人员    修改说明
*********************************************/
package com.wangxy.devkit.cache;

import java.lang.*;

public class CurrencyPair{
 
    public static final String VERSION="@system 资金交易风险管理系统V2.0  @version 2.5.0.1  @lastModiDate  2014-07-09  @describe ";
	//货币对
	private String currencyPair = " ";
	public String getCurrencyPair(){
		return currencyPair;
	}
	public void setCurrencyPair(String currencyPair){
		this.currencyPair = currencyPair;
	}

	//中文名
	private String currencyPairName = " ";
	public String getCurrencyPairName(){
		return currencyPairName;
	}
	public void setCurrencyPairName(String currencyPairName){
		this.currencyPairName = currencyPairName;
	}

	//货币对数字编码
	private String currencyPairNo = " ";
	public String getCurrencyPairNo(){
		return currencyPairNo;
	}
	public void setCurrencyPairNo(String currencyPairNo){
		this.currencyPairNo = currencyPairNo;
	}

	//报价精度
	private int decimalPoint = 0;
	public int getDecimalPoint(){
		return decimalPoint;
	}
	public void setDecimalPoint(int decimalPoint){
		this.decimalPoint = decimalPoint;
	}

	//标价方式
	private String pricedWay = " ";
	public String getPricedWay(){
		return pricedWay;
	}
	public void setPricedWay(String pricedWay){
		this.pricedWay = pricedWay;
	}

	//报价单位
	private int quotedUnit = 0;
	public int getQuotedUnit(){
		return quotedUnit;
	}
	public void setQuotedUnit(int quotedUnit){
		this.quotedUnit = quotedUnit;
	}

	//汇率点差折算
	private int spreadObversion = 0;
	public int getSpreadObversion(){
		return spreadObversion;
	}
	public void setSpreadObversion(int spreadObversion){
		this.spreadObversion = spreadObversion;
	}

	//状态
	private String status = " ";
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status = status;
	}

	//报价源
	private String source = " ";
	public String getSource(){
		return source;
	}
	public void setSource(String source){
		this.source = source;
	}

	//最小交易金额
	private double minimumAmount = 0.0;
	public double getMinimumAmount(){
		return minimumAmount;
	}
	public void setMinimumAmount(double minimumAmount){
		this.minimumAmount = minimumAmount;
	}

	//最小交易金额币种
	private String minimumAmountCurrency = " ";
	public String getMinimumAmountCurrency(){
		return minimumAmountCurrency;
	}
	public void setMinimumAmountCurrency(String minimumAmountCurrency){
		this.minimumAmountCurrency = minimumAmountCurrency;
	}

	//流动性限额
	private double thresholdAmount = 0.0;
	public double getThresholdAmount(){
		return thresholdAmount;
	}
	public void setThresholdAmount(double thresholdAmount){
		this.thresholdAmount = thresholdAmount;
	}

	//流动性限额币种
	private String thresholdAmountCurrency = " ";
	public String getThresholdAmountCurrency(){
		return thresholdAmountCurrency;
	}
	public void setThresholdAmountCurrency(String thresholdAmountCurrency){
		this.thresholdAmountCurrency = thresholdAmountCurrency;
	}

	//描述
	private String description = " ";
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
	}

	//备用1
	private String reserve = " ";
	public String getReserve(){
		return reserve;
	}
	public void setReserve(String reserve){
		this.reserve = reserve;
	}

	//备用2
	private String reserve2 = " ";
	public String getReserve2(){
		return reserve2;
	}
	public void setReserve2(String reserve2){
		this.reserve2 = reserve2;
	}

	//备用3
	private String reserve3 = " ";
	public String getReserve3(){
		return reserve3;
	}
	public void setReserve3(String reserve3){
		this.reserve3 = reserve3;
	}

	//最后更新日期
	private int lastDate = 0;
	public int getLastDate(){
		return lastDate;
	}
	public void setLastDate(int lastDate){
		this.lastDate = lastDate;
	}

	//最后更新时间
	private int lastTime = 0;
	public int getLastTime(){
		return lastTime;
	}
	public void setLastTime(int lastTime){
		this.lastTime = lastTime;
	}


}
