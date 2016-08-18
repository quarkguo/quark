package com.ccg.ingestion.extract;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Category {
	String title;
	String content;
	int startPosition;
	int endPosition;
	int startPage;
	int endPage;	
	List<Category> subCategory;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
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
		ps.println("title: "+this.getTitle()+ " "+this.getStartPosition()+"," +this.getEndPosition()+".."+this.doesCategoryHasContent());
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
	
	 
}
