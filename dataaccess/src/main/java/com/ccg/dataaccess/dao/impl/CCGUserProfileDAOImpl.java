package com.ccg.dataaccess.dao.impl;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGUserProfileDAO;
import com.ccg.dataaccess.entity.CCGUserProfile;

@Repository("CCGUserProfileDAO")
public class CCGUserProfileDAOImpl extends CCGBaseDAOImpl<CCGUserProfile, Integer> implements CCGUserProfileDAO {

}
