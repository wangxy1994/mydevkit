package com.wangxy.exoskeleton.controller;

import java.io.IOException;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wangxy.exoskeleton.service.IDealI18NService;

@RestController
@RequestMapping("/cave")
public class CaveController{
	private static Logger logger = LoggerFactory.getLogger(CaveController.class);
	@Autowired
	IDealI18NService dealI18NService;

	@RequestMapping("/entrance")
	public String entrance(@Param("path") String path) throws IOException {
		System.out.println("path==="+path);
		if (path==null||path.trim().length()==0) {
			path = "G:/同步dev/国际化/virtualcombiBefore/";
//			path = "I:/同步dev/国际化/virtualcombiBefore/";
		}
		
		long startTime = System.currentTimeMillis(); //获取开始时间
		dealI18NService.autoDealI18N(path);
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
		dealI18NService.jsCodeReplace(path);
		long endTime = System.currentTimeMillis(); //获取结束时间
		logger.info("服务响应时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
		
		return "OK";
	}

}
