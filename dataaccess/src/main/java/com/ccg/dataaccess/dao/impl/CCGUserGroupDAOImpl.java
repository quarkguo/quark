package com.ccg.dataaccess.dao.impl;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGUserGroupDAO;
import com.ccg.dataaccess.entity.CCGUserGroup;
@Repository("CCGUserGroupDAO")
public class CCGUserGroupDAOImpl extends CCGBaseDAOImpl<CCGUserGroup, Integer> implements CCGUserGroupDAO {

}
