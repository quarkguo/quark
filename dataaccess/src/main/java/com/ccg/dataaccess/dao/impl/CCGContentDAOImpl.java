package com.ccg.dataaccess.dao.impl;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGContentDAO;
import com.ccg.dataaccess.entity.CCGContent;

@Repository("CCGContentDAO")
public class CCGContentDAOImpl extends CCGBaseDAOImpl<CCGContent, Integer> implements CCGContentDAO {

	
}
