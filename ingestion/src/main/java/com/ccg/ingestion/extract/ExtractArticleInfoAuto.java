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
	// this method prepare the document and prepare the text content 
	// it also remove the header and footer
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
	
	public CategoryRegexPattern identifyArticleCategoryPattern() throws Exception
	{
		if(!this.iscontentPrepared)
		{
			throw new Exception ("Content not loaded and prepared!!");
		}
		CategoryRegexPattern res=new CategoryRegexPattern();
		int max_matches=0;
		for(String cat1regex:CategoryRegexPattern._ROOT_LIST_)
		{
			List<Category> mathces=this.extractCategory(aInfo.getContent(), cat1regex, 0);
			// first check key pattern
			if(mathces!=null&&mathces.size()>0)
			{
				Category first=mathces.get(0);
				if(first.getTitle().indexOf("......")>-1)
				{
					res.setRoot(cat1regex);
					this.articleCategoryPattern=res;
					return res;
				}
			}
			if(max_matches<mathces.size())
			{
				res.setRoot(cat1regex);
				max_matches=mathces.size();
			}
		}
		if(max_matches==0)
		{
			throw new Exception ("no supported pattern matches!");
		}
		else
		{
			this.articleCategoryPattern=res;
			return res;
		}
	}
	
	public List<Category> prepareTableContent()
	{
		return null;
	}
	
	// this method will parse out all category recursively
	public List<Category> parseAll()
	{
		List<Category> matches=this.extractCategory(aInfo.getContent(), articleCategoryPattern.getRoot(), 0);
		for(Category c:matches)
		{
			parseCategoryRecursively(c,articleCategoryPattern,1);
		//	c.printMe(System.out);
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
		System.out.println("max_size:"+max_size);
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
			titles.add(c.getTitle());
		}

		
		// find matching title
		for(int j=last_index;j<raw_list.size();j++)
		{
			Category l=raw_list.get(j);
			if(searchTitle(l.getTitle(),titles))
			{
				last_index=j;				
				break;
			}
		}
		List<Category> tableofcontent=new ArrayList<Category>();
		for(int i=start_index;i<last_index;i++)
		{
			tableofcontent.add(raw_list.get(i));
		}
		return tableofcontent;
	}
	
	public boolean searchTitle(String title, List<String> l)
	{
		for(String c:l)
		{
			if(c.indexOf(title)==0)
			{
				System.out.println("^^^^"+c);
				System.out.println(title);
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
