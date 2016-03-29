package com.ccg.common;

import org.apache.log4j.*;

public class TEMMLogBase implements Loggable {

	@Override
	public Logger getLogger() {
		// TODO Auto-generated method stub
		return Logger.getLogger(getClass().getName());
	}

}
