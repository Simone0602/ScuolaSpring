package com.exprivia.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exprivia.demo.dto.ClasseDto;
import com.exprivia.demo.service.ClassService;

@RestController
@RequestMapping(path = "classe")
public class ClassController {

	private final ClassService service;

	@Autowired
	public ClassController(ClassService service) {
		this.service = service;
	}
	
	@GetMapping(path = "/findAll")
	public ResponseEntity<List<ClasseDto>> getAllClassi() {

		List<ClasseDto> classi = service.findAllClassi();
		return new ResponseEntity<>(classi, HttpStatus.OK);
	}
	
	@PutMapping(path = "/update")
	public ResponseEntity<ClasseDto> updateClasse(@RequestBody ClasseDto classeDto) {
		ClasseDto classe = service.updateClasse(classeDto);
		return new ResponseEntity<>(classe, HttpStatus.CREATED);
	}
}
