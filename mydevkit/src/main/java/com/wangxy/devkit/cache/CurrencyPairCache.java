package com.wangxy.devkit.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wangxy.devkit.exception.CommonBusinessException;
import com.wangxy.devkit.exception.IErrMsg;
import com.wangxy.devkit.util.DataUtil;
import com.wangxy.devkit.util.SpringContextUtil;
/********************************************
 * 文件名称: CurrencyPairCache.java
 * 系统名称: 资金交易风险管理系统
 * 模块名称:
 * 软件版权:  
 * 功能说明: 货币对表信息内存管理类
 * 系统版本: 3.0.0.1
 * 开发人员: qianxm
 * 开发时间: 2013-02-26 下午02:09:19
 * 审核人员:
 * 相关文档:
 * 修改记录: 修改日期    修改人员    修改说明
 * 			20140212 qianxm 修改异常提示
 * 			20140409 huangqun 如果获取不到相应货币对信息，抛出异常提示
 * 			20141226  chenxz 修改返回货币对-从缓存里面取，不通过查询
 *          20190528 daijy 控制货币对不能为同一币种
 *********************************************/
public class CurrencyPairCache {
 
    public static final String VERSION="@system 资金交易风险管理系统V2.0  @version 2.5.0.1  @lastModiDate  2014-07-09  @describe ";
	/**
     * CurrencyPair Map缓存
     */
	private List<CurrencyPair> currencyPairList=new ArrayList<CurrencyPair>();
	
	/**
	 * 交易缓存
	 */
	private  static  CurrencyPairCache cacheObj=null; 
    
	/**
     * 私有构造
     */
	private CurrencyPairCache(){
           init();      	
    }
	
	/**
	 * 初始化,从数据中装载到内存
	 */
	private void init(){
		JdbcTemplate jt = (JdbcTemplate) SpringContextUtil.getBean("jdbcTemplate");
		List<CurrencyPair> list = jt.query("select * from tbcurrencypair", new BeanPropertyRowMapper<CurrencyPair>(CurrencyPair.class));
    	for(CurrencyPair currencyPairObj : list){
	    	currencyPairList.add(currencyPairObj);
    	}
	}
	
	private void clear(){
		this.currencyPairList.clear();
	}
	/**
	 * 返回CurrencyPairCache实例，采用启动的时候加载实例，故不需要锁
	 * @return
	 */
	public static CurrencyPairCache getInstance(){
		if(cacheObj==null){
			cacheObj=new CurrencyPairCache();
		}
	   return cacheObj;	
	}
	
	/**
	 * 销毁对象
	 */
	public static void destroy(){
		if(cacheObj!=null){
			cacheObj.clear();
		}
		cacheObj=null;
	}
	/**
	 * 按交易码获取指定交易实例(改成与businClass无关)
	 * @param transCode
	 * @return
	 */
	public CurrencyPair getCurrencyPair(String currencyPair, boolean throwException) {
		if (currencyPairList == null && throwException) {
			throw new CommonBusinessException(IErrMsg.ERR_NOTRANS,"货币对【"+currencyPair+"】未配置。");
		}	
		if(currencyPairList!=null){
			for(CurrencyPair cp:currencyPairList){
				if(currencyPair.equals(cp.getCurrencyPair())){
					return cp;
				}
			}
		}
		if(throwException){
			throw new CommonBusinessException(IErrMsg.ERR_NOTRANS,"货币对【"+currencyPair+"】未配置。");
		}
		return null;
	}
	
	/**
	 * 根据币种获取相应的货币对
	 * @param cur1
	 * @param cur2
	 * @return
	 * @throws CommonBusinessException
	 */
	public String getCurrencyPair(String cur1,String cur2) throws CommonBusinessException {
		String currPair = null;
		//20190528 daijy 控制货币对不能为同一币种
		if(cur1!=null && cur1.equals(cur2)){
			throw new CommonBusinessException(IErrMsg.ERR_PARAMETER,"货币对不能为同一币种");
		}
		for(int i=0;i<currencyPairList.size();i++){
			currPair = currencyPairList.get(i).getCurrencyPair();
			if(currPair.indexOf(cur1)!=-1 && currPair.indexOf(cur2)!=-1){
				return currPair;
			}
		}
		return null;
	}
	/**
	 * 获取完整的货币对
	 * @param cur1
	 * @param cur2
	 * @return
	 * @throws CommonBusinessException
	 */
	public String getWholeCurrencyPair(String cur1,String cur2) throws CommonBusinessException{
		String currencyPair = getCurrencyPair(cur1,cur2);
		Map<String,CurrencyPair>  map=getCurrencyMap();
		//2014-04-09  huangqun 如果获取不到相应货币对信息，抛出异常提示
		if (DataUtil.isNullStr(currencyPair)) {
			throw new CommonBusinessException(IErrMsg.ERR_DBSELECT, "找不到币种" + cur1 + "和" + cur2 + "的货币对信息，请在基础参数下币种设置界面进行维护");
		}
		//chenxz 20141226 修改返回货币对-从缓存里面取，不通过查询
		CurrencyPair cp=map.get(currencyPair);
		if(cp!=null){
			return currencyPair;
		}else{
			//20140212 qianxm 修改异常提示
			throw new CommonBusinessException(IErrMsg.ERR_DBSELECT,"找不到货币对【"+currencyPair+"】！");
		}
	}
	/**
	 * 返回货币对对应货币的对象列表
	 * @return
	 */
	private Map<String,CurrencyPair> getCurrencyMap(){
		Map<String,CurrencyPair>  map=new HashMap<String, CurrencyPair>();
		if(currencyPairList!=null){
			for(CurrencyPair cp:currencyPairList){
				map.put(cp.getCurrencyPair(), cp);
			}
		}
		return map;
		
	}
	/**
	 * 返回货币对信息列表
	 * @return
	 */
	public List<CurrencyPair>getCurrencyList(){
		return currencyPairList;
	}
	
	/**
	 * 刷新内存
	 */
	public void refresh(){
		synchronized(this.currencyPairList){
			currencyPairList.clear();
			init();
		}
	}
	
}
