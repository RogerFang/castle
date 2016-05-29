package com.whenling.castle.api.admin.user;

import java.util.List;

import com.whenling.castle.domain.user.Admin;

public interface AdminService {

	Admin save(Admin admin);

	void delete(String id);

	Admin findOne(String id);

	long count();

	List<Admin> findAll();

}
