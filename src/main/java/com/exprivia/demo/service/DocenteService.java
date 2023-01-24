package com.exprivia.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.model.Docente;
import com.exprivia.demo.repository.DocenteRepo;

@Service
public class DocenteService {

	private final DocenteRepo drepository;

	public DocenteService(DocenteRepo drepository) {
		this.drepository = drepository;
	}

	public List<DocenteDto> findAllDocenti() {
		List<Docente> docenti = drepository.findAll();
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

		if (!drepository.existsById(docenteDto.getId())) {
			drepository.save(docente);
			return "Docente salvato";
		}
		return "Docente già presente";
	}

	public Docente updateDocente(DocenteDto docenteDto) {

		return null;
	}

}
