package com.ccg.services.data;

import java.util.List;

import com.ccg.common.data.user.User;
import com.ccg.common.data.user.UserGroup;
import com.ccg.common.data.user.UserProfile;

public interface CCGUserService {
	
	public boolean updatePassword(String userid, String oldPassword, String newPassword);
	public boolean createUser(String useremail);
	public boolean createGroup(String groupname, String userID);
	public boolean addUserToGroup(String useremail, String groupname);
	public boolean resetPassword(String useremail);
	public UserProfile getUserProfileByUserName(String useremail);  // user email is the user name
	public boolean updateUserProfile(UserProfile profile);
	public boolean removeUserFromGroup(String userId, String groupName);
	public List<User> getUserList();
	public User getUserById(Integer id);
	public List<UserGroup> getUserGroupList();
	public UserGroup getUserGroupByGroupId(Integer groupId);
	
	
}
