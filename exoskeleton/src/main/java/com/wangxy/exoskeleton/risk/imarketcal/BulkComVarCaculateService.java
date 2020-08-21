package com.wangxy.exoskeleton.risk.imarketcal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.avengers.base.business.ServiceFactory;
import com.avengers.base.common.ContextUtil;
import com.avengers.base.console.BaseService;
import com.avengers.base.constant.IErrMsg;
import com.avengers.base.util.BeanUtil;
import com.avengers.base.util.DataUtil;
import com.avengers.base.util.ValidField;
import com.avengers.base.util.ValidateResult;
import com.avengers.base.util.ValidateUtils;
import com.avengers.derivative.bulkvar.BulkComVarParam;
import com.avengers.derivative.business.bulkcomtype.interfaces.IBulkComTypeService;
import com.avengers.derivative.domain.bean.BulkComType;
import com.avengers.framework.bizframe.core.framework.util.SysParameterUtil;
import com.avengers.framework.common.share.dataset.DatasetService;
import com.avengers.framework.impl.bizkernel.runtime.exception.BizBussinessException;
import com.avengers.framework.interfaces.bizkernel.runtime.core.IContext;
import com.avengers.framework.interfaces.share.dataset.DatasetColumnType;
import com.avengers.framework.interfaces.share.dataset.IDataset;
import com.avengers.risk.var.VaRResult;
import com.wangxy.exoskeleton.risk.bulkcomvar.interfaces.IBulkComVar;

/********************************************
* 文件名称: BulkComVarCaculateService.java
* 系统名称: 资金交易风险管理系统
* 模块名称: 大宗商品Var分析计算接口
* 软件版权:  
* 功能说明: 
* 系统版本: 
* 开发人员: wangxy  
* 开发时间: 2019-02-25
* 审核人员:
* 相关文档:
* 修改记录: 修改日期    修改人员    修改说明
*********************************************/
public class BulkComVarCaculateService{
	
	private void tempCal(String bulkComTypeTemp) {
		// 执行业务处理
		IBulkComVar bulkComVar = ServiceFactory.getBean(IBulkComVar.class, "bulkComVar");
		int tradeCalDate = request.getInt("calDate");// 计算日期
		String varType = request.getString("calMethod");// var计算方式
		String targetCurr = request.getString("targetCurr");// 折算币种
		int num = request.getInt("num");// 样本数
		double percentile = request.getDouble("percentile");// 置信度
		int days = request.getInt("days");// 持有天数
		List<BulkComVarParam> BulkComParams = packageToBulkComVar(request, targetCurr);
		if (BulkComParams.size() == 0) {
			throw new BizBussinessException(IErrMsg.ERR_COMMERR, "请求的大宗商品的头寸数量都为0");
		}
		VaRResult varResult = bulkComVar.cal(varType, BulkComParams, tradeCalDate, num, percentile, Integer.parseInt(SysParameterUtil.getProperty("varDefSim", "0")), days);
		if (varResult != null) {
			IDataset[] result = varResult.packgeToDataset();
			IDataset varDs = result[0];
			// 将大宗商品头寸存入Var分析结果
			varDs.addColumn("baseCurr", DatasetColumnType.DS_STRING);
			varDs.addColumn("bulkComCnt", DatasetColumnType.DS_DOUBLE);
			for (BulkComVarParam param : BulkComParams) {
				String bulkComType = param.getBulkComType();
				varDs.beforeFirst();
				while (varDs.hasNext()) {
					varDs.next();
					varDs.updateString("baseCurr", param.getTargetCurr());
					if (varDs.getString("title").equals(bulkComType)) {
						varDs.updateDouble("bulkComCnt", param.getSrcCnt()); // 使用原始金额返报文
						if (param.getSrcCnt() >= 0) {
							varDs.updateDouble("positionAmt", varDs.getDouble("positionAmt"));// qibb 20200311 修改为原始头寸金额
						} else {
							varDs.updateDouble("positionAmt", -varDs.getDouble("positionAmt"));// qibb 20200311 修改为原始头寸金额
						}
					}
				}
			}

			// 相关系数矩阵
			if (result.length == 2) {
				context.setResult("result0", getResultMatrix(result[1]));
			}
			// Var分析结果
			context.setResult("result1", getVarResultDs(varDs));
		}
	}
		

