/********************************************
 * 文件名称: HistoricalCalVaR.java
 * 系统名称: 外汇资金风险管理
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 历史模拟法var计算处理类
 * 系统版本: 2.0.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20190125 daijy    使用历史法的mVaR计算
 *           20190314 daijy    金额按目标币种进行四舍五入
 *********************************************/
package com.wangxy.exoskeleton.risk.calvar;

import java.util.List;

import org.springframework.stereotype.Service;

import com.avengers.base.constant.IErrMsg;
import com.avengers.base.util.BigDecimalUtil;
import com.avengers.framework.impl.bizkernel.runtime.exception.BizBussinessException;
import com.avengers.risk.var.HistoricalVaR;
import com.avengers.risk.var.VaRParam;
import com.avengers.risk.var.VaRResult;

@Service
public class HistoricalCalVaR extends AbstractCalVaR{

	@Override
	public VaRResult calVar(List<VaRParam> paramlist, double percentile, int simulatedtimes, int days) throws BizBussinessException {
		double[] result;
		try {
			result = HistoricalVaR.getVaR(paramlist, percentile, days);
		} catch (Exception e) {
			throw new BizBussinessException(IErrMsg.ERR_COMMERR,e);
		}
		VaRResult vaRResult = new VaRResult();
		vaRResult.setVar(result[0]);
		//20190314 daijy 获取金额小数位，并四舍五入
	    int decimalPoint = getDecimalPoint(paramlist);
		vaRResult.setVarAmt(BigDecimalUtil.round(result[1],decimalPoint));
		return vaRResult;
	}
	
	protected double[] getMVaR(List<VaRParam> paramlist, double percentile, int days,VaRResult varResult) throws Exception {
		//使用历史法的mVaR计算
		return HistoricalVaR.getMVaR(paramlist, percentile, days);
	}

}
