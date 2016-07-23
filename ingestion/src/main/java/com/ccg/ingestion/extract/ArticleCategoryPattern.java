package com.ccg.ingestion.extract;

import javax.xml.bind.annotation.XmlRootElement;

import com.ccg.util.ConfigurationManager;
import com.ccg.util.XML;

/*
 * ArticleCaltegoryPattern value is a regular expression
 */
@XmlRootElement
public class ArticleCategoryPattern {
	private String name;
	private String display;
	private String description;
	private String[] value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getValue() {
		return value;
	}
	public void setValue(String[] value) {
		this.value = value;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public static void main(String[] args) throws Exception{
		ArticleCategoryPattern pattern = new ArticleCategoryPattern();
		pattern.setName("proposal_1");
		pattern.setDisplay("1.  2.  |  1.1.  2.1. ");
		String[] value = {"(\\nExecutive\\s+Summary)|(\\r?\\n\\d+\\.\\D)", "(\\r?\\n\\d+\\.\\d+\\.\\D)"};
		pattern.setValue(value);
		pattern.setDescription("description");
		
		ConfigurationManager cm = new ConfigurationManager();
		
		String xml = XML.toXml(pattern);
		System.out.println("===>>" + xml);
		
		pattern = XML.fromXml(xml, ArticleCategoryPattern.class);
		
		System.out.println(pattern.getValue()[0]);
		System.out.println(pattern.getValue()[1]);
		
		
		//cm.addConfig(pattern);
		
	}
	
}
