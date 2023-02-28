package com.exprivia.demo.controller;

import java.io.UnsupportedEncodingException;
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

import com.exprivia.demo.dto.AssenzaDto;
import com.exprivia.demo.dto.RegistroFamiglia;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.exception.DontSendEmailException;
import com.exprivia.demo.exception.IllegalDatiException;
import com.exprivia.demo.exception.IllegalMailException;
import com.exprivia.demo.exception.IllegalPasswordException;
import com.exprivia.demo.exception.NotFoundClasseException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.exception.NotFoundTokenException;
import com.exprivia.demo.service.StudentService;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@RestController
@RequestMapping(path = "studente")
public class StudentController {

	private final StudentService service;

	public StudentController(StudentService service) {
		this.service = service;
	}

	/* Segreteria */
	@GetMapping(path = "/find-student/{mail}")
	public ResponseEntity<StudenteDto> getStudentByMail(@PathVariable("mail") String mail) {
		StudenteDto studente = service.findStudentByMail(mail);
		return new ResponseEntity<>(studente, HttpStatus.OK);
	}

	//aggiungere uno studente
	@PostMapping(path = "/add")
	public ResponseEntity<String> addStudent(@RequestBody StudenteDto studenteDto) {
		String message = service.addStudent(studenteDto);
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}

	// cambiare i parametri dello studente
	@PutMapping(path = "/update-by-segreteria")
	public ResponseEntity<StudenteDto> updateStudent(@RequestBody StudenteDto studenteDto) {
		StudenteDto studente = service.updateStudentBySegreteria(studenteDto);
		return new ResponseEntity<>(studente, HttpStatus.CREATED);
	}

	// per eliminare gli studenti che hanno terminato gli studi
	@DeleteMapping(path = "/delete/{userCode}")
	public ResponseEntity<String> deleteStudent(@PathVariable("useCode") String userCode) {
		String message = service.deleteStudent(userCode);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	//METODI UTILIZZATI DALLO STUDENTE
	
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
	public ResponseEntity<String> resetPassword(@RequestBody StudenteDto studenteDto) throws UnsupportedEncodingException {
		try {
			String message = service.resetPassword(studenteDto);
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

	/* update studente tramite dati anagrici */
	@PutMapping(path = "/update")
	public ResponseEntity<String> updateStudente(@RequestBody StudenteDto studenteDto) {
		try {
			String message = service.updateStudent(studenteDto);
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch (NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (NotFoundClasseException e) {
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
	@GetMapping(path = "/{email}/getRegistro")
	public ResponseEntity<RegistroFamiglia> getVoti(@PathVariable("email") String email) {
		try {
			RegistroFamiglia registroFamiglia = service.getVoti(email);
			return new ResponseEntity<>(registroFamiglia, HttpStatus.OK);
		} catch (NotFoundStudentException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
