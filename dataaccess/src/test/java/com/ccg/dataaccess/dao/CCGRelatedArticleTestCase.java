package com.ccg.dataaccess.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import com.ccg.dataaccess.dao.api.CCGRelatedArticleDAO;
import com.ccg.dataaccess.entity.CCGRelatedArticle;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/ccg_dataacess_beans_test.xml")
@Transactional
public class CCGRelatedArticleTestCase {

	@Autowired
	private CCGRelatedArticleDAO r_articleDAO;
	@Test
	public void testAnnotation() {
		CCGRelatedArticle ra=r_articleDAO.findById(1);
		System.out.println(ra);
	}

}
