package com.exprivia.demo.controller;

import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.dto.MateriaDto;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.exception.IllegalDatiException;
import com.exprivia.demo.exception.NotFoundClasseException;
import com.exprivia.demo.exception.NotFoundDocenteException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.service.SegreteriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "segreteria")
public class SegreteriaController {
	
	private final SegreteriaService segreteriaService;
	
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
	
	@PutMapping(path = "/update-studente")
	public ResponseEntity<String> updateStudente(@RequestBody StudenteDto studenteDto) {
		try {
			String message = segreteriaService.updateStudent(studenteDto);
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch (NotFoundStudentException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (NotFoundClasseException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (IllegalDatiException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	/* update docente tramite dati anagrafici */
	@PutMapping(path = "/update-docente")
	public ResponseEntity<String> updateDocente(@RequestBody DocenteDto docenteDto) {
		try {
			String message = segreteriaService.updateDocente(docenteDto);
			return new ResponseEntity<>(message, HttpStatus.CREATED);
		} catch (NotFoundDocenteException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (NotFoundClasseException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (IllegalDatiException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
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