	/**
	 * 大宗商品Var分析计算
	 * 
	 * @param context
	 * @throws BizBussinessException
	 */
	private void cal(IContext context) throws BizBussinessException {
		IDataset request = context.getRequestDataset();
		validateParam(request);// 参数校验
		otherValidate(request);
		
		request.beforeFirst();
		request.next();
		// 执行业务处理
		IBulkComVar bulkComVar = ServiceFactory.getBean(IBulkComVar.class, "bulkComVar");
		int tradeCalDate = request.getInt("calDate");// 计算日期
		String varType = request.getString("calMethod");// var计算方式
		String targetCurr = request.getString("targetCurr");// 折算币种
		int num = request.getInt("num");// 样本数
		double percentile = request.getDouble("percentile");// 置信度
		int days = request.getInt("days");// 持有天数
		List<BulkComVarParam> BulkComParams = packageToBulkComVar(request, targetCurr);
		if (BulkComParams.size()==0) {
			throw new BizBussinessException(IErrMsg.ERR_COMMERR,"请求的大宗商品的头寸数量都为0");
		}
		VaRResult varResult = bulkComVar.cal(varType, BulkComParams, tradeCalDate, num,
				percentile, Integer.parseInt(SysParameterUtil.getProperty("varDefSim", "0")), days);
		if (varResult != null) {
			IDataset[] result = varResult.packgeToDataset();
			IDataset varDs = result[0];
			// 将大宗商品头寸存入Var分析结果
			varDs.addColumn("baseCurr", DatasetColumnType.DS_STRING);
			varDs.addColumn("bulkComCnt", DatasetColumnType.DS_DOUBLE);
			for (BulkComVarParam param : BulkComParams) {
				String bulkComType = param.getBulkComType();
				varDs.beforeFirst();
				while (varDs.hasNext()) {
					varDs.next();
					varDs.updateString("baseCurr", param.getTargetCurr());
					if (varDs.getString("title").equals(bulkComType)) {
						varDs.updateDouble("bulkComCnt", param.getSrcCnt()); //使用原始金额返报文
						if(param.getSrcCnt()>=0){
							varDs.updateDouble("positionAmt", varDs.getDouble("positionAmt"));// qibb 20200311 修改为原始头寸金额
						}else{
							varDs.updateDouble("positionAmt", -varDs.getDouble("positionAmt"));// qibb 20200311 修改为原始头寸金额
						}
					}
				}
			}

			// 相关系数矩阵
			if (result.length == 2) {
				context.setResult("result0", getResultMatrix(result[1]));
			}
			// Var分析结果
			context.setResult("result1", getVarResultDs(varDs));
		} else {
			throw new BizBussinessException(IErrMsg.ERR_COMMERR, "大宗商品Var分析计算失败");
		}

	}

