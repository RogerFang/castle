package com.whenling.castle.service.admin.user;

import org.springframework.data.mongodb.core.mapping.Document;

import com.whenling.castle.support.repo.mongodb.entity.BizMongoEntity;

@Document(collection = "admins")
public class AdminDoc extends BizMongoEntity<AdminDoc, String> {

	private static final long serialVersionUID = 6554095987798512514L;

	private String username;
	private String password;

	private String mobile;
	private String email;

	private String name;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
