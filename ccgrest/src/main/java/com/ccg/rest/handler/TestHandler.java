package com.ccg.rest.handler;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;;

@RestController
public class TestHandler {

	
	@RequestMapping(value="/hello",method=RequestMethod.GET)
	public String hello(ModelMap para)
	{
		para.addAttribute("msg","hello world message");
		return "hello world!!";
	}
}
