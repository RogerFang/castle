package com.whenling.castle.support.repo.mongodb.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whenling.castle.support.repo.mongodb.test.entity.StudentDoc;
import com.whenling.castle.support.repo.mongodb.test.repo.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Override
	public void save(StudentDoc studentDoc) {
		studentRepository.save(studentDoc);
	}

}
