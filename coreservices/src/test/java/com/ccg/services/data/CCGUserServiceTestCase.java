package com.ccg.services.data;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ccg.common.data.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:ccg_coreservices_beans.xml","classpath:spring/ccg_dataacess_beans_test.xml"})
@Transactional
public class CCGUserServiceTestCase {

	@Autowired
	private CCGUserService userserv;
	
	@Test
	public void TestFetchGroupMembers()
	{
		List<User> ul=userserv.getGroupMembers(1);
		System.out.println(ul);
	}
}
