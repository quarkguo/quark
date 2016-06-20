package com.ccg.dataaccess.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGUserDAO;
import com.ccg.dataaccess.entity.CCGUser;
@Repository("CCGUserDAO")
public class CCGUserDAOImpl extends CCGBaseDAOImpl<CCGUser, Integer> implements CCGUserDAO {
	public void test(){
		this.getQuery("name");
	}

	@Override
	public boolean updatePassword(String userId, String oldPassword, String newPassword) {
		Query q = entityManager.createQuery("from CCGUser where useremail =:useremail and password =:password");
		q.setParameter("useremail", userId);
		q.setParameter("password", oldPassword);
		CCGUser user = (CCGUser)q.getSingleResult();
		if(user == null){
			return false;
		}else{
			user.setPassword(newPassword);
			entityManager.persist(user);
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public CCGUser findUserByUseremail(String useremail) {
		Query q = entityManager.createQuery("from CCGUser where useremail =:useremail");
		q.setParameter("useremail", useremail);
		List<CCGUser> list = q.getResultList();
		if(list != null)
			return list.get(0);
		return null;
	}
}
