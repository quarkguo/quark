package com.ccg.ingestion.extract;

public class CategoryRegexPattern {
	
	private String root="(\\nExecutive+\\sSummary)|(\\n\\d(1,3)+\\xA0{0,3}\\s{1,4}+[\\D\\d])|(\\nAppendix)";
	private String head="\\n\\d+";
	private String tail="\\xA0{0,3}\\s{1,4}+\\w";
	private String ext="\\.[0-9A-H]+";

	final public static String[] _ROOT_LIST_={
			"(\\nExecutive+\\sSummary)|(\\n\\d{1,3}+\\xA0{0,3}\\s{1,4}+[\\D\\d])|(\\nAppendix)|(\\nFigure\\s\\d+\\:)|(\\nTable\\s\\d+\\:)",
			"(\\nExecutive+\\sSummary)|(\\n\\d{1,3}+\\.0\\xA0{0,3}\\s{1,4}+[\\D\\d])|(\\nAppendix)|(\\nFigure\\s\\d+\\:)|(\\nTable\\s\\d+\\:)",
			"(\\nExecutive+\\sSummary)|(\\r?\\n\\d{1,3}+\\.\\xA0{0,3}\\s{1,4}+[\\D\\d])|(\\nAppendix)|(\\nFigure\\s\\d+\\:)|(\\nTable\\s\\d+\\:)"
	};

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getTail() {
		return tail;
	}

	public void setTail(String tail) {
		this.tail = tail;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
	
	public String getSubCategoryRegex(int level)
	{
		if(level>0)
		{
			StringBuffer buf=new StringBuffer();
			buf.append(head);
			for(int i=0;i<level;i++)
			{
				buf.append(ext);
			}
			buf.append(tail);
			return buf.toString();
		}
		else
		{
			return root;
		}
		
	}
}