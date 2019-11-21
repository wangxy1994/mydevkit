package com.wangxy.devkit.kafka.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.wangxy.devkit.MydevkitApplication;

public class PropertiesUtil {

	private static Properties properties;
	private static Long lastModified = 0L;
	
	private static String getConfigName() {
		if(MydevkitApplication.IS_PROD == false) {
			return "kafka-dev.properties";
		} else if(MydevkitApplication.IS_PROD == true) {
			return "kafka-prod.properties";
		} else {
			return "kafka-dev.properties";
		}
	}
	
	private static void init() {
		properties = new Properties();
		InputStream inStream = null;  
		try {
			inStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(PropertiesUtil.getConfigName()); 
			properties.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
                if(inStream != null){
                	inStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

	/**
     * 判断配置文件是否改动
     * @return returnValue ：true:改动过 ，false:没有改动过
     */
	private static boolean isPropertiesModified() {
        boolean returnValue = false;
        File file = new File(PropertiesUtil.class.getClassLoader().getResource(PropertiesUtil.getConfigName()).getPath());
        if (file.lastModified() > lastModified) {
            lastModified = file.lastModified();
            returnValue = true;
        }
        return returnValue;
    }

	public static String getProperty(String key) {
        if (properties == null || isPropertiesModified()) {
            init();
        }
        String value = properties.get(key).toString();
        return value;
	}

	public static String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		return value != null ? value : defaultValue;
	}
	
	public static String getSystemProperty(String key) {
		String systemProperty = System.getProperty(key);
		if (systemProperty != null) {
			return systemProperty;
		}
	    return "";
	}

	public static String[] getProperties(String key) {
		String string = getProperty(key);
		return string.split(",");
	}
}
