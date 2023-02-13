package com.exprivia.demo.controller;

import java.io.UnsupportedEncodingException;
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

import com.exprivia.demo.dto.RegistroFamiglia;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.exception.DontSendEmailException;
import com.exprivia.demo.exception.IllegalMailException;
import com.exprivia.demo.exception.IllegalPasswordException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.exception.NotFoundTokenException;
import com.exprivia.demo.service.StudentService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "studente")
public class StudentController {

	private final StudentService service;

	public StudentController(StudentService service) {
		this.service = service;
	}
	
	//SERVE ALLA SEGRETERIA
	@GetMapping(path = "/findStudent/{mail}")
	public ResponseEntity<StudenteDto> getStudentByMail(@PathVariable("mail") String mail) {
		StudenteDto studente = service.findStudentByMail(mail);
		return new ResponseEntity<>(studente, HttpStatus.OK);
	}
	//SERVE ALLA SEGRETERIA PER TROVARE TUTTI QUELLI DELL'ULTIMO ANNO
	@GetMapping(path = "/findAllStudent")
	public ResponseEntity<List<StudenteDto>> getAllStudentBySezione5(){
		List<StudenteDto> studenti = service.findAllStudentBySezione5("5");
		return new ResponseEntity<>(studenti, HttpStatus.OK);
	}

	//SERVE ALLA SEGRETERIA
	@PostMapping(path = "/add")
	public ResponseEntity<String> addStudent(@RequestBody StudenteDto studenteDto) {
		String message = service.addStudent(studenteDto);
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}
	//SERVE ALLO STUDENTE PER LOGGARSI (INSERIMENTO DI USERCODE E PASS)
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
	//SERVE PER INVIARE L'EMAIL
	@PostMapping(path = "/sendEmail/{tipoUser}")
	public ResponseEntity<String> resetPassword(@RequestBody StudenteDto studenteDto, @PathVariable("tipoUser") String tipoUser) throws UnsupportedEncodingException{
		try {
			String message = service.resetPassword(studenteDto, tipoUser);
			return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
		}catch(NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(IllegalMailException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}catch(DontSendEmailException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}
	//SERVE PER POTER OTTENERE IL TOKEN
	@PostMapping(path = "/getToken")
	public ResponseEntity<String> getToken(@RequestBody String userCode){
		try {
			String newToken = service.getToken(userCode);
			return new ResponseEntity<>(newToken, HttpStatus.OK);
		}catch(NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(NotFoundTokenException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	//SERVE ALLA SEGRETERIA PER CAMBIARE I PARAMETRI DELLO STUDENTE
	@PutMapping(path = "/update")
	public ResponseEntity<StudenteDto> updateStudent(@RequestBody StudenteDto studenteDto) {
		StudenteDto studente = service.updateStudent(studenteDto);
		return new ResponseEntity<>(studente, HttpStatus.CREATED);
	}
	//SERVE PER FARE L'UPDATE DELLA PASSWORD
	@PutMapping(path = "/updatePassword/{token}")
	public ResponseEntity<String> updatePassword(@RequestBody String password, @PathVariable("token") String token){
		try {
			String message = service.updatePassword(password, token);
			return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
		}catch(NotFoundTokenException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	//SERVE ALLA SEGRETERIA PER ELIMINARE GLI STUDENTI CHE HANNO COMPLETATO GLI STUDI
	@DeleteMapping(path = "/delete/{userCode}")
	public ResponseEntity<String> deleteStudent(@PathVariable("useCode") String userCode) {
		String message = service.deleteStudent(userCode);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	@GetMapping(path = "/{email}/getRegistro")
	public ResponseEntity<RegistroFamiglia> getVoti(@PathVariable("email") String email){
		try {
			RegistroFamiglia registroFamiglia = service.getVoti(email);
			return new ResponseEntity<>(registroFamiglia, HttpStatus.OK);
		}catch(NotFoundStudentException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
