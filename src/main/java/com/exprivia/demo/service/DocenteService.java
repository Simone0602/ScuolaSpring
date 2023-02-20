package com.exprivia.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.ClasseDto;
import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.enums.Materie;
import com.exprivia.demo.exception.IllegalPasswordException;
import com.exprivia.demo.exception.NotFoundDocenteException;
import com.exprivia.demo.model.Classe;
import com.exprivia.demo.model.Docente;
import com.exprivia.demo.model.Materia;
import com.exprivia.demo.repository.DocenteRepo;
import com.exprivia.demo.repository.MateriaRepository;

@Service
public class DocenteService {

	private final DocenteRepo docenteRepository;
	private final MateriaRepository materiaRepository;

	public DocenteService(DocenteRepo docenteRepository, MateriaRepository materiaRepository) {
		this.docenteRepository = docenteRepository;
		this.materiaRepository = materiaRepository;
	}

	// SERVE ALLO STUDENTE
	// TROVA TUTTI I DOCENTI
	public List<DocenteDto> findAllDocenti() {
		List<Docente> docenti = docenteRepository.findAll();
		List<DocenteDto> docentiDto = new ArrayList<>();

		for (Docente docente : docenti) {
			DocenteDto docenteDto = new DocenteDto();

			docenteDto.setNome(docente.getNome());
			docenteDto.setCognome(docente.getCognome());
			docenteDto.setMail(null);
			docenteDto.setPassword(null);
			docenteDto.setMaterie(getMaterie(docente));
			docenteDto.setCodiceFiscale(docente.getCodiceFiscale());

			docentiDto.add(docenteDto);
		}
		return docentiDto;
	}

	// SERVE ALLO STUDENTE
	// TROVA UN DOCENTE TRAMITE CODICE FISCALE
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

	// SERVE ALLA SEGRETERIA
	// AGGIUNGE UN DOCENTE
	public String addDocente(DocenteDto docenteDto) {
		Docente docente = new Docente();

		if (!docenteRepository.existsByCodiceFiscale(docenteDto.getCodiceFiscale())) {
			Set<Materia> materie = docenteDto.getMaterie().stream()
					.map(x -> this.materiaRepository.findByMateria(Materie.valueOf(x))).filter(x -> x != null)
					.collect(Collectors.toSet());

			docente.setNome(docenteDto.getNome());
			docente.setCognome(docenteDto.getCognome());
			docente.setCodiceFiscale(docenteDto.getCodiceFiscale());
			docente.setMail(docenteDto.getMail());
			docente.setPassword(docenteDto.getPassword());
			docente.setMaterie(materie);

			docenteRepository.save(docente);
			return "Docente salvato";
		}
		return "Docente già presente";
	}

	// LOGIN DOCENTE
	public DocenteDto login(DocenteDto docenteDto) {
		DocenteDto docenteDtoLoggato = new DocenteDto();
		Docente docente = docenteRepository.findDocenteByCodiceFiscale(docenteDto.getCodiceFiscale())
				.orElseThrow(() -> new NotFoundDocenteException("Docente non trovato"));

		if (!docente.getPassword().equals(docenteDto.getPassword())) {
			throw new IllegalPasswordException("Password errata");
		}

		docenteDtoLoggato.setNome(docente.getNome());
		docenteDtoLoggato.setCognome(docente.getCognome());
		docenteDtoLoggato.setMail(docente.getMail());
		docenteDtoLoggato.setPassword(docente.getPassword());
		docenteDtoLoggato.setCodiceFiscale(docente.getCodiceFiscale());
		docenteDtoLoggato.setMaterie(getMaterie(docente));

		return docenteDtoLoggato;
	}

	// SERVE ALLA SEGRETERIA
	// AGGIORNAMENTO DATI DOCENTE
	public DocenteDto updateDocente(DocenteDto docenteDto) {
		DocenteDto newDocenteDto = new DocenteDto();
		Docente docente = setDocente_docenteDto(docenteDto);

		newDocenteDto.setNome(docente.getNome());
		newDocenteDto.setCognome(docente.getCognome());
		newDocenteDto.setMail(docente.getMail());
		newDocenteDto.setPassword(null);
		newDocenteDto.setMaterie(getMaterie(docente));
		newDocenteDto.setCodiceFiscale(docente.getCodiceFiscale());

		return newDocenteDto;
	}

	// METODO USATO NELL'UPDATE
	public Docente setDocente_docenteDto(DocenteDto docenteDto) {
		Docente docente = docenteRepository.findDocenteByCodiceFiscale(docenteDto.getCodiceFiscale())
				.orElseThrow(() -> new NotFoundDocenteException("Docente non esistente"));

		Set<Materia> materie = docenteDto.getMaterie().stream()
				.map(x -> this.materiaRepository.findByMateria(Materie.valueOf(x))).filter(x -> x != null)
				.collect(Collectors.toSet());

		docente.setId(docente.getId());
		docente.setNome(docenteDto.getNome());
		docente.setCognome(docenteDto.getCognome());
		docente.setMail(docenteDto.getMail());
		docente.setPassword(docenteDto.getPassword());
		docente.setMaterie(materie);
		docente.setCodiceFiscale(docenteDto.getCodiceFiscale());

		docenteRepository.save(docente);

		return docente = docenteRepository.findDocenteByCodiceFiscale(docenteDto.getCodiceFiscale())
				.orElseThrow(() -> new NotFoundDocenteException("Docente non esistente"));
	}
	
	// ELIMINA DATI DOCENTE TRAMITE CODICE FISCALE
	public String deleteDocente(String codiceFiscale) {
		Optional<Docente> docente = docenteRepository.findDocenteByCodiceFiscale(codiceFiscale);
		if (docente.isPresent()) {
			docenteRepository.deleteById(docente.get().getId());
			return "Docente eliminato";
		}
		return "Docente non presente";
	}

	// STAMPA LE MATERIE DI UN DOCENTE
	private List<String> getMaterie(Docente docente) {
		List<String> materie = docente.getMaterie().stream().map(x -> x.getMateria().name())
				.collect(Collectors.toList());
		return materie;
	}
}
