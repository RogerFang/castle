package com.whenling.castle.service.admin;

import org.springframework.stereotype.Service;

import com.whenling.castle.api.admin.user.AdminService;
import com.whenling.castle.domain.user.Admin;
import com.whenling.castle.framework.core.Provider;
import com.whenling.castle.service.CrudServiceImpl;

@Service
@Provider
public class AdminServiceImpl extends CrudServiceImpl<Admin, String>implements AdminService {

}
