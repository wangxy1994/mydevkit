/********************************************
 * 文件名称: VaRforSingleBond.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 单只债券var计算（包含正态，蒙特卡罗，历史模拟法，极值法）
 * 系统版本: 2.5.0.1
 * 开发人员: gaoyuan
 * 开发时间: 2019-5-28 上午10:27:56
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 * 
 *********************************************/
package com.wangxy.exoskeleton.risk.var;

import com.wangxy.exoskeleton.risk.Cdf;

public class VaRforSingleBond {
 
    public static final String VERSION="@system 资金交易风险管理系统V2.0  @version 2.5.0.1  @lastModiDate  2019-07-09  @describe ";

	static int simulatedperiod;//用以确定蒙特卡罗模拟的日数
	static double percentile;//利用历史模拟法时，需要定义分位数，比如5%（0.05）
	static double[] dailyprice;//该数组为每日债券价格，本例自带的main函数中共20个连续的日价格（公允价）
	static int dailylength;//取到几日的该债券的价格，那么此值就为几
	static double h=1e-5;
	static double threshold ;



	
	public VaRforSingleBond(double[] dailyprice, int simulatedperiod, double percentile,double threshold)throws Exception
	{
		this.dailyprice = dailyprice;
		this.simulatedperiod = simulatedperiod;
		this.percentile = percentile;
		this.dailylength =  dailyprice.length;
		if( percentile <= 0 || 1 <= percentile)
			throw new Exception("分位数越界");
		this.threshold = threshold;
	}
	
	
	/**
	 * 该方法用以计算此债券的每日收益率，如果能取到20日该债券的公允价，则可算出19个日收益率，使用的是对数收益率
	 * @param dailyprice
	 * @return
	 */
	
