package com.ccg.rest.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ccg.services.data.CCGDBService;

@RestController
public class UploadHandler {
	
	@Autowired
	CCGDBService dataservice;
	
	@RequestMapping(value="/myupload",method=RequestMethod.GET)
	public String hello(ModelMap para)
	{
		System.out.println("service: " + dataservice);
		return "upload222";
	}
}
