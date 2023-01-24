package com.exprivia.demo.service;

import org.springframework.stereotype.Service;

import com.exprivia.demo.repository.ClassRepository;

@Service
public class ClassService {
	private final ClassRepository classRepository;

	public ClassService(ClassRepository classRepository) {
		this.classRepository = classRepository;
	}
}
