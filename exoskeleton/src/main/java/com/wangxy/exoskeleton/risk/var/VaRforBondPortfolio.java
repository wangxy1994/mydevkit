/********************************************
 * 文件名称: VaRforBondPortfolio.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 组合var计算（包含正态，蒙特卡罗，历史模拟法）
 * 系统版本: 2.5.0.1
 * 开发人员: gaoyuan
 * 开发时间: 2019-5-28 上午10:27:56
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 * 
 *********************************************/
package com.wangxy.exoskeleton.risk.var;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wangxy.exoskeleton.risk.Cdf;
import com.wangxy.exoskeleton.risk.matrix.Covariance;
import com.wangxy.exoskeleton.risk.matrix.MatrixProduct;
import com.wangxy.exoskeleton.risk.util.DataUtil;


public class VaRforBondPortfolio {
 
    public static final String VERSION="@system 资金交易风险管理系统V2.0  @version 2.5.0.1  @lastModiDate  2019-07-09  @describe ";

	
	/**
	 * 生成债券组合（本例main函数中债券组合包含3只债券）
	 */
	
	private List<double[][]> bondlist = new ArrayList<double[][]>();
	private int level;
	
	/**
	 * 
	 * @param bondlistA
	 */
	public VaRforBondPortfolio(List<double[][]> bondlistA)
	{
		this.bondlist = bondlistA;
		this.level = bondlist.size();
	}
		
	
	public List<double[][]> getBondList()
	{
		return bondlist;
	}
	
	
	public void setBondList(List<double[][]> bondlistA)
	{
		this.bondlist = bondlistA;
	}
	
	
	/**
	 * 为方便整齐地打印矩阵内容而做的一个方法，不影响任何计算
	 * @param bondinfor
	 * @return
	 */
		
	public String toString(double [][] bondinfor)
	{
		int maxSize = 20;
		StringBuffer sbr = new StringBuffer();
		sbr.append("Bond[").append(bondinfor.length).append(",").append(bondinfor[0].length).append("]:\n");
		for(int i = 0; i < bondinfor.length; i++)
		{
			for(int j = 0; j < bondinfor[0].length; j++)
			{
				sbr.append("\t").append(DataUtil.fixSpaceBeforeStringByByte(bondinfor[i][j]+"",maxSize));
			}
			sbr.append("\n");
		}
		
		return sbr.toString();
	}
	
	
	/**
	 * 取到组合中某一债券的每个交易日的盯市全价 (本例中债券信息这一数组为一个2行20列的数组，第一行为每日债券价格，第二行为每日债券数量)
	 * @param bondinfor
	 * @return
	 */
	
	public double [] getPrincipalInfor(double [][] bondinfor)
	{
		return bondinfor[0];
	}
	
	
	/**
	 * 由每只债券每日的价格，获得该债券的日收益率序列（本例中19个日收益率）
	 * @param Bond
	 * @return
	 */
	
	public double [] getDaily(double [] bondPrices)
	{
		double [] dailyrate = new double [bondPrices.length  - 1];
		for(int i = 0; i < bondPrices.length  - 1; i++)
		{
			dailyrate[i] = Math.log(bondPrices[i + 1] / bondPrices[i]);
		}
		
		return dailyrate;
	}
	
	
	
	
	
	/**
	 * 计算日收益率的协方差矩阵
	 * @return
	 */
	
	public double [][] getCovarianceMatrix()throws Exception
	{
		double [][] covmatrix = new double [level][level];

		for(int i = 0; i < level; i++)
		{
			for(int j = 0; j < level; j++)
			{
				covmatrix[i][j] = Covariance.getCovariance(getDaily(getPrincipalInfor(bondlist.get(i))),getDaily(getPrincipalInfor( bondlist.get(j))));
			}
		}

		
		return covmatrix;
	}
	
	
	/**
	 * 计算最末一日的债券组合中，各债券的权重
	 * @return
	 */
	
