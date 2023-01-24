package com.exprivia.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.model.Docente;
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

			docenteDto.setNome(docente.getNome());
			docenteDto.setCognome(docente.getCognome());
			docenteDto.setMail(docente.getMail());
			docenteDto.setPas(null);
			docenteDto.setMateria(docente.getMateria());
			docenteDto.setCodiceFiscale(docente.getCodiceFiscale());

			docentiDto.add(docenteDto);

		}

		return docentiDto;
	}

	public String addDocente(DocenteDto docenteDto) {
		Docente docente = new Docente();

		docente.setNome(docenteDto.getNome());
		docente.setCognome(docenteDto.getCognome());
		docente.setCodiceFiscale(docenteDto.getCodiceFiscale());
		docente.setMail(docenteDto.getMail());
		docente.setPas(docenteDto.getPas());
		docente.setMateria(docenteDto.getMateria());

		if (!repository.existsByCodiceFiscale(docenteDto.getCodiceFiscale())) {
			repository.save(docente);
			return "Docente salvato";
		}
		return "Docente già presente";
	}

	public DocenteDto updateDocente(DocenteDto docenteDto) {
		DocenteDto newDocenteDto = new DocenteDto();
		Docente docente = repository.findDocenteByCodiceFiscale(docenteDto.getCodiceFiscale())
				.orElseThrow(() -> new RuntimeException("Utente non esistente"));

		docente.setNome(docenteDto.getNome());
		docente.setCognome(docenteDto.getCognome());
		docente.setMail(docenteDto.getMail());
		docente.setPas(docenteDto.getPas());
		docente.setMateria(docenteDto.getMateria());
		docente.setCodiceFiscale(docenteDto.getCodiceFiscale());

		repository.save(docente);
		
		docente = repository.findDocenteByCodiceFiscale(docenteDto.getCodiceFiscale())
				.orElseThrow(() -> new RuntimeException("Utente non esistente")); 
		
		newDocenteDto.setNome(docente.getNome());
		newDocenteDto.setCognome(docente.getCognome());
		newDocenteDto.setMail(docente.getMail());
		newDocenteDto.setPas(null);
		newDocenteDto.setMateria(docente.getMateria());
		newDocenteDto.setCodiceFiscale(docenteDto.getCodiceFiscale());
		
		return newDocenteDto;

	}
	
	public String deleteDocente(String codiceFiscale) {
		Optional<Docente> docente = repository.findDocenteByCodiceFiscale(codiceFiscale);
		if (docente.isPresent()) {
			repository.deleteById(docente.get().getId());
			return "utente eliminato";
		}
		return "Utente non presente";

	}

}
