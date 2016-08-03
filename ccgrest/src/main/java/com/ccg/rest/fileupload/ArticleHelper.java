package com.ccg.rest.fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import com.ccg.util.JSON;

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
	
	private static final String TEMP_REPOSITORY = "temp";
	private static final String ARTICLE_REPOSITORY = "article";
	private static final String PATTERN_REPOSITORY = "pattern";
	
	private String repositoryDirectory;
	
	public ArticleHelper(){	
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
		is.close();
		
		/////////////
		// move file from temp to repository
		/////////////
		File articleRepository = new File(this.repositoryDirectory + File.separator + ARTICLE_REPOSITORY);
		if(!articleRepository.exists()){
			articleRepository.mkdirs();
		}
		String filename = requestData.getFilename();
		Path source = Paths.get(requestData.getFilepath());
		Path destination = Paths.get(this.repositoryDirectory + File.separator + ARTICLE_REPOSITORY + File.separator + filename);
		Files.move(source, destination, StandardCopyOption.ATOMIC_MOVE);
		String articleURL = destination.toString();
				
		//////////
		// save pattern for future use(?)
		//////////
		File patternRepository = new File(this.repositoryDirectory + File.separator + PATTERN_REPOSITORY);
		if(!patternRepository.exists()){
			patternRepository.mkdirs();
		}
		File patternFile = new File(this.repositoryDirectory + File.separator + PATTERN_REPOSITORY + File.separator + filename);
		FileOutputStream fos = new FileOutputStream(patternFile);
		String jsonString = JSON.toJson(getMatchPattern(requestData.getPattern()));
		fos.write(jsonString.getBytes("UTF-8"));
		fos.close();
		
		////////////
		// use file as title, filename start with timestamp, remove it
		////////////
		String title = requestData.getFilename();
		int position = filename.indexOf("_");
		if(position != -1){
			title = filename.substring(position+1);
		}
		
//		String title = info.getTitle();
//
//		if(title == null || title.trim().length() == 0){
//			title = requestData.getFilename();
//		}
		article.setTitle(title);
		
		CCGContent content = new CCGContent();
		content.setContent(info.getContent());
		content.setContentTitle(title);
		content.setLength(info.getContent().length());
		content.setFilename(requestData.getFilename());
		content.setUrl(articleURL);
		
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

	public String saveTempFile(InputStream is, String filename) throws IOException{
		File tempRepository = new File(this.repositoryDirectory + File.separator + TEMP_REPOSITORY);
		if(!tempRepository.exists()){
			tempRepository.mkdirs();
		}
		File file = new File(this.repositoryDirectory + File.separator + TEMP_REPOSITORY + File.separator + filename);
		
		String path = file.getAbsolutePath();
		//System.out.println("===== file path:" + path);
		
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
