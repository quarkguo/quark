package com.ccg.common;

import java.util.List;
import java.util.Map;

public class SimplePaginableRecords<T> extends SimplePaginable implements
		PaginableRecords<T> {

	@Override
	public List<T> getRecords() {
		// TODO Auto-generated method stub
		return records;
	}

	@Override
	public Long getTotal() {
		// TODO Auto-generated method stub
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	Long total;
	List<T> records;
	
	public Map<String,Object> getDataMap()
	{
		Map<String,Object>res= super.getDataMap();
		res.put("total", total);
		res.put("records", records);
		return res;
	}

	@Override
	public Integer getStart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getLimit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStart(Integer start) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLimit(Integer limit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getJSON() {
		// TODO Auto-generated method stub
		return null;
	}
}
