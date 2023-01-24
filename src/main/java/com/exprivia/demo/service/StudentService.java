package com.exprivia.demo.service;

import java.util.ArrayList;
import java.util.List;

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

			studenteDto.setId(studente.getId());
			studenteDto.setNome(studente.getNome());
			studenteDto.setCognome(studente.getCognome());
			studenteDto.setUserCode(studente.getUserCode());
			studenteDto.setMail(studente.getMail());
			studenteDto.setPas(" ");

			studentiDto.add(studenteDto);

		}

		return studentiDto;
	}

	public String addStudent(StudenteDto studenteDto) {
		Studente studente = new Studente();

		studente.setNome(studenteDto.getNome());
		studente.setCognome(studenteDto.getCognome());
		studente.setUserCode(studenteDto.getUserCode());
		studente.setMail(studenteDto.getMail());
		studente.setPas(studenteDto.getPas());

		if(!repository.existsById(studenteDto.getId())) {
			repository.save(studente);
			return "Studente salvato";
		}
		return "Studente già presente";
	}

	public Studente modStudent(StudenteDto studenteDto) {

		Studente studente = new Studente();

		studente.setId(studenteDto.getId());
		studente.setNome(studenteDto.getNome());
		studente.setCognome(studenteDto.getCognome());
		studente.setUserCode(studenteDto.getUserCode());
		studente.setMail(studenteDto.getMail());
		studente.setPas(studenteDto.getPas());

		return repository.save(studente);

	}

	public void delStudent(long id) {
		repository.deleteById(id);
	}
}