	public double [] getDailyRate(double [] dailyprice)
	{
		double [] dailyrate = new double [dailyprice.length - 1];
		for (int i = 0; i < dailyprice.length - 1; i++)
		{
			dailyrate[i] = Math.log(dailyprice[i + 1]/dailyprice[i]);
		}
		
		return dailyrate;
	}
	
	
	/**
	 * 该方法用以定义极值法中，对数似然函数相对于第一个参数的偏导数
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double getPartialDifferentialEquationWRTKsi(double ksi, double sigma)
	{
		double w = 0;
		double z = 0;
		double [] temp = new double [getProcessedDR(dailyprice, threshold).length];
		temp = getProcessedDR(dailyprice, threshold);
		for (int i =0; i < temp.length; i++)
		{
			w += Math.log(1 + ksi * temp[i] / sigma);
			z += 1 / (sigma / temp[i]+ ksi);
		}
//		double pdek = (1 / (ksi * ksi)) * w - (1 + 1 / ksi) * z;
		double pdek = w - ksi * (1 + ksi) * z;
		
		return pdek;
	}
	
	
	/**
	 * 该方法用以定义极值法中，对数似然函数相对于第二个参数的偏导数
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double getPartialDifferentialEquationWRTSigma(double ksi, double sigma)
	{
		double z = 0;
		double [] temp = new double [getProcessedDR(dailyprice, threshold).length];
		temp = getProcessedDR(dailyprice, threshold);
		for (int i =0; i < temp.length; i++)
		{
			z += 1 / (sigma / temp[i]+ ksi);
		}
//		double pdes = - (temp.length) / sigma + (1 + ksi) / sigma * z;
		double pdes = - (temp.length) + (1 + ksi) * z;
		
		return pdes;
	}
	
	
	/**
	 * 该方法用以定义极值法中，对数似然函数对两个参数的偏导数的偏导数（二阶偏导数）矩阵
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
//	public double [][] getFisrtOrderDerivative(double ksi, double sigma)
//	{
//		double [][] value=new double [2][2];
//		
//		double X2=0.0;
//		double X1=0.0;
//		X2=getPartialDifferentialEquationWRTKsi(ksi-h,sigma);
//		X1=getPartialDifferentialEquationWRTKsi(ksi+h,sigma);
//		value[0][0]=((X1-X2)/(2*h));
//		
//		double Z2=0.0;
//		double Z1=0.0;
//		Z2=getPartialDifferentialEquationWRTSigma(ksi-h,sigma);
//		Z1=getPartialDifferentialEquationWRTSigma(ksi+h,sigma);
//		value[0][1]=((Z1-Z2)/(2*h));
//		
//		double Y2=0.0;
//		double Y1=0.0;
//		Y2=getPartialDifferentialEquationWRTKsi(ksi,sigma-h);
//		Y1=getPartialDifferentialEquationWRTKsi(ksi,sigma+h);
//		value[1][0]=((Y1-Y2)/(2*h));
//
//		double W2=0.0;
//		double W1=0.0;
//		W2=getPartialDifferentialEquationWRTSigma(ksi,sigma-h);
//		W1=getPartialDifferentialEquationWRTSigma(ksi,sigma+h);
//		value[1][1]=((W1-W2)/(2*h));
//		
//		return value;
//	}
	
	public double [][] getFisrtOrderDerivative(double ksi, double sigma)
	{
		double [][] value=new double [2][2];
		
		double w = 0;
		double z = 0;
		double s = 0;
		double t = 0;
		double [] temp = new double [getProcessedDR(dailyprice, threshold).length];
		temp = getProcessedDR(dailyprice, threshold);
		for (int i =0; i < temp.length; i++)
		{
			w += 1 / (sigma / temp[i]+ ksi);
			z += temp[i] * temp[i] / ((ksi * temp[i] + sigma) * (ksi * temp[i] + sigma));
			s += temp[i] / (ksi * sigma * temp[i] + sigma * sigma);
			t += temp[i] / ((ksi * temp[i] + sigma) * (ksi * temp[i] + sigma));
		}
		value[0][0]= - 2 * ksi * w + (ksi * ksi + ksi) * z;
		value[0][1]= w - (1 + ksi) * z;
		value[1][0]= - ksi * s + ksi * (ksi + 1) * t;
		value[1][1]= -(1 + ksi) * t;
		
		return value;
	}
	
	
	/**
	 * 该方法用以计算一个给定的2×2矩阵的行列式的值
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double getDete(double [][] matrix) throws Exception
	{
		if(matrix.length !=2 || matrix[0].length != 2)
			throw new Exception ("不是二维矩阵");
		else
		{
			double dete = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
			
			return dete;
		}
	}
	
	
	/**
	 * 该方法用以计算一个给定的2×2矩阵的逆矩阵
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double [][] getInvforTwo(double [][] matrix) throws Exception
	{
		if(matrix.length !=2 || matrix[0].length != 2)
			throw new Exception ("不是二维矩阵");
		
		else
		{
			double dete = getDete(matrix);
			double [][] inv = new double [2][2]; 
			inv[0][0] = matrix[1][1] / dete;
			inv[0][1] = - matrix[0][1] / dete;
			inv[1][0] = - matrix[1][0] / dete;
			inv[1][1] = matrix[0][0] / dete;

			return inv;
		}
	}
	
	
	/**
	 * 该方法用以计算一个给定1×2的二维向量和一个给定的的2×2矩阵的乘积的结果
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double [] OneRowTwoColumnTimesTwoRowTwoColumn(double [] ma1,double [][] ma2) throws Exception
	{
		if( ma1.length != 2)
		{
			throw new Exception("向量不是二维列向量");
		}
		if( ma2.length != 2 || ma2[0].length != 2)
		{
			throw new Exception("矩阵不是二维矩阵");
		}
		else
		{
			double [] result = new double [2];
			result[0] = ma1[0] * ma2 [0][0] + ma1[1] * ma2[1][0];
			result[1] = ma1[0] * ma2 [0][1] + ma1[1] * ma2[1][1];
			
			return result;
		}
	}
	
	
	/**
	 * 该方法用以计算两个给定的1×2向量相减的结果
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double [] VectorSubtraction(double [] ma3, double [] ma4) throws Exception
	{
		if(ma3.length != 2 || ma4.length != 2)
		{
			throw new Exception("不全是二维向量，无法相减");
		}
		else
		{
			double [] result = new double [2];
			result[0] = ma3[0] - ma4[0];
			result[1] = ma3[1] - ma4[1];
			
			return result;
		}
	}
	
	
	/**
	 * 使用牛顿迭代法，计算对数似然函数中，关键的两个参数的值
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double [] newton(double ksi, double sigma) throws Exception
	{
		double eps = 0.0001;
		double tol = 1;
		int count = 1;
		double [] result = new double [2];
		double [] storage = new double [2];
		double [] temp = new double [2];
		double [][] inv = new double [2][2];
		double [] product = new double [2];
		double [][] dfm = new double [2][2];
		
		while(tol > eps)
		{
			storage[0] = ksi;
			storage[1] = sigma;
			dfm = getFisrtOrderDerivative(ksi,sigma);
			inv = getInvforTwo(dfm);
			temp[0] = getPartialDifferentialEquationWRTKsi(ksi, sigma);
			temp[1] = getPartialDifferentialEquationWRTSigma(ksi, sigma);
			product =  OneRowTwoColumnTimesTwoRowTwoColumn(temp,inv);
			result = VectorSubtraction(storage,product);
			ksi = result[0];
			sigma = result[1];
			tol = Math.sqrt((result[0] - storage[0]) * (result[0] - storage[0]) + (result[1] - storage[1]) * (result[1] - storage[1]));
			count ++;
			if(count > 1000)
				System.out.println("迭代次数太多，可能函数不收敛");
		}
		
		return result;
	}
	
	
	/**
	 * 该方法先从给定的每日债券价格序列中获取日收益率序列，之后将日收益率序列按照由小到大顺序进行排序，
	 * 接着求这一序列的相反数序列，得到一个由大到小排列的日收益率的序列（全是实际收益率的相反数），然后
	 * 根据阈值（阀值）取这一序列中超过阈值的数据所组成的序列，如果原序列有250个元素，阈值为10%，就取到
	 * 25个最大的值，以备后续对这25个数据组成的序列进行处理
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double [] getProcessedDR(double [] dailyprice, double threshold)
	{
		double [] dr = new double [dailyprice.length -1];
		dr = getDailyRate(dailyprice);
		double [] ordereddr = new double [dr.length];
		ordereddr = fromBottomToTop(dr);
		double [] negdr = new double [ordereddr.length];
		int N = negdr.length;
		for(int i = 0; i < N; i++)
		{
			negdr[i] = - ordereddr[N -1 - i];
		}
		int TforU = (int)Math.round(N * (1 -threshold));//
		int numberpicked = N - TforU;
		double [] numpicked = new double [numberpicked];
		for(int i = 0; i < numberpicked; i++)
		{
			numpicked[i] = negdr[i + TforU];
		}
		
		return numpicked;
	}
	
	
	/**
	 * 根据阈值、求解对数似然函数方程组所得的两个参数、指定的分位数、样本数据总数得到通过极值法计算的VaR值
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double EVTVaR(double [] dailyprice, double ksi,double sigma,double percentile)throws Exception
	{
		
		double [] dr = new double [dailyprice.length -1];
		dr = getDailyRate(dailyprice);
		double [] ordereddr = new double [dr.length];
		ordereddr = fromBottomToTop(dr);
		double [] negdr = new double [ordereddr.length];
		int N = negdr.length;
		for(int i = 0; i < N; i++)
		{
			negdr[i] = - ordereddr[N -1 - i];
		}
		int TforU = (int)Math.round(N * (1 -threshold));//该数用来获得阈值（阀值）点，根据情况需要，本句也可写作int TforU = (int)Math.floor(N * (1 -threshold));
		int numberpicked = (int)(N - TforU);
		double u = negdr[TforU];
		double para1 = newton(ksi, sigma)[0];
		double para2 = newton(ksi, sigma)[1];
		double result = u + para2 / para1 * ( Math.pow((numberpicked / percentile / N), para1) - 1);
		
		return - result;
	}
	
	
	/**
	 * 该方法用于计算每日收益率的平均数和标准差
	 * @param ksi
	 * @param sigma
	 * @return
	 */

