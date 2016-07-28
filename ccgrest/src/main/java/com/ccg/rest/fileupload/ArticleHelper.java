package com.ccg.rest.fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ccg.common.data.ArticleMetaData;
import com.ccg.dataaccess.entity.CCGArticle;
import com.ccg.dataaccess.entity.CCGCategory;
import com.ccg.dataaccess.entity.CCGContent;
import com.ccg.dataaccess.entity.CCGSubcategory;
import com.ccg.ingestion.extract.ArticleCategoryPattern;
import com.ccg.ingestion.extract.ArticleCategoryPatternConfig;
import com.ccg.ingestion.extract.ArticleInfo;
//import com.ccg.common.data.Category;
import com.ccg.ingestion.extract.Category;
import com.ccg.ingestion.extract.ExtractArticleInfo;
import com.ccg.services.data.CCGDBService;
import com.ccg.util.ConfigurationManager;

public class ArticleHelper {
	
	@Autowired
	CCGDBService dataservice;
	
	{
	   	@SuppressWarnings("resource")
		ApplicationContext context = 
		      	  new ClassPathXmlApplicationContext(new String[]{
		      			  "com/ccg/config/ccg_coreservices_beans.xml",
		      			  "com/ccg/config/ccg_dataacess_beans_test.xml",
		      			  "com/ccg/config/ccgrest-servlet.xml"
		      	  });
		dataservice = context.getBean(CCGDBService.class);		
	}
	
	private String repositoryDirectory;
	
	public ArticleHelper(){
		ConfigurationManager cm = new ConfigurationManager();		
		Properties prop = ConfigurationManager.getConfig("/ccg.properties");
		repositoryDirectory = prop.getProperty("article.repository", "."); // default to current directory;
	}
	
	public void saveArticle(RequestData requestData) throws IOException {
		
		CCGArticle article = new CCGArticle();
		File file = new File(requestData.getFilepath());		
		InputStream is = new FileInputStream(file);

		ExtractArticleInfo extract = new ExtractArticleInfo();
		ArticleInfo info = extract.fromPDF(is, getMatchPattern(requestData.getPattern()));
		
		article.setArticleType(info.getType());

//		String title = info.getTitle();
//
//		if(title == null || title.trim().length() == 0){
//			title = requestData.getFilename();
//		}
		
		String title = requestData.getFilename();
		
		article.setTitle(title);
		
		CCGContent content = new CCGContent();
		content.setContent(info.getContent());
		content.setContentTitle(title);
		content.setLength(info.getContent().length());
		content.setFilename(requestData.getFilename());
		content.setUrl(requestData.getFilepath());
		
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
		dataservice.saveArticle(article);		
		Integer articleId = article.getArticleID();

		System.out.println("==== articleId: " + articleId);
		
		ArticleMetaData meta = new ArticleMetaData();
		meta.setArticleId(articleId);
		meta.setCompany(requestData.getCompany());
		meta.setCreateDate(new Date());
		meta.setLastUpdateDate(new Date());
		meta.setTitle(title);
		meta.setType(requestData.getArticleType());
		meta.setAcceptStatus(requestData.getAcceptStatus());
		
		dataservice.saveOrUpdateArticleMetaData(meta);
		dataservice.indexingArticle(articleId);
		
	}
	
	
	public String getCategoryForVerify(RequestData requestData) throws IOException{
		
		File file = new File(requestData.getFilepath());
		
		InputStream myinputstream = new FileInputStream(file);
		ExtractArticleInfo extract = new ExtractArticleInfo();
		ArticleInfo info = extract.fromPDF(myinputstream, getMatchPattern(requestData.getPattern()));
		
		List<Category> categoryList = info.getCategoryList();

		System.out.println(info.getTitle());
		//System.out.println(info.getContent());
		
		StringBuffer sb = new StringBuffer();
		
		for(Category cat : categoryList){

			int start = cat.getStartPosition();
			int end = cat.getEndPosition();
			String catTitle = cat.getTitle();
			System.out.println(catTitle + " " + start + ", " + end);
			sb.append(catTitle + " " + start + ", " + end).append("\n");
			for(Category sub : cat.getSubCategory()){
				String subTitle = sub.getTitle();
				int sbstart = sub.getStartPosition();
				int sbend = sub.getEndPosition();
				System.out.println("\t" + subTitle + " " + sbstart + ", " + sbend);
				sb.append("\t" + subTitle + " " + sbstart + ", " + sbend).append("\n");
			}			
		}
		return sb.toString();	
	}
	
	public String saveFileInRepository(InputStream is, String filename) throws IOException{

		File repository = new File(this.repositoryDirectory + File.separator + "article_repository");
		if(!repository.exists()){
			repository.mkdirs();
		}
		File file = new File(this.repositoryDirectory + File.separator + "article_repository/" + filename);
		String path = file.getAbsolutePath();
		System.out.println("===== file path:" + path);
		
		byte[] buffer = new byte[1024];
		boolean b = file.createNewFile();
		if(b){
			OutputStream os = new FileOutputStream(file);
			int readInBytes = -1;
			while((readInBytes = is.read(buffer)) != -1){
				os.write(buffer, 0, readInBytes);
			}
			os.close();
		}
		is.close();
		return path;
	}
	
	private String[] getMatchPattern(String patternName){
		String[] patterns = null;
		try{
			ArticleCategoryPatternConfig config = ConfigurationManager.getConfig(ArticleCategoryPatternConfig.class);
			List<ArticleCategoryPattern> list = config.getPatternConfig();
			for(ArticleCategoryPattern pattern : list){
				if(pattern.getName().equals(patternName)){
					patterns = pattern.getValue();
					break;
				}
			}				
		}catch(Exception e){
			e.printStackTrace();
		}
		return patterns;
	}
}