	public double [] getWeight()
	{
		double [] weight = new double [level];
		double total = 0.00;
		for(int i = 0; i < level; i++)
		{
			weight[i] = bondlist.get(i)[0][bondlist.get(i)[1].length - 1] * bondlist.get(i)[1][bondlist.get(i)[1].length - 1];
			total += weight[i];
		}
		for(int i = 0; i < bondlist.size(); i++)
		{
			weight[i] = weight[i] / total;
		}
		return weight;
	}
	
	
	/**
	 * 上一方法计算出的债券权重为一个一维数组，为使用周斌斌之前所写的矩阵相乘的方法，需将其变为多维数组
	 * @return
	 */
	
	public double [][] getRevisedWeight()
	{
		double [] weight = getWeight();
		double [][] revisedweight = new double [1][weight.length];
		revisedweight[0] = weight;
		return revisedweight;
	}
	
	
//	/**
//	 * 获得权重矩阵的转置矩阵
//	 * @return
//	 */
//	
//	public double [][] getTransposedWeight()
//	{
//		double [][] revisedweight = getRevisedWeight();
//		int m = revisedweight.length;
//		int n = revisedweight[0].length;
//		
//		double [][] transposedweight = new double[m][n];
//
//			for(int i = 0; i < m; i++)
//			{
//				for(int j =0; j < n ;j++)
//				{
//					transposedweight[j][i] = revisedweight[i][j];
//				}
//			}
//			return transposedweight;
//	}

	
//	/**
//	 * 周斌斌之前所写的计算两个矩阵相乘的方法
//	 * @param matrixA
//	 * @param matrixB
//	 * @return
//	 */
//	
//	public static double[][] MatrixProduct(double[][] matrixA, double[][] matrixB) 
//	{
//		int k = matrixA.length;
//		int l = matrixA[0].length;
//		int m = matrixB.length;
//		int n = matrixB[0].length;
//		if (l != m)
//		{
//			return null;
//		} 
//		else 
//		{
//			double[][] product = new double[k][n];
//			for (int i = 0; i < k; i++)
//			{
//				for (int j = 0; j < n; j++) 
//				{
//					product[i][j] = 0;
//					for (int q = 0; q < m; q++) 
//					{
//						product[i][j] += matrixA[i][q] * matrixB[q][j];
//					}
//				}
//			}
//			return product;
//		}
//	}
	
	
	/**
	 * 根据权重矩阵、收益率协方差矩阵和权重矩阵的转置，计算债券组合的波动率
	 * @return
	 */
	
	public double getVaRforPort()throws Exception
	{
		double weight [] = getWeight();
		
		double covmatrix [][] = getCovarianceMatrix();
		double transposedweight [][] = new double [weight.length][1];
		for(int i = 0; i < weight.length; i ++)
		{
			transposedweight [i][0] = weight[i];
		}
		int m = covmatrix.length;
		int n  = covmatrix[0].length;
		double varforport [][] = new double [m][n];
		double temp [][] = new double [m][n];
		temp = MatrixProduct.matrixProduct(getRevisedWeight(),covmatrix);
		varforport = MatrixProduct.matrixProduct(temp,transposedweight);
		return Math.sqrt(varforport[0][0]);
	}
	
	
	/**
	 * 得到债券组合每日的收益率，假设该组合每日的权重都等于最末一日的权重
	 * @return
	 */
	
	public double [] getExpectedRate()
	{
		double[] daily = getDaily(getPrincipalInfor(bondlist.get(0)));
		double expectedPortrate[] = new double [daily.length];
		double[] weight = getWeight();
		for(int i =0; i < expectedPortrate.length; i++)
		{
			for(int j =0; j < level; j++)
			{
				expectedPortrate[i] += weight[j] *  getDaily(getPrincipalInfor(bondlist.get(j)))[i];
			}
		}
		
		return expectedPortrate;
	}
	
	
	/**
	 * 计算债券组合的每日的收益率的数学期望，假设该组合每日的权重都等于最末一日的权重
	 * @return
	 */
	
