/********************************************
 * 文件名称: CdfCalVaR.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 正态法var计算处理类
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20190314 daijy    金额按目标币种进行四舍五入
 *********************************************/
package com.wangxy.exoskeleton.risk.calvar;

import java.util.List;

import org.springframework.stereotype.Service;

import com.avengers.base.constant.IErrMsg;
import com.avengers.base.matrix.Covariance;
import com.avengers.base.util.BigDecimalUtil;
import com.avengers.framework.impl.bizkernel.runtime.exception.BizBussinessException;
import com.avengers.risk.var.CdfVaR;
import com.avengers.risk.var.CdfVaR.CdfVarResult;
import com.avengers.risk.var.DailyRateUtil;
import com.avengers.risk.var.VaRParam;
import com.avengers.risk.var.VaRResult;
import com.avengers.risk.var.VaRResultCdf;
import com.avengers.risk.var.VaRResultDtl;
import com.avengers.risk.var.VaRResultDtlCdf;


@Service
public class CdfCalVaR extends AbstractCalVaR{

	@Override
	public VaRResult calVar(List<VaRParam> paramlist, double percentile, int simulatedtimes, int days) throws BizBussinessException {
		try {
			//20190109 daijy 调整为计算相对var
			CdfVarResult result = CdfVaR.calRelVaR(paramlist, percentile, days);
			VaRResultCdf vaRResult = new VaRResultCdf();
			vaRResult.setVar(result.getVar());
			//20190314 daijy 获取金额小数位，并四舍五入
		    int decimalPoint = getDecimalPoint(paramlist);
			vaRResult.setVarAmt(BigDecimalUtil.round(result.getVarAmt(),decimalPoint));
			vaRResult.setCovMatrix(result.getCovMatrix());
			//根据协方差矩阵计算相关系数矩阵
			vaRResult.setRelMatrix(Covariance.getRelMatrix(result.getCovMatrix()));
			vaRResult.setWeight(result.getWeight());
			//20190109 daijy 计算平均收益率
			vaRResult.setAvgDailyRate(DailyRateUtil.getAverage(paramlist));
			vaRResult.setVolatility(result.getVolatility());
			return vaRResult;
		} catch (Exception e) {
			throw new BizBussinessException(IErrMsg.ERR_COMMERR,e);
		}
	}
	
	@Override
	protected VaRResultDtl newVaRResultDtl(){
		return new VaRResultDtlCdf();
	}
	
	@Override
	protected VaRResultDtl packageToDtl(VaRResult vaRResult){
		VaRResultDtlCdf vaRDtl = new VaRResultDtlCdf();
		vaRDtl.setVaR(vaRResult.getVarAmt());
		if(vaRResult instanceof VaRResultCdf){
			VaRResultCdf result = (VaRResultCdf) vaRResult;
			vaRDtl.setAvgDailyRate(result.getAvgDailyRate());
			vaRDtl.setVolatility(result.getVolatility());
		}
		return vaRDtl;
	}

}
