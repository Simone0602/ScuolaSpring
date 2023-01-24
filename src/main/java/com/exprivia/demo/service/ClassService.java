package com.exprivia.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.ClasseDto;
import com.exprivia.demo.model.Classe;
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

		classe.setSezione(classeDto.getSezione());
		classe.setCordinatore(classeDto.getCordinatore());
		classe.setAula(classeDto.getAula());
		
		repository.save(classe);
		
		classe = repository.findClasseById(classeDto.getId())
				.orElseThrow(() -> new RuntimeException("Classe non esistente"));
		
		newClasseDto.setSezione(classe.getSezione());
		newClasseDto.setCordinatore(classe.getCordinatore());
		newClasseDto.setAula(classe.getAula());
		
		
		return newClasseDto;
	}
}
