package com.whenling.castle.api.admin.user;

import java.util.List;

import com.whenling.castle.domain.user.Admin;

public interface AdminService {
	Admin save(Admin admin);

	void delete(Admin admin);

	Admin findOne(String id);

	List<Admin> findAll();
}
