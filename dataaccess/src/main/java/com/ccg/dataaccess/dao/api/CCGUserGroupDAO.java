package com.ccg.dataaccess.dao.api;

import com.ccg.dataaccess.entity.CCGUserGroup;

public interface CCGUserGroupDAO extends CCGGenericDAO<CCGUserGroup, Integer>{
	CCGUserGroup findUserGroupByGroupName(String groupname);
}
