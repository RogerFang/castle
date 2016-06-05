package com.whenling.castle.support.repo.mongodb.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whenling.castle.support.repo.mongodb.test.entity.CourseDoc;
import com.whenling.castle.support.repo.mongodb.test.repo.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseRepository courseRepository;

	@Override
	public void save(Iterable<CourseDoc> courseDocs) {
		courseRepository.save(courseDocs);
	}

}
