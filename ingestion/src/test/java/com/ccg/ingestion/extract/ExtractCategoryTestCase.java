package com.ccg.ingestion.extract;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.Test;

import com.ccg.ingestion.extract.tocfinder.DotTOCFinder;
import com.ccg.ingestion.extract.tocfinder.TOCFinderRegexConstants;
import com.ccg.ingestion.extract.tocfinder.TOCItem;
import com.ccg.util.JsonHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.StringTokenizer;

public class ExtractCategoryTestCase {
	
	@Test
	public void testNoTOCParsing() throws Exception
	{
		String fn="C:\\ccgworkspace\\testfiles\\newtest1.pdf";
		InputStream is = new FileInputStream(
				//new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
				new File(fn));
		ArticleExtractorNoTOC extract = new ArticleExtractorNoTOC();
		extract.processArticle(is);
		List<TOCItem> itemlist=extract.searchSectionMatches(extract.aInfo.content, TOCFinderRegexConstants._ARTICLESECTON_[0]);
		itemlist=extract.validate(itemlist);
		
		for(TOCItem item:itemlist)
		{
			System.out.println(item.getTitle()+" "+item.getClasifyFlag());
			
		}
	}
	//@Test
	public void testProcessArticleNew() throws Exception
	{
		String fn="C:\\ccgworkspace\\testfiles\\test7.pdf";
		InputStream is = new FileInputStream(
				//new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
				new File(fn));
		ExtractArticleInfobyDotTOC extract = new ExtractArticleInfobyDotTOC();
		extract.processArticle(is);
		System.out.println("final categories!!!");
		for(Category c:extract.aInfo.categoryList)
		{
			c.printMe(System.out);
		}
	}
	//@Test
	public void testOneFile() throws Exception
	{
		String fn="C:\\ccgworkspace\\testfiles\\test7.pdf";
		testBuildTOCContent(fn,true);
	}
	//@Test
	public void testAllPDF() throws Exception
	{
		int count =0;
		String dir="C:\\ccgworkspace\\testfiles\\";
		for(int i=1;i<8;i++)
		{
			String fn=dir+"newtest"+i+".pdf";
			if(testBuildTOCContent(fn,false)>0)
			{
				count++;
			}
			System.out.println(fn+"TOC found "+count);
		}
		for(int i=1;i<18;i++)
		{
			String fn=dir+"test"+i+".pdf";
			if(testBuildTOCContent(fn,false)>0)
			{
				count++;
			}
			System.out.println(fn+"TOC found "+count);
		}
		
	}
	//@Test
	public int testBuildTOCContent(String filename,boolean logswitch) throws Exception
	{
		//String filename="C:\\ccgworkspace\\testfiles\\newtest1.pdf";
		InputStream is = new FileInputStream(
				//new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
				new File(filename));
		ExtractArticleInfobyDotTOC extract = new ExtractArticleInfobyDotTOC();
		extract.prepareDocument(is);
		DotTOCFinder f=DotTOCFinder.getTOCTailFinder();
		List<TOCItem> res=f.searchTOC(extract.aInfo.content, TOCFinderRegexConstants._TOCDOTREGEX_, 0);
		System.out.println("size of TOC   ... "+res.size());
		
		for(TOCItem toc:res)
		{
			if(logswitch)
			System.out.println(toc.getTitle()+" "+toc.getStartposi()+","+toc.getEndposi());
			if(toc.isHasSection())
			{
				if(logswitch)
				{
					System.out.println(toc.getS_toc());
				}
			}
			else
			{
				if(logswitch)
				System.out.println("no section");
			}
		}
		List<Category> clist=f.buildCategoryList(res);
		System.out.println("size of Category   ... "+clist.size());
		for(Category c:clist)
		{
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$");
			c.printMe(System.out);
		}
		List<int[]> arealist=extract.getTOCCoverArea(clist);
		for(int[] a:arealist)
		{
			if(a!=null)
			{
				System.out.println("area:"+a[0]+","+a[1]);
			}
		}
		return clist.size();
	}
	//@Test
	public void testBuillArticle() throws Exception
	{
		String filename="C:\\ccgworkspace\\testfiles\\newtest7.pdf";
		InputStream is = new FileInputStream(
				//new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
				new File(filename));
		ExtractArticleInfoAuto extract = new ExtractArticleInfoAuto();
		extract.prepareDocument(is);
		DotTOCFinder f=DotTOCFinder.getTOCTailFinder();
		List<TOCItem> res=f.searchTOC(extract.aInfo.content, TOCFinderRegexConstants._TOCDOTREGEX_, 0);
		
		for(TOCItem toc:res)
		{
			System.out.println("title:---->\n"+toc.getTitle());
			/*
			StringTokenizer sk=new StringTokenizer(k);
			String header=sk.nextToken();
			System.out.println("----> header:["+header+"]");
			Matcher matcher = pattern.matcher(header);
			while(matcher.find())
			{
				System.out.println("find header matched!!"+matcher.group(0));
			}
			*/
		}
		
		/*
		ArticleInfo ainfo=extract.processArticle(is);
		for(Category c:ainfo.categoryList)
		{
			c.printMe(System.out);
		}
		*/
		/*
		String json=JsonHelper.toJson(ainfo.categoryList.toArray());
		System.out.println(json);
		Category[] res=JsonHelper.fromJson(json,Category[].class);
		for(Category c:res)
		{
			c.printMe(System.out);
		}
		*/
		
	}
	//@Test
	public void testpullCategory() throws Exception
	{
		InputStream is = new FileInputStream(
				//new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
				new File("C:\\ccgworkspace\\testfiles\\test14.pdf"));
		ExtractArticleInfoAuto extract = new ExtractArticleInfoAuto();
		extract.prepareDocument(is);		
		ArticleInfo info=extract.aInfo;
		extract.identifyArticleCategoryPattern();
		List<Category> list=extract.parseAll();		
		extract.mergeCategorys(list);
		info.setCategoryList(list);
	
		System.out.println("---> raw");
		for(Category c:list)
		{
			c.printMe(System.out);
		}
	
		List<Category> tableofcontent=extract.findTableOfContent(list);
		
		List<Category> main=extract.buildMainCategory(tableofcontent);
		System.out.println("---> Real Category");
		for(Category c:main)
		{
				extract.buildPageNumber(c);
				c.printMe(System.out);
		}
		
		
	}
	
	//@Test
	public void testmatchedPattern()
	{
		/*
		String pstr="\\d{1,3}+\\xA0\\s";
		String text="1  PSIM Solution ......................................................................................................................... 4 ";
		char[] c_list=text.toCharArray();
		System.out.println((int)c_list[1]);
		System.out.println((int)c_list[2]);
		Pattern p = Pattern.compile(pstr);
		Matcher m=p.matcher(text);
		while(m.find())
		{
			System.out.println(m.start());
		}
		*/
		String s1="1  PSIM Solution ......................................................................................................................... 4 ";
		String s2="1 PSIM Solution ";
		System.out.println(s1.indexOf(s2));
	}
}
