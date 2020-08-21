package com.wangxy.exoskeleton.risk.var;
/********************************************
 * 文件名称: VaRParam.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: var计算参数
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20190314 daijy    支持返回值四舍五入，增加目标币种
 *********************************************/
public class VaRParam {
	
	/**
	 * 参数标题
	 */
	private String title;
	/**
	 * 当前资产数量/币种金额
	 */
	private double amount;
	/**
	 * 历史价格数组
	 */
	private double[] hisPriceArray;
	/**
	 * 价格样本数
	 */
	private int priceCnt;
	/**
	 * 目标币种
	 */
	private String targetCurr;
	
	public VaRParam(double amount, double[] hisPriceArray) {
		setAmount(amount);
		setHisPriceArray(hisPriceArray);
	}

	/**
	 * 获取最近价格
	 * @return
	 */
	public double getLastPrice(){
		if(hisPriceArray!=null){
			return hisPriceArray[hisPriceArray.length-1];
		}
		return 0;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double[] getHisPriceArray() {
		return hisPriceArray;
	}
	public void setHisPriceArray(double[] hisPriceArray) {
		this.hisPriceArray = hisPriceArray;
		if(hisPriceArray!=null){
			//同时设置价格样本数量
			this.priceCnt = hisPriceArray.length;
		}
	}
	public int getPriceCnt() {
		return priceCnt;
	}

	public String getTargetCurr() {
		return targetCurr;
	}

	public void setTargetCurr(String targetCurr) {
		this.targetCurr = targetCurr;
	}
	
	
}
