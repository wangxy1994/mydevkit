/********************************************
 * 文件名称: VaRResultCdf.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 正态法var计算结果包装类
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20190314 daijy    调整IDataset数组字段格式为实际格式
 *********************************************/
package com.wangxy.exoskeleton.risk.var;

import java.util.Arrays;

import com.wangxy.exoskeleton.risk.matrix.Matrix;
import com.wangxy.exoskeleton.risk.util.BigDecimalUtil;


public class VaRResultCdf extends VaRResult{
	
	/** 收益率协方差矩阵 */
	private double[][] covMatrix;
	/** 相关系数矩阵 */
	private double[][] relMatrix;
	/** 权重数组 */
	private double[] weight;
	/** 组合波动率 */
	private double volatility;
	/** 组合平均收益率（预期收益率） */
	private double avgDailyRate;
	
	public double[][] getCovMatrix() {
		return covMatrix;
	}
	public void setCovMatrix(double[][] covMatrix) {
		this.covMatrix = covMatrix;
	}
	public double[][] getRelMatrix() {
		return relMatrix;
	}
	public void setRelMatrix(double[][] relMatrix) {
		this.relMatrix = relMatrix;
	}
	public double[] getWeight() {
		return weight;
	}
	public void setWeight(double[] weight) {
		this.weight = weight;
	}
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
	public String getVarStr() {
		String str = super.getVarStr()+", volatility:"+volatility+", avgDailyRate:"+avgDailyRate + "\n";
		return str + "covMatrix:" + Matrix.toString(covMatrix) + "relMatrix:" + Matrix.toString(relMatrix)
		       + "weight:" + Arrays.toString(weight);
	}
/*
	@Override
	public IDataset[] packgeToDataset() {
		IDataset[] a=super.packgeToDataset();
		IDataset ds=a[0];
		IDataset ds2=DatasetService.getDefaultInstance().getDataset();
		ds2.addColumn("colum",DatasetColumnType.DS_STRING);
		for (int j = 0; j <relMatrix[0].length;j++) {
			ds2.addColumn("colum"+j,DatasetColumnType.DS_DOUBLE);
		}
		int i=0;
		ds.beforeFirst();
		while(ds.hasNext()){
			ds.next();
			ds2.appendRow();
			ds2.updateString("colum", ds.getString("title"));
			for (int j = 0; j <relMatrix[i].length;j++) {
				ds2.updateDouble("colum"+j, BigDecimalUtil.round(relMatrix[i][j],8));
			}
			i++;
		}
		IDataset b[]={ds,ds2}; 
		return b;
	}
	*/
}
