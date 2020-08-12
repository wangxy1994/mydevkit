package com.wangxy.exoskeleton.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.wangxy.exoskeleton.vo.JslListResponseData;
import com.wangxy.exoskeleton.vo.Row;


@RestController
@RequestMapping("/data")
public class DataController{
	private static Logger logger = LoggerFactory.getLogger(DataController.class);
	@Autowired
	RestTemplate restTemplate;

	@RequestMapping("/preCBList")
	public String getPreCBList(@Param("progress") String progress,@Param("rp") String rp) {
		System.out.println("获取待发转债列表");
		String url = "https://www.jisilu.cn/data/cbnew/pre_list/";
		Map<String,String> uriVariables = new HashMap<String, String>();
		String LST___t = "LST___t="+System.currentTimeMillis();
		uriVariables.put("___jsl", LST___t);
		/**
		 * postman测试请求情况
		 * postman自动加的
		 * Cookie
		 * Cache-Control
		 * Postman-Token
		 * Host必须有否则400
		 * Content-Type，Content-Length必须有，否则返回的数据是过时的数据（就像没有传表单参数）
		 */
		HttpHeaders headers = new HttpHeaders();
	    //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Host","www.jisilu.cn");
		headers.add("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
//	    headers.setContentLength();
	    MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
	    map.add("progress", progress);
	    map.add("rp", rp);
	 
	    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		
		long startTime = System.currentTimeMillis(); //获取开始时间
//		ResponseEntity<JslListResponseData> responseEntity = restTemplate.getForEntity(url, JslListResponseData.class, param);
		ResponseEntity<JslListResponseData> responseEntity = restTemplate.postForEntity(url, request, JslListResponseData.class, uriVariables);
		List<Row> rows = responseEntity.getBody().getRows();
		for (Row row : rows) {
			
			System.out.println(row.getCell().toString());
		}
		long endTime = System.currentTimeMillis(); //获取结束时间
		logger.info("服务响应时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
		
		return "OK";
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
