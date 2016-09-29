package com.ccg.ingestion.extract;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.junit.Test;

import com.ccg.util.JsonHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

public class ExtractCategoryTestCase {
	
	@Test
	public void testBuillArticle() throws Exception
	{
		InputStream is = new FileInputStream(
				//new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
				new File("C:\\ccgworkspace\\testfiles\\newtest2.pdf"));
		ExtractArticleInfoAuto extract = new ExtractArticleInfoAuto();
		ArticleInfo ainfo=extract.processArticle(is);
		for(Category c:ainfo.categoryList)
		{
			c.printMe(System.out);
		}
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
		String text="1� PSIM Solution ......................................................................................................................... 4�";
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
		String s1="1� PSIM Solution ......................................................................................................................... 4�";
		String s2="1 PSIM Solution ";
		System.out.println(s1.indexOf(s2));
	}
}