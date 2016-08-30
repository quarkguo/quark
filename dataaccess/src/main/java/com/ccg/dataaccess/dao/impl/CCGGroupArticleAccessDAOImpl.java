package com.ccg.dataaccess.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGGroupArticleAccessDAO;
import com.ccg.dataaccess.entity.CCGGroupArticleAccess;
@Repository("CCGGroupArticleAccessDAO")
public class CCGGroupArticleAccessDAOImpl extends CCGBaseDAOImpl<CCGGroupArticleAccess, Integer>
		implements CCGGroupArticleAccessDAO {

	@Override
	public CCGGroupArticleAccess findRecord(int groupID, int articleID) {
		// TODO Auto-generated method stub
		Query q = entityManager.createQuery("from CCGGroupArticleAccess where group.ccggroupID =:groupID and article.articleID =:articleID");
		q.setParameter("groupID", groupID);
		q.setParameter("articleID", articleID);
		
		return (CCGGroupArticleAccess)q.getSingleResult();
	}
	
	@Override
	public List<CCGGroupArticleAccess> findRecordsByArticleId(int articleID){
		Query q = entityManager.createQuery("from CCGGroupArticleAccess where articleID =:articleID");
		q.setParameter("articleID", articleID);		
		return (List<CCGGroupArticleAccess>)q.getSingleResult();			
	}

}
