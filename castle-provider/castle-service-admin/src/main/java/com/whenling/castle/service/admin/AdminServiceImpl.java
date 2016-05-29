package com.whenling.castle.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import com.whenling.castle.api.admin.user.AdminService;
import com.whenling.castle.domain.user.Admin;
import com.whenling.castle.framework.core.Provider;

@Service
@Provider
public class AdminServiceImpl implements AdminService {

	@Override
	public Admin save(Admin admin) {
		System.out.println("save");
		return null;
	}

	@Override
	public void delete(Admin admin) {
		System.out.println("delete");
	}

	@Override
	public Admin findOne(String id) {
		System.out.println("findOne");
		return null;
	}

	@Override
	public List<Admin> findAll() {
		System.out.println("findAll");
		return null;
	}

}
