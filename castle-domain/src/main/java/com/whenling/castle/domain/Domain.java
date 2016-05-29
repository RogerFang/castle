package com.whenling.castle.domain;

import java.io.Serializable;

public class Domain<ID extends Serializable> implements Serializable {

	private static final long serialVersionUID = -2519390107662706651L;

	private ID id;

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}
}
