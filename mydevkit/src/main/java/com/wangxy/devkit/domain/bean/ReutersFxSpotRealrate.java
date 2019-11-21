/********************************************
* 文件名称: ReutersFxSpotRealrate.java
* 系统名称: 现代金融综合仿真平台
* 模块名称: reuters_fx_spot_realrate路透外汇实时报价表即期
* 软件版权: 
* 功能说明: 
* 系统版本: 
* 开发人员: 现代金融综合仿真平台
* 开发时间:  
* 审核人员:
* 相关文档:
* 修改记录: 修改日期    修改人员    修改说明
*********************************************/
package com.wangxy.devkit.domain.bean;

import java.lang.*;

public class ReutersFxSpotRealrate{
	//货币对
	private String cypairname = " ";
	public String getCypairname(){
		return cypairname;
	}
	public void setCypairname(String cypairname){
		this.cypairname = cypairname;
	}

	//牌价日期时间
	private long updatedatetime = 0;
	public long getUpdatedatetime(){
		return updatedatetime;
	}
	public void setUpdatedatetime(long updatedatetime){
		this.updatedatetime = updatedatetime;
	}

	//数量
	private String countnum = " ";
	public String getCountnum(){
		return countnum;
	}
	public void setCountnum(String countnum){
		this.countnum = countnum;
	}

	//牌价类型
	private String quotetype = " ";
	public String getQuotetype(){
		return quotetype;
	}
	public void setQuotetype(String quotetype){
		this.quotetype = quotetype;
	}

	//产品名称
	private String productname = " ";
	public String getProductname(){
		return productname;
	}
	public void setProductname(String productname){
		this.productname = productname;
	}

	//基点位数
	private int basepoint = 0;
	public int getBasepoint(){
		return basepoint;
	}
	public void setBasepoint(int basepoint){
		this.basepoint = basepoint;
	}

	//是否可交易标识
	private String trademark = " ";
	public String getTrademark(){
		return trademark;
	}
	public void setTrademark(String trademark){
		this.trademark = trademark;
	}

	//报价有效时间
	private String quotettl = " ";
	public String getQuotettl(){
		return quotettl;
	}
	public void setQuotettl(String quotettl){
		this.quotettl = quotettl;
	}

	//升贴水买价（核心管理价格数据）
	private double bid = 0.0;
	public double getBid(){
		return bid;
	}
	public void setBid(double bid){
		this.bid = bid;
	}

	//升贴水卖价（核心管理价格数据）
	private double ask = 0.0;
	public double getAsk(){
		return ask;
	}
	public void setAsk(double ask){
		this.ask = ask;
	}

	//升贴水中间价（核心管理价格数据）
	private double mid = 0.0;
	public double getMid(){
		return mid;
	}
	public void setMid(double mid){
		this.mid = mid;
	}

	//升贴水买价(源价格数据)
	private double sourcebid = 0.0;
	public double getSourcebid(){
		return sourcebid;
	}
	public void setSourcebid(double sourcebid){
		this.sourcebid = sourcebid;
	}

	//升贴水卖价(源价格数据)
	private double sourceask = 0.0;
	public double getSourceask(){
		return sourceask;
	}
	public void setSourceask(double sourceask){
		this.sourceask = sourceask;
	}

	//升贴水中间价(源价格数据)
	private double sourcemid = 0.0;
	public double getSourcemid(){
		return sourcemid;
	}
	public void setSourcemid(double sourcemid){
		this.sourcemid = sourcemid;
	}

	//价格开停价状态
	private String runstate = " ";
	public String getRunstate(){
		return runstate;
	}
	public void setRunstate(String runstate){
		this.runstate = runstate;
	}

	//起息日
	private int valuedate = 0;
	public int getValuedate(){
		return valuedate;
	}
	public void setValuedate(int valuedate){
		this.valuedate = valuedate;
	}

	//价格类型
	private String pricetype = " ";
	public String getPricetype(){
		return pricetype;
	}
	public void setPricetype(String pricetype){
		this.pricetype = pricetype;
	}

	//价源代码
	private String feedcode = " ";
	public String getFeedcode(){
		return feedcode;
	}
	public void setFeedcode(String feedcode){
		this.feedcode = feedcode;
	}

	//原始数据实际外部流水号
	private String datasourceid = " ";
	public String getDatasourceid(){
		return datasourceid;
	}
	public void setDatasourceid(String datasourceid){
		this.datasourceid = datasourceid;
	}

	//牌价批次号
	private String priceid = " ";
	public String getPriceid(){
		return priceid;
	}
	public void setPriceid(String priceid){
		this.priceid = priceid;
	}

	//小数有效位数
	private int ticksize = 0;
	public int getTicksize(){
		return ticksize;
	}
	public void setTicksize(int ticksize){
		this.ticksize = ticksize;
	}

	//报价单位
	private int quoteunit = 0;
	public int getQuoteunit(){
		return quoteunit;
	}
	public void setQuoteunit(int quoteunit){
		this.quoteunit = quoteunit;
	}

	//成本买入价
	private double costbid = 0.0;
	public double getCostbid(){
		return costbid;
	}
	public void setCostbid(double costbid){
		this.costbid = costbid;
	}

	//成本卖出价
	private double costask = 0.0;
	public double getCostask(){
		return costask;
	}
	public void setCostask(double costask){
		this.costask = costask;
	}

	//成本中间价
	private double costmid = 0.0;
	public double getCostmid(){
		return costmid;
	}
	public void setCostmid(double costmid){
		this.costmid = costmid;
	}

	//现汇买入价
	private double exchbid = 0.0;
	public double getExchbid(){
		return exchbid;
	}
	public void setExchbid(double exchbid){
		this.exchbid = exchbid;
	}

	//现汇卖出价
	private double exchask = 0.0;
	public double getExchask(){
		return exchask;
	}
	public void setExchask(double exchask){
		this.exchask = exchask;
	}

	//现汇中间价
	private double exchmid = 0.0;
	public double getExchmid(){
		return exchmid;
	}
	public void setExchmid(double exchmid){
		this.exchmid = exchmid;
	}

	//现钞买入价
	private double cashbid = 0.0;
	public double getCashbid(){
		return cashbid;
	}
	public void setCashbid(double cashbid){
		this.cashbid = cashbid;
	}

	//现钞卖出价
	private double cashask = 0.0;
	public double getCashask(){
		return cashask;
	}
	public void setCashask(double cashask){
		this.cashask = cashask;
	}

	//现钞卖中间价
	private double cashmid = 0.0;
	public double getCashmid(){
		return cashmid;
	}
	public void setCashmid(double cashmid){
		this.cashmid = cashmid;
	}

	//导入日期
	private int impdate = 0;
	public int getImpdate(){
		return impdate;
	}
	public void setImpdate(int impdate){
		this.impdate = impdate;
	}

	//导入时间
	private int imptime = 0;
	public int getImptime(){
		return imptime;
	}
	public void setImptime(int imptime){
		this.imptime = imptime;
	}


}
