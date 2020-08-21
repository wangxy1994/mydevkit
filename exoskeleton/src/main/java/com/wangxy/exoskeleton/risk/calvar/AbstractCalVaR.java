/********************************************
 * 文件名称: AbstractCalVaR.java
 * 系统名称: 外汇资金风险管理
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: var计算抽象类
 * 系统版本: 2.0.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20190125 daijy    增加getMVaR方法
 *           20190314 daijy    明细结果增加头寸金额；金额按目标币种进行四舍五入
 *           20190424 daijy    四舍五入后再进行风险贡献计算
 *********************************************/
package com.wangxy.exoskeleton.risk.calvar;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.avengers.base.business.ServiceFactory;
import com.avengers.base.constant.IErrMsg;
import com.avengers.base.util.BigDecimalUtil;
import com.avengers.framework.impl.bizkernel.runtime.exception.BizBussinessException;
import com.avengers.pub.business.currency.interfaces.ICurrencyService;
import com.avengers.pub.domain.bean.Currency;
import com.avengers.risk.var.CdfVaR;
import com.avengers.risk.var.PortfolioCalUtil;
import com.avengers.risk.var.VaRParam;
import com.avengers.risk.var.VaRResult;
import com.avengers.risk.var.VaRResultDtl;

public abstract class AbstractCalVaR implements ICalVaR{
	
	public abstract VaRResult calVar(List<VaRParam> paramlist, double percentile, int simulatedtimes, int days) throws BizBussinessException;
	
	@Override
	public VaRResult calVarAndDtl(List<VaRParam> paramlist, double percentile, int simulatedtimes, int days) throws BizBussinessException {
		try {
			//计算组合var金额
			VaRResult varResult = calVar(paramlist, percentile, simulatedtimes, days);
			//计算边际var、成分var、成分var贡献
			double[] mVaR = getMVaR(paramlist, percentile, days,varResult);
			double[] cVaR = CdfVaR.getCVaR(mVaR, PortfolioCalUtil.getWeight(paramlist));
			List<VaRResultDtl> varDetails = newVaRResultDtlList();
			double sumIndividualAmt = 0;
			//20190314 daijy 获取金额小数位
		    int decimalPoint = getDecimalPoint(paramlist);
			for(int i=0;i<paramlist.size();i++){
				//单资产var金额计算
				List<VaRParam> singleParam = new ArrayList<VaRParam>();
				singleParam.add(paramlist.get(i));
				VaRResult singleVar = calVar(singleParam, percentile, simulatedtimes, days);
				VaRResultDtl vaRDtl = packageToDtl(singleVar);
				sumIndividualAmt = BigDecimalUtil.add(sumIndividualAmt, Math.abs(singleVar.getVarAmt()));
				//20190314 daijy 增加头寸金额，处理金额四舍五入
				double positionAmt = paramlist.get(i).getLastPrice()*paramlist.get(i).getAmount();
				vaRDtl.setPositionAmt(BigDecimalUtil.round(positionAmt,decimalPoint));
				vaRDtl.setmVaR(BigDecimalUtil.round(mVaR[i],decimalPoint));
				vaRDtl.setcVaR(BigDecimalUtil.round(cVaR[i],decimalPoint));
				//20190424 daijy 四舍五入后再进行风险贡献计算
				vaRDtl.setcVaRContri(BigDecimalUtil.div(vaRDtl.getcVaR(), varResult.getVarAmt()));
				vaRDtl.setTitle(paramlist.get(i).getTitle());
				//计算增量var，去掉当前资产重新计算var后相减
				if(paramlist.size()==1){
					vaRDtl.setIncrVaR(varResult.getVarAmt());
				}else{
					List<VaRParam> removedParamlist = new ArrayList<VaRParam>(paramlist);
					removedParamlist.remove(i);
					VaRResult removedVar = calVar(removedParamlist, percentile, simulatedtimes, days);
					vaRDtl.setIncrVaR(BigDecimalUtil.sub(varResult.getVarAmt(),removedVar.getVarAmt()));
				}
				varDetails.add(vaRDtl);
			}
			varResult.setSumIndividualAmt(sumIndividualAmt);
			//计算分散效应百分比
			varResult.setDiversification(1-Math.abs(varResult.getVarAmt())/varResult.getSumIndividualAmt());
			varResult.setVarDetails(varDetails);
			return varResult;
		} catch (Exception e) {
			throw new BizBussinessException(IErrMsg.ERR_COMMERR,e);
		}
	}

	protected double[] getMVaR(List<VaRParam> paramlist, double percentile, int days,VaRResult varResult) throws Exception {
		//默认使用正态法的mVaR计算
		return CdfVaR.getMVaR(paramlist, percentile, days);
	}
	
	protected double[] getMVaR(List<VaRParam> paramlist, double percentile, int days) throws Exception {
		//默认使用正态法的mVaR计算
		return CdfVaR.getMVaR(paramlist, percentile, days);
	}

	protected List<VaRResultDtl> newVaRResultDtlList(){
		return new ArrayList<VaRResultDtl>();
	}
	
	protected VaRResultDtl newVaRResultDtl(){
		return new VaRResultDtl();
	}
	
	protected VaRResultDtl packageToDtl(VaRResult vaRResult){
		VaRResultDtl vaRDtl = newVaRResultDtl();
		vaRDtl.setVaR(vaRResult.getVarAmt());
		return vaRDtl;
	}
	
	/**
	 * 获取VaR计算金额小数位
	 * @param paramlist
	 * @return
	 * @throws BizBussinessException
	 */
	protected int getDecimalPoint(List<VaRParam> paramlist) throws BizBussinessException {
		if(paramlist!=null && !paramlist.isEmpty() && paramlist.get(0)!=null && StringUtils.isNotBlank(paramlist.get(0).getTargetCurr())){
			String targetCurr = paramlist.get(0).getTargetCurr();
			Currency currency = ServiceFactory.getBean(ICurrencyService.class).getCurrency(targetCurr);
			if(currency==null){
				throw new BizBussinessException(IErrMsg.ERR_PARAMETER,"找不到"+targetCurr+"币种信息！");
			}
			return currency.getDecimalPoint();
		}
		//金额默认两位小数
		return 2;
	}

}
