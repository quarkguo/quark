package com.ccg.dataaccess.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ccg.dataaccess.dao.api.CCGUserGroupDAO;
import com.ccg.dataaccess.entity.CCGUserGroup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/ccg_dataacess_beans_test.xml")
@Transactional
public class CCGUserGroupTestCase {

	@Autowired
	private CCGUserGroupDAO usergroupDAO;
	
	@Test
	public void testUserGroupMembers()
	{
		CCGUserGroup ug=usergroupDAO.findById(1);
		System.out.println(ug.getGroupmembers());
	}
}
