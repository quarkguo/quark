package com.ccg.dataaccess.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@NamedQueries({
	 @NamedQuery(name="CCGArticleType.findAll", query="select a from CCGArticleType a")
	})
@Entity
@Table(name="ccgarticletype")
public class CCGArticleType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;

	@Id	
	@Column(name = "id", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getID() {
		return id;
	}
	public void setID(Integer id) {
		this.id = id;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CCGArticleType other = (CCGArticleType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
}
