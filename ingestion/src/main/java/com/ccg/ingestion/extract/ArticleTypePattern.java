package com.ccg.ingestion.extract;

public class ArticleTypePattern {

	public static final String[] PROPOSALS_1 = {
			// 1. , 2.1., 3.2.1., ...
			"(\\nExecutive\\s+Summary)|(\\r?\\n\\d+\\.\\D)", // LEVEL 1, --- 1.,
																// 2.,
			"(\\r?\\n\\d+\\.\\d+\\.\\D)" // LEVEL 2 ---- 1.1. 2.1. 2.2.
	};
	public static final String[] PROPOSALS_2 = {
			// 1.0 , 2.1 , 3.2.1 , ....
			"(\\nExecutive\\s+Summary)|(\\n\\d+\\.0\\D)", // LEVEL 1 --- 1.0 2.0
															// 3.0
			"(\\n\\d+\\.\\d+\\s+\\D)" // LEVEL 2 ----- 1.1 2.1 2.2
	};
	public static final String[] PROPOSALS_3 = {
			// 1 , 2.1 , 3.2.1 , ....
			"(\\nExecutive\\s+Summary)|(\\n\\d+\\s+\\D)", // LEVEL 1 --- 1 2 3
			"(\\n\\d+\\.\\d+\\s+\\D)" // LEVEL 2 ------ 1.1 2.1 2.2 3.1 3.2
	};
	
	
	
	
}
