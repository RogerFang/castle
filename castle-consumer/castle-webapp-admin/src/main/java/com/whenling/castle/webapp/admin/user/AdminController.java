package com.whenling.castle.webapp.admin.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.whenling.castle.api.admin.user.AdminService;
import com.whenling.castle.framework.core.Consumer;

@Controller
@RequestMapping("/admin/admin")
public class AdminController {

	@Consumer
	private AdminService adminService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void list() {
		adminService.findAll();
	}
}
