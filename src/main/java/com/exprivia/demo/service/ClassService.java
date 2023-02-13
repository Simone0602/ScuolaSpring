package com.exprivia.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.ClasseDto;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.model.Classe;
import com.exprivia.demo.model.Studente;
import com.exprivia.demo.repository.ClassRepository;

@Service
public class ClassService {
	private final ClassRepository repository;

	public ClassService(ClassRepository repository) {
		this.repository = repository;
	}

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

	public ClasseDto updateClasse(ClasseDto classeDto) {
		ClasseDto newClasseDto = new ClasseDto();
		Classe classe = repository.findClasseById(classeDto.getId())
				.orElseThrow(() -> new RuntimeException("Classe non esistente"));
		
		classe.setId(classeDto.getId());
		classe.setSezione(classeDto.getSezione());
		classe.setCordinatore(classeDto.getCordinatore());
		classe.setAula(classeDto.getAula());
		
		repository.save(classe);
		
		classe = repository.findClasseById(classeDto.getId())
				.orElseThrow(() -> new RuntimeException("Classe non esistente"));
		
		newClasseDto.setId(classe.getId());
		newClasseDto.setSezione(classe.getSezione());
		newClasseDto.setCordinatore(classe.getCordinatore());
		newClasseDto.setAula(classe.getAula());
		
		
		return newClasseDto;
	}
	
	public List<StudenteDto> findAllStudentBySezione(String sezione) {
		List<StudenteDto> studentiDto = new ArrayList<>();
		List<Studente> studenti = repository.findBySezione(sezione).get().getStudenti();

		for (Studente studente : studenti) {
			StudenteDto studenteDto = new StudenteDto();

			studenteDto = StudentService.conversioneStudente_StudenteDto(studente);
			studenteDto.setUserCode(null);
			studenteDto.setPassword(null);

			studentiDto.add(studenteDto);
		}
		return studentiDto;
	}
}
