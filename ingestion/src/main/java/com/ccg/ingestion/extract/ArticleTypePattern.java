package com.ccg.ingestion.extract;

public class ArticleTypePattern {
	
	public static final String[] PROPOSALS_1 = {
			// 1. , 2.1., 3.2.1., ...
			"(\\nExecutive\\s+Summary)|(\\r?\\n\\d+\\.\\D)", // LEVEL 1
			"(\\r?\\n\\d+\\.\\d+\\.\\D)"		// LEVEL 2
		};
	public static final String[] PROPOSALS_2 = {
			// 1 , 2.1 , 3.2.1 , ....
			"(\\nExecutive\\s+Summary)|(\\n\\d+\\.0\\D)", // LEVEL 1
			"(\\n\\d+\\.\\d+\\s+\\D)"		// LEVEL 2
		};	
	public static final String[] PROPOSALS_3 = {
			// 1 , 2.1 , 3.2.1 , ....
			"(\\nExecutive\\s+Summary)|(\\n\\d+\\s+\\D)", // LEVEL 1
			"(\\n\\d+\\.\\d+\\s+\\D)"		// LEVEL 2
		};
}
