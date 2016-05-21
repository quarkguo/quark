package com.ccg.ingestion.extract;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ccg.dataaccess.dao.api.CCGArticleDAO;
import com.ccg.dataaccess.dao.api.CCGArticleMetadataDAO;
import com.ccg.dataaccess.dao.impl.CCGArticleDAOImpl;
import com.ccg.dataaccess.dao.impl.CCGArticleMetadataDAOImpl;
import com.ccg.dataaccess.entity.CCGArticleMetadata;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/ccg_dataacess_beans_test.xml")
@Transactional
public class DeleteAllArticleTest {

	@Autowired
	private CCGArticleDAO articleDAO = new CCGArticleDAOImpl();
	private CCGArticleMetadataDAO metaDAO = new CCGArticleMetadataDAOImpl();
	
	@Rollback(false)
	@Test
	public void insertArticle() throws Exception{
//		List<CCGArticle> list = articleDAO.findAll();
//		for(CCGArticle a : list){
//			articleDAO.delete(a);
//		}
		
		List<CCGArticleMetadata> list = metaDAO.findAll();
		for(CCGArticleMetadata data : list){
			metaDAO.delete(data);
		}
		
	}
	
//	public static void main(String[] args) throws Exception{
//		File file = new File("/Users/zchen323/Downloads/HH60Gsimulatorproposal_sample.docx.pdf");
//		new InsertArticleToDBTest().insertArticle(file);
//	}
}
