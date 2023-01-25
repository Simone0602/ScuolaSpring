package com.exprivia.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.exception.NotFoundSezioneException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.model.Classe;
import com.exprivia.demo.model.Studente;
import com.exprivia.demo.repository.ClassRepository;
import com.exprivia.demo.repository.StudentRepo;

@Service
public class StudentService {

	private final StudentRepo studentRepository;
	private final ClassRepository classRepository;

	@Autowired
	public StudentService(StudentRepo studentRepository, ClassRepository classRepository) {
		this.studentRepository = studentRepository;
		this.classRepository = classRepository;
	}

	//SERVE SOLO ALLA SEGRETERIA PER TROVARE UNO STUDENTE
	public StudenteDto findStudentByCodeUser(String codeUser) {
		StudenteDto studenteDto = new StudenteDto();
		Studente studente = studentRepository.findStudentByUserCode(codeUser)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));

		studenteDto.setNome(studente.getNome());
		studenteDto.setCognome(studente.getCognome());
		studenteDto.setUserCode(null);
		studenteDto.setMail(studente.getMail());
		studenteDto.setPas(null);
		studenteDto.setSezione(studente.getClasse().getSezione());
		
		return studenteDto;
	}
	
	//SERVE PER L'UTENTE
	public List<StudenteDto> findAllStudentBySezione(String sezione) {
		List<StudenteDto> studentiDto = new ArrayList<>();
		List<Studente> studenti = studentRepository.findAll();

		for (Studente studente : studenti) {
			StudenteDto studenteDto = new StudenteDto();

			if(studente.getClasse().getSezione().equalsIgnoreCase(sezione)) {
				studenteDto.setNome(studente.getNome());
				studenteDto.setCognome(studente.getCognome());
				studenteDto.setUserCode(null);
				studenteDto.setMail(studente.getMail());
				studenteDto.setPas(null);
				studenteDto.setSezione(studente.getClasse().getSezione());

				studentiDto.add(studenteDto);
			}
		}
		return studentiDto;
	}

	//SERVE SOLO PER LA SEGRETERIA
	public String addStudent(StudenteDto studenteDto, String sezione) {
		Studente studente = new Studente();
		Classe classe = classRepository.findBySezione(sezione)
				.orElseThrow(() -> new NotFoundSezioneException("Sezione non trovata"));
		
		if(!studentRepository.existsByUserCode(studenteDto.getUserCode())) {
			studente.setNome(studenteDto.getNome());
			studente.setCognome(studenteDto.getCognome());
			studente.setUserCode(studenteDto.getUserCode());
			studente.setMail(studenteDto.getMail());
			studente.setPas(studenteDto.getPas());
			studente.setClasse(classe);
			
			studentRepository.save(studente);
			return "Studente salvato";
		}
		return "Studente già presente";
	}

	public StudenteDto updateStudent(StudenteDto studenteDto) {
		StudenteDto newStudenteDto = new StudenteDto();
		Studente studente = studentRepository.findStudentByUserCode(studenteDto.getUserCode())
				.orElseThrow(() -> new RuntimeException("Utente non esistente"));

		studente.setId(studente.getId());
		studente.setNome(studenteDto.getNome());
		studente.setCognome(studenteDto.getCognome());
		studente.setMail(studenteDto.getMail());
		studente.setPas(studenteDto.getPas());

		studentRepository.save(studente);
		
		studente = studentRepository.findStudentByUserCode(studenteDto.getUserCode())
				.orElseThrow(() -> new RuntimeException("Utente non esistente")); 
		
		newStudenteDto.setNome(studente.getNome());
		newStudenteDto.setCognome(studente.getCognome());
		newStudenteDto.setUserCode(studente.getUserCode());
		newStudenteDto.setMail(studente.getMail());
		newStudenteDto.setPas(studente.getPas());
		
		return newStudenteDto;

	}

	public String deleteStudent(String userCode) {
		Optional<Studente> studente = studentRepository.findStudentByUserCode(userCode);
		if (studente.isPresent()) {
			studentRepository.deleteById(studente.get().getId());
			return "utente eliminato";
		}
		return "Utente non presente";

	}
}
