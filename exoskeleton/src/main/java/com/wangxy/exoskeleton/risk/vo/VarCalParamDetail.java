package com.wangxy.exoskeleton.risk.vo;

public class VarCalParamDetail {
	/** 资产唯一id */
	private String assetId;
	/** 数量 */
	private double cnt;
	/** 目标币种 */
	private String targetCurr;
	/** 原始数量 */
	private double srcCnt;
	
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public double getCnt() {
		return cnt;
	}
	public void setCnt(double cnt) {
		this.cnt = cnt;
	}
	public String getTargetCurr() {
		return targetCurr;
	}
	public void setTargetCurr(String targetCurr) {
		this.targetCurr = targetCurr;
	}
	public double getSrcCnt() {
		return srcCnt;
	}
	public void setSrcCnt(double srcCnt) {
		this.srcCnt = srcCnt;
	}
	@Override
	public String toString() {
		return "VarCalParamDetail [assetId=" + assetId + ", cnt=" + cnt + ", targetCurr=" + targetCurr + ", srcCnt=" + srcCnt + "]";
	}
	
	

}
