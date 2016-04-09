package com.ccg.ingestion.extract;

public class ArticleTypePattern {
	
	public static final String[] PROPOSALS = {
			"(\\r?\\nExecutive\\s+Summary)|(\\r?\\n\\d\\.\\D)", // LEVEL 1
			"(\\r?\\n\\d\\.\\d\\.\\D)"		// LEVEL 2
		};
	public static final String[] PROPOSALS_2 = {
			"(\\r?\\nExecutive\\s+Summary)|(\\r?\\n\\d\\s+\\D)", // LEVEL 1
			"(\\r?\\n\\d\\.\\d\\s+\\D)"		// LEVEL 2
		};	
}
