/********************************************
 * 文件名称: BulkComPriceUtil.java
 * 系统名称: 资金业务管理系统
 * 模块名称: var计算
 * 软件版权:  
 * 功能说明: 大宗商品价格工具类
 * 系统版本: 2.5.0.1
 * 开发人员: daijy
 * 开发时间: 
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 *           20181217 daijy    var计算样本不同币种日期不需要一致
 *           20190321 daijy    查询大宗商品计价信息表，获取默认计算的报价类型及期限
 *********************************************/
package com.wangxy.exoskeleton.risk.bulkcomvar;

import java.sql.SQLException;

import com.wangxy.exoskeleton.risk.exception.BizBussinessException;
import com.wangxy.exoskeleton.risk.exception.IErrMsg;
import com.wangxy.exoskeleton.risk.util.SpringContextUtil;
import com.wangxy.exoskeleton.risk.vo.BulkComPrice;
import com.wangxy.exoskeleton.risk.vo.VarCalParam;


public class BulkComPriceUtil {
	
	/**
	 * 获取大宗商品折算价格开始结束时间
	 * 假设商品价格存在的日期，汇率必然存在
	 * @param bulkComType
	 * @param targetCurr
	 * @param calDate
	 * @param num
	 * @return
	 * @throws BizBussinessException
	 */
	public static int[] getPeriod(String bulkComType, String targetCurr, int calDate, int num) throws BizBussinessException {
		//查询大宗商品品种
//		BulkComType typeBean = SpringContextUtil.getBean(IBulkComTypeService.class).getBulkComType(bulkComType);
//		if(typeBean!=null){
//			//20190321 daijy 查询大宗商品计价信息表，获取默认计算的报价类型及期限
//			BulkComValue bulkComValue = SpringContextUtil.getBean(IBulkComValueService.class).getVarBulkComValue(bulkComType, typeBean.getBulkComMarket());
//			if(bulkComValue==null){
//				throw new BizBussinessException(IErrMsg.ERR_LACK_OF_DATA,"找不到编号为"+bulkComType+"，报价来源为"+typeBean.getBulkComMarket()+"的大宗商品计价数据！");
//			}
//			BulkComPrice priceParam = new BulkComPrice();
//			priceParam.setBulkComType(bulkComType);
//			priceParam.setBulkComMarket(typeBean.getBulkComMarket());
//			priceParam.setPriceType(bulkComValue.getPriceType());
//			priceParam.setPriceLimit(bulkComValue.getPriceLimit());
//			return getPeriod(priceParam, calDate, num);
//		}else{
//			throw new BizBussinessException(IErrMsg.ERR_PARAMETER,"找不到编号为"+bulkComType+"的大宗商品品种数据！");
//		}
		
		VarCalParam priceParam = new VarCalParam();
//		priceParam.set
		return getPeriod(priceParam, calDate, num);
	}
	
	/**
	 * 获取大宗商品折算价格开始结束时间
	 * @param priceParam
	 * @param num
	 * @return
	 * @throws BizBussinessException
	 */
	private static int[] getPeriod(VarCalParam priceParam, int calDate, int num) throws BizBussinessException {
		
		//查询样本数量及开始、结束日期(只支持oracle)
//		String sql = " select count(1) as cnt,min(trans_date) as min,max(trans_date) as max"
//				   + " from (select * from tbbulkcomprice where bulk_com_type=? and bulk_com_market=? and price_type=? and price_limit=? and trans_date<=? order by trans_date desc) a where rownum<=?";
//		IDataset dataSet = null;
//		IDBSession session = DBSessionFactory.getSession();
//		try {
//			dataSet = session.getDataSet(sql, priceParam.getBulkComType(), priceParam.getBulkComMarket(), priceParam.getPriceType(), priceParam.getPriceLimit(), calDate, num);
//		} catch (SQLException e) {
//			throw new BizBussinessException(IErrMsg.ERR_DBSELECT, "获取汇率信息数据库错误");
//		}
//		//样本数量不足报错
//		if(dataSet.getInt("cnt")<num){
//			throw new BizBussinessException(IErrMsg.ERR_PARAMETER, "获取"+priceParam.getBulkComType()+"商品价格样本数量不足"+num);
//		}
		int[] period = new int[2];
//		period[0] = dataSet.getInt("min");
//		period[1] = dataSet.getInt("max");
		return period;
	}
	
