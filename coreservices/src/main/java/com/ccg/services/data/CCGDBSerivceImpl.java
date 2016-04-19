package com.ccg.services.data;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.ccg.dataaccess.dao.api.CCGArticleDAO;
import com.ccg.dataaccess.entity.CCGArticle;

@Resource
public class CCGDBSerivceImpl implements CCGDBService {

	@Autowired
	private CCGArticleDAO articleDAO;
	@Override
	public void saveArticle(CCGArticle article) {
		// TODO Auto-generated method stub
		
	}

}
