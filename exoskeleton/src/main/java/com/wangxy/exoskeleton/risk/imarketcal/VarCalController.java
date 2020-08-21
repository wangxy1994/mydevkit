package com.wangxy.exoskeleton.risk.imarketcal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wangxy.exoskeleton.risk.vo.VarCalParam;


@RestController
@RequestMapping("/varCal")
public class VarCalController {
	private static Logger logger = LoggerFactory.getLogger(VarCalController.class);

	@RequestMapping("/cal")
	public String cal(@RequestBody VarCalParam varCalParam) {

		long startTime = System.currentTimeMillis(); // 获取开始时间
		
		System.out.println(varCalParam);
		
		long endTime = System.currentTimeMillis(); // 获取结束时间
		logger.info("服务响应时间：" + (endTime - startTime) + "ms"); // 输出程序运行时间

		return "OK";
	}
}

