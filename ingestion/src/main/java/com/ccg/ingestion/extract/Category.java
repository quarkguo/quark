package com.ccg.ingestion.extract;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Category {
	String title;
	//String content;
	int articleID;
	int startPosition;
	int endPosition;
	int startPage;
	int endPage;	
	int level=0;  // default level and upmost level
	String matchedToken;
	public String getMatchedToken() {
		return matchedToken;
	}
	public int getArticleID() {
		return articleID;
	}
	public void setArticleID(int articleID) {
		this.articleID = articleID;
	}
	public void setMatchedToken(String matchedToken) {
		this.matchedToken = matchedToken;
	}

	Category tobCategory=null;
	public Category getTobCategory() {
		return tobCategory;
	}
	public void setTobCategory(Category tobCategory) {
		this.tobCategory = tobCategory;
	}

	boolean isTOC=false;
	boolean isCOVER=false;
	public boolean isTOC() {
		return isTOC;
	}
	public void setTOC(boolean isTOC) {
		this.isTOC = isTOC;
	}
	public boolean isCOVER() {
		return isCOVER;
	}
	public void setCOVER(boolean isCOVER) {
		this.isCOVER = isCOVER;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	List<Category> subCategory;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isTableOrFigure()
	{
		if(title.toLowerCase().indexOf("table ")>-1) return true;
		if(title.toLowerCase().indexOf("figure ")>-1) return true;
		if(title.toLowerCase().indexOf("resumes ")>-1) return true;
		return false;
	}
	public String getTrimTitle()
	{
		String res=title;
		int posi=title.indexOf("....");
		// preprocess remove tailing token
		if(posi>0)
		{
			res=title.substring(0,posi);
		}
		 else
		{
		   posi=title.lastIndexOf(" ");
		   res=res.substring(0,posi).trim();
		}
		return res;
	}
	/*
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	*/
	public int getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}
	public int getEndPosition() {
		return endPosition;
	}
	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public List<Category> getSubCategory() {
		if(subCategory == null)
			subCategory = new ArrayList<Category>();
		return subCategory;
	}
	public void setSubCategory(List<Category> subCategory) {
		this.subCategory = subCategory;
	}
	
	// if category has less than ten character, we consider it is empty
	static final int _content_len_=7;
	public boolean doesCategoryHasContent()
	{
		if(this.getTitle().indexOf(".......")>0) return false;
		if(this.subCategory!=null&&this.subCategory.size()>0)
		{
			for(Category sub :this.subCategory)
			{
				if(sub.doesCategoryHasContent())
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			int diff=this.getEndPosition()-this.getStartPosition();
			int l_c= this.getTitle().length()+_content_len_;
			if(diff>l_c) {
				return true;
			}
			else
			{
				return false;
			}
		}
	}
		
	public int getNodeCount()
	{
		int res=1;
		if(this.subCategory!=null && this.subCategory.size()>0)
		{
			for(Category sub:subCategory)
			{
				res+=sub.getNodeCount();
			}
		}
		return res;
	}
	public void printMe(PrintStream ps)
	{
		ps.println("level:"+this.getLevel()+" title: "+this.getTitle()+" "+this.startPage+","+this.endPage+ " "+this.getStartPosition()+"," +this.getEndPosition()+".."+this.doesCategoryHasContent()+".."+this.getMatchedToken());
		if(subCategory!=null&&subCategory.size()>0)
		{
			ps.println(".... children....");
			for(Category sub:subCategory)
			{
				sub.printMe(ps);
				ps.println("------");
			}
		}
		
	}
	
	public String[] getCategoryRef()
	{
		if(title!=null)
		{
			StringTokenizer tk1=new StringTokenizer(title);
			String refstr=tk1.nextToken();
			System.out.println("^^^"+refstr+"^^"+title);
			String[] ary=refstr.split("\\.");
			return ary;
		}
		else
		{
			return null;
		}
	}
	
	public boolean comparePierRef(Category c)
	{
		String[] me_ref=getCategoryRef();
		String[] c_ref=c.getCategoryRef();
		if(c_ref==null||me_ref==null||c_ref.length!=me_ref.length)
		{
			return false;
		}
		else
		{
			for(int i=0;i<me_ref.length-2;i++)
			{
				if(me_ref[i]!=c_ref[i])
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean compareParentRef(Category p)
	{
		String[] me_ref=getCategoryRef();
		String[] c_ref=p.getCategoryRef();
		if(c_ref==null||me_ref==null||(c_ref.length+1)!=me_ref.length)
		{
			return false;
		}
		else
		{
			for(int i=0;i<c_ref.length-1;i++)
			{
				if(me_ref[i]!=c_ref[i])
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public  void getTitlesFlat(List<String> list)
	{
		list.add(this.getTitle());
		if(this.subCategory!=null&& this.subCategory.size()>0)
		{
			for(Category sub:subCategory)
			{
				sub.getTitlesFlat(list);
			}
		}
	}
	
}
