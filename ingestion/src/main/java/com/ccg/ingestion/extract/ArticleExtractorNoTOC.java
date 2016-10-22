package com.ccg.ingestion.extract;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ccg.ingestion.extract.tocfinder.TOCItem;
import com.ccg.ingestion.extract.tocfinder.TOCSection;

public class ArticleExtractorNoTOC extends ExtractArticleInfobyDotTOC {
	public List<TOCItem> searchSectionMatches(String content,String regex)
	{
		List<TOCItem> res=new ArrayList<TOCItem>();
		Pattern p=Pattern.compile(regex);
		Matcher matcher=p.matcher(content);
		while(matcher.find())
		{
			TOCItem item=new TOCItem();
			int start=matcher.start();
			int end=content.indexOf("\n",matcher.end());
			if(end==-1) end=content.length();
			item.setTitle(content.substring(start,end));
			item.setEndposi(end);
			item.setStartposi(start);
			res.add(item);
		}		
		return res;
	}
	
	// this clean up is purely based on section matching
	// item with sections header does not link to any neightbours are bad and need to be removed
	// item confirmed with both neightbours are good
	// item confirmed link with any good neighbour are good
	// appendix should be at the end of the article
	public List<TOCItem> validate(List<TOCItem> raw_list)
	{
		List<TOCItem> bad_list=new ArrayList<TOCItem>();
		List<TOCItem> good_list=raw_list;
		int good_count=0;
		for(TOCItem it:good_list)
		{
			it.checkTOCheader();
			it.setClasifyFlag(0);
		}		
		System.out.println("---->List Size:"+good_list.size()+" good record:"+good_count);

		while(good_count<good_list.size())
		{
			boolean changed=false;
			// check for good sections			
			for(int i=1;i<good_list.size()-1;i++)
			{
				TOCItem me=good_list.get(i);
				if(me.getClasifyFlag()>0) continue; // me is good
				TOCItem prev=good_list.get(i-1);				
				TOCItem next=good_list.get(i+1);
				// now to mark me to be good
				if(prev.isHasSection()&&me.isHasSection()&&next.isHasSection())
				{
					TOCSection s_me=me.getS_toc();
					TOCSection s_prev=prev.getS_toc();
					TOCSection s_next=next.getS_toc();
					if((s_me.isDirectParentSection(s_prev)||s_me.isPrevSection(s_prev))&&(s_me.isDirectChildSection(s_next)||s_me.isNextSection(s_next)))
					{
						me.setClasifyFlag(1);  // good item
						changed=true;
						continue;
					}
				// now check 
					if(prev.getClasifyFlag()>0)
					{
						if(!(s_me.isDirectParentSection(s_prev)||s_me.isPrevSection(s_prev)))
						{
							me.setClasifyFlag(-1);
							changed=true;
						}
						else
						{
							changed=true;
							me.setClasifyFlag(1);
						}
					
					}
					if(next.getClasifyFlag()>0)
					{
						if(!(s_me.isDirectChildSection(s_next)||s_me.isNextSection(s_next)))
						{
							changed=true;
							me.setClasifyFlag(-1);
						}
						else
						{
							changed=true;
							me.setClasifyFlag(1);
						}
					}
				}
			}
			TOCItem first=good_list.get(0);
			TOCItem second=good_list.get(1);
			if(second.isHasSection()&&second.getClasifyFlag()>0&&first.isHasSection())
			{
				if(first.getS_toc().isDirectChildSection(second.getS_toc())||first.getS_toc().isNextSection(second.getS_toc()))
				{
					first.setClasifyFlag(1);
				}
				else
				{
					first.setClasifyFlag(-1);
				}
			}
			TOCItem last=good_list.get(good_list.size()-1);
			TOCItem secondlast=good_list.get(good_list.size()-2);
			if(secondlast.isHasSection()&&secondlast.getClasifyFlag()>0&&last.isHasSection())
			{
				if(secondlast.getS_toc().isDirectChildSection(last.getS_toc())||secondlast.getS_toc().isNextSection(last.getS_toc()))
				{
					last.setClasifyFlag(1);
				}
				else
				{
					last.setClasifyFlag(-1);
				}
			}
			good_count=0;
			List<TOCItem> badapples=new ArrayList<TOCItem>();
			for(int i=good_list.size()-1;i>=0;i--)
			{
				TOCItem item=good_list.get(i);
				if(item.getClasifyFlag()>0)
				{
					good_count++;
				}
				if(item.getClasifyFlag()<0)
				{
					badapples.add(item);
				}
			}
			if(badapples.size()>0)
			{
				good_list.removeAll(badapples);
				changed=true;
			}
			System.out.println("removed sized:"+badapples.size());
			System.out.println("---->List Size:"+good_list.size()+" good record:"+good_count);
			if(!changed) break;
		}
		return good_list;
	}
}
