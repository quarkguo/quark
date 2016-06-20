package com.ccg.dataaccess.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGUserGroupDAO;
import com.ccg.dataaccess.entity.CCGUser;
import com.ccg.dataaccess.entity.CCGUserGroup;
@Repository("CCGUserGroupDAO")
public class CCGUserGroupDAOImpl extends CCGBaseDAOImpl<CCGUserGroup, Integer> implements CCGUserGroupDAO {

	@Override
	public CCGUserGroup findUserGroupByGroupName(String groupname) {
		
		Query q = entityManager.createQuery("from CCGUserGroup where groupname =:groupname");
		q.setParameter("groupname", groupname);
		CCGUserGroup usergroup = (CCGUserGroup)q.getSingleResult();
		return usergroup;
	}

}
