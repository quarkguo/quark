package com.ccg.dataaccess.dao.api;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.ccg.common.*;

public interface CCGGenericDAO <T, ID extends Serializable>{
	T findById(ID id);
    
	public ID getPkId(T t);
	
	public T save(T entity);
	
	public void save(List<T> entity);
	
	public void delete(T entity);
	
	public int countAll();
	
	public T update(T entity);
	public void update(List<T> entitylist);
	
	public List<T> findAll();
	
	public TypedQuery<T> getTypedQuery(String name);
	
	public Query getQuery(String name);
	
	@SuppressWarnings("rawtypes")
	public TypedQuery<T> getTypeQuery(CriteriaQuery qry);
	
	public Query getCountAllQuery();
	public TypedQuery getFindAllQuery();
    
}
