/********************************************
 * 文件名称: ICurrencyVar.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 汇率var计算接口
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *********************************************/
package com.wangxy.exoskeleton.risk.calvar;

import java.util.List;

import com.avengers.framework.impl.bizkernel.runtime.exception.BizBussinessException;
import com.avengers.risk.var.VaRResult;

public interface ICurrencyVar {
	
	/**
	 * 汇率var计算
	 * @param calMethod      计算方法
	 * @param currencyParams 汇率var计算参数
	 * @param calDate        计算日期
	 * @param num            样本数
	 * @param percentile     置信度
	 * @param simulatedtimes 模拟次数
	 * @param days           持有天数
	 * @return
	 * @throws BizBussinessException
	 */
	public VaRResult cal(String calMethod, List<CurrencyVarParam> currencyParams, int calDate, int num, double percentile,int simulatedtimes,int days) throws BizBussinessException;

}
