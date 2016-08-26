package com.ccg.ingestion.extract;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


import org.junit.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractCategoryTestCase {
	
	@Test
	public void testBuillArticle() throws Exception
	{
		InputStream is = new FileInputStream(
				//new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
				new File("C:\\ccgworkspace\\testfiles\\test8.pdf"));
		ExtractArticleInfoAuto extract = new ExtractArticleInfoAuto();
		ArticleInfo ainfo=extract.processArticle(is);
		for(Category c:ainfo.categoryList)
		{
			c.printMe(System.out);
		}
	}
	//@Test
	public void testpullCategory() throws Exception
	{
		InputStream is = new FileInputStream(
				//new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
				new File("C:\\ccgworkspace\\testfiles\\test3.pdf"));
		ExtractArticleInfoAuto extract = new ExtractArticleInfoAuto();
		extract.prepareDocument(is);		
		ArticleInfo info=extract.aInfo;
		extract.identifyArticleCategoryPattern();
		List<Category> list=extract.parseAll();		
		extract.mergeCategorys(list);
		info.setCategoryList(list);
	/*
		System.out.println("---> raw");
		for(Category c:list)
		{
			c.printMe(System.out);
		}
		*/
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
		String pstr="\\n\\d+\\.\\d+\\.\\d+\\.\\d+\\s+\\w";
		String text="\r\n2.2.9 Organizational Resources and Management .......................................................... 23 \n2.2.9.1 Organizational Chart ...................................................................................... 23";
		Pattern p = Pattern.compile(pstr);
		Matcher m=p.matcher(text);
		while(m.find())
		{
			System.out.println(m.start());
		}

	}
}
