package com.exprivia.demo.auth;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exprivia.demo.config.JwtService;
import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.enums.Materie;
import com.exprivia.demo.exception.NotFoundClasseException;
import com.exprivia.demo.model.Classe;
import com.exprivia.demo.model.Docente;
import com.exprivia.demo.model.Materia;
import com.exprivia.demo.model.Studente;
import com.exprivia.demo.repository.ClassRepository;
import com.exprivia.demo.repository.DocenteRepo;
import com.exprivia.demo.repository.MateriaRepository;
import com.exprivia.demo.repository.StudentRepo;

@Service
public class AuthenticationService {
	private final StudentRepo studentRepository;
	private final ClassRepository classRepository;
	private final DocenteRepo docenteRepo;
	private final MateriaRepository materiaRepository;
	
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	public AuthenticationService(
			StudentRepo studentRepository, 
			ClassRepository classRepository,
			DocenteRepo docenteRepo,
			MateriaRepository materiaRepository,
			PasswordEncoder passwordEncoder,  
			AuthenticationManager authenticationManager,
			JwtService jwtService
	) {
		this.studentRepository = studentRepository;
		this.classRepository = classRepository;
		this.docenteRepo = docenteRepo;
		this.materiaRepository = materiaRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	public AuthenticationResponse registerStudente(StudenteDto request) {
		Classe classe = classRepository.findBySezione(request.getSezione())
				.orElseThrow(() -> new NotFoundClasseException("Classe non trovata"));
		Studente studente = new Studente();
		
		studente.setNome(request.getNome());
		studente.setCognome(request.getCognome());
		studente.setMail(request.getMail());
		studente.setPass(passwordEncoder.encode(request.getPassword()));
		studente.setClasse(classe);
		studente.setUserCode(request.getUserCode());
		studentRepository.save(studente);

		var jwtToken = jwtService.generateToken(studente);
		AuthenticationResponse authResponse = new AuthenticationResponse(jwtToken, "studente");
		return authResponse;
	}
	
	public AuthenticationResponse registerDocente(DocenteDto request) {
		Set<Materia> materie = request.getMaterie()
				.stream()
				.map(x -> this.materiaRepository.findByMateria(Materie.valueOf(x)))
				.filter(x -> x != null)
				.collect(Collectors.toSet());
		Docente docente = new Docente();
		
		docente.setNome(request.getNome());
		docente.setCognome(request.getCognome());
		docente.setMail(request.getMail());
		docente.setPass(passwordEncoder.encode(request.getPassword()));
		docente.setCodiceFiscale(request.getCodiceFiscale());
		docente.setMaterie(materie);
		docenteRepo.save(docente);
		
		var jwtToken = jwtService.generateToken(docente);
		AuthenticationResponse authResponse = new AuthenticationResponse(jwtToken, "docente");
		return authResponse;
	}

	public AuthenticationResponse authenticateStudent(StudentRequest request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
					request.getUserCode(), 
					request.getPassword()
			)
		);
		var studente = studentRepository.findStudentByUserCode(request.getUserCode())
				.orElseThrow();
		var jwtToken = jwtService.generateToken(studente);
		AuthenticationResponse authResponse = new AuthenticationResponse(jwtToken, "studente");
		return authResponse;
	}
	
	public AuthenticationResponse authenticateDocente(DocenteRequest request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
					request.getCodiceFiscale(),
					request.getPassword()
			)
		);
		var docente = docenteRepo.findDocenteByCodiceFiscale(request.getCodiceFiscale())
				.orElseThrow();
		var jwtToken = jwtService.generateToken(docente);
		AuthenticationResponse authResponse = new AuthenticationResponse(jwtToken, "docente");
		return authResponse;
	}
}
