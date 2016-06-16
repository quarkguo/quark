package com.ccg.dataaccess.dao.impl;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGGroupMembersDAO;
import com.ccg.dataaccess.entity.CCGGroupMembers;

@Repository("CCGGroupMembersDAO")
public class CCGGroupMembersDAOImpl extends CCGBaseDAOImpl<CCGGroupMembers, Integer> implements CCGGroupMembersDAO {

}
