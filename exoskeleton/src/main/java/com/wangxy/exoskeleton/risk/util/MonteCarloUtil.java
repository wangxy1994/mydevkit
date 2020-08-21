/********************************************
 * 文件名称: MonteCarloVaR.java
 * 系统名称: 外汇资金风险管理
 * 模块名称: 蒙特卡罗模拟法
 * 软件版权:  
 * 功能说明: 蒙特卡罗模拟法计算
 * 系统版本: 2.0.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *          20190422  daijy    整体调整，支持获取单次模拟结果
 *          20191224  qibb    修改模拟价格生成方式,根域广义维纳过程
 *********************************************/
package com.wangxy.exoskeleton.risk.util;

import java.util.List;

import com.wangxy.exoskeleton.risk.Cdf;
import com.wangxy.exoskeleton.risk.matrix.Covariance;
import com.wangxy.exoskeleton.risk.var.DailyRateUtil;
import com.wangxy.exoskeleton.risk.var.VaRParam;

public class MonteCarloUtil {

	/**
	 * 单次模拟获取估算值
	 * @param value 原始值
	 * @param expected 期望值
	 * @param volatility 波动率
	 * @return
	 */
	public static double getEstimatedValue(double value, double expected, double volatility) {
		double stochastic = Cdf.icudf(Math.random());
		double estimatedValue = expected + volatility * stochastic * expected;
		return estimatedValue;
	}

	/**
	 * 单次模拟获取估算值
	 * @param value 原始值
	 * @param expected 期望值
	 * @param volatility 波动率
	 * @expectedRate 预期收益率
	 * @return
	 * @author 20191224  qibb    修改模拟价格生成方式
	 */
//	public static double getEstimatedValue(double value, double expected, double volatility, double expectedRate) {
//		double stochastic = Cdf.icudf(Math.random());
//		double estimatedValue = expected + (volatility * stochastic * expectedRate * expected);
//		return estimatedValue;
//	}

	/**
	 * 对单个交易工具的价格进行模拟,不考虑工具间的相关性
	 * @param value  当前价格
	 * @param expected  平均收益率
	 * @param days    持有天数
	 * @param volatility   收益率波动
	 * @param expectedRate  预期收益率
	 * @return
	 */
	public static double getEstimatedValue(double value, double days, double volatility, double expectedRate) {
		double price = value;
		double stochastic = Cdf.icudf(Math.random());
		price += value * (expectedRate * days + volatility * stochastic * Math.sqrt(days));   //20191224  qibb    修改模拟价格生成方式,根域广义维纳过程
		return price;
	}
	
	
	/**
	 * 对单个交易工具的价格进行模拟,不考虑工具间的相关性
	 * @param value  当前价格
	 * @param expected  平均收益率
	 * @param days    持有天数
	 * @param volatility   收益率波动
	 * @param expectedRate  预期收益率
	 * @param relMatrix 相关系数矩阵
	 * @return
	 */
	public static double[] getEstimatedValue(double value, double days, double volatility, double expectedRate,double[][] relMatrix) {
		double[] price = new double[relMatrix[0].length] ;
		for(int i=0 ;i<relMatrix[0].length;i++){
			double stochastic = Cdf.icudf(Math.random());
			price[i]= value +value * (expectedRate * days + volatility * stochastic * Math.sqrt(days));   //20191224  qibb    修改模拟价格生成方式,根域广义维纳过程
		}
	
		
		return price;
	}

	/**
	 * 获取估算价值
	 * 对LnValue进行抽样
	 * @param value 原始价值
	 * @param expectedRate  期望收益率
	 * @param volatility   利率波动率
	 * @param t 期限
	 * @return
	 */
	public static double getEstimatedValueLn(double value, double expectedRate, double volatility, double t) {
		double stochastic = Cdf.icudf(Math.random());
		double estimatedValue = value * Math.exp((expectedRate - volatility * volatility / 2) * t + volatility * stochastic * Math.sqrt(t));
		return estimatedValue;
	}
	
	
	/**
	 * 计算组合日收益率的相关系数矩阵
	 * @param paramlist  var计算参数
	 * @return relMatrix
	 */
	public static double[][] getRelMatrix(List<VaRParam> paramlist) throws Exception{
		//先计算协方差矩阵，后转换为相关系数矩阵
		int size = paramlist.size();
		double[][] covMatrix = new double [size][size];
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				covMatrix[i][j] = Covariance.getCovariance(DailyRateUtil.getDaily(paramlist.get(i).getHisPriceArray()),DailyRateUtil.getDaily(paramlist.get(j).getHisPriceArray()));
			}
		}
		double[][] relMatrix = Covariance.getRelMatrix(covMatrix);
		return relMatrix;
	}

}
