/********************************************
 * 文件名称: CdfVaRC.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 正态分布法组合var计算 方法C
 * 			 先计算个体var金额，根据收益率相关系数矩阵，再计算置信度下的var金额
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *********************************************/
package com.wangxy.exoskeleton.risk.var;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wangxy.exoskeleton.risk.Cdf;
import com.wangxy.exoskeleton.risk.matrix.Covariance;
import com.wangxy.exoskeleton.risk.matrix.Matrix;
import com.wangxy.exoskeleton.risk.matrix.MatrixProduct;
import com.wangxy.exoskeleton.risk.util.BigDecimalUtil;


public class CdfVaRC {
	
	/**
	 * 根据价格列表计算波动率
	 * @param varParam   var计算参数
	 * @return
	 */
	private static double getVolatility(VaRParam vaRParam)throws Exception{
		//波动率即为收益率数组的标准差
		double[] dailyRateArray = DailyRateUtil.getDaily(vaRParam.getHisPriceArray());
		double variance = Covariance.getCovariance(dailyRateArray, dailyRateArray);
		double volatility = Math.sqrt(variance);
		return volatility;
	}
	
	/**
	 * 计算个体的相对VaR金额
	 * @param varParam   var计算参数
	 * @param flag       方向
	 * @param percentile 置信度
	 * @param days       持有天数
	 * @return 
	 */
	private static double getRelVaR(VaRParam vaRParam,int flag,double percentile,int days)throws Exception{
		double fractile = Cdf.icudf(percentile);
		double var = flag * getVolatility(vaRParam) * fractile * Math.sqrt(days);
		//20190111 daijy 相对var返回正数
		double varAmt = Math.abs(vaRParam.getAmount() * vaRParam.getLastPrice() * var);
		return varAmt;
	}
	
	/**
	 * 计算个体的绝对VaR金额
	 * @param paramlist  var计算参数
	 * @param flag       方向
	 * @param percentile 置信度
	 * @param days       持有天数
	 * @param result     正态var计算结果
	 * @param result     正态var计算结果位置
	 * @return 
	 */
	private static double getVaR(VaRParam vaRParam,int flag,double fractile,int days,CdfVaRCResult result,int position) throws Exception{
		double[] dailyRateArray = DailyRateUtil.getDaily(vaRParam.getHisPriceArray());
		double avgDailyRate = DailyRateUtil.getAverage(dailyRateArray);
		double volatility = getVolatility(vaRParam);
		double var = avgDailyRate * days - flag * volatility * fractile * Math.sqrt(days);
		double varAmt = vaRParam.getAmount() * vaRParam.getLastPrice() * var * flag;
		if(result!=null){
			result.getSingleAvgDailyRate()[position]=avgDailyRate;
			result.getSingleVolatility()[position]=volatility;
			result.getSingleVarAmt()[position]=varAmt;
		}
		return varAmt;
	}

