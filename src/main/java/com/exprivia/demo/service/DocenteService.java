package com.exprivia.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.ClasseDto;
import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.exception.NotFoundDocenteException;
import com.exprivia.demo.model.Classe;
import com.exprivia.demo.model.Docente;
import com.exprivia.demo.repository.DocenteRepo;

@Service
public class DocenteService {

	private final DocenteRepo docenteRepository;

	public DocenteService(DocenteRepo docenteRepository) {
		this.docenteRepository = docenteRepository;
	}

	// SERVE ALLO STUDENTE
	public List<DocenteDto> findAllDocenti() {
		List<Docente> docenti = docenteRepository.findAll();
		List<DocenteDto> docentiDto = new ArrayList<>();

		for (Docente docente : docenti) {
			DocenteDto docenteDto = new DocenteDto();

			docenteDto.setNome(docente.getNome());
			docenteDto.setCognome(docente.getCognome());
			docenteDto.setMail(null);
			docenteDto.setPassword(null);
			docenteDto.setMateria(docente.getMateria());
			docenteDto.setCodiceFiscale(docente.getCodiceFiscale());

			docentiDto.add(docenteDto);
		}
		return docentiDto;
	}

	// SERVE ALLO STUDENTE
	public List<ClasseDto> findAllClassByDocente(String codiceFiscale) {
		List<ClasseDto> classiDto = new ArrayList<>();
		List<Classe> classi = docenteRepository.findDocenteByCodiceFiscale(codiceFiscale).get().getClassi();

		for (Classe classe : classi) {
			ClasseDto classeDto = new ClasseDto();

			classeDto.setAula(classe.getAula());
			classeDto.setCordinatore(classe.getCordinatore());
			classeDto.setSezione(classe.getSezione());

			classiDto.add(classeDto);
		}
		return classiDto;
	}

	//SERVE ALLA SEGRETERIA
	public String addDocente(DocenteDto docenteDto) {
		Docente docente = new Docente();

		docente.setNome(docenteDto.getNome());
		docente.setCognome(docenteDto.getCognome());
		docente.setCodiceFiscale(docenteDto.getCodiceFiscale());
		docente.setMail(docenteDto.getMail());
		docente.setPassword(docenteDto.getPassword());
		docente.setMateria(docenteDto.getMateria());

		if (!docenteRepository.existsByCodiceFiscale(docenteDto.getCodiceFiscale())) {
			docenteRepository.save(docente);
			return "Docente salvato";
		}
		return "Docente già presente";
	}

	//SERVE ALLA SEGRETERIA
	public DocenteDto updateDocente(DocenteDto docenteDto) {
		DocenteDto newDocenteDto = new DocenteDto();
		Docente docente = setDocente_docenteDto(docenteDto);

		newDocenteDto.setNome(docente.getNome());
		newDocenteDto.setCognome(docente.getCognome());
		newDocenteDto.setMail(docente.getMail());
		newDocenteDto.setPassword(null);
		newDocenteDto.setMateria(docente.getMateria());
		newDocenteDto.setCodiceFiscale(docente.getCodiceFiscale());

		return newDocenteDto;
	}
	// METODO USATO NELL'UPDATE
	public Docente setDocente_docenteDto(DocenteDto docenteDto) {
		Docente docente = docenteRepository.findDocenteByCodiceFiscale(docenteDto.getCodiceFiscale())
				.orElseThrow(() -> new NotFoundDocenteException("Docente non esistente"));

		docente.setId(docente.getId());
		docente.setNome(docenteDto.getNome());
		docente.setCognome(docenteDto.getCognome());
		docente.setMail(docenteDto.getMail());
		docente.setPassword(docenteDto.getPassword());
		docente.setMateria(docenteDto.getMateria());
		docente.setCodiceFiscale(docenteDto.getCodiceFiscale());

		docenteRepository.save(docente);

		return docente = docenteRepository.findDocenteByCodiceFiscale(docenteDto.getCodiceFiscale())
				.orElseThrow(() -> new NotFoundDocenteException("Docente non esistente"));
	}

	public String deleteDocente(String codiceFiscale) {
		Optional<Docente> docente = docenteRepository.findDocenteByCodiceFiscale(codiceFiscale);
		if (docente.isPresent()) {
			docenteRepository.deleteById(docente.get().getId());
			return "Docente eliminato";
		}
		return "Docente non presente";

	}
}
