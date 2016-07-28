package com.ccg.ingestion.extract;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import com.ccg.util.ConfigurationManager;

@XmlRootElement
public class ArticleCategoryPatternConfig {
	List<ArticleCategoryPattern> patternConfigs; 

	public List<ArticleCategoryPattern> getPatternConfig() {
		if(patternConfigs == null){
			patternConfigs = new ArrayList<ArticleCategoryPattern>();
		}
		return patternConfigs;
	}

	public void setPatternConfig(List<ArticleCategoryPattern> patternConfigs) {
		this.patternConfigs = patternConfigs;
	}
	
	
//	public static void main(String[] args) throws SQLException, JAXBException{
//		ArticleCategoryPattern pattern_1 = new ArticleCategoryPattern();
//		pattern_1.setName("proposal_1");
//		pattern_1.setDisplay("1.  2.  |  1.1.  2.1. ");
//		pattern_1.setValue(new String[]{
//				"(\\nExecutive\\s+Summary)|(\\r?\\n\\d+\\.\\D)",
//				"(\\r?\\n\\d+\\.\\d+\\.\\D)"});
//		pattern_1.setDescription("description");
//		
//		/////////
//		ArticleCategoryPattern pattern_2 = new ArticleCategoryPattern();
//		pattern_2.setName("proposal_2");
//		pattern_2.setDisplay("1.0  2.0  |  1.1  2.1 ");
//		pattern_2.setValue(new String[]{
//				// 1.0 , 2.1 , 3.2.1 , ....
//				"(\\nExecutive\\s+Summary)|(\\n\\d+\\.0\\D)", // LEVEL 1 --- 1.0 2.0
//																// 3.0
//				"(\\n\\d+\\.\\d+\\s+\\D)" // LEVEL 2 ----- 1.1 2.1 2.2
//		});
//		pattern_2.setDescription("description");
//		
//		
//		/////////
//		ArticleCategoryPattern pattern_3 = new ArticleCategoryPattern();
//		pattern_3.setName("proposal_3");
//		pattern_3.setDisplay("1  2  |  1.1  2.1 ");
//		pattern_3.setValue(new String[]{
//				// 1 , 2.1 , 3.2.1 , ....
//				"(\\nExecutive\\s+Summary)|(\\n\\d+\\s+\\D)", // LEVEL 1 --- 1 2 3
//				"(\\n\\d+\\.\\d+\\s+\\D)" // LEVEL 2 ------ 1.1 2.1 2.2 3.1 3.2
//		});
//		pattern_3.setDescription("description");
//		
//		
//		
//		ArticleCategoryPatternConfig config = new ArticleCategoryPatternConfig();
//		config.getPatternConfig().add(pattern_1);
//		config.getPatternConfig().add(pattern_2);
//		config.getPatternConfig().add(pattern_3);
//		
//		
//		ConfigurationManager cm = new ConfigurationManager();
//		cm.addConfig(config);
//		
//		
//		
//	}
	
}
