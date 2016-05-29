package com.whenling.castle.api.admin;

import java.util.List;

public interface CrudService<T, ID> {

	T newDomain();

	T save(T domain);
	
	void delete(ID id);
	
	T findOne(ID id);
	
	T getOne(ID id);
	
	long count();
	
	List<T> findAll();
	
	List<T> findAll(Iterable<ID> ids);
	

}
