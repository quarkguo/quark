package com.ccg.common.data.user;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import com.ccg.util.XML;

@XmlRootElement
public class NewUserEmailConfig {
	
	private String subject;
	private String fromEmail;
	private String content;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public static void main(String[] args) throws JAXBException{
		NewUserEmailConfig config = new NewUserEmailConfig();
		config.setSubject("Thi is  a subject");
		config.setContent("This is a content {0}, content2 {1}");
		config.setFromEmail("admin@ccg.com");
		System.out.println(config.getClass().getName());
		System.out.println(XML.toXml(config));
	}
}