	public double [] getMoment(double [] dailyprice)throws Exception
	{
		double [] dailyrate = getDailyRate(dailyprice);
		int drlength = dailyrate.length;
		int count = 0;
		double sum = 0;
		double sumvariance = 0; 
		double [] moment = new double [2];
		if (drlength == 0)
			throw new Exception("每日收益率为空");

		else
		{
			for(int i = 0; i < drlength; i++)
			{
				sum += dailyrate[i];
				count++;
			}
			double average = sum / count;
		
			for(int j = 0; j < drlength; j++)
			{
				sumvariance += (dailyrate[j] - average) * (dailyrate[j] - average);
			}
			double volatility = Math.sqrt(sumvariance / count);
			moment[0] = average;
			moment[1] = volatility;
		}
		
		return moment;
	}
	
	
	/**
	 * 该方法用于计算正态VaR
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double [] getParametricVaR(double percentile, double [] dailyprice)throws Exception
	{
		double [] moment = getMoment(dailyprice);
		double fractile = Cdf.icudf(percentile);
		double parametricvar = moment[0] + moment[1] * fractile;
		double parametricvaramount = dailyprice[dailylength - 1] * parametricvar;//正态VaR算法的VaR金额为该债券最近一日的公允价乘以VaR值
		double [] paravar = new double [2];
		paravar[0] = parametricvar;
		paravar[1] = parametricvaramount;
		
		return paravar;
	}
	
	
	/**
	 * 该方法用于将数组中的两个数交换位置
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public void swap(int one, int two, double []disorderedrate)
	{
		double temp = disorderedrate[one];
		disorderedrate[one] = disorderedrate[two];
		disorderedrate[two] = temp;
	}
	
	
	/**
	 * 该方法用于将数组内的数进行从小到大排列
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double [] fromBottomToTop(double [] disorderedrate)
	{
		double [] fromBottomToTop = new double [disorderedrate.length];
		fromBottomToTop = disorderedrate;
		int in,out;
		for (out = dailylength - 1;out > 0;out--)
			for(in = 0;in < out-1;in++)
				if(fromBottomToTop[in] > fromBottomToTop[in + 1])
					swap(in, in+1,fromBottomToTop);
		
		return fromBottomToTop;
	}
	
	
	/**
	 * 该方法用于从有序数组中得到指定分位数的值
	 * @param ksi
	 * @param sigma
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
	 * 该方法用于得到历史模拟法计算出的VaR
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double getHistoricalVaR()
	{
		return getPercentile(percentile , fromBottomToTop(getDailyRate(dailyprice)));
	}	

	
	/**
	 * 该方法用于得到蒙特卡罗模拟法计算出的VaR
	 * @param ksi
	 * @param sigma
	 * @return
	 */
	
	public double getMonteCarlo(int simulatedtimes,int simulatedperiod)throws Exception
	{
		int timestep = 1;
		double [] moment = getMoment(dailyprice);
		double [] simurate = new double [simulatedtimes];
		for(int i = 0; i < simulatedtimes; i++)
		{
			double simulatedprice = dailyprice[dailylength - 1];
			
		for(int j = 0; j < simulatedperiod; j++)
		{
			
			double stochastic = Cdf.icudf(Math.random());
			simulatedprice = simulatedprice + simulatedprice * (moment[0] * timestep + moment[1] * stochastic * Math.sqrt(timestep));
		}
		simurate[i] = simulatedprice;
		}
		double montecarloVaR = getPercentile(percentile , fromBottomToTop(getDailyRate(simurate)));
		
		return montecarloVaR;
	}
	
}
