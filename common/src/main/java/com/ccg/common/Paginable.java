package com.ccg.common;
public interface Paginable extends JsonDTO{
	public Integer getStart();
	public Integer getLimit();
	public void setStart(Integer start);
	public void setLimit(Integer limit);
}
