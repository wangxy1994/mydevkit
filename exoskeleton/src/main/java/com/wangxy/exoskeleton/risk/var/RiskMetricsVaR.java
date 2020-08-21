/********************************************
 * 文件名称: RiskMetricsVaR.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 单只债券var计算（RiskMetrics法）
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
import java.util.List;

import com.wangxy.exoskeleton.risk.Cdf;


public class RiskMetricsVaR {
 
    public static final String VERSION="@system 资金交易风险管理系统V2.0  @version 2.5.0.1  @lastModiDate  2019-07-09  @describe ";

	double decayfactor;
	int level;
	double percentile;
	List<double[][]> bondlist = new ArrayList<double[][]>();
	
	public RiskMetricsVaR(double df, double p, List<double[][]> bondlistA)
	{
		this.decayfactor = df;
		this.bondlist = bondlistA;
		percentile = p;
		level = bondlist.size();
	}
	
	
	public double [] getWeight()
	{
		double [] weight = new double [level];
		double total = 0.00;
		for(int i = 0; i < level; i++)
		{
			total += bondlist.get(i)[0][bondlist.get(i)[0].length - 1] * bondlist.get(i)[1][bondlist.get(i)[1].length - 1];
		}
		for(int i = 0; i < level; i++)
		{
			weight[i] = bondlist.get(i)[0][bondlist.get(i)[0].length - 1] * bondlist.get(i)[1][bondlist.get(i)[1].length - 1] / total;
		}
		
		return weight;
	}
	
	
//	public List<double[]> getRateArray()
//	{
//		java.util.List<double []> ratelist = new ArrayList<double []>();
//		for(int i = 0; i < level; i++)
//		{
//			double [] rate = new double [bondlist.get(i)[0].length - 1] ;
//			for(int j = 0; j < rate.length; j++)
//			{
//				rate[i] = Math.log(bondlist.get(i)[0][j + 1] / bondlist.get(i)[0][j]);
//			}
//			ratelist.add(rate);
//		}
//		
//		return ratelist;
//		
//	}
	
	public double [] getRateforPort()
	{
		double [] priceforport = new double [bondlist.get(0)[0].length];
		for(int i = 0; i < priceforport.length; i++)
		{
			for (int j = 0; j < level; j++)
			{
				priceforport[i] += bondlist.get(j)[0][i] * getWeight()[j];
			}
		}
		
		double [] rateserial = new double [priceforport.length - 1];
		for(int i = 0; i < rateserial.length; i++)
		{
			for (int j = 0; j < level; j++)
			{
				rateserial[i] = Math.log(priceforport[i + 1] / priceforport[i]);
			}
		}
		
		return rateserial;
	}
	
	
	
	public double [] getRiskMetricsRateSerial()
	{
		double [] riskmetrics = new double [getRateforPort().length];
		riskmetrics[0] = (1 - decayfactor) * getRateforPort()[0] * getRateforPort()[0];
		for(int i = 1; i < riskmetrics.length; i++)
		{
			riskmetrics[i] = (1 - decayfactor) * getRateforPort()[i - 1] * getRateforPort()[i - 1] + decayfactor * riskmetrics[i - 1];
		}
		
		double [] sqrtofriskmetrics = new double [riskmetrics.length];
		for(int i = 0; i < sqrtofriskmetrics.length; i++)
		{
			sqrtofriskmetrics[i] = Math.sqrt(riskmetrics[i]);
		}
		
		double [] finalvolatility = new double [sqrtofriskmetrics.length];
		double fractile = Cdf.icudf(percentile);
		for(int i = 0; i < finalvolatility.length; i++)
		{
			finalvolatility[i] = fractile * sqrtofriskmetrics[i];
		}
		
		return finalvolatility;
	}
	
	
	public double getVolatility()
	{
		double volatility = getRiskMetricsRateSerial()[getRiskMetricsRateSerial().length - 1];
		return volatility;
	}
	
	
	
}
