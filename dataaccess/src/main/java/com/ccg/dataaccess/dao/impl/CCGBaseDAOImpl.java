package com.ccg.dataaccess.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.log4j.Logger;

import com.ccg.common.*;
import com.ccg.dataaccess.dao.api.CCGGenericDAO;


public  class CCGBaseDAOImpl  <T, ID extends Serializable> implements CCGGenericDAO<T, ID>, Loggable 
{
	
	@Resource
	@PersistenceContext
	protected EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public CCGBaseDAOImpl() 
	{
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	private Class<T> persistentClass;	
	public Class<T> getPersistentClass()
	{
		return persistentClass;
	}
	
	public T findById(ID id)
	{
    		T entity;
    			entity = (T) entityManager.find(getPersistentClass(), id);
    		return entity;
	}

	@Override
	public Logger getLogger() {	
		return Logger.getLogger(getClass().getName());
	}

	 @SuppressWarnings("unchecked")
	@Override
	public ID getPkId(T t) {
	
		return (ID)entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(t);
	}

	@Override
	public T save(T entity) {
	
		entityManager.persist(entity);
		return entity;
	}

	@Override
	public void save(List<T> entitylist) {
		for(T t:entitylist)
		{
			entityManager.persist(t);
		}
		entityManager.flush();
	}

	@Override
	public void delete(T entity) {
		entityManager.remove(entity);		
	}

	// this method depends on a predefined query
	@Override
	public int countAll() {
		Query q=this.getCountAllQuery();
		Number res=(Number) q.getSingleResult();
		return res.intValue();
	}

	@Override
	public T update(T entity) {
	
		return null;
	}

	@Override
	public void update(List<T> entitylist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<T> findAll() {
		TypedQuery<T> qry=getFindAllQuery();
		return qry.getResultList();
	}

	@Override
	public TypedQuery<T> getTypedQuery(String name) {
		TypedQuery<T> qry=entityManager.createNamedQuery(persistentClass.getSimpleName()+"."+name,persistentClass);
		if(qry==null)
		{
			throw new RuntimeException("Can't find TypedQuery '"+persistentClass.getSimpleName()+"."+name+"'");
		}
		else
		{
			return qry;
		}		
	}

	@Override
	public Query getQuery(String name) {
		Query qry=entityManager.createNamedQuery(this.persistentClass.getSimpleName()+"."+name);
		if(qry==null)
		{
			throw new RuntimeException("Can't find Query '"+persistentClass.getSimpleName()+"."+name+"'");
		}
		else
		{
			return qry;
		}
		
	}

	@SuppressWarnings({"rawtypes","unchecked"})
	@Override
	public TypedQuery<T> getTypeQuery( CriteriaQuery qry) {
		TypedQuery<T> tqry=entityManager.createQuery(qry);
		if(qry==null)
		{
			throw new RuntimeException("cant have input as null");
		}
		else if(tqry==null)
		{
			throw new RuntimeException("cant find typedquery '"+persistentClass.getSimpleName()+"."+qry.toString());
		}
		else
		{
			return tqry;
		}
	}

	@Override
	public Query getCountAllQuery() {
		// TODO Auto-generated method stub
		return getQuery("countAll");
	}

	@Override
	public TypedQuery<T> getFindAllQuery() {
		// TODO Auto-generated method stub
		return getTypedQuery("findAll");
	}
}