/********************************************
 * 文件名称: CurrencyVar.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 汇率var计算实现类
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20181217 daijy    var计算样本不同币种日期不需要一致
 *           20190314 daijy    varParam增加目标币种设置
 *********************************************/
package com.wangxy.exoskeleton.risk.calvar;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.avengers.base.constant.IErrMsg;
import com.avengers.base.log.BfmLog;
import com.avengers.framework.impl.bizkernel.runtime.exception.BizBussinessException;
import com.avengers.framework.interfaces.businessLogging.BizLog;
import com.avengers.risk.util.CurrencyPriceUtil;
import com.avengers.risk.var.VaRParam;
import com.avengers.risk.var.VaRResult;

@Service
public class CurrencyVar implements ICurrencyVar{
	private final static BizLog logger = BfmLog.getTransLogNoCache();
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
	public VaRResult cal(String calMethod, List<CurrencyVarParam> currencyParams, int calDate, int num, double percentile,int simulatedtimes,int days) throws BizBussinessException {
		if(currencyParams!=null && currencyParams.size()>0 && currencyParams.get(0)!=null){
			List<VaRParam> paramlist = new ArrayList<VaRParam>();
			//获取汇率var值计算价格样本
			for(CurrencyVarParam currencyParam : currencyParams){
				double[] priceArray;
				try {
					priceArray = CurrencyPriceUtil.getCurrencyRate(currencyParam.getSourceCurr(), currencyParam.getTargetCurr(), calDate, num);
				} catch (BizBussinessException e) {
					//如果当前币种行情数据不足就不参与计算
					if (IErrMsg.ERR_LACK_OF_DATA.equals(e.getErrorNo())) {
						logger.error(currencyParam.getSourceCurr()+"不参与计算",e);
						continue;
					}else {
						throw e;
					}
				}
				VaRParam varParam = new VaRParam(currencyParam.getAmt(),priceArray);
				varParam.setTitle(currencyParam.getSourceCurr());
				//20190314 daijy 目标币种设置
				varParam.setTargetCurr(currencyParam.getTargetCurr());
				paramlist.add(varParam);
			}
			if (paramlist.size()==0) {
				throw new BizBussinessException(IErrMsg.ERR_COMMERR,"请求的币种的汇率样本数量都不足");
			}
			ICalVaR calVar = CalVaRFactory.get(calMethod);
			VaRResult varResult=calVar.calVarAndDtl(paramlist, percentile, simulatedtimes, days);
			int[] period = CurrencyPriceUtil.getPeriod(currencyParams.get(0).getSourceCurr(), currencyParams.get(0).getTargetCurr(), calDate, num);
			varResult.setBeginDate(period[0]);
			varResult.setEndDate(period[1]);
			return varResult;
		}
		return null;
	}

}
