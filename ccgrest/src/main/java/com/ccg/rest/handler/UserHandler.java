package com.ccg.rest.handler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccg.common.data.Article;
import com.ccg.common.data.user.User;
import com.ccg.common.data.user.UserGroup;
import com.ccg.common.data.user.UserProfile;
import com.ccg.services.data.CCGUserService;

@RestController
//@RequestMapping(value="/user")
public class UserHandler {

	@Autowired
	CCGUserService userService;

	@RequestMapping(value="user/logout", method=RequestMethod.GET)
	public String logout(HttpServletRequest request){
		System.out.println("about to logout");
		request.getSession().invalidate();
		System.out.println("logged out");
		return "success";
	}	
	
	@RequestMapping(value="user/profile", method=RequestMethod.GET)
	public UserProfile getUserProfile(HttpServletRequest request){
		String username = request.getRemoteUser();
		
		UserProfile profile = userService.getUserProfileByUserName(username);	 
		return profile;
	}
	
	@RequestMapping(value="user/updateProfile", method=RequestMethod.POST)
	public String updateProfile(@RequestBody UpdateProfile input, HttpServletRequest request){
		//String namename = request.getRemoteUser();
		
		UserProfile profile = new UserProfile();
		profile.setAddress(input.address);
		profile.setImageURL(input.imageURL);
		profile.setName(input.name);
		profile.setPhone(input.phone);
		profile.setUserID(Integer.parseInt(input.userID));
		userService.updateUserProfile(profile);		
		return "done";
	}
	
	@RequestMapping(value="user/updatePassword", method=RequestMethod.POST)
	public String updatePassword(@RequestBody UpdatePassword input, HttpServletRequest request){
		String username = request.getRemoteUser();
		
		if (userService.updatePassword(username, input.oldpass, input.newpass))
		{
			return "success";
		}
		else
		{
			return "error";
		}
		
	}
	
	@RequestMapping(value="user/groups", method=RequestMethod.GET)
	public List<String> isAdminUser(HttpServletRequest request){
		String username = request.getRemoteUser();
		return userService.getUserGroups(username);
	}
	
	
	
	@RequestMapping(value="admin/createUser",method=RequestMethod.POST)
	public String createUser(@RequestBody CreateUser input)
	{	
		try{
			System.out.println(input.useremail);			
			userService.createUser(input.useremail);
				return "done";
			
		}catch(Throwable t){
			t.printStackTrace();
			return ("==" + t.getMessage());
		}
		
	}
	
	@RequestMapping(value="admin/createGroup",method=RequestMethod.POST)
	public String createGroup(@RequestBody CreateGroup input, HttpServletRequest request)
	{	
		//String name = request.getRemoteUser();
	
		try{
			if(userService.createGroup(input.groupName, Integer.parseInt(input.ownerID))){
				return "done";
			}else{
				return "fail";
			}
		}catch(Throwable t){
			t.printStackTrace();
			return ("==" + t.getMessage());
		}
		
	}	
	
	
	@RequestMapping(value="admin/addDocToGroup", method=RequestMethod.POST)
	public String addDocToGroup(@RequestBody addDocToGroup input, HttpServletRequest request){
		System.out.println(input.groupID);
		System.out.println(input.documentID);
	//	userService.addUserToGroup(input.userId, input.groupName);		
	//	userService.addUserToGroup(input.usernames, input.groupID);
		int groupID =Integer.parseInt(input.groupID);
		int docID=Integer.parseInt(input.documentID);
		userService.addDocToGroup(groupID, docID);
		return "done";
	}
	
	@RequestMapping(value="admin/addUserToGroup", method=RequestMethod.POST)
	public String addUserToGroup(@RequestBody AddUserToGroup input, HttpServletRequest request){
		System.out.println(input.groupID);
		System.out.println(input.usernames);
	//	userService.addUserToGroup(input.userId, input.groupName);		
		userService.addUserToGroup(input.usernames, input.groupID);
		return "done";
	}
	
	@RequestMapping(value="admin/resetPassword", method=RequestMethod.POST)
	public String resetPassword(@RequestBody ResetPassword input){
		userService.resetPassword(input.userId);		
		return "done";
	}

	@RequestMapping(value="admin/removeDocFromGroup", method=RequestMethod.POST)
	public String removeDocFromGroup(@RequestBody addDocToGroup input){
		int groupID =Integer.parseInt(input.groupID);
		int docID=Integer.parseInt(input.documentID);
		userService.removeDocFromGroup(groupID, docID);	
		return "done";
	}
	
	@RequestMapping(value="admin/removeUserFromGroup", method=RequestMethod.POST)
	public String removeUserFromGroup(@RequestBody RemoveUserFromGroup input){
		userService.removeUserFromGroup(input.usernames, input.groupID);	
		return "done";
	}
	
	@RequestMapping(value="admin/user/all", method=RequestMethod.GET)
	public List<User> getAllUsers (){
		return userService.getUserList();
	}

	@RequestMapping(value="admin/removeUser/{id}", method=RequestMethod.POST)
	public String deleteUser(@PathVariable("id") Integer id)
	{
		userService.deleteUser(id);
		return "success";
	}
	
	@RequestMapping(value="admin/removeGroup/{id}", method=RequestMethod.POST)
	public String deleteGroup(@PathVariable("id") Integer id)
	{
		userService.deleteGroup(id);
		return "success";
	}
	
	@RequestMapping(value="admin/user/{id}", method=RequestMethod.GET)
	public User getUserById (@PathVariable("id") Integer id){
		return userService.getUserById(id);
	}
	@RequestMapping(value="admin/usergroup/all", method=RequestMethod.GET)
	public List<UserGroup> getAllUserGroups (){
		return userService.getUserGroupList();
	}

	@RequestMapping(value="admin/usergroup/{id}", method=RequestMethod.GET)
	public UserGroup getUserGroupById (@PathVariable("id") Integer id){
		return userService.getUserGroupByGroupId(id);
	}
	
	@RequestMapping(value="admin/userGroupNotMembers/{id}", method=RequestMethod.GET)
	public List<User> getGroupMembers(@PathVariable("id") Integer id){
		System.out.println(id);
		List<User> au=userService.getUserList();
		List<User> ul=userService.getGroupMembers(id);
		for(User u : ul)
		{
			au.remove(u);
		}
		return au;
	}
	
	@RequestMapping(value="admin/userGroupMembers/{id}", method=RequestMethod.GET)
	public List<User> getNotGroupMembers(@PathVariable("id") Integer id){
		System.out.println(id);
		return userService.getGroupMembers(id);
	}
	
	@RequestMapping(value="admin/userGroupArticles/{id}", method=RequestMethod.GET)
	public List<Article> getGroupDocAccess(@PathVariable("id") Integer id){
		System.out.println(id);
		return userService.getGroupArticles(id);
	}
	
	
	@RequestMapping(value="admin/userGroupArticleCandidate/{id}", method=RequestMethod.GET)
	public List<Article> getGroupAccessCandidate(@PathVariable("id") Integer id){
		System.out.println(id);
		return userService.getGroupArticlesCandidate(id);
	}
	
	
}

class UpdateProfile{String imageURL, phone, address,userID,username,name;}
class UpdatePassword{String oldpass, newpass;}
class CreateUser{String useremail;}
class CreateGroup{String groupName;String ownerID;}
class AddUserToGroup{String[] usernames; String groupID;}
class ResetPassword{String userId;}
class RemoveUserFromGroup{String[] usernames; String groupID;}
class addDocToGroup{String groupID, documentID ;}