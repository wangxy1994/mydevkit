/********************************************
 * 文件名称: VaRResultDtl.java
 * 系统名称: 外汇资金风险管理
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: var计算结果明细包装类
 * 系统版本: 2.0.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *          20190314  daijy    增加头寸金额；调整IDataset字段格式为实际格式
 *********************************************/
package com.wangxy.exoskeleton.risk.var;

import com.wangxy.exoskeleton.risk.util.BigDecimalUtil;

public class VaRResultDtl {
	
	/** 明细标题 */
	private String title;
	/** 头寸金额 */
	private double positionAmt = 0.0;
	/** VaR金额 */
	private double vaR = 0.0;
	/** 边际VaR金额 */
	private double mVaR = 0.0;
	/** 成分VaR金额 */
	private double cVaR = 0.0;
	/** 成分VaR贡献 */
	private double cVaRContri = 0.0;
	/** 增量VaR金额 */
	private double incrVaR = 0.0;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getPositionAmt() {
		return positionAmt;
	}
	public void setPositionAmt(double positionAmt) {
		this.positionAmt = positionAmt;
	}
	public double getVaR() {
		return vaR;
	}
	public void setVaR(double vaR) {
		this.vaR = vaR;
	}
	public double getmVaR() {
		return mVaR;
	}
	public void setmVaR(double mVaR) {
		this.mVaR = mVaR;
	}
	public double getcVaR() {
		return cVaR;
	}
	public void setcVaR(double cVaR) {
		this.cVaR = cVaR;
	}
	public double getcVaRContri() {
		return cVaRContri;
	}
	public void setcVaRContri(double cVaRContri) {
		this.cVaRContri = cVaRContri;
	}
	public double getIncrVaR() {
		return incrVaR;
	}
	public void setIncrVaR(double incrVaR) {
		this.incrVaR = incrVaR;
	}
	
	@Override
	public String toString() {
		return "title:"+title+", positionAmt:"+positionAmt+"vaR:"+vaR+", mVaR:"+mVaR+", cVaR:"+cVaR+", cVaRContri:"+cVaRContri+", incrVaR:"+incrVaR;
	}
	
	/**
	 * 公共的打包前为IDataset增加列字段的方法
	 */
	/*
	public void addColumn(IDataset ds){
		ds.addColumn("title",DatasetColumnType.DS_STRING);
		//20190314 daijy 增加头寸金额
		ds.addColumn("positionAmt",DatasetColumnType.DS_DOUBLE);
		ds.addColumn("vaR",DatasetColumnType.DS_DOUBLE);
		ds.addColumn("mVaR",DatasetColumnType.DS_DOUBLE);
		ds.addColumn("cVaR",DatasetColumnType.DS_DOUBLE);
		ds.addColumn("cVaRContri",DatasetColumnType.DS_DOUBLE);
		ds.addColumn("incrVaR",DatasetColumnType.DS_DOUBLE);
	}
	*/
	
	/**
	 * 公共的打包为IDataset以供界面显示的方法
	 * @return
	 */
	/*
	public IDataset packgeToDataset(IDataset ds) {
		ds.updateString("title", title);
		//20190314 daijy 增加头寸金额
		ds.updateDouble("positionAmt",positionAmt);
		ds.updateDouble("vaR", Math.abs(vaR));
		ds.updateDouble("mVaR", mVaR);
		ds.updateDouble("cVaR", cVaR);
		ds.updateDouble("cVaRContri", BigDecimalUtil.round(cVaRContri,8));
		ds.updateDouble("incrVaR", incrVaR);
		return ds;
	}
	*/
}
