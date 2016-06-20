package com.ccg.rest.handler;

import java.util.List;

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
import com.ccg.common.data.Category;
import com.ccg.common.data.CategoryContent;
import com.ccg.common.data.SearchResult;
import com.ccg.common.data.SubCategoryContent;
import com.ccg.dataaccess.entity.CCGArticle;
import com.ccg.services.data.CCGDBService;
import com.ccg.services.index.Indexer;
import com.ccg.services.index.SearchEngine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
@RequestMapping(value="/article")
public class ArticleHandler {

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
		List<Category> catList = dataservice.getCategoryByArticleId(articleId);
		String json = toJson(catList);
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(json, responseHeaders, HttpStatus.CREATED);
	}

	@RequestMapping(method=RequestMethod.GET, value="/category/{categoryId}/content")
	public ResponseEntity<String> getCategoryContentById(@PathVariable("categoryId") Integer categoryId) {
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
		
		dataservice.saveOrUpdateArticleMetaData(meta);
		
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
	    return new ResponseEntity<String>(input, responseHeaders, HttpStatus.CREATED);
	}
	
	// indexing rest service
	@RequestMapping(method=RequestMethod.GET, value="/indexing/article/{articleId}")
	public ResponseEntity<String> indexingArticle(@PathVariable("articleId") Integer articleId) {
		String json = "";
		GenericResponseMessage response = new GenericResponseMessage();
		CCGArticle article = dataservice.getCCGArticleById(articleId);
		Indexer indexer = new Indexer();
		try{
			indexer.indexArticle(article);
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
	
	@RequestMapping(method=RequestMethod.GET, value="/indexing/article/all")
	public ResponseEntity<String> indexingAllArticle() {
		String json = "";
		GenericResponseMessage response = new GenericResponseMessage();
		List<CCGArticle> articleList = dataservice.getAllCCGArticle();
		Indexer indexer = new Indexer();
		try{
			indexer.rebuildIndexes(articleList);;
			response.code = 0;
			response.status = "Success";
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
		
		int default_limit = 100;
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
			List<SearchResult> srList = se.search(query, default_limit);
			json = toJson(srList);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	
	
	
	
	private String toJson(Object obj){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}
	
	private <T>T fromJson(String json, Class<T> type){
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, type);
	}
}
