package com.ccg.services.data;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public UserProfile getUserProfile(String name) {
		CCGUserProfile profile = userProfileDAO.findUserProfileByName(name);
		UserProfile userProfile = new UserProfile();
		userProfile.setAddress(profile.getAddress());
		userProfile.setImageURL(profile.getImageURL());
		userProfile.setName(name);
		userProfile.setPhone(profile.getPhone());
		userProfile.setUserID(profile.getUserID());
		return userProfile;
	}

	@Override
	@Transactional
	public boolean updateUserProfile(UserProfile profile) {
		CCGUserProfile ccgProfile = userProfileDAO.findUserProfileByName(profile.getName());
		ccgProfile.setAddress(profile.getAddress());
		ccgProfile.setImageURL(profile.getImageURL());
		ccgProfile.setPhone(profile.getPhone());
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
}
