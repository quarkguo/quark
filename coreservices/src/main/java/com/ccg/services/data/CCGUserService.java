package com.ccg.services.data;

import com.ccg.common.data.user.UserProfile;

public interface CCGUserService {
	
	public boolean updatePassword(String userid, String oldPassword, String newPassword);
	public boolean createUser(String useremail);
	public boolean createGroup(String groupname, String userID);
	public boolean addUserToGroup(String useremail, String groupname);
	public boolean resetPassword(String useremail);
	public UserProfile getUserProfile(String name);
	public boolean updateUserProfile(UserProfile profile);
	public boolean removeUserFromGroup(String userId, String groupName);
	
	
}
