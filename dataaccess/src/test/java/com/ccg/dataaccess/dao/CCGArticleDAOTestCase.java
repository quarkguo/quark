package com.ccg.dataaccess.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ccg.dataaccess.dao.api.CCGArticleDAO;
import com.ccg.dataaccess.entity.CCGArticle;
import com.ccg.dataaccess.entity.CCGCategory;
import com.ccg.dataaccess.entity.CCGContent;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/ccg_dataacess_beans_test.xml")
@Transactional
public class CCGArticleDAOTestCase {

	@Autowired
	private CCGArticleDAO articleDAO;
	
//	@Test
	@Rollback(true)
	public void testCountAll() {
		int number=articleDAO.countAll();
		System.out.println("number:"+number);
	}

//	@Test
	@Rollback(true)
	public void testCreate(){
		CCGArticle a=new CCGArticle();
		a.setArticleType("proposal");
		a.setDomain("DOE");
		a.setEndPosi(10);
		a.setStartPosi(0);
		a.setTitle("Test");
		a.setSubdomain("IT Security");
		articleDAO.save(a);
		int number=articleDAO.countAll();
		System.out.println("number:"+number);
	}
	
//	@Test
	@Rollback(false)
	public void testCasecadeContentMapping()
	{
		CCGArticle a=new CCGArticle();
		a.setArticleType("proposal");
		a.setDomain("DOE");
		a.setEndPosi(10);
		a.setStartPosi(0);
		a.setTitle("Test");
		a.setSubdomain("IT Security");
		CCGContent content=new CCGContent();
		content.setContent("hello world");
		content.setContentTitle("test");
		content.setFilename("testfilename");
		content.setLength(100);
		content.setMetatype("pdf");
		content.setUrl("http://ccg.com");
		a.setContent(content);
		articleDAO.save(a);
	}
	
	@Test
	@Rollback(false)
	public void testCasecadeCategoryMapping()
	{
		CCGArticle a=new CCGArticle();
		a.setArticleType("proposal");
		a.setDomain("DOE");
		a.setEndPosi(10);
		a.setStartPosi(0);
		a.setTitle("Test");
		a.setSubdomain("test case2");
		for(int i=0;i<3;i++)
		{
			CCGCategory cg=new CCGCategory();
			cg.setArticle(a);
			cg.setCategorycontent("cate"+i);
			cg.setCategoryref(i+".");
			cg.setStartposi(1);
			cg.setEndposi(2);
			cg.setCategorytitle("title"+1);
			cg.setCategoryseq(i);
			cg.setType("main");
			a.getCategorylist().add(cg);
		}
		articleDAO.save(a);
	}
}
