package com.ccg.rest.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccg.common.data.ArticleBasicInfo;
import com.ccg.common.data.ArticleContent;
import com.ccg.common.data.ArticleMetaData;
import com.ccg.common.data.CategoryContent;
import com.ccg.common.data.SearchResult2;
import com.ccg.common.data.SubCategoryContent;
import com.ccg.common.data.WCategory;
import com.ccg.common.pdf.util.PdfUtil;
import com.ccg.ingestion.extract.ArticleCategoryPatternConfig;
import com.ccg.services.data.CCGDBService;
import com.ccg.services.index.SearchEngine;
import com.ccg.util.ConfigurationManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.DocumentException;


@RestController
public class TestHandler {

	@Autowired
	CCGDBService dataservice;
	
	@RequestMapping(value="/hello",method=RequestMethod.GET)
	public String hello(ModelMap para)
	{
		para.addAttribute("msg","hello world message");
		return "hello world!!";
	}
	
	@RequestMapping(value="/articleList",method=RequestMethod.GET)
	public String articleList(ModelMap para)
	{
		String res=dataservice.getArticleListJson();
		System.out.println(res);
		return res;
	}
	
	@RequestMapping(value="/countArticle",method=RequestMethod.GET)
	public String countArticle(ModelMap para)
	{
		para.addAttribute("msg","Count article");
		if(dataservice==null)
		{
			System.out.println("autowired failed");
		}
		else
		{
			System.out.println("autowired success!!");
		}
		return dataservice.getArticleCouont()+" henry is a chabby boy and Smarty pants!!! <img src=image.jpg/>";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/article/basicInfo/listAll")
	public ResponseEntity<String> getArticleBasicInfoList() {
		List<ArticleBasicInfo> infoList =dataservice.getArticleBasicInfo();
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(toJson(infoList), responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/article/{articleId}/content")
	public ResponseEntity<String> getContent(@PathVariable("articleId") Integer articleId) {
		
		// security check firt
		// TODO
		
		
		ArticleContent content = dataservice.getArticleContent(articleId);
		String json = toJson(content);
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(json, responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/content/{contentId}")
	public ResponseEntity<String> getContentById(@PathVariable("contentId") Integer contentId) {
		ArticleContent content = dataservice.getContentById(contentId);
		String json = toJson(content);
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(json, responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/article/{articleId}/category")
	public ResponseEntity<String> getCategoryByArticleId(@PathVariable("articleId") Integer articleId) {
		List<WCategory> catList = dataservice.getCategoryByArticleId(articleId);
		String json = toJson(catList);
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(json, responseHeaders, HttpStatus.CREATED);
	}

	@RequestMapping(method=RequestMethod.GET, value="/category/{categoryId}/content")
	public ResponseEntity<String> getCategoryContentById(@PathVariable("categoryId") Integer categoryId, HttpServletRequest request) {
		// TODO security check
		String user = request.getRemoteUser();
		
		
		CategoryContent content = dataservice.getCategoryContentById(categoryId);
		String json = toJson(content);
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(json, responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/subcategory/{subcategoryId}/content")
	public ResponseEntity<String> getSubCategoryContentById(@PathVariable("subcategoryId") Integer subcategoryId) {
		SubCategoryContent content = dataservice.getSubCategoryContentById(subcategoryId);
		String json = toJson(content);
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(json, responseHeaders, HttpStatus.CREATED);
	}	
	@RequestMapping(method = RequestMethod.GET, value="/article/{articleId}/metadata")
	ResponseEntity<String> getArticleMetadata(@PathVariable("articleId") Integer articleId) {
		String json = "";
		ArticleMetaData metadata = dataservice.getArticleMetaDataByArticleId(articleId);		
//		meta.setAcceptStatus("acceptStatus");
//		meta.setArtileId(articleId);
//		meta.setAuthor("author");
//		meta.setCompany("company");
//		meta.setCreateDate(new Date());
//		meta.setLastUpdateDate(new Date());
//		meta.setPraisalscore(12.0f);
//		meta.setTitle("title");
//		meta.setType("type");
		
		json = toJson(metadata);
		
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(json, responseHeaders, HttpStatus.CREATED);
	}	
	
	@RequestMapping(method = RequestMethod.POST, value="/article/metadata")
	ResponseEntity<?> addArticleMetadata(@RequestBody String input) {
		String json = "";
		System.out.println("====>>>" + input + "<<<<<====");
		
		ArticleMetaData meta = fromJson(input, ArticleMetaData.class);
		
		json = toJson(meta);
		
		System.out.println(json);
		if(meta.isDeleteArticle()){
			dataservice.deleteArticle(meta.getArticleId());
		}else{
			dataservice.saveOrUpdateArticleMetaData(meta);
		}
		
		
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(input, responseHeaders, HttpStatus.CREATED);
	}
	
	// indexing rest service
	@RequestMapping(method=RequestMethod.GET, value="/indexing/article/{articleId}")
	public ResponseEntity<String> indexingArticle(@PathVariable("articleId") Integer articleId) {
		String json = "";
		GenericResponseMessage response = new GenericResponseMessage();
		//CCGArticle article = dataservice.getCCGArticleById(articleId);
		//Indexer indexer = new Indexer();
		try{
			//indexer.indexArticle(article);
			
			dataservice.indexingArticle(articleId);
			
			response.code = 0;
			response.status = "success";
		}catch(Exception e){
			response.status = "fail";
			response.code = 1;
			response.message = e.getMessage();
			e.printStackTrace();
		}
		json = toJson(response);
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(json, responseHeaders, HttpStatus.CREATED);
	}		
	
	@RequestMapping(method=RequestMethod.GET, value="/indexing/article/all")
	public ResponseEntity<String> indexingAllArticle() {
		String json = "";
		GenericResponseMessage response = new GenericResponseMessage();
		//List<CCGArticle> articleList = dataservice.getAllCCGArticle();
		//Indexer indexer = new Indexer();
		try{
			//indexer.rebuildIndexes(articleList);;
			dataservice.indexingAll2();
			response.code = 0;
			response.status = "success";
		}catch(Exception e){
			response.code = 1;
			response.status = "fail";
			response.message = e.getMessage();
			e.printStackTrace();
		}
		json = toJson(response);
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(json, responseHeaders, HttpStatus.CREATED);
	}		
	
	@RequestMapping(method=RequestMethod.GET, value="/search")
	public ResponseEntity<String> search(
			@RequestParam(value="query", required=false) String query,
			@RequestParam(value="limit", required=false) String limit) {
		
		int default_limit = 1000;
		if(limit != null){
			try{
				default_limit = Integer.parseInt(limit);
			}catch(Exception e){
				;
			}
		}
		
		String json = "";
		try {
			SearchEngine se = new SearchEngine();
			List<SearchResult2> srList = se.search2(query, default_limit);
			srList = dataservice.filterDeletedResult(srList);
			
			List<WCategory> res=dataservice.buildSearchCategory(srList,query);
			json = toJson(res);
			
		//	System.out.println("Search Result: " + json);
			
		} catch (Exception e) {
			e.printStackTrace();
			json = e.getMessage();
		}
		
		
		
		
//		RestResponseMessage rrm = new RestResponseMessage();
//		List<CCGArticle> articleList = dataservice.getAllCCGArticle();
//		Indexer indexer = new Indexer();
//		try{
//			indexer.rebuildIndexes(articleList);;
//			rrm.setSuccess();
//		}catch(Exception e){
//			rrm.setFailed();
//			rrm.setMessage(e.getMessage());
//			e.printStackTrace();
//		}
		//json = toJson(rrm);
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(json, responseHeaders, HttpStatus.CREATED);
	}			
	
	@RequestMapping(value="/article/{articleId}/delete",method=RequestMethod.GET)
	public String deleteArticle(@PathVariable("articleId") Integer articleId)
	{
		dataservice.deleteArticle(articleId);
		return "done";
	}		

	@RequestMapping(value="/article/{articleId}/download",method=RequestMethod.GET)
	public void downloadArticle(@PathVariable("articleId") Integer articleId, HttpServletResponse response) throws Exception
	{
		ArticleContent content = dataservice.getArticleContent(articleId);
		String filename = content.getUrl();
		//try {
			File file = new File(filename);
			byte[] buffer = new byte[1024];
			InputStream is = new FileInputStream(file);
			OutputStream os = response.getOutputStream();
			response.setHeader("content-disposition", "inline; filename=" + file.getName());
			if(file.getName().endsWith("pdf") || file.getName().endsWith("PDF")){
				response.setContentType("application/pdf");
			}
			int readBytes = -1;
			while((readBytes = is.read(buffer)) != -1) {
				os.write(buffer, 0, readBytes);
			}
			os.flush();
			is.close();			
		//} catch (Exception e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}		
	}	
	
	@RequestMapping(value="/article/{articleId}/{selectedPages}/download",method=RequestMethod.GET)
	public void downloadParticalArticle(
			@PathVariable("articleId") Integer articleId, 
			@PathVariable("selectedPages") String selectedPages,  
			HttpServletResponse response) throws Exception
	{
		ArticleContent content = dataservice.getArticleContent(articleId);
		String filename = content.getUrl();
		File pdfFile = new File(filename);
		response.setHeader("content-disposition", "inline; filename=" + pdfFile.getName());
		response.setContentType("application/pdf");		
		OutputStream out = response.getOutputStream();
		getParticalPdf(pdfFile, selectedPages, out);
		out.flush();
		out.close();
	}	

	private void getParticalPdf(File pdfFile, String selectedPages, OutputStream outputStream) throws IOException, DocumentException{
		
		List<Integer> pages = new ArrayList<Integer>();
		
		int startPage = 0;
		int endPage = 0;
		
		// page range 1-5 (from page 1 to page 5)
		if(selectedPages.indexOf("-") != -1){
			String[] pageRanges = selectedPages.split("-");
			startPage = Integer.parseInt(pageRanges[0]);
			endPage = Integer.parseInt(pageRanges[1]);
			
			for(int i = startPage; i < endPage + 1; i++ ){
				pages.add(i);
			}
		//	selected pages: 1,4,5,9 (page 1, page 4, page 5 and page 9)
		}else if(selectedPages.indexOf(",") != -1){			
			String[] pageRanges = selectedPages.split(",");
			for(String string : pageRanges){
				pages.add(Integer.parseInt(string));
			}
		// single page: 5 (page 5)	
		}else{
			pages.add(Integer.parseInt(selectedPages));
		}	
		
		//synchronized(this){
			// extract selected pages
			InputStream is = new FileInputStream(pdfFile);
			PdfUtil.extractSelectPage(is, outputStream, pages);
			is.close();
		//}
	}
	
	@RequestMapping(value="/article/{articleId}/{selectedPages}/{highlightRegEx}/download",method=RequestMethod.GET)
	public void downloadParticalArticleAndHighlightText(
			@PathVariable("articleId") Integer articleId, 
			@PathVariable("selectedPages") String selectedPages,
			@PathVariable("highlightRegEx") String highlightRegEx,
			HttpServletResponse response) throws Exception
	{
		ArticleContent content = dataservice.getArticleContent(articleId);
		String filename = content.getUrl();
		File originalFile = new File(filename);
		
		//if(highlightRegEx.startsWith("\" ") && highlightRegEx.)
		System.out.println("=========>>>>" + selectedPages);
		System.out.println("=========>>>>" + highlightRegEx);
		

		ByteArrayOutputStream particalPdfOutStream = new ByteArrayOutputStream();
		
		getParticalPdf(originalFile, selectedPages, particalPdfOutStream);
		
		byte[] particalPdfBytes = particalPdfOutStream.toByteArray();		
		InputStream is = new ByteArrayInputStream(particalPdfBytes);	
		
		response.setHeader("content-disposition", "inline; filename=" + originalFile.getName());
		response.setContentType("application/pdf");
		OutputStream out = response.getOutputStream();
		PdfUtil.textHighlight(is, out, highlightRegEx);
		//out.flush();
		//out.close();
	}	
	
	
	
	private String toJson(Object obj){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}
	
	private <T>T fromJson(String json, Class<T> type){
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, type);
	}
	
	@RequestMapping(value="/config/pattern",method=RequestMethod.GET)
	public String getConfig()
	{
		ArticleCategoryPatternConfig patternConfig = ConfigurationManager.getConfig(ArticleCategoryPatternConfig.class);
		String patternString = toJson(patternConfig);
		return patternString;
	}		

	
	
}

//class RestResponseMessage{
//	private String status = "failed";
//	private String message;
//	
//	public String getStatus(){
//		return this.status;
//	}
//	
//	public void setSuccess(){
//		this.status = "success";
//	}
//	
//	public void setFailed(){
//		this.status = "failed";
//	}
//	
//	public void setMessage(String message){
//		this.message = message;
//	}
//	public String getMessage(){
//		return this.message;
//	}
//}
