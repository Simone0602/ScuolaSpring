package com.exprivia.demo.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.RegistroFamiglia;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.dto.ValutazioneDto;
import com.exprivia.demo.enums.Materie;
import com.exprivia.demo.exception.IllegalMailException;
import com.exprivia.demo.exception.IllegalPasswordException;
import com.exprivia.demo.exception.NotFoundSezioneException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.exception.NotFoundTokenException;
import com.exprivia.demo.mail.SendMailStudenteService;
import com.exprivia.demo.model.Classe;
import com.exprivia.demo.model.Studente;
import com.exprivia.demo.model.Token;
import com.exprivia.demo.model.Valutazione;
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

		studenteDto = conversioneStudente_StudenteDto(studente);

		return studenteDto;
	}
	
	//SERVE ALLA SEGRETERIA
	public List<StudenteDto> findAllStudentBySezione5(String numeroSezione) {
		List<StudenteDto> studentiDto = new ArrayList<>();
		List<Studente> studenti = studentRepository.findAll();
		
		for(Studente studente : studenti) {
			StudenteDto studenteDto = new StudenteDto();
			
			if(studente.getClasse().getSezione().contains(numeroSezione)) {
				studenteDto = conversioneStudente_StudenteDto(studente);

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
		
		studenteDtoLoggato = conversioneStudente_StudenteDto(studente);
		studenteDtoLoggato.setUserCode(null);
		studenteDtoLoggato.setPassword(null);

		return studenteDtoLoggato;
	}

	//UPDATE
	public StudenteDto updateStudent(StudenteDto studenteDto) {
		Studente studente = setStudente_studenteDto(studenteDto);
		return conversioneStudente_StudenteDto(studente);
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

	//DELETE STUDENTE
	public String deleteStudent(String userCode) {
		Studente studente = studentRepository.findStudentByUserCode(userCode)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		if (studente != null) {
			studentRepository.deleteById(studente.getId());
			return "Utente eliminato";
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
	
	public RegistroFamiglia getVoti(String email) {
		Studente studente = studentRepository.findStudentByMail(email)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		RegistroFamiglia registroFamiglia = new RegistroFamiglia();
		
		HashMap<Materie, List<ValutazioneDto>> voti = new HashMap<>();
		Iterator<Valutazione> iter = studente.getVoti().iterator();
		while(iter.hasNext()) {
			Valutazione voto = iter.next();
			ValutazioneDto votoDto = new ValutazioneDto(voto.getVoto(), voto.getData());
			
			voti.computeIfPresent(voto.getMateria().getMateria(), (key, val) -> getList(val, votoDto));
			voti.computeIfAbsent(voto.getMateria().getMateria(), k -> new ArrayList<ValutazioneDto>() {{ add(votoDto); }});
		}
		
		registroFamiglia.setListaVoti_materie(voti);
		
		return registroFamiglia;
	}
	private List<ValutazioneDto> getList(List<ValutazioneDto> val, ValutazioneDto voto){
		val.add(voto);
		return val;
	}
	
	static StudenteDto conversioneStudente_StudenteDto(Studente studente) {
		StudenteDto studenteDto = new StudenteDto();
		
		studenteDto.setNome(studente.getNome());
		studenteDto.setCognome(studente.getCognome());
		studenteDto.setMail(studente.getMail());
		studenteDto.setPassword(studente.getPassword());
		studenteDto.setUserCode(studente.getUserCode());
		studenteDto.setSezione(studente.getClasse().getSezione());
		
		return studenteDto;
	}
}