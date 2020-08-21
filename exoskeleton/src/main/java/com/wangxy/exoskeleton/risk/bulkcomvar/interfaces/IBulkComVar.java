package com.wangxy.exoskeleton.risk.bulkcomvar.interfaces;
/********************************************
 * 文件名称: IBulkComVar.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 大宗商品var计算接口
 * 系统版本: 2.5.0.1
 * �?发人�?: d2.5.0.1* �?发时�?: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *****************/

import java.util.List;

import com.wangxy.exoskeleton.risk.var.VaRResult;
import com.wangxy.exoskeleton.risk.vo.VarCalParamDetail;

public interface IBulkComVar {
	
	/**
	 * 大宗商品var计算
	 * @param calMethod      计算方法
	 * @param bulkComParams  大宗商品var计算参数
	 * @param calDate        计算日期
	 * @param num            样本�?
	 * @param percentile     置信�?
	 * @param simulatedtimes 模拟次数
	 * @param days           持有天数
	 * @return
	 * @throws BizBussinessException
	 */
	public VaRResult cal(String calMethod, List<VarCalParamDetail> varCalParamDetail, int calDate, int num, double percentile,int simulatedtimes,int days) throws Exception;

}
