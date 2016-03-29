package com.ccg.common;

import java.util.List;

public interface PaginableRecords<T> extends Paginable {
	Long getTotal();
	void setTotal(Long total);	
	List<T> getRecords();
	void setRecords(List<T> records);
}
