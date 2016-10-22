package com.ccg.ingestion.extract;

import java.util.ArrayList;
import java.util.List;

import com.ccg.ingestion.extract.tocfinder.DotTOCFinder;
import com.ccg.ingestion.extract.tocfinder.TOCFinderRegexConstants;
import com.ccg.ingestion.extract.tocfinder.TOCItem;

public class ExtractArticleInfobyDotTOC extends ExtractArticleInfoAuto {
	DotTOCFinder finder=DotTOCFinder.getTOCTailFinder();
	
	@Override
	public ArticleInfo processDocument() throws Exception
	{
		List<TOCItem> res=finder.searchTOC(aInfo.content, TOCFinderRegexConstants._TOCDOTREGEX_, 0);
		List<Category> TOC=finder.buildCategoryList(res);	
		List<Category> main=buildMainCategory(TOC);
		//	System.out.println("---> Real Category");
			for(Category c:main)
			{
					buildPageNumber(c);
	   //				c.printMe(System.out);
			}
			// set category to the info
			aInfo.setCategoryList(main);			
			return aInfo;	
	}
	
	public boolean isCovered(int posi,List<int []> coverArea)
	{
		
		for(int[] area: coverArea)
		{
			System.out.println(posi+" , "+area[0]+" , "+area[1]);
			if(posi<area[0])
			{
				return false;
			}
			else  if(posi<=area[1])
			{
				return true;
			}
		}
		return false;
	}
	
	// this method calculate the cover area for the category and
	public List<int[]> getTOCCoverArea(List<Category> catlist)
	{
		List<int[]> res=new ArrayList<int[]>();
		int []prev_sec=null;
		for(Category cat:catlist)
		{
			int [] cur_sec=getTOCCoverArea(cat,null);
			if(prev_sec!=null)
			{
				// here we are trying to merging the two section if there
				if(cur_sec[0]<=prev_sec[1]+120)  // merge two area if not needed
				{
					// there is overlay and we will merge the section
					prev_sec[1]=cur_sec[1];
				}
				else
				{
					// need to start new and log existing
					res.add(prev_sec);
					prev_sec=cur_sec;
				}
			}
			else
			{
				prev_sec=cur_sec;
			}
			
		}
		res.add(prev_sec);
		return res;
	}
	
	// this is based on all TOC coverarea within the level 1 category should be continous 
	public int[] getTOCCoverArea(Category c, int[] coverarea)
	{
		int [] cover=null;
		if(coverarea!=null)
		{
			cover=new int[]{Math.min(c.getStartPosition(), coverarea[0]),Math.max(c.getEndPosition(), coverarea[1])};
		}
		else
		{
			cover=new int[]{c.getStartPosition(),c.getEndPosition()};
		}
		for(Category sub:c.getSubCategory())
		{
			cover=getTOCCoverArea(sub,cover);
		}
		return cover;
	}
	
	@Override 
	public List<Category> buildMainCategory(List<Category> tocs) throws Exception
	{
		
		List<int[]> exclude_areas=this.getTOCCoverArea(tocs);
		List<Category> main=findCategoryLevel1FromTOC(tocs,exclude_areas);
		fillEndPosition(main, aInfo.content.length());
		for(Category ele:main)
		{
			//ele.printMe(System.out);
			parsingSubCategoryRecursive(ele, exclude_areas);
		}
		return main;
	}
	
	public List<int[]> getIntersectAreas(int start, int end, List<int[]> exclude_areas)
	{
		List<int[]> res =new ArrayList<int[]>();
		for(int i=0;i<exclude_areas.size();i++)
		{
			int[] ex=exclude_areas.get(i);
			List<int[]> tmp=this.getIntersetArea(start, end, ex[0], ex[1]);
			if(tmp.size()>0)
			{
				res.addAll(tmp);
			}
		}
		return res;
	}
	
	public List<Category> findCategoryLevel1FromTOC(List<Category> tableOfContent,List<int[]> exclude_ares) throws Exception
	{
		int end=this.aInfo.content.length();
		List<int[]> arealists=null;
		List<Category> res=new ArrayList<Category>();
		int lastposi=0;
		for(Category tc:tableOfContent)
		{
			// search first half
			if(tc.isTableOrFigure()) continue;  // table and figure continue
			Category tmp=null;	
			// reverse search
			arealists=this.getIntersectAreas(0, end, exclude_ares);
			for(int i=arealists.size()-1;i>=0&&(tmp==null);i--)
			{
				int[] range=arealists.get(i);
				tmp=searchByTitleMatch(tc,range[0],range[1]);
			}
			if(tmp!=null)
			{
				tmp.setTobCategory(tc);
				res.add(tmp);
			}
			else
			{
				System.out.println(" XXXXXXXXXX Cound not find "+tc.getTitle());
			}
		}
		return res;
	}
	
	
	public void parsingSubCategoryRecursive(Category data,List<int[]> exclude_areas)
	{
		Category tob=data.getTobCategory();
		if(tob==null) return; // not table of content no further parsing
		List<Category> tobsubs=tob.getSubCategory();
		if(tobsubs!=null&&tobsubs.size()>0)
		{
			List<Category> datasub=new ArrayList<Category>();			
				for(Category tobsub:tobsubs)
				{
					List<int[]> searchareas=this.getIntersectAreas(data.startPosition, data.endPosition, exclude_areas);
					Category tmp=null;
					for(int i=searchareas.size()-1;i>=0&&(tmp==null);i--)
					{
						int[] area=searchareas.get(i);
						tmp=searchByTitleMatch(tobsub,area[0],area[1]);
						if(tmp!=null)
						{
							datasub.add(tmp);
							tmp.setTobCategory(tobsub);
							tmp.setLevel(tobsub.getLevel());
						}
					}
				}
				// now do end postions
				if(datasub.size()>0)
				{
					this.fillEndPosition(datasub, data.endPosition);
					data.setSubCategory(datasub);
				
					// now do recurisve search
					for(Category dataele:datasub)
					{
						parsingSubCategoryRecursive(dataele,exclude_areas);
					}
				}
		}
		return;
	}
	
}
