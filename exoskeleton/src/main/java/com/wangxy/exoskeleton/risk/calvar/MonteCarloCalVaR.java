/********************************************
 * 文件名称: MonteCarloCalVaR.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 蒙特卡罗模拟法var计算处理类
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20190314 daijy    金额按目标币种进行四舍五入
 *           20190422 daijy    蒙特卡罗模拟法计算时直接同时计算var明细
 *********************************************/
package com.wangxy.exoskeleton.risk.calvar;

import java.util.List;

import org.springframework.stereotype.Service;

import com.avengers.base.constant.IErrMsg;
import com.avengers.base.util.BigDecimalUtil;
import com.avengers.framework.impl.bizkernel.runtime.exception.BizBussinessException;
import com.avengers.risk.var.HistoricalVaR;
import com.avengers.risk.var.MonteCarloVaR;
import com.avengers.risk.var.MonteCarloVaR.MonteCarloVarResult;
import com.avengers.risk.var.MonteCarloVaR.MonteCarloVarResultDtl;
import com.avengers.risk.var.VaRParam;
import com.avengers.risk.var.VaRResult;
import com.avengers.risk.var.VaRResultDtl;

@Service
public class MonteCarloCalVaR extends AbstractCalVaR{
	
//	@Override
//	public VaRResult calVarAndDtl(List<VaRParam> paramlist, double percentile, int simulatedtimes, int days) throws BizBussinessException {
//		//计算组合var金额
//		VaRResult varResult = calVar(paramlist, percentile, simulatedtimes, days);
//		return varResult;
//	}
	@Override
	public VaRResult calVarAndDtl(List<VaRParam> paramlist, double percentile, int simulatedtimes, int days) throws BizBussinessException {
		try {
			MonteCarloVaR.SimulatePrice(paramlist, simulatedtimes, 1); //仿真处理价格按照一天的价格进行仿真
			return super.calVarAndDtl(paramlist, percentile, simulatedtimes, days);  //qibb 20200110 调用父类的var 使用var和var明细计算
		} catch (Exception e) {
			throw new BizBussinessException(IErrMsg.ERR_COMMERR,e);
		}
	}
	
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
	
	protected double[] getMVaR(List<VaRParam> paramlist, double percentile, int days,VaRResult varResult,double[] portfolioVarResult) throws Exception {
		//使用历史法的mVaR计算
		return HistoricalVaR.getMVaR(paramlist, percentile, days);
	}
	
	
	public VaRResult calVar2(List<VaRParam> paramlist, double percentile, int simulatedtimes, int days) throws BizBussinessException {
		MonteCarloVarResult result;
		try {
			result = MonteCarloVaR.getVaR(paramlist, percentile, simulatedtimes, days, true);
		} catch (Exception e) {
			throw new BizBussinessException(IErrMsg.ERR_COMMERR,e);
		}
		VaRResult vaRResult = new VaRResult();
		vaRResult.setVar(result.getVar());
		//20190314 daijy 获取金额小数位，并四舍五入
	    int decimalPoint = getDecimalPoint(paramlist);
		vaRResult.setVarAmt(BigDecimalUtil.round(result.getVarAmt(),decimalPoint));
		//20190422 daijy 记录var明细
		List<VaRResultDtl> varDetails = newVaRResultDtlList();
		int i = 0;
		double sumIndividualAmt = 0;
		for(MonteCarloVarResultDtl dtl : result.getDetailList()){
			VaRResultDtl vaRDtl = new VaRResultDtl();
			vaRDtl.setTitle(paramlist.get(i).getTitle());
			//四舍五入处理
			vaRDtl.setPositionAmt(BigDecimalUtil.round(dtl.getPositionAmt(),decimalPoint));
			vaRDtl.setVaR(BigDecimalUtil.round(dtl.getVaR(),decimalPoint));
			vaRDtl.setmVaR(BigDecimalUtil.round(dtl.getmVaR(),decimalPoint));
			vaRDtl.setcVaR(BigDecimalUtil.round(dtl.getcVaR(),decimalPoint));
			//四舍五入后再进行风险贡献计算
			vaRDtl.setcVaRContri(BigDecimalUtil.div(vaRDtl.getcVaR(), vaRResult.getVarAmt()));
			varDetails.add(vaRDtl);
			sumIndividualAmt = BigDecimalUtil.add(sumIndividualAmt,Math.abs(vaRDtl.getVaR()));
			i++;
		}
		vaRResult.setVarDetails(varDetails);
		vaRResult.setSumIndividualAmt(sumIndividualAmt);
		//计算分散效应百分比
		vaRResult.setDiversification(1-vaRResult.getVarAmt()/vaRResult.getSumIndividualAmt());
		return vaRResult;
	} 

}
