
package com.wangxy.exoskeleton.risk.var;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wangxy.exoskeleton.risk.util.BigDecimalUtil;


/********************************************
 * 文件名称: HistoricalVaR.java
 * 系统名称: 外汇资金风险管理
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 历史模拟法组合var计算
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20190125 daijy    增加边际var、成分var计算
 *           20190422 daijy    计算边际var时精度取常量
 *           20200206 qibb    修改var为原始var 
 *********************************************/
public class HistoricalVaR {

	/**
	 * 计算组合的VaR值和VaR金额
	 * @param paramlist  组合参数列表
	 * @param percentile 置信度
	 * @param days       持有天数
	 * @return
	 * @throws Exception
	 */
	public static double[] getVaR(List<VaRParam> paramlist, double percentile, int days) throws Exception {
		return getVaR(paramlist, percentile, days, false);
	}

	/**
	 * 计算组合的VaR值和VaR金额
	 * @param paramlist  组合参数列表
	 * @param percentile 置信度
	 * @param days       持有天数
	 * @param flag        空头资产计算方式
	 * @return
	 * @throws Exception
	 */
	public static double[] getVaR(List<VaRParam> paramlist, double percentile, int days, boolean flag) throws Exception {
		//计算组合每日收益率并从小到大排序
		double[] expectedRate = DailyRateUtil.getDailyRate(paramlist);
		double totalValue = PortfolioCalUtil.getTotalValue(paramlist);
		if (totalValue > 0 && !flag) { //  正敞口
			Arrays.sort(expectedRate);
		} else if (!flag) { //正敞口 不反向
			Arrays.sort(expectedRate);
		} else if (flag) { //负敞口，反向
			Arrays.sort(expectedRate);
			double[] tmp = new double[expectedRate.length];
			int j = 0;
			for (int i = tmp.length - 1; i >= 0; i--) {
				tmp[j] = expectedRate[i];
				j++;
			}
			System.arraycopy(tmp, 0, expectedRate, 0, tmp.length);
		}
		//		
		double var = CommonCalUtil.getPercentile(percentile, expectedRate) * Math.sqrt(days);
		//20190111 daijy var返回正数
		//double varAmount = Math.abs(totalValue * var);  
		// qibb 20200206  修改var原始var 
		double varAmount = totalValue * Math.abs(var);
		double[] result = new double[2];
		result[0] = var;
		result[1] = varAmount;
		return result;
	}

	/**
	 * 计算组合中每个资产的边际VaR金额
	 * @param paramlist  var计算参数
	 * @param percentile 置信度
	 * @param days 持有天数
	 * @since 20190125
	 * @return
	 * @throws Exception 
	 */
	public static double[] getMVaR(List<VaRParam> paramlist, double percentile, int days) throws Exception {
		//计算组合VaR金额
		double[] portfolioVarResult = getVaR(paramlist, percentile, days);
		double portfolioVarAmt = portfolioVarResult[1];
		double[] mVar = new double[paramlist.size()];
		double[] weight = PortfolioCalUtil.getWeight(paramlist);
		//计算每个资产的边际VaR金额
		for (int i = 0; i < paramlist.size(); i++) {
			VaRParam varParam = paramlist.get(i);
			double oriAmount = varParam.getAmount();
			varParam.setAmount(oriAmount * (1 + CommonCalUtil.DEFINE_METHOD_PRECISION));//增加定义法精度后的数量
			double plusVarAmt = getVaR(paramlist, percentile, days)[1];
			mVar[i] = (plusVarAmt - portfolioVarAmt) / CommonCalUtil.DEFINE_METHOD_PRECISION / weight[i];
			varParam.setAmount(oriAmount);
		}
		return mVar;
	}

	/**
	 * 计算组合中每个资产的成分VaR金额
	 * @param paramlist  var计算参数
	 * @param percentile 置信度
	 * @param days 持有天数
	 * @since 20190125
	 * @return
	 * @throws Exception 
	 */
	public static double[] getCVaR(List<VaRParam> paramlist, double percentile, int days) throws Exception {
		double[] mVar = getMVaR(paramlist, percentile, days);
		return getCVaR(mVar, PortfolioCalUtil.getWeight(paramlist));
	}

	/**
	 * 计算组合中每个资产的成分VaR金额
	 * @param mVar
	 * @param weight
	 * @since 20190125
	 * @return
	 */
	public static double[] getCVaR(double[] mVar, double[] weight) {
		double[] cVar = new double[mVar.length];
		for (int i = 0; i < cVar.length; i++) {
			cVar[i] = BigDecimalUtil.mul(mVar[i], weight[i]);
		}
		return cVar;
	}

	public static void main(String[] args) throws Exception {
		List<VaRParam> paramlist = new ArrayList<VaRParam>();
		double[] bondArray1 = { 86.18972, 96.98414, 97.06766, 87.08506, 96.57826, 105.7291, 109.9562, 114.0616, 106.0532, 105.3674, 91.04952, 102.5975, 96.05026, 84.89747,
				116.6732, 103.9013, 107.6561, 98.126, 101.8085, 99.85676, 112.221, 95.54725, 97.30556, 76.96113, 101.0114, 109.0754, 87.08713, 105.5178, 95.31154, 77.56936 };
		double[] bondArray2 = { 108.7188, 99.89877, 101.6105, 94.96865, 90.33898, 101.2129, 113.8938, 104.5597, 87.87705, 107.9842, 97.00672, 90.75454, 107.688, 100.9077,
				95.03933, 97.16732, 112.9257, 94.46408, 91.64893, 77.92765, 109.9464, 89.89493, 104.1682, 104.4682, 100.5068, 102.2271, 99.6319, 107.984, 108.202, 102.0385 };
		double[] bondArray3 = { 97.33568, 102.2566, 97.64427, 96.42648, 101.2071, 104.1888, 94.48551, 104.3771, 104.3244, 109.8168, 107.0142, 106.8557, 95.80566, 100.351,
				99.79264, 95.68033, 108.9699, 111.2136, 99.21859, 96.2421, 111.6526, 102.8475, 113.1656, 99.03962, 103.9826, 99.41893, 104.0182, 105.9428, 104.358, 96.11644 };
		paramlist.add(new VaRParam(40000, bondArray1));
		paramlist.add(new VaRParam(25000, bondArray2));
		paramlist.add(new VaRParam(35000, bondArray3));
		System.out.println(Arrays.toString(HistoricalVaR.getVaR(paramlist, 0.95, 1)));
	}
}
