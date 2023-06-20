package com.exprivia.demo.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exprivia.demo.dto.ClasseDto;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.exception.NotFoundClasseException;
import com.exprivia.demo.service.ClassService;

@AllArgsConstructor
@RestController
@RequestMapping(path = "classe")
public class ClassController {

	private final ClassService service;
	
	//TROVA TUTTE LE CLASSI
	@GetMapping(path = "/find-all")
	public ResponseEntity<List<ClasseDto>> getAllClassi() {
		List<ClasseDto> classi = service.findAllClassi();
		return new ResponseEntity<>(classi, HttpStatus.OK);
	}

	//TROVA GLI STUDENTI DI UNA DETERMINATA CLASSE
	@GetMapping(path = "/{sezione}/studenti")
	public ResponseEntity<List<StudenteDto>> getAllStudentBySezione(@PathVariable("sezione") String sezione) {
		try {
			List<StudenteDto> studenti = service.findAllStudentBySezione(sezione);
			return new ResponseEntity<>(studenti, HttpStatus.OK);
		}catch(NotFoundClasseException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
