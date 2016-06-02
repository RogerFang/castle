package com.whenling.castle.webapp.admin.user;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.whenling.castle.api.admin.user.AdminService;
import com.whenling.castle.domain.user.Admin;
import com.whenling.castle.support.core.Consumer;

@Controller
@RequestMapping("/admin/admin")
public class AdminController {

	@Consumer
	private AdminService adminService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void list() {
		List<Admin> admins = adminService.findAll();
		if (admins != null) {
			for (Admin admin : admins) {
				System.out.println(admin.getId());
			}
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public void save() {
		Admin admin = new Admin();
		admin.setUsername("abcd");
		admin.setPassword("bbb");
		admin.setName("essss");
		adminService.save(admin);
	}

}
