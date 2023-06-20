package com.exprivia.demo.service;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.ClasseDto;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.exception.NotFoundClasseException;
import com.exprivia.demo.model.Classe;
import com.exprivia.demo.model.Studente;
import com.exprivia.demo.repository.ClassRepository;

@Service
@AllArgsConstructor
public class ClassService {
	private final ClassRepository repository;

	//TROVA TUTTE LE CLASSI
	public List<ClasseDto> findAllClassi() {
		List<Classe> classi = repository.findAll();
		List<ClasseDto> classiDto = new ArrayList<>();

		for (Classe classe : classi) {

			ClasseDto classeDto = new ClasseDto();

			classeDto.setSezione(classe.getSezione());
			classeDto.setCordinatore(classe.getCordinatore());
			classeDto.setAula(classe.getAula());

			classiDto.add(classeDto);

		}

		return classiDto;
	}
	
	//TROVA TUTTI GLI STUDENTI DI UNA DETERMINATA CLASSE
	public List<StudenteDto> findAllStudentBySezione(String sezione) {
		List<StudenteDto> studentiDto = new ArrayList<>();
		Classe classe = repository.findBySezione(sezione)
				.orElseThrow(() -> new NotFoundClasseException("Classe non trovata"));
		List<Studente> studenti = classe.getStudenti();
		
		for (Studente studente : studenti) {
			StudenteDto studenteDto = new StudenteDto();

			studenteDto.setNome(studente.getNome());
			studenteDto.setCognome(studente.getCognome());
			studenteDto.setMail(studente.getMail());
			studenteDto.setPassword(null);
			studenteDto.setUserCode(null);
			studenteDto.setSezione(studente.getClasse().getSezione());

			studentiDto.add(studenteDto);
		}
		return studentiDto;
	}
}