	public double getAverage()
	{
		double [] dailyrate = getExpectedRate();
		int drlength = dailyrate.length;
		double sum = 0;
		for(int i = 0; i < drlength; i++)
		{
			sum += dailyrate[i];
			
		}
		double average = sum / drlength;

		return average;
	}
	
	/**
	 * 计算债券组合的VaR值和VaR金额
	 * @param percentile 分位数
	 * @return
	 */
	
	public double [] getFinalVaR(double percentile)throws Exception
	{
		double fractile = Cdf.icudf(percentile);
		double finalvar = getAverage() + getVaRforPort() * fractile;
		
		double total = 0.00;
		for(int i = 0; i < level; i++)
		{
			total += bondlist.get(i)[0][bondlist.get(i)[0].length - 1] * bondlist.get(i)[1][bondlist.get(i)[1].length - 1];
		}
		double finalvaramount = total * finalvar;//正态VaR算法的VaR金额为该债券组合最近一日的公允价乘以VaR值
		double [] fvar = new double [2];
		fvar[0] = finalvar;
		fvar[1] = finalvaramount;
		
		return fvar;
	}
	
	
	/**
	 * 将数组中的两个数交换位置
	 * @param one
	 * @param two
	 * @param disorderedrate
	 */
	
	public void swap(int one, int two, double []disorderedrate)
	{
		double temp = disorderedrate[one];
		disorderedrate[one] = disorderedrate[two];
		disorderedrate[two] = temp;
	}
	
	
	/**
	 * 将数组内的数进行从小到大排列
	 * @param disorderedrate
	 * @return
	 */
	
	public double [] fromBottomToTop(double [] disorderedrate)
	{
		double [] temp = disorderedrate;
		 Arrays.sort(temp);
		 return temp;
		
//		double [] fromBottomToTop = new double [disorderedrate.length];
//		fromBottomToTop = disorderedrate;
//		int in,out;
//		for (out = fromBottomToTop.length - 1;out > 0;out--)
//			for(in = 0;in < out-1;in++)
//				if(fromBottomToTop[in] > fromBottomToTop[in + 1])
//					swap(in, in+1,fromBottomToTop);
//		
//		return fromBottomToTop;
	}
	
	
	/**
	 * 从有序数组中得到指定分位数的值
	 * @param percentile
	 * @param fromBottomToTop
	 * @return
	 */
	
	public double getPercentile(double percentile , double [] fromBottomToTop)
	{
		double accurate = percentile * (fromBottomToTop.length - 1);
		int floor = (int)Math.floor(accurate);
		double residule = accurate - floor; 		
		double per = (1- residule) * fromBottomToTop[floor] + residule * fromBottomToTop[floor + 1];
		
		return per;
	}
	
	
	/**
	 * 历史模拟法计算VaR
	 * @param percentile
	 * @return
	 */
	
	public double getHistoricalVaR(double percentile)
	{
		
		
		return getPercentile(percentile , fromBottomToTop(getExpectedRate()));
	}	
	
	
	/**
	 * 蒙特卡罗模拟法计算VaR
	 * @param percentile 分位数
	 * @param simulatedtimes 模拟次数
	 * @param simulatedperiod 模拟天数s
	 * @return
	 */
	
	public double getMonteCarlo(double percentile,int simulatedtimes,int simulatedperiod)throws Exception
	{
		int timestep = 1;
		double [] simurate = new double [simulatedtimes];
		double average = getAverage();
		double vaRforPort = getVaRforPort()-average;
		for(int i = 0; i < simulatedtimes; i++)
		{
			double simulatedrate = getExpectedRate()[getExpectedRate().length - 1];
			
			for(int j = 0; j < simulatedperiod; j++)
			{
				double stochastic = Cdf.icudf(Math.random());
				simulatedrate += simulatedrate * (average * timestep + vaRforPort * stochastic * Math.sqrt(timestep));
			}
			simurate[i] = simulatedrate;
		}
		double montecarloVaR = getPercentile(percentile , fromBottomToTop(simurate));
		
		return montecarloVaR;
	}
	
	
	
	
}
