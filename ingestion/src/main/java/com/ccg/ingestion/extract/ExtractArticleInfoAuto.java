package com.ccg.ingestion.extract;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ExtractArticleInfoAuto extends ExtractArticleInfo {

	CategoryRegexPattern articleCategoryPattern;
	boolean iscontentPrepared=false;
	public static int _PAGESPERCATEGORY_=5;
	// this method prepare the document and prepare the text content 
	// it also remove the header and footer
	// here we assuming the prepareDocument is done
	
	public ArticleInfo processArticle(InputStream is) throws IOException
	{
		prepareDocument(is);
		try{
			this.processDocument();
		}
		catch(Exception e)
		{
			System.out.println("processing normal category fail, adopt default processing");
			e.printStackTrace();
			this.processDocumentDefault(_PAGESPERCATEGORY_);
		}
		return aInfo;
	}
	
	public ArticleInfo processDocument() throws Exception
	{
		
	  //  prepareDocument(is);		
		identifyArticleCategoryPattern();
		System.out.println("####Found pattern -->["+this.articleCategoryPattern.getRoot()+"]");
		//System.out.println(this.);
		List<Category> list=parseAll();		
		mergeCategorys(list);
		aInfo.setCategoryList(list);
	
		System.out.println("---> raw");
		for(Category c:list)
		{
			c.printMe(System.out);
		}
		
		List<Category> tableofcontent=findTableOfContent(list);
		
		List<Category> main=buildMainCategory(tableofcontent);
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
	
	// please notice if reusing the input stream the process has to be reset
	public ArticleInfo processDocumentDefault(int nPage) throws IOException
	{
		  // document has been pre processed
		// step 1 build category
		List<Category> main=new ArrayList<Category>();
		Category curCategory=null;
		int count=0;
		int lastPageEnding=-1;
		for(PageInfo p:pageList)
		{
			if((count % nPage)==0)
			{
				curCategory=new Category();
				main.add(curCategory);
				curCategory.setLevel(0);
				curCategory.setStartPosition(lastPageEnding+1);
				
				curCategory.setStartPage(count);
			}
			lastPageEnding=pageEndIndex.get(count);
			curCategory.setEndPage(count);
			curCategory.setEndPosition(lastPageEnding);
			curCategory.setTitle(main.size()+" Category "+main.size()+" ( Page "+curCategory.startPage+"--"+count+" )");
			count=count+1;
		}
		aInfo.setCategoryList(main);
		return aInfo;
	}
	public void prepareDocument(InputStream is) throws IOException {
		aInfo.setType("PDF");
		PdfReader reader = new PdfReader(is);
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		TextExtractionStrategy strategy;

		aInfo.setPages(reader.getNumberOfPages());		
		
		//PDF file page number starts from 1, not 0
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
			// System.out.println(strategy.getResultantText());
			String temp = strategy.getResultantText() + "\n";
			PageInfo pageInfo = new PageInfo();
			pageInfo.pageNumber = i;
			pageInfo.content = temp;
			if(i==2||i==3) System.out.println(temp);
			//pageInfo.numOfChars = temp.length();
			pageList.add(pageInfo);
			//sb.append(temp);
			setPossiblePageHeaderAndFooter(temp, i);
		}
		////////////
		List<String> pageHeader = findPageHeader();
		List<String> pageFooter = findPageFooter();
		removePageHeaderAndFooter(pageHeader, pageFooter);
		setContent();
		setTitle(pageHeader);
		//printPageInfo();
		setPageEndIndex();	
		this.iscontentPrepared=true;
	}
	
	public void fillEndPosition(List<Category>list,int endposi)
	{
		int previous_start=endposi+1;
		for(int j=list.size()-1;j>=0;j--)
		{
			Category c=list.get(j);
			c.setEndPosition(previous_start-1);
			previous_start=c.getStartPosition();
		}
	}

	public List<Category> buildMainCategory(List<Category> tobs) throws Exception
	{
		List<Category> main=findCategoryLevel1FromTableOfContent(tobs);
	//	addCoverpageAndTableofContent(main, tobs);
		fillEndPosition(main, aInfo.content.length());
		int exclu_start=tobs.get(0).startPosition;
		int exclu_end=tobs.get(tobs.size()-1).getEndPosition();
		for(Category ele:main)
		{
			//ele.printMe(System.out);
			
			parsingSubCategoryRecursive(ele,exclu_start,exclu_end);
			
		}
		return main;
	}
	
	public List<int[]> getIntersetArea(int start, int end, int exclude_start, int exclude_end)
	{
		System.out.println("A:"+start+" B:"+end+" C:"+exclude_start+" D:"+exclude_end);
		List<int[]> res=new ArrayList<int[]>();
		if(end<=exclude_end&&start>=exclude_start)
		{
			// none
			
		}
		else if( start>=end)
		{
			// none
		}
		else if(start<exclude_start&&end>exclude_end)
		{
			// two area
			res.add(new int[]{start,exclude_start});
			res.add(new int[]{exclude_end,end});
		} 
		else if(exclude_end<=end)
		{
			// here we only have one situation
			res.add(new int[]{Math.max(start, exclude_end),end});
		}
		else if(exclude_start>=start)
		{
			res.add(new int[]{start,Math.min(end, exclude_end)});
		}		
		return res;
	}
	// assume all end position is set. 
	public void parsingSubCategoryRecursive(Category data, int ex_start, int ex_end)
	{
		Category tob=data.getTobCategory();
		if(tob==null) return; // not table of content no further parsing
		List<Category> tobsubs=tob.getSubCategory();
		if(tobsubs!=null&&tobsubs.size()>0)
		{
			List<Category> datasub=new ArrayList<Category>();			
				for(Category tobsub:tobsubs)
				{
					List<int[]> searchareas=this.getIntersetArea(data.startPosition, data.endPosition, ex_start, ex_end);
					for(int[] areas:searchareas)
					{
						Category tmp=searchByTitleMatch(tobsub,areas[0],areas[1]);
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
						parsingSubCategoryRecursive(dataele,ex_start,ex_end);
					}
				}
		}
		return;
	}
	
	public List<Category> addCoverpageAndTableofContent(List<Category>main, List<Category> tblist)
	{
		Category first=tblist.get(0);
		Category last=tblist.get(tblist.size()-1);
		Category tb=new Category();
		tb.setTitle("Table Of Content");
		tb.setStartPosition(first.startPosition);
		tb.setEndPosition(last.endPosition);
		tb.setTOC(true);
		// now
		int index=-1;
		for(int i=0;i<main.size();i++)
		{
			index=i;
			Category c=main.get(i);
			if(c.getStartPosition()>tb.startPosition)
			{
				break;
			}
		}
		main.add(index,tb);
		
		Category mainfirst=main.get(0);
		if(mainfirst.getStartPosition()>2)
		{
			Category cover=new Category();
			cover.setTitle("CoverPages");
			cover.setStartPosition(0);
			cover.setEndPosition(mainfirst.startPosition-1);
			cover.setCOVER(true);
			main.add(0,cover);
		}		
		return main;
	}
	
	public List<Category> findCategoryLevel1FromTableOfContent(List<Category> tableOfContent) throws Exception
	{
		int exclu_start=tableOfContent.get(0).startPosition;
		int exclu_end=tableOfContent.get(tableOfContent.size()-1).getEndPosition();
		int end=this.aInfo.content.length();
		int start=0;
		List<Category> res=new ArrayList<Category>(); 
		for(Category tc:tableOfContent)
		{
			// search first half
			if(tc.isTableOrFigure()) continue;
			Category tmp=searchByTitleMatch(tc,exclu_end,end);
			if(tmp==null)
			{				 
				 tmp=searchByTitleMatch(tc,0,exclu_start);
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
	
	// this method will recursivly search for the category
	// if could not find it will try to reduce the matching requirements
	// it will exclude the the current 
	public Category searchByTitleMatch(Category tb_item, int  start, int end)
	{
		String searchToken=tb_item.getTitle();
		if(searchToken.length()<10) return null; // why 10???
		int posi=searchToken.indexOf("....");
		// preprocess remove tailing token
		if(posi>0)
		{
			searchToken=searchToken.substring(0,posi);
		}
		 else
		{
		   posi=searchToken.lastIndexOf(" ");
		   if(posi>-1)
		   {
			   searchToken=searchToken.substring(0,posi).trim();
		   }
		}
		
		
		// trying to find match
		// always try to find first match
		System.out.println("[start:"+start+" end:"+end);
		
		String content=aInfo.getUpperCaseContent().substring(start, end);
		searchToken=searchToken.toUpperCase();
		boolean found=false;
		int original_len=searchToken.length();
		int matchPosi=content.indexOf(searchToken);
		if(matchPosi>-1)
		{
			found=true;
			System.out.println("Found token:"+searchToken);
		}
		while(matchPosi==-1)
		{
			posi=searchToken.lastIndexOf(" ");
			if(posi>-1)
			{
				searchToken=searchToken.substring(0,posi);
				System.out.println("search token..."+searchToken);
			}
			else
			{
				break;
			}
			matchPosi=content.indexOf(searchToken);
			if(matchPosi>0)
			{
				found=true;
				break;
			}
		}
		if(searchToken.length()*2<original_len)
		{
			found=false;
		}
		if(found)
		{
			Category c=new Category();
			c.setMatchedToken(searchToken);
			c.setStartPosition(matchPosi+start);
			String title=tb_item.getTrimTitle();			
			c.setTitle(title);
			c.setLevel(tb_item.getLevel());
			return c;
		}
		else
		{
			return null;
		}
	}
	
	public CategoryRegexPattern identifyArticleCategoryPattern() throws Exception
	{
		if(!this.iscontentPrepared)
		{
			throw new Exception ("Content not loaded and prepared!!");
		}
		CategoryRegexPattern res=new CategoryRegexPattern();
		int max_matches=0;
		int max_TOC_pattern=0;
		for(String cat1regex:CategoryRegexPattern._ROOT_LIST_)
		{
			List<Category> matches=this.extractCategory(aInfo.getContent(), cat1regex, 0);
			// first check key pattern
			int cur_toc_pattern_match=0;

			if(matches!=null&&matches.size()>0)
			{
				for(Category c:matches)
				{
					if(c.getTitle().indexOf("...")>-1)
					{
						cur_toc_pattern_match++;
					}
				}
			}
			if(max_TOC_pattern<cur_toc_pattern_match&&cur_toc_pattern_match>0)
			{
				res.setRoot(cat1regex);
				max_TOC_pattern=cur_toc_pattern_match;
			}
			
			if((max_matches<matches.size())&&(max_TOC_pattern==0))
			{
				res.setRoot(cat1regex);
				max_matches=matches.size();
			}
		}
		if(max_matches==0&&max_TOC_pattern==0)
		{
			throw new Exception ("no supported pattern matches!");
		}
		else
		{
			this.articleCategoryPattern=res;	
			//System.out.println("find pattern:"+res.getRoot());
			return res;
		}
		
	}
	
	public void buildPageNumber(Category c)
	{
		c.setStartPage(indexToPageNumber(c.getStartPosition()));
		c.setEndPage(indexToPageNumber(c.getEndPosition()));
		if(c.subCategory!=null&&c.subCategory.size()>0)
		{
			for(Category sub:c.getSubCategory())
			{
				buildPageNumber(sub);
			}
		}
	}
	// this method will parse out all category recursively
	public List<Category> parseAll()
	{
		List<Category> matches=this.extractCategory(aInfo.getContent(), articleCategoryPattern.getRoot(), 0);
		for(Category c:matches)
		{
		//	c.printMe(System.out);
			parseCategoryRecursively(c,articleCategoryPattern,1);
			System.out.println("------------------------------------------");
			c.printMe(System.out);
		}
		return matches;
	}
	
	public void parseCategoryRecursively(Category c, CategoryRegexPattern regex, int recursiveLevel)
	{
		//System.out.println("start of search:"+c.getTitle());
		String pattern=regex.getSubCategoryRegex(recursiveLevel);
		List<Category> subs=extractCategory(aInfo.getContent().substring(c.getStartPosition(), c.getEndPosition()), pattern, c.getStartPosition());
		if(subs.size()>0)
		{
			for(Category sub:subs)
			{
				sub.setLevel(recursiveLevel);
			}
			c.setSubCategory(subs);
			// now parse the recursive level
			for(Category sub: subs)
			{			
				
				parseCategoryRecursively(sub,regex,recursiveLevel+1);
			}
		}		
		//System.out.println("end of search:"+c.getTitle());
	}

	public void mergeCategorys(List<Category> input_list)
	{
		for(Category c:input_list)
		{
//			System.out.println("^^^^^^^^^^^^^!!#$!@#$!#@ merging:"+c.getTitle());
			mergeMultiLineTitle(c);
		}
	}
	// here all list are parsed recursively
	// content length are calculated
	// this method help find larggest empty category block and use it as the begining of table of content
	public int findTableContentStartIndex(List<Category> raw_list)
	{
		int max_size=0;
		int max_index=-1;
		int cur_size=0;
		int cur_index=-1;		

		for(Category c:raw_list)
		{
			boolean flag=c.doesCategoryHasContent();
			if(!flag)
			{
				// empty category
				if(cur_index==-1)
				{
					cur_index=raw_list.indexOf(c);
					cur_size+=c.getNodeCount();
				}
				else
				{
					// not the start of the block
					cur_size+=c.getNodeCount();
				}
			} 
			else
			{
				// not an empty node
				if(cur_size>max_size)
				{
					// swap the node index
					max_size=cur_size;
					max_index=cur_index;
				}
				// now reset counter
				cur_size=0;
				cur_index=-1;
			}
		}
		// now further correction for table of content and "...."
		// check for table to content
		// check for  first "...."
		int i_toc=-1;
		int i_toc_sign=-1;
		for(int index=max_index;index<raw_list.size();index++)
		{
			String title=raw_list.get(index).getTitle();
			if(i_toc==-1&&title.indexOf("Table of Content")>-1)
			{
				i_toc=index+1;
			}
			if(i_toc_sign==-1&&title.indexOf("....")>-1)
			{
				i_toc_sign=index;
			}
		}
		if(i_toc_sign!=-1)
		{
			max_index=i_toc_sign;
		}
		else if(i_toc!=-1)
		{
			max_index=i_toc;
		}
		System.out.println("max_size:"+max_size);
		System.out.println("start index :"+max_index);
		System.out.println("start title:"+raw_list.get(max_index).getTitle());
		return max_index;
	}
	
	public List<Category> findTableOfContent(List<Category> raw_list)
	{
		// find the start index
		int start_index=this.findTableContentStartIndex(raw_list);
		// find the end position of the table of content
		
		List<String> titles=new ArrayList<String>();
		int last_index=-1;
		for(int i=start_index;i<raw_list.size();i++)
		{
			Category c=raw_list.get(i);
			last_index=i;
			if(c.doesCategoryHasContent()) break;
			//System.out.println("Adding title>>>>>>"+c.getTitle());
			//titles.add(c.getTitle());
			c.getTitlesFlat(titles);
		}
		
		for(String t:titles)
		{
			System.out.println("XXXXXXXXX "+t);
		}
		// find matching title
		boolean found=false;
		for(int j=last_index;j<raw_list.size();j++)
		{
			Category l=raw_list.get(j);
			System.out.println("search end: on --->"+l.getTitle());
			if(searchTitleDeep(l,titles))
			{
				last_index=j;
				found=true;
				System.out.println("find end of table of index:"+l.getTitle());
				break;
			}
		}
		List<Category> tableofcontent=new ArrayList<Category>();
		if(found)
		{
			for(int i=start_index;i<last_index;i++)
			{
				tableofcontent.add(raw_list.get(i));
			}
		}
		else
		{
			// only look for ... and also using ... as sub folder boundary
			for(int i=start_index;i<last_index;i++)
			{
				Category rawele=raw_list.get(i);
				if(rawele.getTitle().indexOf(".....")>0)
				{
					this.filterSubCategoryRecursive(rawele, "....");
					tableofcontent.add(rawele);
				}
			}
		}
		return tableofcontent;
	}
	
	public void filterSubCategoryRecursive(Category root,String filter)
	{
		List<Category> subs=root.getSubCategory();
		if(subs!=null&&subs.size()>0)
		{			
			int size=subs.size();
			for(int i=size-1;i>=0;i--)
			{
				Category c=subs.get(i);
				if(c.getTitle().indexOf(filter)==-1&&c.doesCategoryHasContent()) // dont have filter
				{
					
						subs.remove(c);						
				}
				// reset the last position of category
				if(subs.size()>0)
				{
					root.setEndPosition(subs.get(subs.size()-1).getEndPosition());
				}
				else
				{
					root.setEndPosition(root.getStartPosition()+root.getTitle().length());
				}
			}
			for(Category sub:subs)
			{
				filterSubCategoryRecursive(sub,filter);
			}
		}
	}
	
	public boolean searchTitleDeep(Category c,List<String> l)
	{
		List<String> titles=new ArrayList<String>();
		c.getTitlesFlat(titles);
		for(String t:titles)
		{
			for(String t_toc:l)
			{
				if(t_toc.toUpperCase().indexOf(t.toUpperCase())==0)
				{
					return true;
				}
			}
		}
		return false;
	}
	public boolean searchTitle(String title, List<String> l)
	{
		for(String c:l)
		{
			if(c.toUpperCase().indexOf(title.toUpperCase())==0)
			{
				return true;
			}
		}
		return false;
	}
	
	public void mergeMultiLineTitle(Category c)
	{
		if(c.doesCategoryHasContent())
		{
		
			// first if content only has two line
			String content=this.aInfo.getContent().substring(c.getStartPosition(),c.getEndPosition());
			String[] c_ary=content.split("\\n");
			System.out.println("merging title:>>>>>>>"+c.getTitle()+" "+c_ary.length);
			
			if(c_ary.length==3)
			{
				c.setTitle((c_ary[0].trim()+" "+c_ary[1].trim()+" "+c_ary[2].trim()).trim());
			}
			else if(c_ary.length==4)
			{
				if(c_ary[3].indexOf(".....")>-1)
				{
					c.setTitle((c_ary[0].trim()+" "+c_ary[1].trim()+" "+c_ary[2].trim()+" "+c_ary[3].trim()).trim());
				}
			}else if(c_ary[2].indexOf("......")>-1)
			{
				c.setTitle((c_ary[0].trim()+" "+c_ary[1].trim()+" "+c_ary[2].trim()).trim());
			}
			System.out.println("new title:"+c.getTitle());
		}
			// 	now merging the sub category line
		if(c.subCategory!=null&&c.subCategory.size()>0)
		{
			for(Category sub:c.subCategory)
			{
				mergeMultiLineTitle(sub);
			}
		}
	}
	@Override
	protected List<String> findPageHeader(){
		List<String> headerList = new ArrayList<String>();
		if(this.possibleHeaderList.size() / 2 > 5){
			// more than 10 pages
			// check from middle of the page number
			int startPage = possibleHeaderList.size() / 2;
			PossiblePageHeaderOrFooter header1 = possibleHeaderList.get(startPage);
			PossiblePageHeaderOrFooter header2 = possibleHeaderList.get(startPage + 1);
			double line1_similarity = StringSimilarity.similarity(header1.getLine1(), header2.getLine1());
			System.out.println("===>>> Similarity 1: " + line1_similarity);
			if(line1_similarity > .6 && header1.getLine1().trim().length() != 0){
				headerList.add(header1.getLine1());
				double line2_similarity = StringSimilarity.similarity(header1.getLine2(), header2.getLine2());
				System.out.println("===>>> Similarity 2: " + line2_similarity);
				if(line2_similarity > 0.6 && header1.getLine2().trim().length() != 0){
					headerList.add(header1.getLine2());
					double line3_similarity = StringSimilarity.similarity(header1.getLine3(), header2.getLine3());
					System.out.println("===>>> Similarity 3: " + line3_similarity);
					if(line3_similarity > .6 && header1.getLine3().trim().length() != 0){
						headerList.add(header1.getLine3());
					}
				}
			}
			
			System.out.println("===== Page Header ======");
			System.out.println(">>>" + headerList + "<<<");
		}
		return headerList;
	}
}
