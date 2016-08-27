package com.ccg.dataaccess.dao.impl;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGArticleInfoDAO;
import com.ccg.dataaccess.entity.CCGArticleInfo;

@Repository("CCGArticleInfoDAO")
public class CCGArticleInfoDAOImpl extends CCGBaseDAOImpl<CCGArticleInfo, Integer> implements CCGArticleInfoDAO {

}
