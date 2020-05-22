package com.wangxy.exoskeleton.controller;

import java.io.IOException;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wangxy.exoskeleton.service.IDealI18NService;

@RestController
@RequestMapping("/cave")
public class CaveController{
	@Autowired
	IDealI18NService dealI18NService;

	@RequestMapping("/entrance")
	public String entrance(@Param("path") String path) throws IOException {
		System.out.println("path==="+path);
		if (path==null||path.trim().length()==0) {
			path = "G:/同步dev/国际化/virtualcombiBefore/";
//			path = "I:/同步dev/国际化/virtualcombiBefore/";
		}
		dealI18NService.autoDealI18N(path);
        
		return "OK";
	}

}
