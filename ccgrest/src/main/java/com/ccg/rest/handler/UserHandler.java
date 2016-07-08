package com.ccg.rest.handler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
		String name = request.getRemoteUser();
		if(name == null){
			name = "name";
		}
		userService.updatePassword(name, input.oldPassword, input.newPassword);		
		return "done";
	}
	
	@RequestMapping(value="admin/createUser",method=RequestMethod.POST)
	public String createUser(@RequestBody CreateUser input)
	{	
		try{
			if(userService.createUser(input.userId)){
				return "done";
			}else{
				return "you oldPassword is not currect";
			}
		}catch(Throwable t){
			t.printStackTrace();
			return ("==" + t.getMessage());
		}
		
	}
	
	@RequestMapping(value="admin/createGroup",method=RequestMethod.POST)
	public String createGroup(@RequestBody CreateGroup input, HttpServletRequest request)
	{	
		String name = request.getRemoteUser();
		if(name == null){
			name = "name";
		}
		try{
			if(userService.createGroup(input.groupName, name)){
				return "done";
			}else{
				return "fail";
			}
		}catch(Throwable t){
			t.printStackTrace();
			return ("==" + t.getMessage());
		}
		
	}	
	
	@RequestMapping(value="admin/addUserToGroup", method=RequestMethod.POST)
	public String addUserToGroup(@RequestBody AddUserToGroup input, HttpServletRequest request){
		String name = request.getRemoteUser();
		if(name == null){
			name = "name";
		}
		userService.addUserToGroup(input.userId, input.groupName);		
		return "done";
	}
	
	@RequestMapping(value="admin/resetPassword", method=RequestMethod.POST)
	public String resetPassword(@RequestBody ResetPassword input){
		userService.resetPassword(input.userId);		
		return "done";
	}

	@RequestMapping(value="admin/removeUserFromGroup", method=RequestMethod.POST)
	public String removeUserFromGroup(@RequestBody RemoveUserFromGroup input){
		userService.removeUserFromGroup(input.userId, input.groupName);	
		return "done";
	}
	
	@RequestMapping(value="admin/user/all", method=RequestMethod.GET)
	public List<User> getAllUsers (){
		return userService.getUserList();
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
	
}

class UpdateProfile{String imageURL, phone, address,userID,username,name;}
class UpdatePassword{String oldPassword, newPassword;}
class CreateUser{String userId;}
class CreateGroup{String groupName;}
class AddUserToGroup{String userId, groupName;}
class ResetPassword{String userId;}
class RemoveUserFromGroup{String userId, groupName;}