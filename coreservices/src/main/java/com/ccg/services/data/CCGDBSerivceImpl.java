package com.ccg.services.data;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccg.dataaccess.dao.api.CCGArticleDAO;
import com.ccg.dataaccess.entity.CCGArticle;

@Service("CCGDBService")
public class CCGDBSerivceImpl implements CCGDBService {

	@Autowired
	private CCGArticleDAO articleDAO;
	@Override
	public void saveArticle(CCGArticle article) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getArticleCouont() {
		// TODO Auto-generated method stub
		return articleDAO.countAll();
	}

}
