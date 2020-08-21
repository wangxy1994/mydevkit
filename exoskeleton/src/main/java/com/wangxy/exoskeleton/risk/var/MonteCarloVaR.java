/********************************************
 * 文件名称: MonteCarloVaR.java
 * 系统名称: 外汇资金风险管理
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 蒙特卡罗模拟法组合var计算
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *          20190422  daijy    蒙特卡罗模拟法按成分价格进行随机处理整体修改
 *          20190422  qibb    蒙卡模拟是日价格不在处理天数
 *********************************************/
package com.wangxy.exoskeleton.risk.var;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wangxy.exoskeleton.risk.matrix.Covariance;
import com.wangxy.exoskeleton.risk.util.BigDecimalUtil;
import com.wangxy.exoskeleton.risk.util.MonteCarloUtil;

public class MonteCarloVaR {
	
	/**
	 * 蒙特卡罗模拟法计算VaR值和VaR金额
	 * @param paramlist      var计算参数
	 * @param percentile     置信度
	 * @param simulatedtimes 模拟次数
	 * @param days           模拟天数
	 * @return
	 */
	public static MonteCarloVarResult getVaR(List<VaRParam> paramlist,double percentile,int simulatedtimes,int days) throws Exception{
		return getVaR(paramlist, percentile, simulatedtimes, days, false);
	}
	
	public static void SimulatePrice(List<VaRParam> paramlist,int simulatedtimes,int days) throws Exception{
		if(simulatedtimes==0){
			simulatedtimes = 5000;//默认模拟5000次
		}
		//建立每种成分收益率的随机模型
		for(VaRParam varParam : paramlist){
			//成分估算价格数组
			double[] simPrice = new double[simulatedtimes];
			//成分价格均值
			double rates[] = DailyRateUtil.getDaily(varParam.getHisPriceArray()); //收益率
			double averageRate = DailyRateUtil.getAverage(rates); //预期收益率
			//成分波动率
			double volatility = Math.sqrt(Covariance.getCovariance(rates,rates));
			double last_price =varParam.getLastPrice();
			for(int i = 0; i < simulatedtimes; i++){
				simPrice[i] = MonteCarloUtil.getEstimatedValueLn(last_price, averageRate, volatility, days);   //qibb 20200108  蒙卡模拟是日价格不在处理天数
			}
			varParam.setHisPriceArray(simPrice);   // qibb 20200109  重新设置新的模拟价格进行重新计算。
		}
	}
	
	/**
	 * 蒙特卡罗模拟法计算VaR值和VaR金额
	 * @param paramlist      var计算参数
	 * @param percentile     置信度
	 * @param simulatedtimes 模拟次数
	 * @param days           模拟天数
	 * @param detailFlag     计算var明细标志，默认不计算
	 * @return
	 */
	public static MonteCarloVarResult getVaR(List<VaRParam> paramlist,double percentile,int simulatedtimes,int days, boolean detailFlag) throws Exception{
		if(simulatedtimes==0){
			simulatedtimes = 5000;//默认模拟5000次
		}
		MonteCarloVarResult result = new MonteCarloVarResult();
		result.setFractile(percentile);
		List<Double[]> simPriceList = new ArrayList<Double[]>();
		//建立每种成分收益率的随机模型
		for(VaRParam varParam : paramlist){
			//成分估算价格数组
			Double[] simPrice = new Double[simulatedtimes];
			//成分价格均值
			double rates[] = DailyRateUtil.getDaily(varParam.getHisPriceArray()); //收益率
			double averageRate = DailyRateUtil.getAverage(rates); //预期收益率
			//成分波动率
			double volatility = Math.sqrt(Covariance.getCovariance(rates,rates));
			
			double last_price =varParam.getLastPrice();
			//成分当前价值
			double currentValue = varParam.getLastPrice()*varParam.getAmount();
			//成分估算收益率数组
			double[] simRate = new double[simulatedtimes];
		
			
			//double[][] relMatric = MonteCarloUtil.getRelMatrix(paramlist);
			//double timestep =  BigDecimalUtil.div(days, 100); 
			for(int i = 0; i < simulatedtimes; i++){
				//simPrice[i] = MonteCarloUtil.getEstimatedValue(varParam.getLastPrice(), average, days, volatility, averageRate);
				simPrice[i] = MonteCarloUtil.getEstimatedValueLn(last_price, averageRate, volatility, 1);   //qibb 20200108  蒙卡模拟是日价格不在处理天数
				if(detailFlag){
					//成分估算价值
					double futureValue = simPrice[i] * varParam.getAmount();
					//simRate[i] = BigDecimalUtil.div(futureValue - currentValue, currentValue);
				}
			}
			simPriceList.add(simPrice);
			
			//计算每个成分的VaR值
			if(detailFlag){
				Arrays.sort(simRate);
				//从估算收益率列表中获取指定分位的值
				double var = CommonCalUtil.getPercentile(percentile, simRate)* Math.sqrt(days);
				double varAmt = Math.abs(currentValue * var);
				MonteCarloVarResultDtl detail = new MonteCarloVarResultDtl();
				detail.setPositionAmt(varParam.getLastPrice()*varParam.getAmount());
				detail.setVaR(varAmt);
				result.getDetailList().add(detail);
			}
		}
		//根据随机出来的价格列表及权重，获得组合估算收益率列表
		double[] varResult = calPortfolioVaR(paramlist, simPriceList, percentile, days);
		result.setVar(varResult[0]);
		result.setVarAmt(varResult[1]);
		
		//计算每个成分的mVaR、cVaR、贡献度
		if(detailFlag){
			int i = 0;
			double[] weight = PortfolioCalUtil.getWeight(paramlist);
			for(VaRParam varParam : paramlist){
				double oriAmount = varParam.getAmount();
				varParam.setAmount(BigDecimalUtil.mul(oriAmount,BigDecimalUtil.add(1,CommonCalUtil.DEFINE_METHOD_PRECISION)));//按定义精度进行计算
				double plusVarAmt = calPortfolioVaR(paramlist, simPriceList, percentile, days)[1];
				MonteCarloVarResultDtl detail = result.getDetailList().get(i);
				double mVaR = BigDecimalUtil.div(BigDecimalUtil.sub(plusVarAmt,result.getVarAmt()), BigDecimalUtil.mul(CommonCalUtil.DEFINE_METHOD_PRECISION,weight[i]));
				detail.setmVaR(mVaR);
				detail.setcVaR(BigDecimalUtil.mul(mVaR, weight[i]));
				varParam.setAmount(oriAmount);//计算结束，将amount还原
				i++;
			}
		}
		return result;
	}
	
