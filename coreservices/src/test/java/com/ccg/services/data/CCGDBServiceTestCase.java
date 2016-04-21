package com.ccg.services.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:ccg_coreservices_beans.xml","classpath:spring/ccg_dataacess_beans_test.xml"})
@Transactional
public class CCGDBServiceTestCase {
	@Autowired
	private CCGDBService dataservice;
	
	@Test
	public void testCountAll()
	{
		System.out.println("--->"+dataservice.getArticleCouont());
	}
}
