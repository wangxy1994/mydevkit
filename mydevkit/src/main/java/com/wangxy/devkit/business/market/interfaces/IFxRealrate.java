package com.wangxy.devkit.business.market.interfaces;

import org.apache.kafka.clients.consumer.ConsumerRecords;

/********************************************
 * 文件名称: IReutersFxSwapPriceRealtime.java
 * 系统名称: 信贷综合业务管理系统
 * 模块名称: 外汇实时行情业务层接口
 * 软件版权:
 * 功能说明:
 * 系统版本:
 * 开发人员: wangxy
 * 开发时间: 2018-5-14
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期 修改人员 修改说明
 *********************************************/
public interface IFxRealrate {

	/**
	 * 新增外汇实时行情
	 * @param fxPriceRealTimeRecords
	 */
	public void impFxRealrate(ConsumerRecords<String, String> fxPriceRealTimeRecords);

	void impFxRealrate4Test(ConsumerRecords<String, String> fxPriceRealTimeRecords);

	
}
