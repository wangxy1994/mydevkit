package com.wangxy.exoskeleton.api.tushare;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.wangxy.exoskeleton.api.tushare.vo.Data;
import com.wangxy.exoskeleton.api.tushare.vo.StockBasic;
import com.wangxy.exoskeleton.api.tushare.vo.TushareApiResponseData;

@Component
public class TushareApi{
	private static Logger logger = LoggerFactory.getLogger(TushareApi.class);
	@Autowired
	RestTemplate restTemplate;

	/**
	 * 股票基本信息
	 * @param is_hs
	 * @param list_status
	 * @param exchange
	 * @return
	 */
	public List<StockBasic> stock_basic(String is_hs,String list_status,String exchange) {
		System.out.println("获取股票基本信息");
		String api_name="stock_basic";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("is_hs", is_hs);
		map.put("list_status", list_status);
		map.put("exchange", exchange);
		JSONObject param = new JSONObject(map);
		String fields = "ts_code,symbol,name,area,industry,fullname,enname,market,exchange,curr_type,list_status,delist_date,is_hs";
		//		ResponseEntity<JslListResponseData> responseEntity = restTemplate.getForEntity(url, JslListResponseData.class, param);
		ResponseEntity<TushareApiResponseData> responseEntity = callBaseHttpApi(api_name, param, fields);
		Data data = responseEntity.getBody().getData();
		List<StockBasic> list = TushareApiUtil.getList(StockBasic.class, data);
		for (StockBasic stockBasic : list) {
			System.out.println(stockBasic);
		}
		return list;
	}
	
	/**
	 * 股票基本信息
	 * @param is_hs
	 * @param list_status
	 * @param exchange
	 * @return
	 */
	public ResponseEntity<TushareApiResponseData> callBaseHttpApi(String api_name,JSONObject params,String fields) {
		System.out.println("获取股票基本信息");
		String url = "http://api.waditu.com";
		Map<String,String> uriVariables = new HashMap<String, String>();
		HttpHeaders headers = new HttpHeaders();
	    //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//		headers.add("Host","www.jisilu.cn");
//		headers.add("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
	    
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("api_name", api_name);
	    String token = "ff7e7c79244b78baaeb61d11ce914e3ac36ba49210b5ea2bd709247d";
		map.put("token", token );
	    map.put("params", params);
	    map.put("fields", fields);
//	    map.add("is_hs", is_hs);
//	    map.add("list_status", list_status);
//	    map.add("exchange", exchange);
	    JSONObject allParam = new JSONObject(map);
		String paramsJsonString = allParam.toJSONString();
	 
	    HttpEntity<String> request = new HttpEntity<String>(paramsJsonString, headers);
	    logger.info("请求参数=========");
	    logger.info(paramsJsonString);
	    
	    
		long startTime = System.currentTimeMillis(); //获取开始时间
//		ResponseEntity<JslListResponseData> responseEntity = restTemplate.getForEntity(url, JslListResponseData.class, param);
		ResponseEntity<TushareApiResponseData> responseEntity = restTemplate.postForEntity(url, request, TushareApiResponseData.class, uriVariables);
		long endTime = System.currentTimeMillis(); //获取结束时间
		logger.info("服务响应时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
		HttpStatus statusCode = responseEntity.getStatusCode();
		if (HttpStatus.OK!=responseEntity.getStatusCode()) {
			logger.error(responseEntity.toString());
		}
		return responseEntity;
	}
	
	@RequestMapping("/adjust")
	public String adjust(@Param("path") String path) throws IOException {
		System.out.println("path==="+path);
		if (path==null||path.trim().length()==0) {
			path = "G:/同步dev/国际化/virtualcombiBefore/";
//			path = "I:/同步dev/国际化/virtualcombiBefore/";
		}
		
		long startTime = System.currentTimeMillis(); //获取开始时间
		long endTime = System.currentTimeMillis(); //获取结束时间
		logger.info("服务响应时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
		
		return "OK";
	}

}