	/**
	 * 其他校验
	 * 
	 * @param request
	 * @throws BizBussinessException
	 */
	private void otherValidate(IDataset request) throws BizBussinessException {
		String bulkComType = request.getString("bulkComType");// 大宗商品类型
		String bulComCnt = request.getString("cnt");// 商品头寸数量
		if (DataUtil.isNullStr(bulkComType) || DataUtil.isNullStr(bulComCnt)) {
			throw new BizBussinessException(IErrMsg.ERR_COMMERR, "大宗商品类型或商品头寸数量不能为空");
		}
		// 样本数必须小于等于500。样本数是数字在前面已经校验过
		int num = request.getInt("num");// 样本数量
		if (num > 500) {
			throw new BizBussinessException(IErrMsg.ERR_COMMERR, "样本数必须小于等于500");
		}
		HashSet<String> hs = new HashSet<String>();
		request.beforeFirst();
		while (request.hasNext()) {
			request.next();
			// 大宗商品类型与对应金额校验。
			String bulkComTypeTemp = request.getString("bulkComType");// 大宗商品类型
			String bulComCntTemp = request.getString("cnt");// 商品头寸数量
			if (DataUtil.isNullStr(bulkComTypeTemp) || DataUtil.isNullStr(bulComCntTemp)) {
				throw new BizBussinessException(IErrMsg.ERR_COMMERR, "大宗商品类型与商品头寸数量数量不对应");
			}

			if (!isBulkComTypeExist(bulkComTypeTemp)) {
				throw new BizBussinessException(IErrMsg.ERR_COMMERR, "大宗商品类型不存在:"+bulkComTypeTemp);
			}
			try {
				Double.valueOf(bulComCntTemp);
			} catch (NumberFormatException e) {
				throw new BizBussinessException(IErrMsg.ERR_COMMERR, "商品头寸数量非数字:"+bulComCntTemp);
			}

			// 大宗商品类型不允许重复
			if (!hs.add(request.getString("bulkComType"))) {
				throw new BizBussinessException(IErrMsg.ERR_COMMERR, "大宗商品类型不允许重复");
			}
		}
	}

