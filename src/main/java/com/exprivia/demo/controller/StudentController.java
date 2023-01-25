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
import com.exprivia.demo.service.StudentService;

@RestController
@RequestMapping(path = "studente")
public class StudentController {

	private final StudentService service;

	public StudentController(StudentService service) {
		this.service = service;
	}
	
	//SERVE PER LA SEGRETERIA
	@GetMapping(path = "/findStudent/{codeUser}")
	public ResponseEntity<StudenteDto> getStudentByCodeUser(@PathVariable("codeUser") String codeUser) {
		StudenteDto studenti = service.findStudentByCodeUser(codeUser);
		return new ResponseEntity<>(studenti, HttpStatus.OK);
	}
	//SERVE PER L'UTENTE
	@GetMapping(path = "/find/{sezione}")
	public ResponseEntity<List<StudenteDto>> getAllStudentBySezione(@PathVariable("sezione") String sezione) {
		List<StudenteDto> studenti = service.findAllStudentBySezione(sezione);
		return new ResponseEntity<>(studenti, HttpStatus.OK);
	}

	//SERVE PER LA SEGRETERIA
	@PostMapping(path = "/add/{sezione}")
	public ResponseEntity<String> addStudent(@RequestBody StudenteDto studenteDto, @PathVariable("sezione") String sezione) {
		String message = service.addStudent(studenteDto, sezione);
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}

	@PutMapping(path = "/update")
	public ResponseEntity<StudenteDto> modStudent(@RequestBody StudenteDto studenteDto) {
		StudenteDto studente = service.updateStudent(studenteDto);
		return new ResponseEntity<>(studente, HttpStatus.CREATED);
	}

	@DeleteMapping(path = "/delete/{userCode}")
	public ResponseEntity<String> delStudent(@PathVariable("userCode") String userCode) {
		String message = service.deleteStudent(userCode);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

}
