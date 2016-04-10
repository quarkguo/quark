package com.ccg.ingestion.extract;

public class ArticleTypePattern {
	
	public static final String[] PROPOSALS = {
			// 1. , 2.1., 3.2.1., ...
			"(\\nExecutive\\s+Summary)|(\\r?\\n\\d+\\.\\D)", // LEVEL 1
			"(\\r?\\n\\d+\\.\\d+\\.\\D)"		// LEVEL 2
		};
	public static final String[] PROPOSALS_2 = {
			// 1 , 2.1 , 3.2.1 , ....
			"(\\nExecutive\\s+Summary)|(\\n\\d+\\s+\\D)", // LEVEL 1
			"(\\n\\d+\\.\\d+\\s+\\D)"		// LEVEL 2
		};	
}
