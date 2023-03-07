package com.exprivia.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.dto.MateriaDto;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.exception.NotFoundClasseException;
import com.exprivia.demo.exception.NotFoundDocenteException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.service.SegreteriaService;

@RestController
@RequestMapping(path = "segreteria")
public class SegreteriaController {
	
	private final SegreteriaService segreteriaService;

	public SegreteriaController(SegreteriaService segreteriaService) {
		this.segreteriaService = segreteriaService;
	}
	
	@GetMapping(path = "find-all-materie")
	public ResponseEntity<Object> findAllMaterie() {
		try {
			List<MateriaDto> materie = segreteriaService.findAllMaterie();
			return new ResponseEntity<>(materie, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping(path = "/find-all-student")
	public ResponseEntity<Object> findAllStudent() {
		try {
			List<StudenteDto> studenti = segreteriaService.findAllStudent();
			return new ResponseEntity<>(studenti, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping(path = "/find-all-docenti")
	public ResponseEntity<Object> findAllDocenti() {
		try {
			List<DocenteDto> docenti = segreteriaService.findAllDocenti();
			return new ResponseEntity<>(docenti, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping(path = "/save-studente")
	public ResponseEntity<String> saveStudent(@RequestBody StudenteDto studenteDto){
		try {
			String message = segreteriaService.saveStudent(studenteDto);
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		}catch (NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@PostMapping(path = "/save-docente")
	public ResponseEntity<String> saveDocente(@RequestBody DocenteDto docenteDto){
		try {
			String message = segreteriaService.saveDocente(docenteDto);
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch (NotFoundDocenteException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (NotFoundClasseException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@DeleteMapping(path = "/{userCode}/delete-studente")
	public ResponseEntity<String> deleteStudent(@PathVariable("userCode") String userCode){
		try {
			String message = segreteriaService.deleteStudent(userCode);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	@DeleteMapping(path = "/{codiceFiscale}/delete-docente")
	public ResponseEntity<String> deleteDocente(@PathVariable("codiceFiscale") String codiceFiscale){
		try {
			String message = segreteriaService.deleteDocente(codiceFiscale);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (NotFoundDocenteException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
}
