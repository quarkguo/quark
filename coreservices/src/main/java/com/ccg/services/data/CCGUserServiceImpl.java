package com.ccg.services.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ccg.common.data.user.User;
import com.ccg.common.data.user.UserGroup;
import com.ccg.common.data.user.UserProfile;
import com.ccg.dataaccess.dao.api.CCGGroupMembersDAO;
import com.ccg.dataaccess.dao.api.CCGUserDAO;
import com.ccg.dataaccess.dao.api.CCGUserGroupDAO;
import com.ccg.dataaccess.dao.api.CCGUserProfileDAO;
import com.ccg.dataaccess.entity.CCGGroupMembers;
import com.ccg.dataaccess.entity.CCGUser;
import com.ccg.dataaccess.entity.CCGUserGroup;
import com.ccg.dataaccess.entity.CCGUserProfile;

@Service("CCGUserService")
public class CCGUserServiceImpl implements CCGUserService{

	@Autowired	
	private CCGUserDAO userDAO;
	
	@Autowired	
	private CCGUserGroupDAO userGroupDAO;
	
	@Autowired	
	private CCGGroupMembersDAO groupMembersDAO;
	
	@Autowired	
	private CCGUserProfileDAO userProfileDAO;
	
	

	@Override
	@Transactional
	public boolean updatePassword(String userId, String oldPassword, String newPassword) {
		
		userDAO.updatePassword(userId, oldPassword, newPassword);
		
		return true;
	}

	@Override
	@Transactional
	public boolean createUser(String useremail) {
		CCGUser user = new CCGUser();
		user.setUseremail(useremail);
		user.setCreatedTS(new Date(System.currentTimeMillis()));
		user.setLastUpdateTS(new Date(System.currentTimeMillis()));
		user.setPassword("password");
		userDAO.save(user);
		return true;
	}

	@Override
	@Transactional
	public boolean createGroup(String groupname, String userId) {
		
		CCGUser user = userDAO.findUserByUseremail(userId);
		
		CCGUserGroup group = new CCGUserGroup();
		group.setGroupname(groupname);
		group.setOwner(user);
		group.setCreatedTS(new Date(System.currentTimeMillis()));
		group.setLastupdateTS(new Date(System.currentTimeMillis()));
		
		userGroupDAO.save(group);
		
		return true;
	}

	@Override
	public boolean addUserToGroup(String useremail, String groupname) {
		CCGGroupMembers gm = new CCGGroupMembers();
		CCGUserGroup usergroup = userGroupDAO.findUserGroupByGroupName(groupname);
		CCGUser user = userDAO.findUserByUseremail(useremail);
		gm.setGroup(usergroup);
		gm.setMember(user);
		gm.setGroupname(groupname);
		gm.setUseremail(useremail);
		groupMembersDAO.save(gm);
		return true;
	}

	@Override
	@Transactional
	public boolean resetPassword(String useremail) {
		CCGUser user = userDAO.findUserByUseremail(useremail);
		user.setPassword("defaultPassword");
		userDAO.save(user);
		return true;
	}

	@Override
	public UserProfile getUserProfileByUserName(String username) {
		//CCGUserProfile profile = userProfileDAO.findUserProfileByName(username);
		CCGUser user=userDAO.findUserByUseremail(username);
		CCGUserProfile profile=user.getProfile();
		UserProfile userProfile = new UserProfile();
		userProfile.setUserID(user.getUserID());
		userProfile.setUsername(user.getUseremail());
		userProfile.setAddress(profile.getAddress());
		userProfile.setImageURL(profile.getImageURL());
		userProfile.setName(profile.getName());
		userProfile.setPhone(profile.getPhone());
		//userProfile.setUserID(profile.getUserID());
		return userProfile;
	}

	@Override
	@Transactional
	public boolean updateUserProfile(UserProfile profile) {
		CCGUserProfile ccgProfile = userProfileDAO.findById(profile.getUserID());
		ccgProfile.setAddress(profile.getAddress());
		ccgProfile.setImageURL(profile.getImageURL());
		ccgProfile.setPhone(profile.getPhone());
		ccgProfile.setName(profile.getName());;
		userProfileDAO.save(ccgProfile);
		return true;
	}

	@Override
	@Transactional
	public boolean removeUserFromGroup(String userId, String groupName){

		CCGUser user = userDAO.findUserByUseremail(userId);
		CCGUserGroup group = userGroupDAO.findUserGroupByGroupName(groupName); 
		CCGGroupMembers gm = groupMembersDAO.getUserGroup(user, group);
		groupMembersDAO.delete(gm);
		return true;
	}

	@Override
	public List<User> getUserList() {
		List<CCGUser> ccgUsers = userDAO.findAll();
		List<User> users = new ArrayList<User>();
		for(CCGUser ccgUser : ccgUsers){
			User user = new User();
			user.setId(ccgUser.getUserID());
			user.setUseremail(ccgUser.getUseremail());
			users.add(user);
		}
		return users;
	}

	@Override
	public User getUserById(Integer id) {
		CCGUser ccgUser = userDAO.findById(id);
		User user = new User();
		user.setId(ccgUser.getUserID());
		user.setUseremail(ccgUser.getUseremail());		
		return user;
	}

	@Override
	public List<UserGroup> getUserGroupList() {
		List<CCGUserGroup> ccgGroupList = userGroupDAO.findAll();
		List<UserGroup> groupList = new ArrayList<UserGroup>();
		for(CCGUserGroup ccgGroup : ccgGroupList){
			UserGroup group = new UserGroup();
			group.setGroupId(ccgGroup.getCcggroupID());
			group.setGroupname(ccgGroup.getGroupname());
			if(ccgGroup.getOwner() != null){
				group.setOwnerId(ccgGroup.getOwner().getUserID());
			}
			groupList.add(group);
		}
		
		return groupList;
	}

	@Override
	public UserGroup getUserGroupByGroupId(Integer groupId) {
		CCGUserGroup ccgGroup = userGroupDAO.findById(groupId);
		UserGroup group = new UserGroup();
		group.setGroupId(ccgGroup.getCcggroupID());
		group.setGroupname(ccgGroup.getGroupname());
		if(ccgGroup.getOwner() != null)
			group.setOwnerId(ccgGroup.getOwner().getUserID());
		
		return group;
	}

	

}
