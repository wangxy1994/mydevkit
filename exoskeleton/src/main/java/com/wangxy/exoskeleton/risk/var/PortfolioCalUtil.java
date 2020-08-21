/********************************************
 * 文件名称: PortfolioCalUtil.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 组合计算工具类
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20190111 daijy    计算组合权重时按照绝对值进行计算
 *********************************************/
package com.wangxy.exoskeleton.risk.var;

import java.util.List;

public class PortfolioCalUtil {
	
	/**
	 * 计算组合中，各资产的权重
	 * @param paramlist  var计算参数
	 * @return
	 */
	public static double[] getWeight(List<VaRParam> paramlist){
		int size = paramlist.size();
		double [] weight = new double [size];
		double total = 0.00;
		for(int i = 0; i < size; i++){
			if(paramlist.get(i)!=null){
				//20190111 daijy 计算组合权重时数量按照绝对值进行计算
				weight[i] = Math.abs(paramlist.get(i).getAmount()) * paramlist.get(i).getLastPrice();
				total += weight[i];
			}else{
				weight[i] = 0.0;
			}
		}
		for(int i = 0; i < paramlist.size(); i++){
			weight[i] = weight[i] / total;
		}
		return weight;
	}
	
	/**
	 * 获取组合中各资产的权重矩阵
	 * @param paramlist  var计算参数
	 * @return
	 */
	public static double[][] getWeightMatrix(List<VaRParam> paramlist){
		double [] weight = getWeight(paramlist);
		return getWeightMatrix(weight);
	}
	
	/**
	 * 获取组合中各资产的权重矩阵
	 * @param weight  权重数组
	 * @return
	 */
	public static double[][] getWeightMatrix(double[] weight){
		double weightMatrix[][] = new double [weight.length][1];
		for(int i = 0; i < weight.length; i ++){
			weightMatrix[i][0] = weight[i];
		}
		return weightMatrix;
	}
	
	/**
	 * 计算组合总价值
	 * @param paramlist  var计算参数
	 * @return
	 */
	public static double getTotalValue(List<VaRParam> paramlist){
		//计算组合总价值，即所有组合数量*最近价格的总和
		double total = 0.00;
		for(int i = 0; i < paramlist.size(); i++){
			total += paramlist.get(i).getAmount()*paramlist.get(i).getLastPrice();
		}
		return total;
	}

}
