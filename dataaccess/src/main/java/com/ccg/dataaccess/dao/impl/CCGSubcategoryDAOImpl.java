package com.ccg.dataaccess.dao.impl;

import org.springframework.stereotype.Repository;

import com.ccg.dataaccess.dao.api.CCGSubcategoryDAO;
import com.ccg.dataaccess.entity.CCGSubcategory;

@Repository("CCGSubcategoryDAO")
public class CCGSubcategoryDAOImpl extends CCGBaseDAOImpl<CCGSubcategory, Integer> implements CCGSubcategoryDAO {

}
