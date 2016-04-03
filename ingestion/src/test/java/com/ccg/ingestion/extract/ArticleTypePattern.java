package com.ccg.ingestion.extract;

public class ArticleTypePattern {
	public static final String PROPOSAL = "(Executive\\s+Summary)|(\\r?\\n\\d+\\.(\\d+\\.)*)";
	public static final String PROPOSAL_LEVEL_1 = "(\\r?\\nExecutive\\s+Summary)|(\\r?\\n\\d\\.\\D)";
	public static final String[] PROPOSALS = {
			"(\\r?\\nExecutive\\s+Summary)|(\\r?\\n\\d\\.\\D)", // LEVEL 1
			"(\\r?\\n\\d\\.\\d\\.\\D)"		// LEVEL 2
			
	};
}
