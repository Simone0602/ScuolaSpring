package com.exprivia.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.model.Studente;
import com.exprivia.demo.repository.StudentRepo;

@Service
public class StudentService {

	private final StudentRepo repository;

	public StudentService(StudentRepo repository) {
		this.repository = repository;
	}

	public List<StudenteDto> findAllStudent() {
		List<Studente> studenti = repository.findAll();
		List<StudenteDto> studentiDto = new ArrayList<>();

		for (Studente studente : studenti) {

			StudenteDto studenteDto = new StudenteDto();

			studenteDto.setNome(studente.getNome());
			studenteDto.setCognome(studente.getCognome());
			studenteDto.setUserCode(null);
			studenteDto.setMail(studente.getMail());
			studenteDto.setPas(null);

			studentiDto.add(studenteDto);

		}

		return studentiDto;
	}

	public String addStudent(StudenteDto studenteDto) {
		Studente studente = new Studente();

		if(!repository.existsById(studenteDto.getId())) {
			studente.setNome(studenteDto.getNome());
			studente.setCognome(studenteDto.getCognome());
			studente.setUserCode(studenteDto.getUserCode());
			studente.setMail(studenteDto.getMail());
			studente.setPas(studenteDto.getPas());
			
			repository.save(studente);
			return "Studente salvato";
		}
		return "Studente già presente";
	}

	public StudenteDto updateStudent(StudenteDto studenteDto) {
		StudenteDto newStudenteDto = new StudenteDto();
		Studente studente = repository.findStudentByUserCode(studenteDto.getUserCode())
				.orElseThrow(() -> new RuntimeException("Utente non esistente"));

		studente.setId(studente.getId());
		studente.setNome(studenteDto.getNome());
		studente.setCognome(studenteDto.getCognome());
		studente.setMail(studenteDto.getMail());
		studente.setPas(studenteDto.getPas());

		repository.save(studente);
		
		studente = repository.findStudentByUserCode(studenteDto.getUserCode())
				.orElseThrow(() -> new RuntimeException("Utente non esistente")); 
		
		newStudenteDto.setNome(studente.getNome());
		newStudenteDto.setCognome(studente.getCognome());
		newStudenteDto.setUserCode(studente.getUserCode());
		newStudenteDto.setMail(studente.getMail());
		newStudenteDto.setPas(studente.getPas());
		
		return newStudenteDto;

	}

	public String deleteStudent(String userCode) {
		Optional<Studente> studente = repository.findStudentByUserCode(userCode);
		if (studente.isPresent()) {
			repository.deleteById(studente.get().getId());
			return "utente eliminato";
		}
		return "Utente non presente";

	}
}
