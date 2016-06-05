package com.whenling.castle.support.repo.mongodb.test.service;

import com.whenling.castle.support.repo.mongodb.test.entity.CourseDoc;

public interface CourseService {

	void save(Iterable<CourseDoc> courseDocs);

}
