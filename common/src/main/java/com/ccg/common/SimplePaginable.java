package com.ccg.common;

import java.util.HashMap;
import java.util.Map;

public class SimplePaginable extends TEMMBaseDTO implements Paginable {

	@Override
	public Integer getLimit() {
		// TODO Auto-generated method stub
		return limit;
	}

	@Override
	public Integer getStart() {
		// TODO Auto-generated method stub
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	// here are the defaults
	private Integer start=0;
	private Integer limit=100;
	
	public Map<String,Object> getDataMap()
	{
		Map<String,Object>res= super.getDataMap();
		res.put("start", start);
		res.put("limit", limit);
		return res;
	}
}
