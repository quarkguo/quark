package com.ccg.common.util;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import com.ccg.util.XML;

@XmlRootElement
public class SMTPConfig {
//    props.put("mail.smtp.host", "localhost");
//    // props.put("mail.smtp.host", "pop-server.satx.rr.com");  
//     //props.put("mail.smtp.auth", "true");  
//     props.put("mail.debug", "false");  
//     props.put("mail.smtp.port", 25);  
//     props.put("mail.smtp.socketFactory.port", 25);  
//     props.put("mail.smtp.starttls.enable", "true");
//     props.put("mail.transport.protocol", "smtp");

	private String host;
	private boolean auth;
	private boolean debug;
	private int port;
	private int socketFactoryPort;
	private boolean starttlsEnable;
	private String protocol;
	private String usename;
	private String password;
	
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public boolean isAuth() {
		return auth;
	}
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getSocketFactoryPort() {
		return socketFactoryPort;
	}
	public void setSocketFactoryPort(int socketFactoryPort) {
		this.socketFactoryPort = socketFactoryPort;
	}
	public boolean isStarttlsEnable() {
		return starttlsEnable;
	}
	public void setStarttlsEnable(boolean starttlsEnable) {
		this.starttlsEnable = starttlsEnable;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getUsename() {
		return usename;
	}
	public void setUsename(String usename) {
		this.usename = usename;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public static void main(String[] args) throws JAXBException{
		SMTPConfig config = new SMTPConfig();
		config.setAuth(false);
		config.setDebug(true);
		config.setHost("localhost");
		config.setPassword("");
		config.setUsename("");
		config.setPort(25);
		config.setProtocol("smtp");
		config.setSocketFactoryPort(25);
		config.setStarttlsEnable(true);
		
		System.out.println(config.getClass().getName());
		System.out.println(XML.toXml(config));
		
	}
	
	
	
}
