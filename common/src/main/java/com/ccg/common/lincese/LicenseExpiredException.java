package com.ccg.common.lincese;

public class LicenseExpiredException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public LicenseExpiredException(String msg){
		super(msg);
	}
}
