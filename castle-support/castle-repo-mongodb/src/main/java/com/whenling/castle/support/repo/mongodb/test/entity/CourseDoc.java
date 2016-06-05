package com.whenling.castle.support.repo.mongodb.test.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.whenling.castle.support.repo.mongodb.entity.BaseMongoEntity;

@Document(collection = "courses")
public class CourseDoc extends BaseMongoEntity {

	private static final long serialVersionUID = 4122704845371085903L;

	@Indexed(unique = true)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
