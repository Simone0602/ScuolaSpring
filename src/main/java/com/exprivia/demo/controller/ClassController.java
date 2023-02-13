package com.exprivia.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exprivia.demo.dto.ClasseDto;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.service.ClassService;

@RestController
@CrossOrigin (origins = "http://localhost:4200")
@RequestMapping(path = "classe")
public class ClassController {

	private final ClassService service;

	public ClassController(ClassService service) {
		this.service = service;
	}
	
	@CrossOrigin
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
	
	@GetMapping(path = "/{sezione}/studenti")
	public ResponseEntity<List<StudenteDto>> getAllStudentBySezione(@PathVariable("sezione") String sezione) {
		List<StudenteDto> studenti = service.findAllStudentBySezione(sezione);
		return new ResponseEntity<>(studenti, HttpStatus.OK);
	}
}
