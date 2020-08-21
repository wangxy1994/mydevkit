package com.wangxy.exoskeleton.risk.bulkcomvar;


/********************************************
 * 文件名称: ServiceFactory.java<br>
 * 系统名称: 资金风险管理系统<br>
 * 模块名称: <br>
 * 软件版权:  <br>
 * 功能说明: <br>
 * 系统版本: 2.0.0.4<br>
 * 开发人员: stone<br>
 * 开发时间: 2019-7-21 下午05:18:12<br>
 * 审核人员:<br>
 * 相关文档:<br>
 * 修改记录: 修改日期    修改人员    修改说明<br>
 *          20181102  daijy    增加根据接口获取bean的方法
 *********************************************/
public class ServiceFactory {

	/**
	 * 根据接口及实现类名称获取bean
	 * @param cls
	 * @param beanName
	 * @return
	 */
	public static <T> T getBean(Class<T> cls, String beanName) {
		return (T) ApplicationContext.getBean(beanName, cls);
	}
	
	/**
	 * 根据接口获取bean
	 * @param cls
	 * @author daijy
	 * @since 20181102
	 * @return
	 */
	public static <T> T getBean(Class<T> cls) {
		return (T) SpringContextFactory.getApplicationContext().getBean(cls);
	}
	
}
