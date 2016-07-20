package com.ccg.dataaccess.dao.api;

import com.ccg.dataaccess.entity.CCGGroupArticleAccess;

public interface CCGGroupArticleAccessDAO extends CCGGenericDAO<CCGGroupArticleAccess, Integer> {
  public CCGGroupArticleAccess findRecord(int groupID,int articleID);
}
