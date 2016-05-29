package com.whenling.castle.service;

import java.util.List;

import com.whenling.castle.api.CrudService;

public class CrudServiceImpl<T, ID> implements CrudService<T, ID> {

	@Override
	public T newDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T save(T domain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ID id) {
		// TODO Auto-generated method stub

	}

	@Override
	public T findOne(ID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T getOne(ID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll(Iterable<ID> ids) {
		// TODO Auto-generated method stub
		return null;
	}

}
