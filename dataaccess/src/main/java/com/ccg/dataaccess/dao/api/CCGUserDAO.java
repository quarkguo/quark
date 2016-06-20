package com.ccg.dataaccess.dao.api;


import com.ccg.dataaccess.entity.CCGUser;

public interface CCGUserDAO extends CCGGenericDAO<CCGUser, Integer> {
	
	public boolean updatePassword(String userId, String oldPassword, String newPassword);
	public CCGUser findUserByUseremail(String useremail);

}
