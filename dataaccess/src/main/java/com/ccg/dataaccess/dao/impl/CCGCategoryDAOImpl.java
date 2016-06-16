package com.ccg.dataaccess.dao.impl;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGCategoryDAO;
import com.ccg.dataaccess.entity.CCGCategory;

@Repository("CCGCategoryDAO")
public class CCGCategoryDAOImpl extends CCGBaseDAOImpl<CCGCategory, Integer> implements CCGCategoryDAO {

}
