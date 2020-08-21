/********************************************
 * 文件名称: VaRResult.java
 * 系统名称: 外汇资金风险管理
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: var计算结果包装类
 * 系统版本: 2.0.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20190314 daijy    调整IDataset数组字段格式为实际格式
 *********************************************/
package com.wangxy.exoskeleton.risk.var;

import java.util.List;

import com.wangxy.exoskeleton.risk.util.BigDecimalUtil;

public class VaRResult {
	
	/** 组合var值 */
	private double var;
	/** 组合var金额 */
	private double varAmt;
	/** 个体var汇总 */
	private double sumIndividualAmt;
	/** 分散效应百分比 */
	private double diversification;
	/** 历史数据开始时间*/
	private int beginDate;
	/** 历史数据结束时间*/
	private int endDate;
	/** 组合中的个体明细var数据 */
	private List<? extends VaRResultDtl> varDetails;
	
	public double getVar() {
		return var;
	}
	public void setVar(double var) {
		this.var = var;
	}
	public double getVarAmt() {
		return varAmt;
	}
	public void setVarAmt(double varAmt) {
		this.varAmt = varAmt;
	}
	public double getSumIndividualAmt() {
		return sumIndividualAmt;
	}
	public void setSumIndividualAmt(double sumIndividualAmt) {
		this.sumIndividualAmt = sumIndividualAmt;
	}
	public double getDiversification() {
		return diversification;
	}
	public void setDiversification(double diversification) {
		this.diversification = diversification;
	}
	public List<? extends VaRResultDtl> getVarDetails() {
		return varDetails;
	}
	public void setVarDetails(List<? extends VaRResultDtl> varDetails) {
		this.varDetails = varDetails;
	}
	public void setBeginDate(int beginDate) {
		this.beginDate = beginDate;
	}
	public int getBeginDate() {
		return beginDate;
	}
	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}
	public int getEndDate() {
		return endDate;
	}
	
	/**
	 * 获取var计算结果显示（不含明细）
	 * @return
	 */
	public String getVarStr(){
		String str = "var:"+var+", varAmt:"+varAmt+", sumIndividualAmt:"+sumIndividualAmt+", diversification:"+diversification;
		return str;
	}
	
	@Override
	public String toString() {
		String str = getVarStr() + "\n" + varDetails.toString();
		return str;
	}
	
	/**
	 * 公共的打包为IDataset数组以供界面显示的方法
	 * @return
	 */
	/*
	public IDataset[]  packgeToDataset() {
		IDataset ds=DatasetService.getDefaultInstance().getDataset();
		ds.addColumn("var",DatasetColumnType.DS_DOUBLE);
		ds.addColumn("varAmt",DatasetColumnType.DS_DOUBLE);
		ds.addColumn("sumIndividualAmt",DatasetColumnType.DS_DOUBLE);
		ds.addColumn("diversification",DatasetColumnType.DS_DOUBLE);
		ds.addColumn("beginDate",DatasetColumnType.DS_INT);
		ds.addColumn("endDate",DatasetColumnType.DS_INT);
		if(varDetails.get(0)!=null){
			varDetails.get(0).addColumn(ds);
		}
		for (VaRResultDtl vrs : varDetails) {
			ds.appendRow();
			ds.updateDouble("var",var);
			ds.updateDouble("varAmt", Math.abs(varAmt));
			ds.updateDouble("sumIndividualAmt", sumIndividualAmt);
			ds.updateDouble("diversification", BigDecimalUtil.round(diversification,8));
			ds.updateInt("beginDate", beginDate);
			ds.updateInt("endDate", endDate);
			ds=vrs.packgeToDataset(ds);
		}
		IDataset a[]={ds};
		return a;
	}
	*/
}
