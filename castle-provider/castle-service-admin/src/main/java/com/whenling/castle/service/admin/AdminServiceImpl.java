package com.whenling.castle.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.whenling.castle.api.admin.user.AdminService;
import com.whenling.castle.domain.user.Admin;
import com.whenling.castle.framework.core.Provider;
import com.whenling.castle.service.admin.user.AdminEntity;
import com.whenling.castle.service.admin.user.AdminRepository;

@Service
@Provider
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public Admin save(Admin admin) {
		Admin result = toDomain(adminRepository.save(toEntity(admin)));
		return result;
	}

	@Override
	public void delete(String id) {
		adminRepository.delete(id);
	}

	@Override
	public Admin findOne(String id) {
		return toDomain(adminRepository.findOne(id));
	}

	@Override
	public long count() {
		return adminRepository.count();
	}

	@Override
	public List<Admin> findAll() {
		return Lists.transform(adminRepository.findAll(), new Function<AdminEntity, Admin>() {
			@Override
			public Admin apply(AdminEntity input) {
				return toDomain(input);
			}
		});
	}

	private AdminEntity toEntity(Admin domain) {
		if (domain == null) {
			return null;
		}

		AdminEntity entity = domain.getId() == null ? new AdminEntity() : adminRepository.findOne(domain.getId());

		entity.setUsername(domain.getUsername());
		entity.setPassword(domain.getPassword());
		entity.setMobile(domain.getMobile());
		entity.setEmail(domain.getEmail());
		entity.setName(domain.getName());

		return entity;
	}

	private Admin toDomain(AdminEntity entity) {
		if (entity == null) {
			return null;
		}

		Admin domain = new Admin();
		if (!entity.isNew()) {
			domain.setId(entity.getId());
		}

		domain.setUsername(entity.getUsername());
		domain.setPassword(entity.getPassword());
		domain.setMobile(entity.getMobile());
		domain.setEmail(entity.getEmail());
		domain.setName(entity.getName());

		return domain;
	}
}
