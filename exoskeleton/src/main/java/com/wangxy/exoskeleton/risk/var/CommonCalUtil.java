/********************************************
 * 文件名称: CommonCalUtil.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 计算工具类
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *          20190422  daijy    新增定义法计算边际VaR金额的精度
 *********************************************/
package com.wangxy.exoskeleton.risk.var;

public class CommonCalUtil {
	
	/** 定义法计算边际VaR金额的精度 */
	public final static double DEFINE_METHOD_PRECISION = 0.00000001;//8位精度
	
	
	/**
	 * 从有序数组中得到指定分位数的值
	 * @param percentile
	 * @param fromBottomToTop
	 * @return
	 */
	public static double getPercentile(double percentile , double[] fromBottomToTop){
		//20190109 daijy 调整从前往后取数据 location=（1-置信度）* (日收益率数组长度)；
		double accurate = (1-percentile) * (fromBottomToTop.length);
		int floor = (int)Math.floor(accurate);
		double residule = accurate - floor; 
		double per = 0.0;
		if(fromBottomToTop.length>1){
			//判断floor+1在数组范围内
			if((floor + 1)>=fromBottomToTop.length){
				per = fromBottomToTop[floor];
			}else{
				per = (1- residule) * fromBottomToTop[floor] + residule * fromBottomToTop[floor +1];
			}
		}else if(fromBottomToTop.length==1){
			per = fromBottomToTop[0];
		}
		return per;
	}

}
