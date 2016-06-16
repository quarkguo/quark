package com.ccg.dataaccess.dao.impl;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGUserDAO;
import com.ccg.dataaccess.entity.CCGUser;
@Repository("CCGUserDAO")
public class CCGUserDAOImpl extends CCGBaseDAOImpl<CCGUser, Integer> implements CCGUserDAO {

}