	/**
	 * 计算组合日收益率的相关系数矩阵
	 * @param paramlist  var计算参数
	 * @param result     正态var计算结果
	 * @return
	 */
	private static double[][] getRelMatrix(List<VaRParam> paramlist,CdfVaRCResult result) throws Exception{
		//先计算协方差矩阵，后转换为相关系数矩阵
		int size = paramlist.size();
		double[][] covMatrix = new double [size][size];
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				covMatrix[i][j] = Covariance.getCovariance(DailyRateUtil.getDaily(paramlist.get(i).getHisPriceArray()),DailyRateUtil.getDaily(paramlist.get(j).getHisPriceArray()));
			}
		}
		double[][] relMatrix = Covariance.getRelMatrix(covMatrix);
		if(result!=null){
			result.setCovMatrix(covMatrix);
			result.setRelMatrix(relMatrix);
		}
		return relMatrix;
	}
	
	/**
	 * 计算组合的绝对VaR值和绝对VaR金额
	 * @param paramlist  var计算参数
	 * @param percentile 置信度
	 * @param days       持有天数
	 * @return 
	 * @throws Exception 
	 */
	public static CdfVaRCResult calVaR(List<VaRParam> paramlist,double percentile,int days) throws Exception{
		double totalValue = PortfolioCalUtil.getTotalValue(paramlist);
		//个体var矩阵
		double[][] vaRMatrix = new double[paramlist.size()][1];
		double fractile = Cdf.icudf(percentile);
		CdfVaRCResult result = new CdfVaRCResult();
		result.setFractile(fractile);
		result.setWeight(PortfolioCalUtil.getWeight(paramlist));
		result.setSingleAvgDailyRate(new double[paramlist.size()]);
		result.setSingleVolatility(new double[paramlist.size()]);
		result.setSingleVarAmt(new double[paramlist.size()]);
		for(int i=0;i<paramlist.size();i++){
			VaRParam vaRParam = paramlist.get(i);
			double value = vaRParam.getAmount()*vaRParam.getLastPrice();
			int flag = Math.signum(totalValue)==Math.signum(value) ? 1 : -1;
			double varAmt = getVaR(vaRParam, flag, fractile, days, result, i);
			vaRMatrix[i][0] = varAmt;
		}
		double[][] relMatrix = getRelMatrix(paramlist, result);
		double[][] temp = MatrixProduct.matrixProduct(relMatrix, vaRMatrix);
		double[][] reverseVaRMatrix = (new Matrix(vaRMatrix)).getTranspose();
		double[][] portfolioMatrix = MatrixProduct.matrixProduct(reverseVaRMatrix,temp);
		double varAmt = Math.sqrt(portfolioMatrix[0][0]);
		result.setVarAmt(varAmt);
		return result;
	}
	
	/**
	 * 计算组合中每个资产的边际VaR金额
	 * @param mVar
	 * @param weight
	 * @return
	 */
	public static double[] getMVaR(double[] cVar, double[] weight){
		double[] mVar = new double[cVar.length];
		for (int i = 0; i < mVar.length; i++) {
			if(weight[i]!=0){
				mVar[i] = BigDecimalUtil.div(cVar[i], weight[i]);
			}else{
				mVar[i] = 0;
			}
		}
		return mVar;
	} 
	
	/**
	 * 计算组合中每个资产的成分VaR金额
	 * @param result  var计算结果
	 * @return
	 */
	public static double[] getCVaR(CdfVaRCResult result)throws Exception{
		//协方差*权重矩阵
		double[][] weight = PortfolioCalUtil.getWeightMatrix(result.getWeight());
		double[][] temp = MatrixProduct.matrixProduct(result.getCovMatrix(), weight);
		double[][] revisedWeight = (new Matrix(weight)).getTranspose();
		double volatility = Math.sqrt(MatrixProduct.matrixProduct(revisedWeight, temp)[0][0]);
		double[] cVar = new double[result.getWeight().length];
		for(int i=0;i<temp.length;i++){
			double corr = temp[i][0] / volatility / result.getSingleVolatility()[i];
			cVar[i] = result.getSingleVarAmt()[i]*corr;
		}
		return cVar;
	}
	
	/**
	 * 计算组合中每个资产的成分VaR贡献
	 * @param cVar
	 * @param vVar
	 * @return
	 */
	public static double[] getCVaRContri(double[] cVar,double var)throws Exception{
		double[] cVaRContri = new double[cVar.length];
		for (int i = 0; i < cVar.length; i++) {
			if(var!=0){
				cVaRContri[i] = BigDecimalUtil.div(cVar[i], var);
			}else{
				cVaRContri[i] = 0;
			}
		}
		return cVaRContri;
	}
	
	/**
	 * 正态法var值计算结果包装类
	 */
	public static class CdfVaRCResult{
		/** 收益率协方差矩阵 */
		private double[][] covMatrix;
		/** 收益率相关系数矩阵 */
		private double[][] relMatrix;
		/** 权重数组 */
		private double[] weight;
		/** 个体平均收益率数组 */
		private double[] singleAvgDailyRate;
		/** 个体波动率数组 */
		private double[] singleVolatility;
		/** 个体VaR金额数组 */
		private double[] singleVarAmt;
		/** 置信度分位数 */
		private double fractile;
		/** 组合var金额 */
		private double varAmt;
		
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
		public double[] getSingleAvgDailyRate() {
			return singleAvgDailyRate;
		}
		public void setSingleAvgDailyRate(double[] singleAvgDailyRate) {
			this.singleAvgDailyRate = singleAvgDailyRate;
		}
		public double[] getSingleVolatility() {
			return singleVolatility;
		}
		public void setSingleVolatility(double[] singleVolatility) {
			this.singleVolatility = singleVolatility;
		}
		public double[] getSingleVarAmt() {
			return singleVarAmt;
		}
		public void setSingleVarAmt(double[] singleVarAmt) {
			this.singleVarAmt = singleVarAmt;
		}
		public double getFractile() {
			return fractile;
		}
		public void setFractile(double fractile) {
			this.fractile = fractile;
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
		CdfVaRCResult result = CdfVaRC.calVaR(paramlist,0.95,1);
		System.out.println("绝对var:"+result.getVarAmt());
		System.out.println("个体var:"+Arrays.toString(result.getSingleVarAmt()));
		System.out.println("cVar:"+Arrays.toString(CdfVaRC.getCVaR(result)));
	}
}
