package com.exprivia.demo.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.exception.IllegalPasswordException;
import com.exprivia.demo.exception.NotFoundSezioneException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.mail.SendMailStudenteService;
import com.exprivia.demo.model.Classe;
import com.exprivia.demo.model.Studente;
import com.exprivia.demo.repository.ClassRepository;
import com.exprivia.demo.repository.StudentRepo;

@Service
public class StudentService {

	private final StudentRepo studentRepository;
	private final ClassRepository classRepository;
	private final SendMailStudenteService studenteMailService;

	@Autowired
	public StudentService(StudentRepo studentRepository, ClassRepository classRepository, SendMailStudenteService studenteMailService) {
		this.studentRepository = studentRepository;
		this.classRepository = classRepository;
		this.studenteMailService = studenteMailService;
	}

	// SERVE SOLO ALLA SEGRETERIA PER TROVARE UNO STUDENTE
	public StudenteDto findStudentByMail(String mail) {
		StudenteDto studenteDto = new StudenteDto();
		Studente studente = studentRepository.findStudentByMail(mail)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));

		studenteDto.setNome(studente.getNome());
		studenteDto.setCognome(studente.getCognome());
		studenteDto.setUserCode(studente.getUserCode());
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
		List<Studente> studenti = classRepository.findBySezione(sezione).get().getStudenti();

		for (Studente studente : studenti) {
			StudenteDto studenteDto = new StudenteDto();

			studenteDto.setNome(studente.getNome());
			studenteDto.setCognome(studente.getCognome());
			studenteDto.setUserCode(null);
			studenteDto.setMail(studente.getMail());
			studenteDto.setPas(null);
			studenteDto.setSezione(studente.getClasse().getSezione());

			studentiDto.add(studenteDto);
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
	public StudenteDto findStudent(StudenteDto studenteDto) {
		StudenteDto studenteDtoLoggato = new StudenteDto();
		Studente studente = studentRepository.findStudentByUserCode(studenteDto.getUserCode())
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		
		if(!studente.getPas().equals(studenteDto.getPas())) {
			throw new IllegalPasswordException("Password errata");
		}
		
		studenteDtoLoggato.setNome(studente.getNome());
		studenteDtoLoggato.setCognome(studente.getCognome());
		studenteDtoLoggato.setUserCode(null);
		studenteDtoLoggato.setMail(studente.getMail());
		studenteDtoLoggato.setPas(studente.getPas());
		studenteDtoLoggato.setSezione(studente.getClasse().getSezione());

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
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		Classe classe = classRepository.findBySezione(studenteDto.getSezione())
				.orElseThrow(() -> new NotFoundSezioneException("Sezione non trovata"));

		studente.setNome(studenteDto.getNome());
		studente.setCognome(studenteDto.getCognome());
		studente.setMail(studenteDto.getMail());
		studente.setPas(studenteDto.getPas());
		studente.setClasse(classe);

		studentRepository.save(studente);

		return studente;
	}

	public String deleteStudent(String userCode) {
		Studente studente = studentRepository.findStudentByUserCode(userCode)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		if (studente != null) {
			studentRepository.deleteById(studente.getId());
			return "utente eliminato";
		}
		return "Utente non presente";
	}
	
	//METODO RESET PASSWORD
	public String resetPassword(StudenteDto studenteDto) throws UnsupportedEncodingException {
		Studente studente = studentRepository.findStudentByUserCode(studenteDto.getUserCode())
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		return studenteMailService.sendEmail(studente);
	}
}
