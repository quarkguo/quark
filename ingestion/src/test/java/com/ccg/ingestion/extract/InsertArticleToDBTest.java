package com.ccg.ingestion.extract;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ccg.dataaccess.dao.api.CCGArticleDAO;
import com.ccg.dataaccess.dao.impl.CCGArticleDAOImpl;
import com.ccg.dataaccess.entity.CCGArticle;
import com.ccg.dataaccess.entity.CCGCategory;
import com.ccg.dataaccess.entity.CCGContent;
import com.ccg.dataaccess.entity.CCGSubcategory;
import com.ccg.ingestion.extract.ArticleInfo;
import com.ccg.ingestion.extract.ArticleTypePattern;
import com.ccg.ingestion.extract.Category;
import com.ccg.ingestion.extract.ExtractArticleInfo;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/ccg_dataacess_beans_test.xml")
@Transactional
public class InsertArticleToDBTest {

	@Autowired
	private CCGArticleDAO articleDAO = new CCGArticleDAOImpl();
	
	@Rollback(false)
	@Test
	public void insertArticle() throws Exception{
		CCGArticle article = new CCGArticle();
		
//		InputStream is = new FileInputStream(
//				new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
//		ExtractArticleInfo extract = new ExtractArticleInfo();
//		ArticleInfo info = extract.fromPDF(is, ArticleTypePattern.PROPOSALS);
		
		InputStream is = new FileInputStream(
				//new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf"));
				new File("/Users/zchen323/Downloads/AFNCRITSAlliantCorpsFinal.pdf"));
		ExtractArticleInfo extract = new ExtractArticleInfo();
		ArticleInfo info = extract.fromPDF(is, ArticleTypePattern.PROPOSALS_2);
		
		article.setArticleType(info.getType());
		article.setTitle(info.getTitle().trim());
		//article.set
		
		CCGContent content = new CCGContent();
		content.setContent(info.getContent());
		content.setContentTitle(info.getTitle());
		content.setLength(info.getContent().length());
		article.setContent(content);
		article.setRfpReference(0);
		article.setDomain("domain");
		article.setSubdomain("subdomain");
		
		//List<CCGCategory> categoryList = new ArrayList<CCGCategory>();
		
		for(Category c : info.getCategoryList()){
			CCGCategory cat = new CCGCategory();
			cat.setArticle(article);
			cat.setCategorycontent(info.getContent().substring(c.getStartPosition(), c.getEndPosition()));
			cat.setCategorytitle(c.getTitle());
			cat.setStartposi(c.getStartPosition());
			cat.setEndposi(c.getEndPosition());
			cat.setStartpage(c.getStartPage());
			cat.setEndpage(c.getEndPage());
			article.getCategorylist().add(cat);
			for(Category sub : c.getSubCategory()){
				CCGSubcategory subCat = new CCGSubcategory();
				subCat.setArticle(article);
				subCat.setCategory(cat);
				subCat.setSubcategorytitle(sub.getTitle());
				subCat.setCreatedTS(new Date());
				subCat.setLastupdateTS(new Date());
				subCat.setStartposi(sub.getStartPosition());
				subCat.setEndposi(sub.getEndPosition());
				subCat.setStartpage(sub.getStartPage());
				subCat.setEndpage(sub.getEndPage());
				cat.getSubcategorylist().add(subCat);
				
			}
		}
		articleDAO.save(article);	
	}
	
//	public static void main(String[] args) throws Exception{
//		File file = new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf");
//		new InsertArticleToDBTest().insertArticle(file);
//	}
}
