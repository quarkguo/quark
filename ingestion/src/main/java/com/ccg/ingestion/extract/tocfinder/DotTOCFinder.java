package com.ccg.ingestion.extract.tocfinder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ccg.ingestion.extract.Category;

public class DotTOCFinder {

	public List<Category> buildCategoryList(List<TOCItem> itemlist)
	{
		List<Category> toc=new ArrayList<Category>();
		List<Category> res=new ArrayList<Category>();
		// first get all section build
		for(TOCItem item:itemlist)
		{
			item.checkTOCheader();
			res.add(buildCategory(item));
		}
	
		// now trying to build the catgory
		for(int i=0;i<itemlist.size()-1;i++)
		{
			TOCItem curItem=itemlist.get(i);
			Category curC=res.get(i);			
			if(curItem.hasSection)
			{
				for(int j=i+1;j<itemlist.size()-1;j++)
				{
					TOCItem it=itemlist.get(j);
					Category sub=res.get(j);
					
					if(it.hasSection)
					{
						if(curItem.getS_toc().isDirectChildSection(it.getS_toc()))
						{
							System.out.println("adding children: parent:"+curItem.getS_toc()+" child:"+it.getS_toc());
							curC.getSubCategory().add(sub);
							sub.setLevel(curC.getLevel()+1);
						}
						else
						{
							// does nothing
						}
					}
				}
			}
		
		}
		
		processNoSectionItem(itemlist,res);
		for(int i=0;i<res.size();i++)
		{
			Category curC=res.get(i);
			if(curC.getLevel()==1)
			{
				toc.add(curC);
			}
		}
		return toc;
	}
	
	public void processNoSectionItem(List<TOCItem> tlist,List<Category>clist)
	{
		for(int i=0;i<tlist.size();i++)
		{
			TOCItem item=tlist.get(i);
			if(!item.hasSection)
			{
				// does not have section
				TOCItem prev=null;
				int i_prev=-1;
				for(int j=i-1;j>1;j--)
				{
					TOCItem p=tlist.get(j);
					if(p.isHasSection())
					{
						prev=p;
						i_prev=j;
						break;
					}
				}
				TOCItem next=null;
				int i_next=-1;
				for(int j=i+1;j<tlist.size();j++)
				{
					TOCItem p=tlist.get(j);
					if(p.hasSection)
					{
						next=p;
						i_next=j;
						break;
					}
				}
				// now assign the item
				if(prev==null||next==null)
				{
					// does nothing since it is 
				}
				else
				{
					Category c=clist.get(i_prev);
					Category sub=clist.get(i);
					c.getSubCategory().add(sub);
					sub.setLevel(c.getLevel()+1);
				}
				
			}
		}
	}
	
	public Category buildCategory(TOCItem item)
	{
		Category c=new Category();
		c.setTitle(item.getTitle().trim());
		c.setStartPosition(item.getStartposi());
		c.setEndPosition(item.getEndposi());
		c.setLevel(1);
		c.setTOC(true);
		return c;
	}
	
	public int findHeaderPosition(String title)
	{
		Pattern pattern = Pattern.compile(TOCFinderRegexConstants._TOCHEADERREGEX_[0]);
		Matcher matcher = pattern.matcher(title);
		if(matcher.find())
		{
			// here we find the match for sections
			 return matcher.start();
		}
		// now check find the other keyword pattern for opening
		for(String kwregex:TOCFinderRegexConstants._TOCKEYWORDREGEX_)
		{
			Pattern p = Pattern.compile(kwregex);
			String low_title=title.toLowerCase();
			Matcher m = p.matcher(low_title);
			if(m.find())
			{
				return m.start();
			}
		}		
		return -1;
	}
	
	public List<TOCItem> searchTOC(String content, String regex, int offset)
	{
		List<TOCItem> matched_list=new ArrayList<TOCItem>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		int lastitem_bound=0;		
		while(matcher.find())
		{
			TOCItem c=new TOCItem();
			int p0 = matcher.start();
			int pend=matcher.end();
			int pstart = content.lastIndexOf("\n", p0);
			pend=content.indexOf("\n",pend);
			String title=	content.substring(pstart,pend);
			c.setTitle(title);
			if(!c.checkTOCheader())
			{
				// here we need to readjust the position	
			//	System.out.println(" no match :"+c.getTitle());
					String newtitle="";
					if(pstart>(600+lastitem_bound))
					{
						newtitle=content.substring(pstart-600,pend);
						pstart=pstart-600;
					}
					else 
					{
						//System.out.println(""+(lastitem_bound+1)+" "+pend+" "+content.length());
						newtitle=content.substring(lastitem_bound,pend);
						pstart=lastitem_bound;
					}
					int headerposi=findHeaderPosition(newtitle);
					if(headerposi>-1)
					{
						pstart=pstart-headerposi;
						newtitle=newtitle.substring(headerposi);
						//newtitle=newtitle.charAt(0)+newtitle.substring(1).replace("\\n", "");
						newtitle="\n"+newtitle.replaceAll("\n", "");
						c.setTitle(newtitle);
						c.checkTOCheader();
					}
					else
					{
					//	newtitle=newtitle.charAt(0)+newtitle.substring(1).replace("\\n", "");
						newtitle="\n"+newtitle.replaceAll("\n", "");
						c.setTitle(newtitle);
						c.checkTOCheader();
					}
			}
			c.setStartposi(pstart+offset);
			c.setEndposi(pend+offset);
			lastitem_bound=c.endposi;			
			matched_list.add(c);
		}
		return matched_list;
	}
	
	public List<String> findMatchLine1(String content,String regex,int offset)
	{
	//	System.out.println("!$%!$%!#$%%$!");
		List<String> res=new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()){
			System.out.println("match ***"+content.substring(matcher.start(),matcher.end()));
			int p0 = matcher.start();
			int pend=matcher.end();		
			int pstart = content.lastIndexOf("\n", p0);
			 pend=content.indexOf("\n",pend);
			res.add(content.substring(pstart,pend));		
		}
		return res;
	}

	synchronized static public DotTOCFinder getTOCTailFinder()
	{
		if(_instance==null)
		{
			_instance=new DotTOCFinder();
		}
		return _instance;
	}
	private static DotTOCFinder _instance=null;
	
}
