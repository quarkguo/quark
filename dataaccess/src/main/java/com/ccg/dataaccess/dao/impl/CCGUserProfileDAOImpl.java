package com.ccg.dataaccess.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGUserProfileDAO;
import com.ccg.dataaccess.entity.CCGUserGroup;
import com.ccg.dataaccess.entity.CCGUserProfile;

@Repository("CCGUserProfileDAO")
public class CCGUserProfileDAOImpl extends CCGBaseDAOImpl<CCGUserProfile, Integer> implements CCGUserProfileDAO {

	@Override
	public CCGUserProfile findUserProfileByName(String name) {
		Query q = entityManager.createQuery("from CCGUserProfile where name =:name");
		q.setParameter("name", name);
		CCGUserProfile profile = (CCGUserProfile)q.getSingleResult();
		return profile;
	
	}

}
