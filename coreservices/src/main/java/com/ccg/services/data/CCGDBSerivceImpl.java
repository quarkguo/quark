package com.ccg.services.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ccg.common.data.ArticleBasicInfo;
import com.ccg.common.data.ArticleContent;
import com.ccg.common.data.Category;
import com.ccg.dataaccess.dao.api.CCGArticleDAO;
import com.ccg.dataaccess.dao.api.CCGContentDAO;
import com.ccg.dataaccess.entity.CCGArticle;
import com.ccg.dataaccess.entity.CCGContent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("CCGDBService")
public class CCGDBSerivceImpl implements CCGDBService {

	@Autowired	
	private CCGArticleDAO articleDAO;
	
	@Autowired
	private CCGContentDAO contentDAO;
	
	@Override
	public void saveArticle(CCGArticle article) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getArticleCouont() {
		// TODO Auto-generated method stub
		return articleDAO.countAll();
	}
	@Override
	public String getArticleListJson() {
		// TODO Auto-generated method stub
		List<Map<String,Object>> tmp=new ArrayList<Map<String,Object>>();
		List<CCGArticle> res=articleDAO.findAll();	
		
		for(CCGArticle art:res)
		{
			HashMap<String,Object> map=new HashMap<String,Object>();
			map.put("text","Doc"+art.getTitle());
			map.put("cls","file");
			map.put("leaf",new Boolean(true));
			tmp.add(map);
		}
//		for(CCGArticle art :res){
//			art.g
//		}
		
		return toJson(tmp);
	}
	
	private String toJson(Object obj){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}
	@Override
	public List<ArticleBasicInfo> getArticleBasicInfo() {
		
		List<CCGArticle> articles = articleDAO.findAll();
		List<ArticleBasicInfo> infoList = new ArrayList<ArticleBasicInfo>();
		for(CCGArticle article : articles){
			ArticleBasicInfo info = new ArticleBasicInfo();
			info.setArticleID(article.getArticleID());
			info.setTitle(article.getTitle());
			info.setArticleType(article.getArticleType());
			infoList.add(info);
		}
		return infoList;
	}
	@Override
	@Transactional(readOnly=true)
	public ArticleContent getArticleContent(Integer articleId) {
		ArticleContent articleContent = new ArticleContent();
		CCGContent content = articleDAO.findById(articleId).getContent();
		articleContent.setContent(content.getContent());
		articleContent.setContentID(content.getContentID());
		articleContent.setContentTitle(content.getContentTitle());
		articleContent.setLength(content.getLength());
		articleContent.setFileName(content.getFilename());
		return articleContent;
	}
	@Override
	public Category getCategory(Integer articleId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getCategoryContent(Integer categoryId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional(readOnly=true)
	public ArticleContent getContentById(Integer contentId) {
		ArticleContent articleContent = new ArticleContent();
		CCGContent content = contentDAO.findById(contentId);
		articleContent.setContent(content.getContent());
		articleContent.setContentID(content.getContentID());
		articleContent.setContentTitle(content.getContentTitle());
		articleContent.setLength(content.getLength());
		articleContent.setFileName(content.getFilename());
		return articleContent;
	}

}
