package com.ccg.dataaccess.dao.impl;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGGroupArticleAccessDAO;
import com.ccg.dataaccess.entity.CCGGroupArticleAccess;
@Repository("CCGGroupArticleAccessDAO")
public class CCGGroupArticleAccessDAOImpl extends CCGBaseDAOImpl<CCGGroupArticleAccess, Integer>
		implements CCGGroupArticleAccessDAO {

}
