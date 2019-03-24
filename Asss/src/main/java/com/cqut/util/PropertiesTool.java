package com.cqut.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 
 * @author ljl
 * 配置文件读取工具
 */
public class PropertiesTool{
	static Properties  properties = new Properties();
	static Map<String, String> propMap = new HashMap<String, String>();
	
	public static void readProperties() {
		InputStream inStream = PropertiesTool.class.getClassLoader()
				.getResourceAsStream("jdbc.properties");
		
		if(inStream != null) {
			try{ 
				properties.load(inStream);
				
				Set<Object> Setkeyset = properties.keySet(); 
				for (Object object : Setkeyset) { 
					String propValue= properties.getProperty(object.toString()).toString(); 
					propMap.put(object.toString(), propValue); 
				} 
			} catch(IOException e){
				System.out.println("读取配置文件出错");
				e.printStackTrace();
			}
		} else {
			System.err.println("系统配置路径出错");
		}
	}

	public static String getSystemPram(String key) {
		readProperties();
		if(propMap.containsKey(key))
			return propMap.get(key);
		else {
			Object o = properties.getProperty(key);
			if(o != null){
				propMap.put(key, o.toString());
				return o.toString();
			} else {
				System.err.println("读取" + key + "出错");
				return null;
			}
		}
	}
}
