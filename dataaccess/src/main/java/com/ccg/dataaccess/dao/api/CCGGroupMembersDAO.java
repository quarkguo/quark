package com.ccg.dataaccess.dao.api;

import com.ccg.dataaccess.entity.CCGGroupMembers;
import com.ccg.dataaccess.entity.CCGUser;
import com.ccg.dataaccess.entity.CCGUserGroup;

public interface CCGGroupMembersDAO extends CCGGenericDAO<CCGGroupMembers, Integer> {
	public CCGGroupMembers getUserGroup(CCGUser user, CCGUserGroup group);
}
