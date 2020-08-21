/********************************************
 * 文件名称: ICalVaR.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: var计算接口
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
import com.avengers.risk.var.VaRParam;
import com.avengers.risk.var.VaRResult;

public interface ICalVaR {
	
	/**
	 * 计算var值及var金额
	 * @param paramlist  var计算参数
	 * @param percentile 置信度
	 * @param simulatedtimes 抽样次数
	 * @param days 持有天数
	 * @return
	 * @throws BizBussinessException
	 */
	public VaRResult calVar(List<VaRParam> paramlist,double percentile,int simulatedtimes,int days) throws BizBussinessException;
	
	/**
	 * 计算var值及明细var列表
	 * @param paramlist  var计算参数
	 * @param percentile 置信度
	 * @param simulatedtimes 抽样次数
	 * @param days 持有天数
	 * @return
	 * @throws BizBussinessException
	 */
	public VaRResult calVarAndDtl(List<VaRParam> paramlist,double percentile,int simulatedtimes,int days) throws BizBussinessException; 
	

}
