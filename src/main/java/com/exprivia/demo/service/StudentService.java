package com.exprivia.demo.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.exception.IllegalMailException;
import com.exprivia.demo.exception.IllegalPasswordException;
import com.exprivia.demo.exception.NotFoundSezioneException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.exception.NotFoundTokenException;
import com.exprivia.demo.mail.SendMailStudenteService;
import com.exprivia.demo.model.Classe;
import com.exprivia.demo.model.Studente;
import com.exprivia.demo.model.Token;
import com.exprivia.demo.repository.ClassRepository;
import com.exprivia.demo.repository.StudentRepo;
import com.exprivia.demo.repository.TokenRepository;

@Service
public class StudentService {

	private final StudentRepo studentRepository;
	private final ClassRepository classRepository;
	private final TokenRepository tokenRepository;
	private final SendMailStudenteService studenteMailService;

	@Autowired
	public StudentService(StudentRepo studentRepository, 
			ClassRepository classRepository, 
			TokenRepository tokenRepository, 
			SendMailStudenteService studenteMailService) {
		this.studentRepository = studentRepository;
		this.classRepository = classRepository;
		this.tokenRepository = tokenRepository;
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
		studenteDto.setPassword(null);
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
				studenteDto.setPassword(studente.getPassword());
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
			studenteDto.setPassword(null);
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
				studente.setPassword(studenteDto.getPassword());
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
		
		if(!studente.getPassword().equals(studenteDto.getPassword())) {
			throw new IllegalPasswordException("Password errata");
		}
		
		studenteDtoLoggato.setNome(studente.getNome());
		studenteDtoLoggato.setCognome(studente.getCognome());
		studenteDtoLoggato.setUserCode(null);
		studenteDtoLoggato.setMail(studente.getMail());
		studenteDtoLoggato.setPassword(studente.getPassword());
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
		newStudenteDto.setPassword(studente.getPassword());
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
		studente.setPassword(studenteDto.getPassword());
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
	public String resetPassword(StudenteDto studenteDto, String tipoUser) throws UnsupportedEncodingException {
		Studente studente = studentRepository.findStudentByUserCode(studenteDto.getUserCode())
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		
		if(!studente.getMail().equals(studenteDto.getMail())) {
			throw new IllegalMailException("Email errata");
		}
		
		UUID uuid = UUID.randomUUID();
		String resetToken = uuid.toString();
		
		Token token = new Token(resetToken, studente);
		tokenRepository.save(token);
		
		return studenteMailService.sendEmail(studente.getMail(), resetToken, tipoUser);
	}

	//SERVE PER L'UPDATE DELLA PASSWORD
	public String updatePassword(String password, String token) {
		Token newToken = tokenRepository.findByIdAndDate(token, LocalDate.now())
				.orElseThrow(() -> new NotFoundTokenException("Token non trovato o scaduto"));
		Studente studente = studentRepository.findById(newToken.getStudente().getId())
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		
		studente.setPassword(password);
		
		studentRepository.save(studente);
		tokenRepository.delete(newToken);
		return "Password aggiornata";
	}
	//SERVE PER OTTENERE IL TOKEN
	public String getToken(String userCode) {
		LocalDate date_now = LocalDate.now();
		Studente studente = studentRepository.findStudentByUserCode(userCode)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		
		for(Token token : studente.getTokens()) {
			if(token.getExpiredDate().isAfter(date_now)) {
				return token.getToken();
			}
			if(token.getExpiredDate().equals(date_now)) {
				return token.getToken();
			}
		}
		throw new NotFoundTokenException("Token scaduto");
	}
}