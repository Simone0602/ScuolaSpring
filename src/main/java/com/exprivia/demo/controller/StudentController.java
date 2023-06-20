package com.exprivia.demo.controller;

import com.exprivia.demo.dto.AssenzaDto;
import com.exprivia.demo.dto.RegistroFamiglia;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.exception.*;
import com.exprivia.demo.service.StudentService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "studente")
public class StudentController {

	private final StudentService service;
	
	/* get studente */
	@GetMapping(path = "/{userCode}/get-studente")
	public ResponseEntity<Object> getStudent(@PathVariable("userCode") String userCode) {
		try {
			StudenteDto studente = service.getStudent(userCode);
			return new ResponseEntity<>(studente, HttpStatus.OK);
		} catch (NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (IllegalPasswordException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

	/* Metodi invio mail e update password */
	@PostMapping(path = "/send-mail")
	public ResponseEntity<String> resetPassword(@RequestBody StudenteDto studenteDto) {
		try {
			String message = service.resetPassword(studenteDto);
			return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
		} catch (NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (IllegalMailException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	/* cambio password */
	@PutMapping(path = "/update-password/{token}")
	public ResponseEntity<String> updatePassword(@RequestBody String password, @PathVariable("token") String token) {
		try {
			String message = service.updatePassword(password, token);
			return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
		} catch (NotFoundTokenException | NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	/* invio messaggio tramite twilio */
	@GetMapping(value = "/send-message")
	public ResponseEntity<String> sendSMS() {

		Twilio.init("AC0d344551b0b5b6cf21f44b37e4832e30", "c69dd633a487c9993157f34da7096405");

		Message.creator(new PhoneNumber("+393895414759"), new PhoneNumber("+15005550006"), "prova messaggio").create();

		return new ResponseEntity<>("Invio messaggio riuscito", HttpStatus.OK);
	}

	/* update studente tramite dati anagrafici */
	@PutMapping(path = "/update")
	public ResponseEntity<String> updateStudente(@RequestBody StudenteDto studenteDto) {
		try {
			String message = service.updateStudent(studenteDto);
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch (NotFoundStudentException | NotFoundClasseException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (IllegalDatiException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	/* get assenze */
	@GetMapping(path = "/{userCode}/get-assenze")
	public ResponseEntity<Object> getAssenze(@PathVariable("userCode") String userCode){
		try {
			List<AssenzaDto> assenze = service.getAssenze(userCode);
			return new ResponseEntity<>(assenze, HttpStatus.OK);
		} catch (NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	/* giustifica assenze */
	@PutMapping(path = "/{userCode}/giustifica-assenze")
	public ResponseEntity<String> giustificaAssenze(
			@RequestBody List<AssenzaDto> assenzaDto, 
			@PathVariable("userCode") String userCode){
		try {
			String message = service.giustificaAssenze(assenzaDto, userCode);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	/* Metodo get lista di materie e voti */
	@GetMapping(path = "/{userCode}/get-registro")
	public ResponseEntity<RegistroFamiglia> getVoti(@PathVariable("userCode") String userCode) {
		try {
			RegistroFamiglia registroFamiglia = service.getVoti(userCode);
			return new ResponseEntity<>(registroFamiglia, HttpStatus.OK);
		} catch (NotFoundStudentException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
