/********************************************
 * 文件名称: Cdf.java
 * 系统名称: 资金业务管理系统
 * 模块名称: 正态分布
 * 软件版权:  
 * 功能说明: 标准正态分布的积累分布函数
 * 系统版本: 2.5.0.1
 * 开发人员: gaoyuan
 * 开发时间: 2019-5-28 上午10:27:56
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20181027 daijy    增加标准正态分布的密度函数
 *********************************************/
package com.wangxy.exoskeleton.risk;

public class Cdf {

	final static double A0 = 0.2316419;
	final static double A1 = 0.31938153;
	final static double A2 = -0.356563782;
	final static double A3 = 1.781477937;
	final static double A4 = -1.821255978;
	final static double A5 = 1.330274429;

	/**
	 * 标准正态分布的积累分布函数
	 * @param x
	 * @return
	 */
	public static double cudf(double x) {
		double t = 1 / (1 + A0 * Math.abs(x));
		double cdf;
		cdf = 1 - 1 / Math.sqrt(2 * 3.1415926) * Math.exp(-0.5 * x * x) * (A1 * t + A2 * Math.pow(t, 2) + A3 * Math.pow(t, 3) + A4 * Math.pow(t, 4) + A5 * Math.pow(t, 5));
		if (x < 0)
			cdf = 1 - cdf;
		return cdf;
	}

	/**
	 * 标准正态分布的积累分布函数的反函数（标准正态分布的分位数函数）
	 * @param y
	 * @return
	 */
	public static double icudf(double y) {
		double[] b = { 0.1570796288E1, 0.3706987906E-1, -0.8364353589E-3, -0.2250947176E-3, 0.6841218299E-5, 0.5824238515E-5, -0.1045274970E-5, 0.8360937017E-7, -0.3231081277E-8,
				0.3657763036E-10, 0.6936233982E-12 };
		double alpha = 0;
		if ((0 < y) && (y < 0.5))
			alpha = y;
		else if ((0.5 < y) && (y < 1))
			alpha = 1 - y;
		double w = -Math.log(4 * alpha * (1 - alpha));
		double u = 0;

		//使用Toda近似公式，最大误差1.2e-8

		for (int i = 0; i < b.length; i++) {
			u += b[i] * Math.pow(w, i);
		}
		u = Math.sqrt(w * u);

		//也可使用山内近似公式，最大误差4.9e-4
		//u = Math.sqrt(y * (2.0611786 - 5.7262204 / (y + 11.640595)));

		double icdf = 0;
		if ((0 < y) && (y < 0.5))
			icdf = -u;
		else if ((0.5 < y) && (y < 1))
			icdf = u;
		return icdf;
	}

	/**
	 * 标准正态分布的密度函数
	 * @param x
	 * @return
	 */
	public static double density(double x) {
		double t = 1 / (Math.sqrt(2 * Math.PI)) * Math.exp(-Math.pow(x, 2) / 2);
		return t;
	}

}