	/**
	 * 获取大宗商品折算价格列表
	 * @param sourceCurr
	 * @param targetCurr
	 * @param startDate
	 * @param endDate
	 * @param num
	 * @return
	 * @throws BizBussinessException
	 */
	public static double[] getPrice(String bulkComType, String targetCurr, int calDate, int num) throws BizBussinessException {
//		
//		//查询大宗商品品种
//		BulkComType typeBean = SpringContextUtil.getBean(IBulkComTypeService.class, "bulkComTypeServiceImp").getBulkComType(bulkComType);
//		if(typeBean!=null){
//			//20190321 daijy 查询大宗商品计价信息表，获取默认计算的报价类型及期限
//			BulkComValue bulkComValue = SpringContextUtil.getBean(IBulkComValueService.class).getVarBulkComValue(bulkComType, typeBean.getBulkComMarket());
//			if(bulkComValue==null){
//				throw new BizBussinessException(IErrMsg.ERR_LACK_OF_DATA,"找不到编号为"+bulkComType+"，报价来源为"+typeBean.getBulkComMarket()+"的大宗商品计价数据！");
//			}
//			BulkComPrice priceParam = new BulkComPrice();
//			priceParam.setBulkComType(bulkComType);
//			priceParam.setBulkComMarket(typeBean.getBulkComMarket());
//			priceParam.setPriceType(bulkComValue.getPriceType());
//			priceParam.setPriceLimit(bulkComValue.getPriceLimit());
//			if(!targetCurr.equals(typeBean.getValueCurrency())){
//				//计价币种与折算币种不同，需要进行汇率折算
//				double[] currRate = CurrencyPriceUtil.getCurrencyRate(typeBean.getValueCurrency(), targetCurr, calDate, 1);
//				double[] price = getPrice(priceParam, calDate, num);
//				for(int i=0;i<num;i++){
//					price[i] = price[i] * currRate[0];
//				}
//				return price;
//			}else{
//				//计价币种与折算币种相同，直接取报价列表
//				return getPrice(priceParam, calDate, num);
//			}
//		}else{
//			throw new BizBussinessException(IErrMsg.ERR_PARAMETER,"找不到编号为"+bulkComType+"的大宗商品品种数据！");
//		}
		/**
		 * 原来这里有汇率折算
		 */
		BulkComPrice priceParam = new BulkComPrice();
		priceParam.setBulkComType(bulkComType);
		return getPrice(priceParam, calDate, num);
	}
	
	/**
	 * 获取大宗商品价格列表
	 * @param priceParam
	 * @param calDate
	 * @param num
	 * @return
	 * @throws BizBussinessException
	 */
	private static double[] getPrice(BulkComPrice priceParam, int calDate, int num) throws BizBussinessException {
		
		//查询汇率信息列表
		String sql = "select closing_price from tbbulkcomprice where bulk_com_type=? and bulk_com_market=? and price_type=? and price_limit=? and trans_date<=? order by trans_date desc";
//		IDataset dataSet = null;
//		IDBSession session = DBSessionFactory.getSession();
//		try {
//			dataSet = session.getDataSetForPage(sql, 0, num, priceParam.getBulkComType(), priceParam.getBulkComMarket(), priceParam.getPriceType(), priceParam.getPriceLimit(), calDate);
//		} catch (SQLException e) {
//			throw new BizBussinessException(IErrMsg.ERR_DBSELECT, "获取汇率信息数据库错误");
//		}
//		//样本数量不足报错
//		if(dataSet.getRowCount()<num){
//			throw new BizBussinessException(IErrMsg.ERR_PARAMETER, "获取"+priceParam.getBulkComType()+"商品价格样本数量不足"+num);
//		}
		
		int resNum = num;
		//获取日期最近的num条数据，日期从大到小
		//返回数据结果是日期从小到大的，要调整顺序
		
		double[] result = new double[resNum];
		//20190109 daijy 将数组按正序进行排列，先填写最后一个
//		int i = dataSet.getRowCount()-1;
//		dataSet.beforeFirst();
//		while(dataSet.hasNext()){
//			dataSet.next();
//			double price = dataSet.getDouble("closing_price");
//			//20190109 daijy 将数组按正序进行排列，先填写最后一个
//			result[i] = price;
//			i--;
//		}
		
		return result;
	}

}
