package com.ccg.dataaccess.dao.impl;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGRelatedArticleDAO;
import com.ccg.dataaccess.entity.CCGRelatedArticle;

@Repository("CCGRelatedArticleDAO")
public class CCGRelatedArcileDAOImple extends CCGBaseDAOImpl<CCGRelatedArticle, Integer>
		implements CCGRelatedArticleDAO {

}
