package com.exprivia.demo.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.dto.StudenteDto;

@RestController
@RequestMapping(path = "auth")
public class AuthenticationController {
	private final AuthenticationService service;
	
	public AuthenticationController(AuthenticationService service) {
		this.service = service;
	}

	@PostMapping(path = "/register-studente")
	public ResponseEntity<AuthenticationResponse> registerStudente(@RequestBody StudenteDto request){
		return new ResponseEntity<>(service.registerStudente(request), HttpStatus.OK);
	}
	
	@PostMapping(path = "/register-docente")
	public ResponseEntity<AuthenticationResponse> registerDocente(@RequestBody DocenteDto request){
		return new ResponseEntity<>(service.registerDocente(request), HttpStatus.OK);
	}
	
	@PostMapping(path = "/authenticate-student")
	public ResponseEntity<AuthenticationResponse> authenticateStudent(@RequestBody StudentRequest request){
		return new ResponseEntity<>(service.authenticateStudent(request), HttpStatus.OK);
	}
	
	@PostMapping(path = "/authenticate-docente")
	public ResponseEntity<AuthenticationResponse> authenticateDocente(@RequestBody DocenteRequest request){
		return new ResponseEntity<>(service.authenticateDocente(request), HttpStatus.OK);
	}
}
