package com.ccg.ingestion.extract.tocfinder;

public class TOCFinderRegexConstants {
	static public String romanregex="(\\d{0,3}|[ivx]{0,3})";
	static public String _TOCDOTREGEX_="(\\s{0,3}[.…]{2,200}[\\s]{0,3}[\\divxIVX]{1,5}[-]{0,1}[\\divxIVX]{0,5})|(\\s{0,3}[.…]{2,200}[\\s]{0,3}Error)";
	
	static public String[] _TOCHEADERREGEX_={
		"\\n[0-9A-H]{1,4}[\\.[0-9A-H]]{0,10}[\\s\\xA0]" // header pattern 1		
	};
	
	static public String[] _TOCKEYWORDREGEX_={
		"\\nglossary\\sof",
		"\\ncross\\sreference\\smatrix",
		"\\ntable\\sof\\scontents",
		"\\nvolume\\s[i]{0,4}",
		"\\nexecutive\\ssummary",
		"\\nappendix\\s[0-9a-h]",
		"\\nfigure\\s[0-9a-h]",
		"\\ntable\\s[0-9a-h]",
		"\\nresumes\\s[0-9a-h]"
	};
	
	static public String[] _ARTICLESECTON_={
			"(\\n[0-9A-H]{1,3}[\\.[0-9A-H]]{0,10}[\\s\\xA0]{1,5}\\w)"
	}; //|(\\nAppendix\\s[0-9A-H])
}
