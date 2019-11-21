package com.wangxy.devkit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wangxy.devkit.business.market.interfaces.IFxRealrate;
import com.wangxy.devkit.kafka.consumer.ConsumerClient;
import com.wangxy.devkit.util.SpringContextUtil;

@SpringBootApplication
public class MydevkitApplication {

	public static final Boolean IS_PROD = false;

	public static void main(String[] args) {
		SpringApplication.run(MydevkitApplication.class, args);
		
		try {
			ConsumerClient client = new ConsumerClient();
			client.consume();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		IFxRealrate fxPriceRealtime = SpringContextUtil.getBean(IFxRealrate.class);
		fxPriceRealtime.impFxRealrate4Test(null);;
		
	}

}
