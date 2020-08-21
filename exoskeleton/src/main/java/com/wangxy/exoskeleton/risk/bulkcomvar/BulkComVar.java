/********************************************
 * 文件名称: BulkComVar.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 大宗商品var计算实现类
 * 系统版本: 2.5.0.1
 * 开发人员: daij2.5.0.1发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20181217 daijy    var计算样本不同币种日期不需要一致
 *           20190314 daijy    varParam增加目标币种设置
 *********************************************/
package com.wangxy.exoskeleton.risk.bulkcomvar;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wangxy.exoskeleton.risk.bulkcomvar.interfaces.IBulkComVar;
import com.wangxy.exoskeleton.risk.calvar.CalVaRFactory;
import com.wangxy.exoskeleton.risk.calvar.ICalVaR;
import com.wangxy.exoskeleton.risk.exception.BizBussinessException;
import com.wangxy.exoskeleton.risk.imarketcal.VarCalController;
import com.wangxy.exoskeleton.risk.var.VaRParam;
import com.wangxy.exoskeleton.risk.var.VaRResult;
import com.wangxy.exoskeleton.risk.vo.VarCalParamDetail;

@Service
public class BulkComVar implements IBulkComVar{
	private static Logger logger = LoggerFactory.getLogger(VarCalController.class);
	/**
	 * 大宗商品var计算
	 * @param calMethod      计算方法
	 * @param bulkComParams  大宗商品var计算参数
	 * @param calDate        计算日期
	 * @param num            样本数
	 * @param percentile     置信度
	 * @param simulatedtimes 模拟次数
	 * @param days           持有天数
	 * @return
	 * @throws Exception 
	 */
	public VaRResult cal(String calMethod, List<VarCalParamDetail> varCalParamDetail, int calDate, int num, double percentile,int simulatedtimes,int days) throws Exception{
		if(varCalParamDetail!=null && varCalParamDetail.get(0)!=null){
			List<VaRParam> paramlist = new ArrayList<VaRParam>();
			VarCalParamDetail effectBulkComVarParam = null;
			//获取大宗商品var值计算价格样本
			for(VarCalParamDetail detail : varCalParamDetail){
				double[] priceArray;
				try {
					priceArray = BulkComPriceUtil.getPrice(detail.getAssetId(), detail.getTargetCurr(), calDate, num);
				}  catch (BizBussinessException e) {
					//如果当前币种行情数据不足就不参与计算
					if ("ERR_LACK_OF_DATA".equals(e.getCode())) {
//						logger.error(detail.getAssetId()+"不参与计算",e);
						continue;
					}else {
						throw e;
					}
				}
				VaRParam varParam = new VaRParam(detail.getCnt(),priceArray);
				//TODO
//				varParam.setTitle(detail.getAssetId());
				//20190314 daijy 目标币种设置
				varParam.setTargetCurr(detail.getTargetCurr());
				paramlist.add(varParam);
				effectBulkComVarParam = detail;
			}
			if (paramlist.size()==0) {
				throw new Exception("请求的大宗商品的价格样本数量都不足");
			}
			ICalVaR calVar = CalVaRFactory.get(calMethod);
			VaRResult varResult=calVar.calVarAndDtl(paramlist, percentile, simulatedtimes, days);
			//20191213	wangxy 第一个参数可能被过滤掉了，所以应该取一个有效的参数
			int[] period = BulkComPriceUtil.getPeriod(effectBulkComVarParam.getAssetId(), effectBulkComVarParam.getTargetCurr(), calDate, num);
			varResult.setBeginDate(period[0]);
			varResult.setEndDate(period[1]);
			return varResult;
		}
		return null;
	}

}
