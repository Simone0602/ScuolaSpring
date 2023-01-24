package com.exprivia.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exprivia.demo.service.ClassService;

@RestController
@RequestMapping(path = "classe")
public class ClassController {

	private final ClassService classService;

	@Autowired
	public ClassController(ClassService classService) {
		this.classService = classService;
	}
}
