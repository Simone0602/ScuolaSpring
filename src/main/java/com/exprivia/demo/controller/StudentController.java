package com.exprivia.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.exception.IllegalPasswordException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.service.StudentService;

@RestController
@RequestMapping(path = "studente")
public class StudentController {

	private final StudentService service;

	public StudentController(StudentService service) {
		this.service = service;
	}
	
	//SERVE ALLA SEGRETERIA
	@CrossOrigin
	@GetMapping(path = "/findStudent/{mail}")
	public ResponseEntity<StudenteDto> getStudentByMail(@PathVariable("mail") String mail) {
		StudenteDto studente = service.findStudentByMail(mail);
		return new ResponseEntity<>(studente, HttpStatus.OK);
	}
	//SERVE ALLA SEGRETERIA PER TROVARE TUTTI QUELLI DELL'ULTIMO ANNO
	@CrossOrigin
	@GetMapping(path = "/findAllStudent")
	public ResponseEntity<List<StudenteDto>> getAllStudentBySezione5(){
		List<StudenteDto> studenti = service.findAllStudentBySezione5("5");
		return new ResponseEntity<>(studenti, HttpStatus.OK);
	}
	//SERVE ALLO STUDENTE
	@CrossOrigin
	@GetMapping(path = "/find/{sezione}")
	public ResponseEntity<List<StudenteDto>> getAllStudentBySezione(@PathVariable("sezione") String sezione) {
		List<StudenteDto> studenti = service.findAllStudentBySezione(sezione);
		return new ResponseEntity<>(studenti, HttpStatus.OK);
	}

	//SERVE ALLA SEGRETERIA
	@CrossOrigin
	@PostMapping(path = "/add")
	public ResponseEntity<String> addStudent(@RequestBody StudenteDto studenteDto) {
		String message = service.addStudent(studenteDto);
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}
	//SERVE ALLO STUDENTE PER LOGGARSI (INSERIMENTO DI USERCODE E PASS)
	@CrossOrigin
	@PostMapping(path = "/loginStudente")
	public ResponseEntity<Object> loginStudent(@RequestBody StudenteDto studenteDto){
		try {
			StudenteDto studente = service.findStudent(studenteDto);
			return new ResponseEntity<>(studente, HttpStatus.OK);
		}catch(NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(IllegalPasswordException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	//SERVE ALLA SEGRETERIA PER CAMBIARE I PARAMETRI DELLO STUDENTE
	@CrossOrigin
	@PutMapping(path = "/update")
	public ResponseEntity<StudenteDto> updateStudent(@RequestBody StudenteDto studenteDto) {
		StudenteDto studente = service.updateStudent(studenteDto);
		return new ResponseEntity<>(studente, HttpStatus.CREATED);
	}
	//SERVE ALLA SEGRETERIA PER ELIMINARE GLI STUDENTI CHE HANNO COMPLETATO GLI STUDI
	@CrossOrigin
	@DeleteMapping(path = "/delete/{userCode}")
	public ResponseEntity<String> deleteStudent(@PathVariable("useCode") String userCode) {
		String message = service.deleteStudent(userCode);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
}
