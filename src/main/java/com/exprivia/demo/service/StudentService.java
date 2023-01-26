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

	// SERVE SOLO ALLA SEGRETERIA PER TROVARE UNO STUDENTE
	public StudenteDto findStudentByMail(String mail) {
		StudenteDto studenteDto = new StudenteDto();
		Studente studente = studentRepository.findStudentByMail(mail)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));

		studenteDto.setNome(studente.getNome());
		studenteDto.setCognome(studente.getCognome());
		studenteDto.setUserCode(null);
		studenteDto.setMail(studente.getMail());
		studenteDto.setPas(null);
		studenteDto.setSezione(studente.getClasse().getSezione());

		return studenteDto;
	}
	
	//SERVE ALLA SEGRETERIA
	public List<StudenteDto> findAllStudentBySezione5(String numeroSezione) {
		List<StudenteDto> studentiDto = new ArrayList<>();
		List<Studente> studenti = studentRepository.findAll();
		
		for(Studente studente : studenti) {
			StudenteDto studenteDto = new StudenteDto();
			
			if(studente.getClasse().getSezione().contains(numeroSezione)) {
				studenteDto.setNome(studente.getNome());
				studenteDto.setCognome(studente.getCognome());
				studenteDto.setUserCode(studente.getUserCode());
				studenteDto.setMail(studente.getMail());
				studenteDto.setPas(studente.getPas());
				studenteDto.setSezione(studente.getClasse().getSezione());

				studentiDto.add(studenteDto);
			}
		}
		return studentiDto;
	}

	// SERVE PER L'UTENTE
	public List<StudenteDto> findAllStudentBySezione(String sezione) {
		List<StudenteDto> studentiDto = new ArrayList<>();
		List<Studente> studenti = studentRepository.findAll();

		for (Studente studente : studenti) {
			StudenteDto studenteDto = new StudenteDto();

			if (studente.getClasse().getSezione().equalsIgnoreCase(sezione)) {
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

	// SERVE SOLO PER LA SEGRETERIA
	public String addStudent(StudenteDto studenteDto) {
		Studente studente = new Studente();
		Classe classe = classRepository.findBySezione(studenteDto.getSezione())
				.orElseThrow(() -> new NotFoundSezioneException("Sezione non trovata"));

		if (!studentRepository.existsByUserCode(studenteDto.getUserCode())) {
			if(!studentRepository.existsByMail(studenteDto.getMail())) {
				studente.setNome(studenteDto.getNome());
				studente.setCognome(studenteDto.getCognome());
				studente.setUserCode(studenteDto.getUserCode());
				studente.setMail(studenteDto.getMail());
				studente.setPas(studenteDto.getPas());
				studente.setClasse(classe);

				studentRepository.save(studente);
				return "Studente salvato";
			}
			return "Email già esistente";
		}
		return "Studente già presente";
	}
	
	//LOGIN PER LO STUDENTE
	public StudenteDto findStudent(String userCode) {
		StudenteDto studenteDtoLoggato = new StudenteDto();
		Studente studente = studentRepository.findStudentByUserCode(userCode)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));;
		
		if(studente != null) {
			studenteDtoLoggato.setNome(studente.getNome());
			studenteDtoLoggato.setCognome(studente.getCognome());
			studenteDtoLoggato.setUserCode(null);
			studenteDtoLoggato.setMail(studente.getMail());
			studenteDtoLoggato.setPas(null);
			studenteDtoLoggato.setSezione(studente.getClasse().getSezione());
			
			return studenteDtoLoggato;
		}
		return studenteDtoLoggato;
	}

	//UPDATE
	public StudenteDto updateStudent(StudenteDto studenteDto) {
		StudenteDto newStudenteDto = new StudenteDto();
		Studente studente = setStudente_studenteDto(studenteDto);

		newStudenteDto.setNome(studente.getNome());
		newStudenteDto.setCognome(studente.getCognome());
		newStudenteDto.setUserCode(studente.getUserCode());
		newStudenteDto.setMail(studente.getMail());
		newStudenteDto.setPas(studente.getPas());
		newStudenteDto.setSezione(studente.getClasse().getSezione());

		return newStudenteDto;
	}
	//METODO USATO NELL'UPDATE
	private Studente setStudente_studenteDto(StudenteDto studenteDto) {
		Studente studente = studentRepository.findStudentByUserCode(studenteDto.getUserCode())
				.orElseThrow(() -> new NotFoundStudentException("Utente non esistente"));
		Classe classe = classRepository.findBySezione(studenteDto.getSezione())
				.orElseThrow(() -> new NotFoundSezioneException("Sezione non trovata"));

		studente.setId(studente.getId());
		studente.setNome(studenteDto.getNome());
		studente.setCognome(studenteDto.getCognome());
		studente.setMail(studenteDto.getMail());
		studente.setPas(studenteDto.getPas());
		studente.setClasse(classe);

		studentRepository.save(studente);

		return studente = studentRepository.findStudentByUserCode(studenteDto.getUserCode())
				.orElseThrow(() -> new NotFoundStudentException("Utente non esistente"));
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
