package com.whenling.castle.support.repo.mongodb.test;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.whenling.castle.support.repo.mongodb.MongodbConfigBean;
import com.whenling.castle.support.repo.mongodb.test.entity.CourseDoc;
import com.whenling.castle.support.repo.mongodb.test.entity.StudentDoc;
import com.whenling.castle.support.repo.mongodb.test.entity.StudentDoc.WorkExperience;
import com.whenling.castle.support.repo.mongodb.test.service.CourseService;
import com.whenling.castle.support.repo.mongodb.test.service.StudentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfig.class, MongodbConfigBean.class })
public class MongoBase {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void testBase() {

		CourseDoc math = new CourseDoc();
		math.setName("math");

		CourseDoc englist = new CourseDoc();
		englist.setName("englist");

		List<CourseDoc> courses = Lists.newArrayList(math, englist);
		mongoTemplate.insert(courses, CourseDoc.class);

		StudentDoc jack = new StudentDoc();
		jack.setName("jack");
		jack.setAge(12);
		jack.setCourses(courses);

		mongoTemplate.save(jack);
	}

	@Autowired
	private CourseService courseService;

	@Autowired
	private StudentService studentService;

	@Test
	public void testService() {
		CourseDoc math = new CourseDoc();
		math.setName("math2");

		CourseDoc englist = new CourseDoc();
		englist.setName("englist2");

		List<CourseDoc> courses = Lists.newArrayList(math, englist);
		courseService.save(courses);

		StudentDoc jack = new StudentDoc();
		jack.setName("jack2");
		jack.setAge(12);
		jack.setCourses(courses);

		WorkExperience experience1 = new WorkExperience();
		experience1.setStartDate(new Date());
		experience1.setEndDate(new Date());
		experience1.setDetail("aaa");
		WorkExperience experience2 = new WorkExperience();
		experience2.setStartDate(new Date());
		experience2.setEndDate(new Date());
		experience2.setDetail("bbb");
		jack.setWorkExperiences(Lists.newArrayList(experience1, experience2));

		studentService.save(jack);
	}
}
