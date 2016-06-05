package com.whenling.castle.support.repo.mongodb.test.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.whenling.castle.support.repo.mongodb.entity.BaseMongoEntity;

@Document(collection = "students")
public class StudentDoc extends BaseMongoEntity {

	private static final long serialVersionUID = -1722006264085204317L;

	@Indexed(unique = true)
	private String name;

	private int age;

	@DBRef(lazy = true)
	private List<CourseDoc> courses;

	private List<WorkExperience> workExperiences;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<CourseDoc> getCourses() {
		return courses;
	}

	public void setCourses(List<CourseDoc> courses) {
		this.courses = courses;
	}

	public List<WorkExperience> getWorkExperiences() {
		return workExperiences;
	}

	public void setWorkExperiences(List<WorkExperience> workExperiences) {
		this.workExperiences = workExperiences;
	}

	public static class WorkExperience {
		private Date startDate;
		private Date endDate;
		private String detail;

		public Date getStartDate() {
			return startDate;
		}

		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		public Date getEndDate() {
			return endDate;
		}

		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}

	}

}
