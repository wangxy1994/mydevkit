/********************************************
 * 文件名称: CurrencyVarParam.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 汇率var计算参数类
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *********************************************/
package com.wangxy.exoskeleton.risk.calvar;

public class CurrencyVarParam {

	/** 原币种 */
	private String sourceCurr;
	/** 目标币种 */
	private String targetCurr;
	/** 金额 */
	private double amt;
	/** 原始数量 */
	private double srcAmt;
	
	public double getSrcAmt() {
		return srcAmt;
	}
	public void setSrcAmt(double srcAmt) {
		this.srcAmt = srcAmt;
	}

	public String getSourceCurr() {
		return sourceCurr;
	}

	public void setSourceCurr(String sourceCurr) {
		this.sourceCurr = sourceCurr;
	}

	public String getTargetCurr() {
		return targetCurr;
	}

	public void setTargetCurr(String targetCurr) {
		this.targetCurr = targetCurr;
	}

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

}