	/**
	 * 计算组合VaR金额
	 * @param paramlist    var计算参数
	 * @param simPriceList 未来价格数组列表
	 * @param percentile     置信度
	 * @param days           模拟天数
	 * @return
	 */
	private static double[] calPortfolioVaR(List<VaRParam> paramlist, List<Double[]> simPriceList, double percentile, int days){
		//组合的当前总价值
		double totalValue = PortfolioCalUtil.getTotalValue(paramlist);
		int simPriceNum = simPriceList.get(0).length;
		//估算的组合收益率数组
		double[] simRate = new double[simPriceNum];
		for(int i = 0; i < simPriceNum; i++){
			//估算的组合总价值
			double futureTotalValue = getFutureTotalValue(paramlist, simPriceList, i);
			simRate[i] = BigDecimalUtil.div(futureTotalValue - totalValue, totalValue);
		}
		//从估算收益率列表中获取指定分位的值
		Arrays.sort(simRate);
		double var = CommonCalUtil.getPercentile(percentile , simRate)* Math.sqrt(days);
		double varAmt = Math.abs(PortfolioCalUtil.getTotalValue(paramlist) * var);
		double[] result = new double [2];
		result[0] = var;
		result[1] = varAmt;
		return result;
	}
	
	/**
	 * 计算未来组合总价值
	 * @param paramlist    var计算参数
	 * @param simPriceList 未来价格数组列表
	 * @param num          未来价格计数
	 * @return
	 */
	private static double getFutureTotalValue(List<VaRParam> paramlist, List<Double[]> simPriceList, int num){
		//计算未来组合总价值，即所有组合数量*某个未来价格的总和
		double total = 0.00;
		for(int i = 0; i < paramlist.size(); i++){
			total += paramlist.get(i).getAmount()*simPriceList.get(i)[num];
		}
		return total;
	}
	
	/**
	 * 蒙特卡罗模拟法var值计算结果包装类
	 */
	public static class MonteCarloVarResult{
		/** 置信度分位数 */
		private double fractile;
		/** 组合var值 */
		private double var;
		/** 组合var金额 */
		private double varAmt;
		/** var明细列表 */
		private List<MonteCarloVarResultDtl> detailList = new ArrayList<MonteCarloVarResultDtl>();
		
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
		public List<MonteCarloVarResultDtl> getDetailList() {
			return detailList;
		}
		public void setDetailList(List<MonteCarloVarResultDtl> detailList) {
			this.detailList = detailList;
		}
		
	}
	
	/**
	 * 蒙特卡罗模拟法var值计算结果明细包装类
	 */
	public static class MonteCarloVarResultDtl{
		/** 头寸金额 */
		private double positionAmt = 0.0;
		/** VaR金额 */
		private double vaR = 0.0;
		/** 边际VaR金额 */
		private double mVaR = 0.0;
		/** 成分VaR金额 */
		private double cVaR = 0.0;
		/** 增量VaR金额 */
		private double incrVaR = 0.0;
		
		public double getPositionAmt() {
			return positionAmt;
		}
		public void setPositionAmt(double positionAmt) {
			this.positionAmt = positionAmt;
		}
		public double getVaR() {
			return vaR;
		}
		public void setVaR(double vaR) {
			this.vaR = vaR;
		}
		public double getmVaR() {
			return mVaR;
		}
		public void setmVaR(double mVaR) {
			this.mVaR = mVaR;
		}
		public double getcVaR() {
			return cVaR;
		}
		public void setcVaR(double cVaR) {
			this.cVaR = cVaR;
		}
		public double getIncrVaR() {
			return incrVaR;
		}
		public void setIncrVaR(double incrVaR) {
			this.incrVaR = incrVaR;
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
		System.out.println(MonteCarloVaR.getVaR(paramlist,0.95,1000,1));
	}

}
