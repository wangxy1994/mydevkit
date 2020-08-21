package com.wangxy.exoskeleton.risk;

/********************************************
 * 文件名称: NormalSCDensity.java
 * 系统名称: 风险管理系统
 * 模块名称: 正态分布
 * 软件版权:  
 * 功能说明: 求指定数据的正态分布累计概率密度分布
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 * 
 *********************************************/
public class NormalSCDensity {
	
	final static double A1 = 2.506628275;
	final static double A2 = 0.2316419;
	final static double A3 = 0.319381530;
	final static double A4 = 0.356563782;
	final static double A5 = 1.781477937;
	final static double A6 = 1.821255978;
	final static double A7 = 1.330274429;

	public static double getDensity(double x){
		double var0 = 1/(1 + A2*Math.abs(x));
		double var1 = Math.exp(-1*Math.pow(x,2)*0.5)/A1;
		double result = 1 - var1*(((((A7*var0 - A6)*var0 + A5)*var0 - A4)*var0 + A3)*var0);
		if(x < 0){
			result = 1 - result;
		}
		return result;
	}
	
}
