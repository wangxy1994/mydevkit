package com.wangxy.exoskeleton.risk.matrix;

import com.wangxy.exoskeleton.risk.util.BigDecimalUtil;

/********************************************
 * 文件名称: Covariance.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 协方差计算
 * 系统版本: 2.5.0.1
 * 开发人员: 
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *********************************************/
public class Covariance {

	/**
	 * 计算两个数列的协方差
	 * @param serial1
	 * @param serial2
	 * @return
	 */
	public static double getCovariance(double[] serial1, double[] serial2) throws Exception {
		if (serial1.length != serial2.length) {
			throw new Exception("两个数列的元素个数不相等，无法计算协方差");
		}
		double mean1, mean2;
		double covariance = 0;
		double sum1 = 0;
		double sum2 = 0;
		for (int i = 0; i < serial1.length; i++) {
			sum1 += serial1[i];
			sum2 += serial2[i];
		}
		//计算平均值
		mean1 = sum1 / serial1.length;
		mean2 = sum2 / serial2.length;

		for (int i = 0; i < serial1.length; i++) {
			covariance += (serial1[i] - mean1) * (serial2[i] - mean2);
		}
		// 分母需要样本数-1,样本数少时返回0
		if(serial1.length>1){
			covariance = BigDecimalUtil.div(covariance, serial1.length - 1);
		}else{
			covariance=0;
		}
		return covariance;
	}
	
	/**
	 * 根据协方差矩阵计算相关系数矩阵
	 * @param covarianceMatrix
	 * @author daijy
	 * @since 20181020
	 * @return
	 */
	public static double[][] getRelMatrix(double[][] covarianceMatrix){
		double[][] relMatrix = new double[covarianceMatrix.length][covarianceMatrix[0].length];
		for(int i=0;i<relMatrix.length;i++){
			for(int j=0;j<relMatrix[0].length;j++){
				relMatrix[i][j] = covarianceMatrix[i][j]/Math.sqrt(covarianceMatrix[i][i]*covarianceMatrix[j][j]);
			}
		}
		return relMatrix;
	}
	
}
