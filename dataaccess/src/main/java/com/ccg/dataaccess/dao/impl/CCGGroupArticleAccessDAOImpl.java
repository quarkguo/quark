package com.ccg.dataaccess.dao.impl;

import java.util.ArrayList;
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
		List<CCGGroupArticleAccess> accessList = new ArrayList<CCGGroupArticleAccess>();
		try{
			accessList = (List<CCGGroupArticleAccess>)q.getSingleResult();
		}catch(javax.persistence.NoResultException e){
			System.out.println(e.getLocalizedMessage() + ": " + articleID);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return 	accessList;		
	}

}
