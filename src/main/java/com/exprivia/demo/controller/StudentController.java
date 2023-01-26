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
	@GetMapping(path = "/findStudent/{mail}")
	public ResponseEntity<StudenteDto> getStudentByCodeUser(@PathVariable("mail") String mail) {
		StudenteDto studente = service.findStudentByMail(mail);
		return new ResponseEntity<>(studente, HttpStatus.OK);
	}
	//SERVE ALLA SEGRETERIA PER TROVARE TUTTI QUELLI DELL'ULTIMO ANNO
	@GetMapping(path = "/findAllStudent")
	public ResponseEntity<List<StudenteDto>> getAllStudentBySezione5(){
		List<StudenteDto> studenti = service.findAllStudentBySezione5("5");
		return new ResponseEntity<>(studenti, HttpStatus.OK);
	}
	//SERVE PER ALLO STUDENTE
	@GetMapping(path = "/find/{sezione}")
	public ResponseEntity<List<StudenteDto>> getAllStudentBySezione(@PathVariable("sezione") String sezione) {
		List<StudenteDto> studenti = service.findAllStudentBySezione(sezione);
		return new ResponseEntity<>(studenti, HttpStatus.OK);
	}

	//SERVE PER LA SEGRETERIA
	@PostMapping(path = "/add")
	public ResponseEntity<String> addStudent(@RequestBody StudenteDto studenteDto) {
		String message = service.addStudent(studenteDto);
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}
	//SERVE ALLO STUDENTE PER LOGGARSI (INSERIMENTO DI USERCODE E PASS)
	@PostMapping(path = "/loginStudente")
	public ResponseEntity<StudenteDto> loginStudent(@RequestBody String userCode){
		StudenteDto studente = service.findStudent(userCode);
		return new ResponseEntity<>(studente, HttpStatus.FOUND);
	}
	
	//SERVE ALLA SEGRETERIA PER CAMBIARE I PARAMETRI DELLO STUDENTE
	@PutMapping(path = "/update")
	public ResponseEntity<StudenteDto> updateStudent(@RequestBody StudenteDto studenteDto) {
		StudenteDto studente = service.updateStudent(studenteDto);
		return new ResponseEntity<>(studente, HttpStatus.CREATED);
	}
	//SERVE ALLA SEGRETERIA PER ELIMINARE GLI STUDENTI CHE HANNO COMPLETATO GLI STUDI
	@DeleteMapping(path = "/delete/{mail}")
	public ResponseEntity<String> deleteStudent(@PathVariable("mail") String mail) {
		String message = service.deleteStudent(mail);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

}
