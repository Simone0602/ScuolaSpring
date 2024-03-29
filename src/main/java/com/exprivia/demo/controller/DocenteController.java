package com.exprivia.demo.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exprivia.demo.dto.ClasseDto;
import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.exception.DontSendEmailException;
import com.exprivia.demo.exception.IllegalDatiException;
import com.exprivia.demo.exception.IllegalMailException;
import com.exprivia.demo.exception.IllegalPasswordException;
import com.exprivia.demo.exception.NotFoundClasseException;
import com.exprivia.demo.exception.NotFoundDocenteException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.exception.NotFoundTokenException;
import com.exprivia.demo.service.DocenteService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@AllArgsConstructor
@RestController
@RequestMapping(path = "docente")
public class DocenteController {

	private final DocenteService service;

	// SERVE ALLO STUDENTE PER VEDERE TUTTI GLI INSEGNANTI
	@GetMapping(path = "/find-all")
	public ResponseEntity<List<DocenteDto>> getAllDocenti() {
		List<DocenteDto> docenti = service.findAllDocenti();
		return new ResponseEntity<>(docenti, HttpStatus.OK);
	}
	
	@GetMapping(path = "/find/{codiceFiscale}")
	public ResponseEntity<Object> findClassiByDocente(@PathVariable("codiceFiscale") String codiceFiscale) {
		try {
			List<ClasseDto> classi = service.findClassByDocente(codiceFiscale);
			return new ResponseEntity<>(classi, HttpStatus.OK);
		} catch (NotFoundDocenteException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	/* get docente */
	@GetMapping(path = "/{codiceFiscale}/get-docente")
	public ResponseEntity<Object> getDocente(@PathVariable("codiceFiscale") String codiceFiscale) {
		try {
			DocenteDto docente = service.getDocente(codiceFiscale);
			return new ResponseEntity<>(docente, HttpStatus.OK);
		} catch (NotFoundDocenteException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (IllegalPasswordException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	/* update docente tramite dati anagrafici */
	@PutMapping(path = "/update")
	public ResponseEntity<String> updateDocente(@RequestBody DocenteDto docenteDto) {
		try {
			String message = service.updateDocente(docenteDto);
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch (NotFoundDocenteException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (NotFoundClasseException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (IllegalDatiException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	/* Metodi invio mail e update password */
	@PostMapping(path = "/send-mail")
	public ResponseEntity<String> resetPassword(@RequestBody DocenteDto docenteDto) throws UnsupportedEncodingException {
		try {
			String message = service.resetPassword(docenteDto);
			return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
		} catch (NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (IllegalMailException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (DontSendEmailException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	/* cambio password */
	@PutMapping(path = "/update-password/{token}")
	public ResponseEntity<String> updatePassword(@RequestBody String password, @PathVariable("token") String token) {
		try {
			String message = service.updatePassword(password, token);
			return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
		} catch (NotFoundTokenException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	/* invio messaggio tramite twilio */
	@GetMapping(value = "/send-message")
	public ResponseEntity<String> sendSMS() {

		Twilio.init("AC0d344551b0b5b6cf21f44b37e4832e30", "c69dd633a487c9993157f34da7096405");

		Message.creator(new PhoneNumber("+393895414759"), new PhoneNumber("+15005550006"), "prova messaggio").create();

		return new ResponseEntity<String>("Invio messaggio riuscito", HttpStatus.OK);
	}
}
