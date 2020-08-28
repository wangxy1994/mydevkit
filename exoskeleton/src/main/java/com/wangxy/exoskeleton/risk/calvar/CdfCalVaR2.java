/********************************************
 * 文件名称: CdfCalVaR2.java
 * 系统名称: 外汇资金风险管理
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 正态法var计算处理类2
 * 			 分别计算组合内个体var，然后根据相关系数矩阵计算组合var
 * 系统版本: 2.0.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *          20190314  daijy    明细结果增加头寸金额；金额按目标币种进行四舍五入
 *          20190424  daijy    四舍五入后再进行风险贡献计算
 *********************************************/
package com.wangxy.exoskeleton.risk.calvar;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wangxy.exoskeleton.risk.exception.BizBussinessException;
import com.wangxy.exoskeleton.risk.exception.IErrMsg;
import com.wangxy.exoskeleton.risk.util.BigDecimalUtil;
import com.wangxy.exoskeleton.risk.var.CdfVaRC;
import com.wangxy.exoskeleton.risk.var.CdfVaRC.CdfVaRCResult;
import com.wangxy.exoskeleton.risk.var.PortfolioCalUtil;
import com.wangxy.exoskeleton.risk.var.VaRParam;
import com.wangxy.exoskeleton.risk.var.VaRResult;
import com.wangxy.exoskeleton.risk.var.VaRResultCdf;
import com.wangxy.exoskeleton.risk.var.VaRResultDtl;
import com.wangxy.exoskeleton.risk.var.VaRResultDtlCdf;


@Service
public class CdfCalVaR2 extends AbstractCalVaR{

	@Override
	public VaRResult calVar(List<VaRParam> paramlist, double percentile, int simulatedtimes, int days) throws BizBussinessException {
		VaRResultCdf vaRResult = new VaRResultCdf();
		calVar(paramlist, percentile, simulatedtimes, days, vaRResult);
		return vaRResult;
	}
	
	/**
	 * 实际的var金额计算处理
	 * @param paramlist
	 * @param percentile
	 * @param simulatedtimes
	 * @param days
	 * @param vaRResult
	 * @return
	 * @throws BizBussinessException
	 */
	private CdfVaRCResult calVar(List<VaRParam> paramlist, double percentile, int simulatedtimes, int days,VaRResultCdf vaRResult) throws BizBussinessException {
		CdfVaRCResult result;
		try {
			// 分别计算组合内个体var，然后再计算组合var
			result = CdfVaRC.calVaR(paramlist, percentile, days);
		} catch (Exception e) {
			throw new BizBussinessException(IErrMsg.ERR_COMMERR,e);
		}
		if(vaRResult!=null){
			double totalValue = PortfolioCalUtil.getTotalValue(paramlist);
			vaRResult.setVar(result.getVarAmt()/totalValue);
			//20190314 daijy 获取金额小数位，并四舍五入
		    int decimalPoint = getDecimalPoint(paramlist);
			vaRResult.setVarAmt(BigDecimalUtil.round(result.getVarAmt(),decimalPoint));
			vaRResult.setCovMatrix(result.getCovMatrix());
			vaRResult.setRelMatrix(result.getRelMatrix());
			vaRResult.setWeight(result.getWeight());
		}
		return result;
	}
	
	@Override
	public VaRResult calVarAndDtl(List<VaRParam> paramlist, double percentile, int simulatedtimes, int days) throws BizBussinessException {
		try {
			//计算组合var金额
			VaRResultCdf vaRResult = new VaRResultCdf();
			CdfVaRCResult result = calVar(paramlist, percentile, simulatedtimes, days, vaRResult);
			//计算成分var、边际var、成分var贡献
			double[] cVaR = CdfVaRC.getCVaR(result);
			double[] mVaR = CdfVaRC.getMVaR(cVaR, PortfolioCalUtil.getWeight(paramlist));
			List<VaRResultDtl> varDetails = newVaRResultDtlList();
			double sumIndividualAmt = 0;
			//20190314 daijy 获取金额小数位
		    int decimalPoint = getDecimalPoint(paramlist);
			for(int i=0;i<paramlist.size();i++){
				//单资产var金额等设置
				VaRResultDtlCdf vaRDtl = new VaRResultDtlCdf();
				//20190314 daijy 增加头寸金额，并进行四舍五入
				double positionAmt = paramlist.get(i).getLastPrice()*paramlist.get(i).getAmount();
				vaRDtl.setPositionAmt(BigDecimalUtil.round(positionAmt,decimalPoint));
				vaRDtl.setVaR(result.getSingleVarAmt()[i]);
				sumIndividualAmt = BigDecimalUtil.add(sumIndividualAmt,Math.abs(vaRDtl.getVaR()));
				vaRDtl.setAvgDailyRate(result.getSingleAvgDailyRate()[i]);
				vaRDtl.setVolatility(result.getSingleVolatility()[i]);
				vaRDtl.setmVaR(BigDecimalUtil.round(mVaR[i],decimalPoint));
				vaRDtl.setcVaR(BigDecimalUtil.round(cVaR[i],decimalPoint));
				//20190424 daijy 四舍五入后再进行风险贡献计算
				vaRDtl.setcVaRContri(BigDecimalUtil.div(vaRDtl.getcVaR(), vaRResult.getVarAmt()));
				vaRDtl.setTitle(paramlist.get(i).getTitle());
				//计算增量var，去掉当前资产重新计算var后相减
				if(paramlist.size()==1){
					vaRDtl.setIncrVaR(result.getVarAmt());
				}else{
					List<VaRParam> removedParamlist = new ArrayList<VaRParam>(paramlist);
					removedParamlist.remove(i);
					VaRResult removedVar = calVar(removedParamlist, percentile, simulatedtimes, days);
					vaRDtl.setIncrVaR(BigDecimalUtil.sub(vaRResult.getVarAmt(),removedVar.getVarAmt()));
				}
				varDetails.add(vaRDtl);
			}
			vaRResult.setSumIndividualAmt(sumIndividualAmt); 
			//计算分散效应百分比
			vaRResult.setDiversification(1-vaRResult.getVarAmt()/vaRResult.getSumIndividualAmt());
			vaRResult.setVarDetails(varDetails);
			return vaRResult;
		} catch (Exception e) {
			throw new BizBussinessException(IErrMsg.ERR_COMMERR,e);
		}
	}

}
