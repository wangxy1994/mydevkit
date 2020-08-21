/********************************************
 * 文件名称: VaRResultDtlCdf.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 正态法var计算结果明细包装类
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *********************************************/
package com.wangxy.exoskeleton.risk.var;

import com.wangxy.exoskeleton.risk.util.BigDecimalUtil;

public class VaRResultDtlCdf extends VaRResultDtl{
	
	/** 波动率 */
	private double volatility;
	/** 平均收益率（预期收益率） */
	private double avgDailyRate;
	
	public double getVolatility() {
		return volatility;
	}
	public void setVolatility(double volatility) {
		this.volatility = volatility;
	}
	public double getAvgDailyRate() {
		return avgDailyRate;
	}
	public void setAvgDailyRate(double avgDailyRate) {
		this.avgDailyRate = avgDailyRate;
	}
	
	@Override
	public String toString() {
		return super.toString()+", volatility:"+volatility+", avgDailyRate:"+avgDailyRate+"\n";
	}
/*
	@Override
	public void addColumn(IDataset ds){
		super.addColumn(ds);
		ds.addColumn("volatility",DatasetColumnType.DS_DOUBLE);
		ds.addColumn("avgDailyRate",DatasetColumnType.DS_DOUBLE);
	}
	
	@Override
	public IDataset packgeToDataset(IDataset ds) {
		ds=super.packgeToDataset(ds);
		ds.updateDouble("volatility",BigDecimalUtil.round(volatility, 8));
		ds.updateDouble("avgDailyRate", BigDecimalUtil.round(avgDailyRate, 8));
		return ds;
	}
	*/
}
