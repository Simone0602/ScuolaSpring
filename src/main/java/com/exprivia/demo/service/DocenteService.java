package com.exprivia.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.model.Docente;
import com.exprivia.demo.model.Studente;
import com.exprivia.demo.repository.DocenteRepo;

@Service
public class DocenteService {

	private final DocenteRepo repository;

	public DocenteService(DocenteRepo repository) {
		this.repository = repository;
	}

	public List<DocenteDto> findAllDocenti() {
		List<Docente> docenti = repository.findAll();
		List<DocenteDto> docentiDto = new ArrayList<>();

		for (Docente docente : docenti) {

			DocenteDto docenteDto = new DocenteDto();

			docenteDto.setId(docente.getId());
			docenteDto.setNome(docente.getNome());
			docenteDto.setCognome(docente.getCognome());
			docenteDto.setMail(docente.getMail());
			docenteDto.setPas(" ");
			docenteDto.setMateria(docente.getMateria());

			docentiDto.add(docenteDto);

		}

		return docentiDto;
	}

	public String addDocente(DocenteDto docenteDto) {
		Docente docente = new Docente();

		docente.setNome(docenteDto.getNome());
		docente.setCognome(docenteDto.getCognome());
		docente.setMail(docenteDto.getMail());
		docente.setPas(docenteDto.getPas());
		docente.setMateria(docenteDto.getMateria());

		if (!repository.existsById(docenteDto.getId())) {
			repository.save(docente);
			return "Docente salvato";
		}
		return "Docente già presente";
	}

	public DocenteDto updateDocente(DocenteDto docenteDto) {

		DocenteDto newDocenteDto = new DocenteDto();
		Docente docente = repository.findDocenteById(docenteDto.getId())
				.orElseThrow(() -> new RuntimeException("Utente non esistente"));

		docente.setId(docenteDto.getId());
		docente.setNome(docenteDto.getNome());
		docente.setCognome(docenteDto.getCognome());
		docente.setMail(docenteDto.getMail());
		docente.setPas(docenteDto.getPas());
		docente.setMateria(docenteDto.getMateria());

		repository.save(docente);
		
		docente = repository.findDocenteById(docenteDto.getId())
				.orElseThrow(() -> new RuntimeException("Utente non esistente")); 
		
		newDocenteDto.setNome(docente.getNome());
		newDocenteDto.setCognome(docente.getCognome());
		newDocenteDto.setMail(docente.getMail());
		newDocenteDto.setPas(null);
		newDocenteDto.setMateria(docente.getMateria());
		
		return newDocenteDto;

	}

}
