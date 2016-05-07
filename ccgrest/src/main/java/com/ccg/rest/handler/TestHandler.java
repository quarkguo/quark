package com.ccg.rest.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccg.common.data.ArticleBasicInfo;
import com.ccg.common.data.ArticleContent;
import com.ccg.common.data.Category;
import com.ccg.common.data.CategoryContent;
import com.ccg.services.data.CCGDBService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


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
	
	private String toJson(Object obj){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}
}
