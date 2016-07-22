package com.ccg.services.data;

import java.util.List;

import com.ccg.common.data.Article;
import com.ccg.common.data.user.User;
import com.ccg.common.data.user.UserGroup;
import com.ccg.common.data.user.UserProfile;

public interface CCGUserService {
	
	public boolean updatePassword(String userid, String oldPassword, String newPassword);
	public boolean createUser(String useremail);
	public boolean createGroup(String groupname, int userID);
	public boolean addUserToGroup(String[] useremail, String groupID);
	public boolean resetPassword(String useremail);
	public UserProfile getUserProfileByUserName(String useremail);  // user email is the user name
	public boolean updateUserProfile(UserProfile profile);
	public boolean removeUserFromGroup(String[] userId, String groupName);
	public List<User> getUserList();
	public User getUserById(Integer id);
	public List<UserGroup> getUserGroupList();
	public UserGroup getUserGroupByGroupId(Integer groupId);
	
	public List<User> getGroupMembers(Integer groupID);
	public List<Article> getGroupArticles(Integer groupID);
	public List<Article> getGroupArticlesCandidate(Integer id);
	public boolean addDocToGroup(int groupID,int docID);
	public void removeDocFromGroup(int groupID, int docID);
	public void deleteUser(int userID);
	public void deleteGroup(int groupID);
	
	
}
