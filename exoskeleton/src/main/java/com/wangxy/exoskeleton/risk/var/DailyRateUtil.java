/********************************************
 * 文件名称: DailyRateUtil.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 每日收益率计算工具类
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *********************************************/
package com.wangxy.exoskeleton.risk.var;

import java.util.List;

public class DailyRateUtil {
	
	/**
	 * 由价格计算日收益
	 * @param price1
	 * @param price2
	 * @return
	 */
	public static double getDailyRate(double price1, double price2){
		return Math.log(price2 / price1);
	}
	
	/**
	 * 由每日价格序列，获得日收益率序列
	 * @param priceArray
	 * @return
	 */
	public static double[] getDaily(double[] priceArray){
		double [] dailyrate = new double [priceArray.length - 1];
		for(int i = 0; i < priceArray.length - 1; i++){
			dailyrate[i] = getDailyRate(priceArray[i], priceArray[i+1]);
		}
		return dailyrate;
	}
	
	/**
	 * 计算收益率序列的平均值
	 * @param rateArray
	 * @return
	 */
	public static double getAverage(double[] rateArray){
		int drlength = rateArray.length;
		double sum = 0;
		for(int i = 0; i < drlength; i++){
			sum += rateArray[i];
		}
		double average = sum / drlength;
		return average;
	}
	
	/**
	 * 计算组合的每日收益率
	 * @param paramlist
	 * @return
	 */
	public static double[] getDailyRate(List<VaRParam> paramlist){		
		//每日收益率数量为价格样本数-1
		double expectedRate[] = new double [paramlist.get(0).getPriceCnt()-1];
		double[] weight = PortfolioCalUtil.getWeight(paramlist);
		for(int i = 0; i < expectedRate.length; i++){
			for(int j = 0; j < paramlist.size(); j++){
				double[] priceArray = paramlist.get(j).getHisPriceArray();
				expectedRate[i] += weight[j] *  getDailyRate(priceArray[i],priceArray[i+1]);
			}
		}		
		return expectedRate;
	}
	
	/**
	 * 计算组合的每日平均收益率
	 * @param paramlist
	 * @return
	 */
	public static double getAverage(List<VaRParam> paramlist){
		double[] dailyrate = getDailyRate(paramlist);
		return getAverage(dailyrate);
	}
	
	/**
	 * 计算组合的最近日期每日收益率
	 * @param paramlist
	 * @return
	 */
	public static double getLastDailyRate(List<VaRParam> paramlist){		
		//每日收益率数量为价格样本数-1
		double expectedRate = 0;
		double[] weight = PortfolioCalUtil.getWeight(paramlist);
		for(int i = 0; i < paramlist.size(); i++){
			double[] priceArray = paramlist.get(i).getHisPriceArray();
			expectedRate += weight[i] *  getDailyRate(priceArray[priceArray.length-2],priceArray[priceArray.length-1]);
		}
		return expectedRate;
	}

}