	/**
	 * 校验大宗商品品种类型是否存在
	 * @param bulkComTypeTemp
	 * @return
	 * @throws BizBussinessException
	 */
	private boolean isBulkComTypeExist(String bulkComTypeTemp) throws BizBussinessException {
		IBulkComTypeService bulkComTypeService = ServiceFactory.getBean(IBulkComTypeService.class,"bulkComTypeServiceImp");
		BulkComType bulkComType = bulkComTypeService.getBulkComType(bulkComTypeTemp);
		if (bulkComType!=null) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 复制var分析结果集，封装为需要的结果集
	 * 
	 * @param varDs
	 * @return
	 */
	private IDataset getVarResultDs(IDataset varDs) {
		IDataset varRestultDs = DatasetService.getDefaultInstance().getDataset();
		varRestultDs.addColumn("portfolioVarAmt", DatasetColumnType.DS_DOUBLE);
		varRestultDs.addColumn("sumIndividualAmt", DatasetColumnType.DS_DOUBLE);
		varRestultDs.addColumn("diversification", DatasetColumnType.DS_DOUBLE);
		varRestultDs.addColumn("beginDate", DatasetColumnType.DS_INT);
		varRestultDs.addColumn("endDate", DatasetColumnType.DS_INT);
		varRestultDs.addColumn("title", DatasetColumnType.DS_STRING);
		varRestultDs.addColumn("vaR", DatasetColumnType.DS_DOUBLE);
		varRestultDs.addColumn("mVaR", DatasetColumnType.DS_DOUBLE);
		varRestultDs.addColumn("cVaR", DatasetColumnType.DS_DOUBLE);
		varRestultDs.addColumn("cVaRContri", DatasetColumnType.DS_DOUBLE);
		varRestultDs.addColumn("incrVaR", DatasetColumnType.DS_DOUBLE);
		varRestultDs.addColumn("volatility", DatasetColumnType.DS_DOUBLE);
		varRestultDs.addColumn("avgDailyRate", DatasetColumnType.DS_DOUBLE);
		varRestultDs.addColumn("positionAmt", DatasetColumnType.DS_DOUBLE);
		varRestultDs.addColumn("baseCurr", DatasetColumnType.DS_STRING);
		varRestultDs.addColumn("bulkComCnt", DatasetColumnType.DS_DOUBLE);
		varRestultDs.beforeFirst();

		varDs.beforeFirst();
		while (varDs.hasNext()) {
			varDs.next();

			varRestultDs.appendRow();
			varRestultDs.updateDouble("portfolioVarAmt", varDs.getDouble("varAmt"));
			varRestultDs.updateDouble("sumIndividualAmt", varDs.getDouble("sumIndividualAmt"));
			varRestultDs.updateDouble("diversification", varDs.getDouble("diversification"));
			varRestultDs.updateInt("beginDate", varDs.getInt("beginDate"));
			varRestultDs.updateInt("endDate", varDs.getInt("endDate"));
			varRestultDs.updateString("title", varDs.getString("title"));
			varRestultDs.updateDouble("vaR", varDs.getDouble("vaR"));
			varRestultDs.updateDouble("mVaR", varDs.getDouble("mVaR"));
			varRestultDs.updateDouble("cVaR", varDs.getDouble("cVaR"));
			varRestultDs.updateDouble("cVaRContri", varDs.getDouble("cVaRContri"));
			varRestultDs.updateDouble("incrVaR", varDs.getDouble("incrVaR"));
			varRestultDs.updateDouble("volatility", varDs.getDouble("volatility"));
			varRestultDs.updateDouble("avgDailyRate", varDs.getDouble("avgDailyRate")*252);
			varRestultDs.updateDouble("positionAmt", varDs.getDouble("positionAmt"));
			varRestultDs.updateString("baseCurr", varDs.getString("baseCurr"));
			varRestultDs.updateDouble("bulkComCnt", varDs.getDouble("bulkComCnt"));
			
			varRestultDs.updateDouble("volatility", varRestultDs.getDouble("volatility")*Math.sqrt(252));
		}
		return varRestultDs;
	}

	/**
	 * 更改关系矩阵中系数数据类型为double
	 * 
	 * @param matRixDs
	 * @return
	 */
	private IDataset getResultMatrix(IDataset matRixDs) {
		int count = matRixDs.getColumnCount();
		for (int i = 1; i <= count; i++) {
			if (!"colum".equals(matRixDs.getColumnName(i))) {
				matRixDs.modifyColumnType(i, DatasetColumnType.DS_DOUBLE);
			}
		}
		return matRixDs;

	}

	/**
	 * 参数校验
	 * 
	 * @param request
	 * @throws BizBussinessException
	 */
	private void validateParam(IDataset request) throws BizBussinessException {
		Map<String, String> params = new HashMap<String, String>();
		try {
			BeanUtil.dataset2Map(params, request);
		} catch (Exception e) {
			loger.error(IErrMsg.ERR_PACKAGE, e);
			throw new BizBussinessException(IErrMsg.ERR_PACKAGE, "解析接口参数出错！");
		}
		ValidField[] fields = { new ValidField("calMethod", "计算方式", true, new String[] { "required:true", "isDict:K_VTYPE" }),
				new ValidField("targetCurr", "折算币种", true, new String[] { "required:true", "isDict:K_BZ" }),
				new ValidField("calDate", "计算日期", true, new String[] { "required:true", "isDate8:true" }),
				new ValidField("num", "样本数", true, new String[] { "required:true", "isInt:true" }),
				new ValidField("percentile", "置信度", true, new String[] { "required:true", "isDouble:true" }),
				new ValidField("days", "持有天数", true, new String[] { "required:true", "isInt:true" }) };
		ValidateResult sk = ValidateUtils.validate(fields, params);
		if (!sk.isResult()) {
			throw new BizBussinessException(IErrMsg.ERR_PACKAGE, sk.getRespMsg());
		}
	}

	/**
	 * 包装Var计算参数
	 * 包括大宗商品类型，折算币种，大宗商品数量
	 * @param request
	 * @return
	 */
	private List<BulkComVarParam> packageToBulkComVar(IDataset request, String targetCurr) {

		List<BulkComVarParam> inParam = new ArrayList<BulkComVarParam>();
		request.beforeFirst();
		while (request.hasNext()) {
			request.next();
			//20191211	wangxy	过滤头寸为0的数据。头寸为0无法计算var
			if (request.getDouble("cnt")!=0) {
				BulkComVarParam bulkVar = new BulkComVarParam();
				bulkVar.setBulkComType(request.getString("bulkComType"));
				bulkVar.setTargetCurr(targetCurr);
				bulkVar.setCnt(Math.abs(request.getDouble("cnt")));
				bulkVar.setSrcCnt(request.getDouble("cnt"));// qibb 20200311 保留原始敞口
				inParam.add(bulkVar);
			}
		}
		return inParam;
	}

}
