package com.ccg.dataaccess.dao.impl;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGArticleDAO;
import com.ccg.dataaccess.entity.CCGArticle;

@Repository("CCGArticleDAO")
public class CCGArticleDAOImpl extends CCGBaseDAOImpl<CCGArticle,Integer> implements CCGArticleDAO {

}
