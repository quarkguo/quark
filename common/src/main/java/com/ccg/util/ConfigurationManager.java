package com.ccg.util;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * All configuration must be in xml format
 */
public class ConfigurationManager {
	
	private Map<String, Object> config = Collections.synchronizedMap(new HashMap<String, Object>(101));
	
	private static String GET_CONFIG = "SELECT value FROM ccgconfig WHERE NAME = ?";
	private static String GET_CONFIG_NAMES = "SELECT NAME FROM ccgconfig";
	private static String UPDATE_CONFIG = "UPDATE ccgconfig SET VALUE=? WHERE NAME=?";
	private static String ADD_CONFIG = "INSERT INTO ccgconfig(name, value) VALUES(?, ?)";
	
	@SuppressWarnings("unchecked")
	public <T>T load(Class<T> type) throws UnsupportedEncodingException, SQLException, JAXBException{
		T t = null;
		if(config.containsKey(type.getName())){
			t = (T)config.get(type.getName());
		}else{
			t = this.loadFromDB(type);
			config.put(type.getName(), t);
		}
		return t;
	}
	
	public String getConfig(String name) throws SQLException{
		String result = "";
		Connection conn = this.getConnection();
		PreparedStatement stm = conn.prepareStatement(GET_CONFIG);
		stm.setString(1, name);
		ResultSet rs = stm.executeQuery();
		while(rs.next()){
			result = rs.getString("value");
		}
		conn.close();
		return result;
	}
	
	public void update(String name, String value) throws SQLException{
		Connection conn = this.getConnection();
		PreparedStatement stm =  conn.prepareStatement(UPDATE_CONFIG);
		stm.setString(1,  value);
		stm.setString(2,  name);
		stm.executeUpdate();
		conn.close();
	}
	
	public void insert(String name, String value) throws SQLException{
		Connection conn = this.getConnection();
		PreparedStatement stm = conn.prepareStatement(ADD_CONFIG);
		 stm.setString(1, name);
		 stm.setString(2,  value);
		 stm.executeUpdate();
		 conn.close();		
	}
	
	public void update(Object configBean) throws SQLException, JAXBException{
		
		String xml = XML.toXml(configBean);
		
		Connection conn = this.getConnection();
		PreparedStatement stm = conn.prepareStatement(UPDATE_CONFIG);			
		stm.setString(1, xml);
		stm.setString(2, configBean.getClass().getName());;
		stm.executeUpdate();
		conn.close();
	}
	
	public void addConfig(Object config) throws SQLException, JAXBException{
		String xml = XML.toXml(config);
		Connection conn = this.getConnection();
		PreparedStatement stm = conn.prepareStatement(ADD_CONFIG);
		stm.setString(1, config.getClass().getName());
		stm.setString(2, xml);
		stm.executeUpdate();
		conn.close();
	}
	
	public List<String> getConfigNames() throws SQLException{
		List<String> configNames = new LinkedList<String>();
		Connection conn = this.getConnection();
		PreparedStatement stm = conn.prepareStatement(GET_CONFIG_NAMES);
		ResultSet rs = stm.executeQuery();
		while(rs.next()){
			String name = rs.getString("name");
			configNames.add(name);
		}
		conn.close();
		return configNames;
	}
	
	private <T>T loadFromDB(Class<T> type) throws SQLException, UnsupportedEncodingException, JAXBException{
		T t = null;
		Connection conn = this.getConnection();
		PreparedStatement stm = conn.prepareStatement(GET_CONFIG);
		stm.setString(1, type.getName());
		ResultSet rs = stm.executeQuery();
		String xml = null;
		while(rs.next()){
			xml = rs.getString("value");
		}
		conn.close();
		t = XML.fromXml(xml, type);
		return t;
	}
	
	private Connection getConnection() {
		Connection conn = null;
//		try {
//			Context ctx = new InitialContext();
//			//DataSource  ds = (DataSource)ctx.lookup("java:comp/env/jdbc/ccgcontent");
//			DataSource  ds = (DataSource)ctx.lookup("jdbc/ccgconfig");
//			conn = ds.getConnection();			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://72.177.234.240:3306/ccgcontent?" +
                "user=ccg&password=ccg");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public static void main(String[] args) throws Exception{
		TestConfig config = new TestConfig();
		config.setName("cinfigName");
		config.setValue("ConfigValue");
		
		ConfigurationManager cm = new ConfigurationManager();
		//cm.addConfig(config);
		
		List<String> nameList = cm.getConfigNames();
		System.out.println(nameList);
		
		System.out.println(cm.getConfig("com.ccg.ingestion.extract.ArticleCategoryPatternConfig"));
//		
//		String value1 = cm.getConfig("com.ccg.util.TestConfig");
//		cm.update("myConfig", value1);
//		System.out.println(cm.getConfig("myConfig"));	
	}
}

@XmlRootElement
class TestConfig {
	String name;
	String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
