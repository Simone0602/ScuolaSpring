package com.exprivia.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.model.Studente;
import com.exprivia.demo.service.DocenteService;

@RestController
@RequestMapping (value = "/doce")
public class DocenteController {
	
	private final DocenteService dservice;

	public DocenteController(DocenteService dservice) {
		this.dservice = dservice;
	}
	
	@GetMapping(path = "/dget")
	public ResponseEntity<List<StudenteDto>> getAllStudent() {
		List<StudenteDto> studenti = service.findAllStudent();
		return new ResponseEntity<>(studenti, HttpStatus.OK);
	}
	
	@PostMapping(path = "/dadd")
	public ResponseEntity<Studente> addStudent(@RequestBody StudenteDto studenteDto) {
		Studente studente = service.addStudent(studenteDto);
		return new ResponseEntity<>(studente, HttpStatus.CREATED);
	}

	@PutMapping(path = "/dmod")
	public ResponseEntity<Studente> modStudent(@RequestBody StudenteDto studenteDto) {
		Studente studente = service.modStudent(studenteDto);
		return new ResponseEntity<>(studente, HttpStatus.CREATED);
	}

	@DeleteMapping(path = "/ddel/{id}")
	public ResponseEntity<?> delStudent(@PathVariable("id") long id) {
		service.delStudent(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
