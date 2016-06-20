package com.ccg.dataaccess.dao.api;

import com.ccg.dataaccess.entity.CCGUserProfile;

public interface CCGUserProfileDAO extends CCGGenericDAO<CCGUserProfile, Integer> {
	CCGUserProfile findUserProfileByName(String name);
}
