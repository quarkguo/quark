package com.ccg.ingestion.extract.tocfinder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TOCItem {

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public int getStartposi() {
		return startposi;
	}

	public void setStartposi(int startposi) {
		this.startposi = startposi;
	}

	public int getEndposi() {
		return endposi;
	}

	public void setEndposi(int endposi) {
		this.endposi = endposi;
	}

	String title;	
	int startposi=-1;
	public TOCSection getS_toc() {
		return s_toc;
	}

	public void setS_toc(TOCSection s_toc) {
		this.s_toc = s_toc;
	}

	public boolean isHasSection() {
		return hasSection;
	}

	public void setHasSection(boolean hasSection) {
		this.hasSection = hasSection;
	}

	int endposi=-1;
	String header;
	TOCSection s_toc=null;
	int rootlevel=1; // root level is 0
	boolean hasSection=false;
	boolean hasMatchedKeyword=false;
	
	public boolean checkTOCheader()
	{
		// check section header  check the section category header
		Pattern pattern = Pattern.compile(TOCFinderRegexConstants._TOCHEADERREGEX_[0]);
		Matcher matcher = pattern.matcher(title);
		if(matcher.find())
		{
			// here we find the match for sections
			 header=title.substring(matcher.start(),matcher.end());
			 s_toc=new TOCSection(header);
			 hasSection=true;
			 return true;
		}
		// now check find the other keyword pattern for opening
		for(String kwregex:TOCFinderRegexConstants._TOCKEYWORDREGEX_)
		{
			Pattern p = Pattern.compile(kwregex);
			String low_title=title.toLowerCase();
			Matcher m = p.matcher(low_title);
			if(m.find())
			{
				header=title.substring(m.start(),m.end());
				hasSection=false;
				hasMatchedKeyword=true;
				return true;
			}
		}		
		return false;
	}
	
	int clasifyFlag=0;
	public int getClasifyFlag() {
		return clasifyFlag;
	}

	public void setClasifyFlag(int clasifyFlag) {
		this.clasifyFlag = clasifyFlag;
	}
}
