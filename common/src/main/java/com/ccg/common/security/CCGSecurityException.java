package com.ccg.common.security;

public class CCGSecurityException extends Exception {

	private static final long serialVersionUID = 1L;

	public CCGSecurityException(String msg){
		super(msg);
	}
	
	public CCGSecurityException(Throwable t){
		super(t);
	}
	
	public CCGSecurityException(String msg, Throwable t){
		super(msg, t);
	}
}
