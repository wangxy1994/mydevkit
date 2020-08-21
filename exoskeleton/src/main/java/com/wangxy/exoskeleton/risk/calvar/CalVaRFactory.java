/********************************************
 * 文件名称: CalVaRFactory.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: var计算处理工厂类
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *********************************************/
package com.wangxy.exoskeleton.risk.calvar;

import com.wangxy.exoskeleton.risk.exception.BizBussinessException;
import com.wangxy.exoskeleton.risk.exception.IErrMsg;

public class CalVaRFactory{
	/** 正态分布法 */
	public static final String CDF="1";
	/** 历史模拟法 */
	public static final String HISTORICAL="2";
	/** 蒙特卡罗模拟法 */
	public static final String MONTE_CARLO="3";
	
	public static ICalVaR get(String calMethod) throws BizBussinessException {
		if(CDF.equals(calMethod)){
			//使用B方法进行处理
			return ServiceFactory.getBean(ICalVaR.class, "cdfCalVaR");
			//return ServiceFactory.getBean(IICalVaR.class, "cdfCalVaR2");
		}else if(HISTORICAL.equals(calMethod)){
			return ServiceFactory.getBean(ICalVaR.class, "historicalCalVaR");
		}else if(MONTE_CARLO.equals(calMethod)){
			return ServiceFactory.getBean(ICalVaR.class, "monteCarloCalVaR");
		}else{
			throw new BizBussinessException(IErrMsg.ERR_PARAMETER,"不支持的var值计算方法");
		}
	}

}
