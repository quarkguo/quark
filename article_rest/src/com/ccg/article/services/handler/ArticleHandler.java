package com.ccg.article.services.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.ccg.article.services.data.ArticleBasicInfo;
import com.ccg.article.services.data.ArticleCategory;
import com.ccg.article.services.data.ArticleContent;
import com.ccg.article.services.data.Category;
import com.ccg.article.services.data.CategoryContent;
import com.ccg.article.services.data.SubCategory;

public class ArticleHandler {
	
	public List<ArticleBasicInfo> getListArticleBasicInfo() throws SQLException{
		
		List<ArticleBasicInfo> list = new ArrayList<ArticleBasicInfo>();
		
		Connection conn = this.getConnection();
		PreparedStatement stm = conn.prepareStatement("select * from ccgarticle");
		ResultSet rs = stm.executeQuery();
		while(rs.next()){
			ArticleBasicInfo info = new ArticleBasicInfo();
			info.setArticleID(rs.getInt("articleID"));
			info.setArticleType(rs.getString("articleType"));
			info.setContentID(rs.getInt("contentID"));
			info.setTitle(rs.getString("title"));
			info.setDomain(rs.getString("domain"));
			list.add(info);
		}
		conn.close();
		return list;
	}

	public ArticleBasicInfo getArticleBasicInfo(Integer articleID) throws SQLException{
		
		ArticleBasicInfo info = new ArticleBasicInfo();;
		
		Connection conn = this.getConnection();
		PreparedStatement stm = conn.prepareStatement("select * from ccgarticle where articleID=?");
		stm.setInt(1, articleID);
		ResultSet rs = stm.executeQuery();
		while(rs.next()){
			info.setArticleID(rs.getInt("articleID"));
			info.setArticleType(rs.getString("articleType"));
			info.setContentID(rs.getInt("contentID"));
			info.setTitle(rs.getString("title"));
			info.setDomain(rs.getString("domain"));
			
		}
		conn.close();
		return info;
	}	
	
	public ArticleContent getContent(Integer contentID) throws SQLException {
		
		ArticleContent content = new ArticleContent();
		Connection conn = this.getConnection();
		PreparedStatement stm = conn.prepareStatement("select * from ccgcontent where contentID = ?");
		stm.setInt(1,  contentID);
		ResultSet rs = stm.executeQuery();
		if(rs.next()){
			content.setContent(rs.getString("content"));
			content.setContentID(rs.getInt("contentID"));
			content.setContentTitle(rs.getString("contentTitle"));
			content.setFileName(rs.getString("filename"));
			content.setLength(rs.getInt("length"));
			content.setMetatpe(rs.getString("metatype"));
			content.setUrl(rs.getString("url"));
		}
		conn.close();
		return content;
	}
	
	public CategoryContent getCategoryContent(Integer categoryID) throws SQLException{
		
		CategoryContent cc = new CategoryContent();
		Connection conn = this.getConnection();
		PreparedStatement stm = conn.prepareStatement(
				"select categoryID, categorytitle, categorycontent from ccgcategory where categoryID=?");
		stm.setInt(1, categoryID);
		ResultSet rs = stm.executeQuery();
		if(rs.next()){
			cc.setCategorycontent(rs.getString("categorycontent"));
			cc.setCategoryID(rs.getInt("categoryID"));
			cc.setCategorytitle(rs.getString("categorytitle"));
		}	
		conn.close();
		return cc;
	}
	
	public ArticleCategory getCategory(Integer articleID) throws SQLException{
		ArticleCategory ac = new ArticleCategory();
		ac.setArticleID(articleID);
		Connection conn = this.getConnection();
		PreparedStatement stm = conn.prepareStatement(
				"select categoryID, categorytitle, startposi, endposi, categoryseq, type from ccgcategory "
				+ "where articleID = ? order by categoryID");
		stm.setInt(1, articleID);
		ResultSet rs = stm.executeQuery();
		while(rs.next()){
			Category category = new Category();
			category.setCategoryID(rs.getInt("categoryID"));
			category.setCategoryseq(rs.getInt("categoryseq"));
			category.setCategorytitle(rs.getString("categorytitle"));
			category.setEndposi(rs.getInt("endposi"));
			category.setStartposi(rs.getInt("startposi"));
			category.setType(rs.getString("type"));
			ac.getCategories().add(category);
		}

		PreparedStatement stm2 = conn.prepareStatement(
				"select subcategoryID, categoryID, subcategorytitle, startposi, endposi, type from ccgsubcategory "
				+ "where articleID = ? order by subcategoryID");		
		stm2.setInt(1, articleID);
		ResultSet rs2 = stm2.executeQuery();
		while(rs2.next()){
			SubCategory sub = new SubCategory();
			sub.setSubcategoryID(rs2.getInt("subcategoryID"));
			sub.setCategoryID(rs2.getInt("categoryID"));
			sub.setSubcategorytitle(rs2.getString("subcategorytitle"));
			sub.setEndposi(rs2.getInt("endposi"));
			sub.setStartposi(rs2.getInt("startposi"));
			this.addSubCategoryToCategory(ac, sub);			
		}
		
		/*
		Integer subcategoryID;
		String subcategorytitle;
		Integer startposi;
		Integer endposi;
		String type;		
		*/
		conn.close();
		return ac;
	}
	
	private void addSubCategoryToCategory(ArticleCategory ac, SubCategory sub){
		List<Category> list = ac.getCategories();
		for(Category category : list){
			if(category.getCategoryID() == sub.getCategoryID()){
				category.getSubCategories().add(sub);
			}
		}
	}
	
	private Connection getConnection() {
		Connection conn = null;
		try {
			Context ctx = new InitialContext();
			DataSource  ds = (DataSource)ctx.lookup("java:comp/env/jdbc/ccgcontent");
			conn = ds.getConnection();			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return conn;
	}

}
