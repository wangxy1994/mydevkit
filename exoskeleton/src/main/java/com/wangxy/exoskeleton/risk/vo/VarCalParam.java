package com.wangxy.exoskeleton.risk.vo;

import java.util.List;


public class VarCalParam {
	
	/**
	 * 计算日期
	 */
	private int calDate;
	/**
	 * 样本数
	 */
	private int num;
	/**
	 * 置信度
	 */
	private double percentile;
	/**
	 * 模拟次数
	 */
	private int simulatedtimes;
	/**
	 * 持有天数
	 */
	private int days;
	
	/**
	 * 要计算的资产明细
	 */
	private List<VarCalParamDetail> varCalParamDetail;

	public int getCalDate() {
		return calDate;
	}

	public void setCalDate(int calDate) {
		this.calDate = calDate;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public double getPercentile() {
		return percentile;
	}

	public void setPercentile(double percentile) {
		this.percentile = percentile;
	}

	public int getSimulatedtimes() {
		return simulatedtimes;
	}

	public void setSimulatedtimes(int simulatedtimes) {
		this.simulatedtimes = simulatedtimes;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public List<VarCalParamDetail> getVarCalParamDetail() {
		return varCalParamDetail;
	}

	public void setVarCalParamDetail(List<VarCalParamDetail> varCalParamDetail) {
		this.varCalParamDetail = varCalParamDetail;
	}

	@Override
	public String toString() {
		return "VarCalParam [calDate=" + calDate + ", num=" + num + ", percentile=" + percentile + ", simulatedtimes=" + simulatedtimes + ", days=" + days + ", varCalParamDetail="
				+ varCalParamDetail + "]";
	}
	
	
	

}
