package com.ccg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBException;

/*
 * All configuration must be in xml format
 */
public class ConfigurationManager {
	
	private static Map<String, Object> config = Collections.synchronizedMap(new HashMap<String, Object>(101));	

	@SuppressWarnings("unchecked")
	public static  <T>T getConfig(Class<T> type){
		T t = null;
		String key = type.getName() + ".xml";
		if(config.containsKey(key)){
			t = (T)config.get(key);
		}else{		
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(key);
			try {
				t = (T)XML.fromXml(is, type);
				
				System.out.println(XML.toXml(t));
				
				config.put(key, t);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		return t;
	}
	
	public static Properties getConfig(String filename){
		String key = filename;
		System.out.println("===== properties file name: " + filename);
		
		System.out.println("catalina.base:" + System.getProperty("catalina.base"));
		System.out.println("catalina.home:" + System.getProperty("catalina.home"));
		Properties prop = new Properties();
		if(config.containsKey(key)){
			prop = (Properties)config.get(key);
		}else{
			String catalinaHome = System.getProperty("catalina.home");
			String configDirectory = catalinaHome + File.separator + "ccgconfig" + File.separator;
			
			// check file location
			File configFile = new File(configDirectory + filename);
			
			InputStream is = null;
			
			try {
				
				if(configFile.exists()){
					is = new FileInputStream(configFile);
				}else{				
					is = Thread.currentThread().getContextClassLoader().getResourceAsStream(key);
					//is = ConfigurationManager.class.getResourceAsStream("/" + key);
				}
				
				prop.load(is);
				config.put(key, prop);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return prop;
	}

	public static void main(String[] args) throws Exception{
		Properties prop = ConfigurationManager.getConfig("ccg.properties");
		System.out.println(prop.getProperty("index.repository"));

	}
	
}
