package com.ccg.article.services;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.ccg.article.services.data.ArticleBasicInfo;
import com.ccg.article.services.data.ArticleCategory;
import com.ccg.article.services.data.ArticleContent;
import com.ccg.article.services.data.CategoryContent;
import com.ccg.article.services.handler.ArticleHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("article")
public class ArticleRestService {

	@GET
	@Path("basicInfo/listAll")
	@Produces("application/json")
	public Response getArticleBasicInfo() {
		ArticleHandler handler = new ArticleHandler();
		String resultJson = "";
		try {
			List<ArticleBasicInfo> list = handler.getListArticleBasicInfo();
			resultJson = toJson(list);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Response.ok().entity(resultJson).build();
	}
	
	@GET
	@Path("{articleID}/basicInfo")
	@Produces("application/json")
	public Response getArticleBasicInfoById(@PathParam("articleID") Integer articleID) {
		ArticleHandler handler = new ArticleHandler();
		String resultJson = "";
		try {
			ArticleBasicInfo info = handler.getArticleBasicInfo(articleID);
			resultJson = toJson(info);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Response.ok().entity(resultJson).build();
	}
	
	@GET
	@Path("content/{contentID}")
	@Produces("application/json")
	public Response getArticleContent(@PathParam("contentID") Integer contentID) {
		
		ArticleHandler handler = new ArticleHandler();
		String resultJson = "";
		
			try {
				ArticleContent content = handler.getContent(contentID);
				resultJson = toJson(content);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return Response.ok().entity(resultJson).build();
	}
	
	@GET
	@Path("{articleID}/category")
	@Produces("application/json")
	public Response getArticleCategory(@PathParam("articleID") Integer articleID){
		
		ArticleCategory ac = new ArticleCategory();
		ArticleHandler handler = new ArticleHandler();
		String resultJson = "";
		try {
			ac = handler.getCategory(articleID);
			resultJson = toJson(ac);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.ok().entity(resultJson).build();
	}
	
	@GET
	@Path("category/{categoryID}/content")
	@Produces("application/json")
	public Response getCategoryContent(@PathParam("categoryID") Integer categoryID){
		ArticleHandler handler = new ArticleHandler();
		String resultJson = "";
		try {
			CategoryContent cc = handler.getCategoryContent(categoryID);
			resultJson = this.toJson(cc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok().entity(resultJson).build();
	}
	
	private String toJson(Object obj){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}
}
