/********************************************
 * 文件名称: CdfVaR.java
 * 系统名称: 外汇资金风险管理
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 正态分布法组合var计算
 * 			 先计算收益率协方差矩阵，根据权重矩阵得到波动率，再计算置信度下的var值
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *          20190424 daijy    删除通用的风险贡献计算方法
 *********************************************/
package com.wangxy.exoskeleton.risk.var;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wangxy.exoskeleton.risk.Cdf;
import com.wangxy.exoskeleton.risk.matrix.Covariance;
import com.wangxy.exoskeleton.risk.matrix.MatrixProduct;
import com.wangxy.exoskeleton.risk.util.BigDecimalUtil;


public class CdfVaR {
	
	/**
	 * 计算组合日收益率的协方差矩阵
	 * @param paramlist  var计算参数
	 * @return
	 * @throws Exception 
	 */
	public static double[][] getCovarianceMatrix(List<VaRParam> paramlist) throws Exception{
		return getCovarianceMatrix(paramlist, null);
	}
	
	/**
	 * 计算组合日收益率的协方差矩阵
	 * @param paramlist  var计算参数
	 * @param result     正态var计算结果
	 * @return
	 * @throws Exception 
	 */
	public static double[][] getCovarianceMatrix(List<VaRParam> paramlist,CdfVarResult result) throws Exception{
		int size = paramlist.size();
		double [][] covMatrix = new double [size][size];
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				covMatrix[i][j] = Covariance.getCovariance(DailyRateUtil.getDaily(paramlist.get(i).getHisPriceArray()),DailyRateUtil.getDaily(paramlist.get(j).getHisPriceArray()));
			}
		}
		if(result!=null){
			result.setCovMatrix(covMatrix);
		}
		return covMatrix;
	}
	
	/**
	 * 获取权重矩阵
	 * @param paramlist  var计算参数
	 * @return
	 */
	public static double[][] getWeightMatrix(List<VaRParam> paramlist){
		return getWeightMatrix(paramlist, null);
	}
	
	/**
	 * 获取权重矩阵
	 * @param paramlist  var计算参数
	 * @param result     正态var计算结果
	 * @return
	 */
	public static double[][] getWeightMatrix(List<VaRParam> paramlist,CdfVarResult result){
		double[] weight = PortfolioCalUtil.getWeight(paramlist);
		double weightMatrix[][] = PortfolioCalUtil.getWeightMatrix(weight);
		if(result!=null){
			result.setWeight(weight);
		}
		return weightMatrix;
	}
	
	/**
	 * 获取权重转置矩阵
	 * @param paramlist  var计算参数
	 * @return
	 */
	public static double[][] getRevisedWeight(List<VaRParam> paramlist){
		return getRevisedWeight(paramlist, null);
	}
	
	/**
	 * 获取权重转置矩阵
	 * @param paramlist  var计算参数
	 * @param result     正态var计算结果
	 * @return
	 */
	public static double[][] getRevisedWeight(List<VaRParam> paramlist,CdfVarResult result){
		double[] weight = PortfolioCalUtil.getWeight(paramlist);
		double[][] revisedweight = new double[1][weight.length];
		revisedweight[0] = weight;
		if(result!=null){
			result.setWeight(weight);
		}
		return revisedweight;
	}
	
	/**
	 * 根据权重矩阵、收益率协方差矩阵和权重矩阵的转置，计算组合的波动率
	 * @param paramlist  var计算参数
	 * @return
	 * @throws Exception 
	 */
	public static double getVolatility(List<VaRParam> paramlist) throws Exception{
		return getVolatility(paramlist, null);
	}
	
	/**
	 * 根据权重矩阵、收益率协方差矩阵和权重矩阵的转置，计算组合的波动率
	 * @param paramlist  var计算参数
	 * @param result     正态var计算结果
	 * @return
	 * @throws Exception 
	 */
	public static double getVolatility(List<VaRParam> paramlist,CdfVarResult result) throws Exception{
		double covMatrix [][] = getCovarianceMatrix(paramlist,result);
		//System.out.println("covmatrix(收益率协方差矩阵) "+Matrix.toString(covMatrix));
		double[][] weight = getWeightMatrix(paramlist,result);
		//System.out.println("weight(权重矩阵) "+Matrix.toString(transposedWeight));
		int m = covMatrix.length;
		int n  = covMatrix[0].length;
		double[][] volatilityMatrix = new double [m][n];
		double[][] temp = new double [m][n];
		//System.out.println("revisedWeight(转置权重矩阵) "+Matrix.toString(getRevisedWeight(paramlist)));
		temp = MatrixProduct.matrixProduct(getRevisedWeight(paramlist),covMatrix);
		volatilityMatrix = MatrixProduct.matrixProduct(temp,weight);
		double volatility = Math.sqrt(volatilityMatrix[0][0]);
		if(result!=null){
			result.setVolatility(volatility);
		}
		return volatility;
	}
	
	/**
	 * 计算组合的相对VaR值和相对VaR金额
	 * @param paramlist  var计算参数
	 * @param percentile 置信度
	 * @param days       持有天数
	 * @return
	 * @throws Exception 
	 */
	public static double[] getRelVaR(List<VaRParam> paramlist,double percentile,int days) throws Exception{
		CdfVarResult varResult = calRelVaR(paramlist, percentile, days);
		double[] result = new double [2];
		result[0] = varResult.getVar();
		result[1] = varResult.getVarAmt();
		return result;
	}
	
	/**
	 * 计算组合的相对VaR值和相对VaR金额
	 * @param paramlist  var计算参数
	 * @param percentile 置信度
	 * @param days       持有天数
	 * @return 正态法var值计算结果包装类
	 * @throws Exception 
	 */
	public static CdfVarResult calRelVaR(List<VaRParam> paramlist,double percentile,int days) throws Exception{
		CdfVarResult result = new CdfVarResult();
		double fractile = Cdf.icudf(percentile);
		double var = getVolatility(paramlist,result) * fractile * Math.sqrt(days);
		//20190111 daijy 相对var返回正数
		//double varAmt = Math.abs(PortfolioCalUtil.getTotalValue(paramlist) * var);
		//20191216 qibb 计算过程中取abs ，对外显示时候取abs
		double varAmt = PortfolioCalUtil.getTotalValue(paramlist) * var;
		result.setFractile(fractile);
		result.setVar(var);
		result.setVarAmt(varAmt);
		return result;
	}
	
	/**
	 * 计算组合的绝对VaR值和绝对VaR金额
	 * @param paramlist  var计算参数
	 * @param percentile 置信度
	 * @param days       持有天数
	 * @return 
	 * @throws Exception 
	 */
	public static double[] getVaR(List<VaRParam> paramlist,double percentile,int days) throws Exception{
		CdfVarResult varResult = calVaR(paramlist, percentile, days);
		double[] result = new double [2];
		result[0] = varResult.getVar();
		result[1] = varResult.getVarAmt();
		return result;
	}
	
	/**
	 * 计算组合的绝对VaR值和绝对VaR金额
	 * @param paramlist  var计算参数
	 * @param percentile 置信度
	 * @param days       持有天数
	 * @return 正态法var值计算结果包装类
	 * @throws Exception 
	 */
	public static CdfVarResult calVaR(List<VaRParam> paramlist,double percentile,int days) throws Exception{
		CdfVarResult result = new CdfVarResult();
		double fractile = Cdf.icudf(percentile);
		double avgDailyRate = DailyRateUtil.getAverage(paramlist);
		double var = avgDailyRate * days - getVolatility(paramlist,result) * fractile * Math.sqrt(days);
		double varAmt = PortfolioCalUtil.getTotalValue(paramlist) * var;
		result.setFractile(fractile);
		result.setAvgDailyRate(avgDailyRate);
		result.setVar(var);
		result.setVarAmt(varAmt);
		return result;
	}
	
	/**
	 * 计算组合中每个资产的边际VaR金额
	 * @param paramlist  var计算参数
	 * @param percentile 置信度
	 * @param days 持有天数
	 * @return
	 * @throws Exception 
	 */
	public static double[] getMVaR(List<VaRParam> paramlist,double percentile,int days) throws Exception{
		double[][] covmatrix = getCovarianceMatrix(paramlist);
		double[][] weight = getWeightMatrix(paramlist);
		double[][] revisedWeight = getRevisedWeight(paramlist);
		double[][] temp = MatrixProduct.matrixProduct(covmatrix,weight);
		double volatility = Math.sqrt(MatrixProduct.matrixProduct(revisedWeight, temp)[0][0]);
		double fractile = Cdf.icudf(percentile);
		double[] mVar = new double[paramlist.size()];
		double totalValue = PortfolioCalUtil.getTotalValue(paramlist);
		for (int i = 0; i < temp.length; i++) {
			mVar[i] = BigDecimalUtil.mul(BigDecimalUtil.div(BigDecimalUtil.mul(fractile, Math.sqrt(days), temp[i][0]), volatility), totalValue);
		}
		return mVar;
	}
	
	/**
	 * 计算组合中每个资产的成分VaR金额
	 * @param paramlist  var计算参数
	 * @param percentile 置信度
	 * @param days 持有天数
	 * @return
	 * @throws Exception 
	 */
	public static double[] getCVaR(List<VaRParam> paramlist,double percentile,int days) throws Exception{
		double[] mVar =  getMVaR(paramlist, percentile, days);
		return getCVaR(mVar, PortfolioCalUtil.getWeight(paramlist));
	}
	
	/**
	 * 计算组合中每个资产的成分VaR金额
	 * @param mVar
	 * @param weight
	 * @return
	 */
	public static double[] getCVaR(double[] mVar, double[] weight){
		double[] cVar = new double[mVar.length];
		for (int i = 0; i < cVar.length; i++) {
			cVar[i] = BigDecimalUtil.mul(mVar[i], weight[i]);
		}
		return cVar;
	}
	
	/**
	 * 正态法var值计算结果包装类
	 */
	public static class CdfVarResult{
		/** 收益率协方差矩阵 */
		private double[][] covMatrix;
		/** 权重数组 */
		private double[] weight;
		/** 组合波动率 */
		private double volatility;
		/** 组合平均收益率（预期收益率） */
		private double avgDailyRate;
		/** 置信度分位数 */
		private double fractile;
		/** 组合var值 */
		private double var;
		/** 组合var金额 */
		private double varAmt;

		public double[][] getCovMatrix() {
			return covMatrix;
		}
		public void setCovMatrix(double[][] covMatrix) {
			this.covMatrix = covMatrix;
		}
		public double[] getWeight() {
			return weight;
		}
		public void setWeight(double[] weight) {
			this.weight = weight;
		}
		public double getAvgDailyRate() {
			return avgDailyRate;
		}
		public void setAvgDailyRate(double avgDailyRate) {
			this.avgDailyRate = avgDailyRate;
		}
		public double getVolatility() {
			return volatility;
		}
		public void setVolatility(double volatility) {
			this.volatility = volatility;
		}
		public double getFractile() {
			return fractile;
		}
		public void setFractile(double fractile) {
			this.fractile = fractile;
		}
		public double getVar() {
			return var;
		}
		public void setVar(double var) {
			this.var = var;
		}
		public double getVarAmt() {
			return varAmt;
		}
		public void setVarAmt(double varAmt) {
			this.varAmt = varAmt;
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		List<VaRParam> paramlist = new ArrayList<VaRParam>();
		double[] bondArray1 = {86.18972,96.98414,97.06766,87.08506,96.57826,105.7291,109.9562,114.0616,106.0532,105.3674,91.04952,102.5975,96.05026,84.89747,116.6732,103.9013,107.6561,98.126,101.8085,99.85676,112.221 ,95.54725,97.30556,76.96113,101.0114,109.0754,87.08713,105.5178,95.31154,77.56936};
		double[] bondArray2 = {108.7188,99.89877,101.6105,94.96865,90.33898,101.2129,113.8938,104.5597,87.87705,107.9842,97.00672,90.75454,107.688,100.9077,95.03933,97.16732,112.9257,94.46408,91.64893,77.92765,109.9464,89.89493,104.1682,104.4682,100.5068,102.2271,99.6319,107.984,108.202,102.0385};
		double[] bondArray3 = {97.33568,102.2566,97.64427,96.42648,101.2071,104.1888,94.48551,104.3771,104.3244,109.8168,107.0142,106.8557,95.80566,100.351,99.79264,95.68033,108.9699,111.2136,99.21859,96.2421,111.6526,102.8475,113.1656,99.03962,103.9826,99.41893,104.0182,105.9428,104.358,96.11644};
		paramlist.add(new VaRParam(40000,bondArray1));
		paramlist.add(new VaRParam(25000,bondArray2));
		paramlist.add(new VaRParam(35000,bondArray3));
		System.out.println("var:"+Arrays.toString(CdfVaR.getRelVaR(paramlist,0.95,1)));
		System.out.println("绝对var:"+Arrays.toString(CdfVaR.getVaR(paramlist,0.95,1)));
		System.out.println("mVar:"+Arrays.toString(CdfVaR.getMVaR(paramlist,0.95,1)));
		System.out.println("cVar:"+Arrays.toString(CdfVaR.getCVaR(paramlist,0.95,1)));
	}
}
