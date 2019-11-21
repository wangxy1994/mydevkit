package com.wangxy.devkit.kafka.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wangxy.devkit.business.market.interfaces.IFxRealrate;
import com.wangxy.devkit.kafka.util.PropertiesUtil;
import com.wangxy.devkit.util.SpringContextUtil;


public class ConsumerClient extends Client{
	

	private static Logger logger = LoggerFactory.getLogger(ConsumerClient.class);
	
	private final static String TOPIC = PropertiesUtil.getProperty("topic.name");
	Properties props = null;
	 
	public ConsumerClient(){
        String clientJassPath = PropertiesUtil.getProperty("client.jass.path");
        String krb5Conf = PropertiesUtil.getProperty("krb5.path");
        System.setProperty("java.security.auth.login.config", clientJassPath);
        System.setProperty("java.security.krb5.conf", krb5Conf);
        
		props = new Properties();
		props.put("bootstrap.servers", PropertiesUtil.getProperty("bootstrap.server"));		// broker的host列表
		props.put("security.protocol", PropertiesUtil.getProperty("security.protocol"));	// 安全协议
		props.put("group.id", PropertiesUtil.getProperty("group.id"));						// 安全协议
		
		props.put("enable.auto.commit", "true"); 						// 如果value合法，则自动提交偏移量
		props.put("auto.commit.interval.ms", "1000");					// 设置多久一次更新被消费消息的偏移量
		props.put("session.timeout.ms", "30000");						// 设置会话响应的时间，超过这个时间kafka可以选择放弃消费或者消费下一条消息
		props.put("auto.offset.reset", "earliest");						// 从何处开始消费,latest 表示消费最新消息,earliest 表示从头开始消费,none表示抛出异常,默认latest 【String must be one of: latest, earliest, none】
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	}
	
	@Override
	public void consume() {
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

		try {
			/* 消费者订阅的topic, 可同时订阅多个 */
			consumer.subscribe(Arrays.asList(TOPIC));
			while (true) {
				/* 读取数据，读取超时时间为60000ms */
				ConsumerRecords<String, String> records = consumer.poll(60000);
				
				IFxRealrate fxPriceRealtime = SpringContextUtil.getBean(IFxRealrate.class);
				fxPriceRealtime.impFxRealrate(records);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			consumer.close();
		}

	}
}
