package com.ccg.dataaccess.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGGroupMembersDAO;
import com.ccg.dataaccess.entity.CCGGroupMembers;
import com.ccg.dataaccess.entity.CCGUser;
import com.ccg.dataaccess.entity.CCGUserGroup;

@Repository("CCGGroupMembersDAO")
public class CCGGroupMembersDAOImpl extends CCGBaseDAOImpl<CCGGroupMembers, Integer> implements CCGGroupMembersDAO {
	
	public CCGGroupMembers getUserGroup(CCGUser user, CCGUserGroup group){
		Query q = entityManager.createQuery("from CCGGroupMembers where groupname =:group and useremail =:member");
		q.setParameter("group", group.getGroupname());
		q.setParameter("member", user.getUseremail());
		CCGGroupMembers gm = (CCGGroupMembers)q.getSingleResult();
		return gm;
	}
}
