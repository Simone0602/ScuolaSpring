package com.exprivia.demo.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.AssenzaDto;
import com.exprivia.demo.dto.RegistroFamiglia;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.dto.ValutazioneDto;
import com.exprivia.demo.enums.Materie;
import com.exprivia.demo.exception.IllegalDatiException;
import com.exprivia.demo.exception.IllegalMailException;
import com.exprivia.demo.exception.NotFoundSezioneException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.exception.NotFoundTokenException;
import com.exprivia.demo.mail.SendMailService;
import com.exprivia.demo.model.Assenza;
import com.exprivia.demo.model.Classe;
import com.exprivia.demo.model.Studente;
import com.exprivia.demo.model.Token;
import com.exprivia.demo.model.Valutazione;
import com.exprivia.demo.repository.AssenzaRepository;
import com.exprivia.demo.repository.ClassRepository;
import com.exprivia.demo.repository.StudentRepo;
import com.exprivia.demo.repository.TokenRepository;

@Service
public class StudentService {

	private final StudentRepo studentRepository;
	private final ClassRepository classRepository;
	private final TokenRepository tokenRepository;
	private final AssenzaRepository assenzaRepository;
	private final PasswordEncoder passwordEncoder;
	private final SendMailService mailService;

	@Autowired
	public StudentService(StudentRepo studentRepository, 
			ClassRepository classRepository, 
			TokenRepository tokenRepository,
			 AssenzaRepository assenzaRepository,
			PasswordEncoder passwordEncoder,
			SendMailService mailService) {
		this.studentRepository = studentRepository;
		this.classRepository = classRepository;
		this.tokenRepository = tokenRepository;
		this.assenzaRepository = assenzaRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
	}

	/* Segreteria */
	//ricerca di uno studente tremite mail
	public StudenteDto findStudentByMail(String mail) {
		StudenteDto studenteDto = new StudenteDto();
		Studente studente = studentRepository.findStudentByMail(mail)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));

		studenteDto = conversioneStudente_StudenteDto(studente);

		return studenteDto;
	}

	//aggiungere uno studente
	public String addStudent(StudenteDto studenteDto) {
		if (!studentRepository.existsByUserCode(studenteDto.getUserCode())) {
			if(!studentRepository.existsByMail(studenteDto.getMail())) {
				studentRepository.save(conversioneStudenteDto_Studente(studenteDto));
				return "Studente salvato";
			}
			return "Email già esistente";
		}
		return "Studente già presente";
	}
	
	//aggiornamento dati studente
	public StudenteDto updateStudentBySegreteria(StudenteDto studenteDto) {
		Studente studente = studentRepository.findStudentByUserCode(studenteDto.getUserCode())
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		studenteDto.setId(studente.getId());
		return conversioneStudente_StudenteDto(conversioneStudenteDto_Studente(studenteDto));
	}
	
	//elimina studente 
	public String deleteStudent(String userCode) {
		Studente studente = studentRepository.findStudentByUserCode(userCode)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		if (studente != null) {
			studentRepository.deleteById(studente.getId());
			return "Studente eliminato";
		}
		return "Studente non presente";
	}
	
	/* get studente */
	public StudenteDto getStudent(String userCode) {
		StudenteDto studenteDtoLoggato = new StudenteDto();
		Studente studente = studentRepository.findStudentByUserCode(userCode)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		
		studenteDtoLoggato = conversioneStudente_StudenteDto(studente);

		return studenteDtoLoggato;
	}
	
	/* Metodi utilizzo send mail e update password */
	//reset della password
	public String resetPassword(StudenteDto studenteDto) throws UnsupportedEncodingException {
		Studente studente = studentRepository.findStudentByUserCode(studenteDto.getUserCode())
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		
		if(!studente.getMail().equals(studenteDto.getMail())) {
			throw new IllegalMailException("Email errata");
		}
		
		UUID uuid = UUID.randomUUID();
		String resetToken = uuid.toString();
		
		Token token = new Token(resetToken, studente);
		tokenRepository.save(token);
		
		return mailService.sendEmail(studente.getMail(), resetToken, "studente");
	}
	//aggiornamento password
	public String updatePassword(String password, String token) {
		Token newToken = tokenRepository.findByIdAndDate(token, LocalDate.now())
				.orElseThrow(() -> new NotFoundTokenException("Token non trovato o scaduto"));
		Studente studente = studentRepository.findById(newToken.getStudente().getId())
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		
		studente.setPass(passwordEncoder.encode(password));
		
		studentRepository.save(studente);
		tokenRepository.delete(newToken);
		return "Password aggiornata";
	}
	
	/* update studente tramite dati anagrafici */
	//aggiorna studente
	public String updateStudent(StudenteDto studenteDto) {
		Studente studente = studentRepository.findStudentByUserCode(studenteDto.getUserCode())
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"));
		
		if(studenteDto.getPassword().equals("****************")){
			studenteDto.setPassword(studente.getPass());
		}
		
		if(studente.getPass().equals(studenteDto.getPassword()) && studente.getMail().equals(studenteDto.getMail())) {
			throw new IllegalDatiException("I dati sono uguali a quelli già presenti.");
		}
		
		studenteDto.setId(studente.getId());
		Studente newStudente = conversioneStudenteDto_Studente(studenteDto);
		newStudente.setPass(studente.getPass());
		studentRepository.save(newStudente);
		
		return "Studente aggiornato";
	}
	
	/* get assenze */
	public List<AssenzaDto> getAssenze(String userCode){
		return studentRepository.findStudentByUserCode(userCode)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"))
				.getAssenze()
				.stream()
				.map(x -> new AssenzaDto(x.getGiornataAssenza(), x.isGiustificata()))
				.collect(Collectors.toList());
	}
	
	/* giustifazione assenze */
	public String giustificaAssenze(List<AssenzaDto> assenzeDto, String userCode) {
		List<Assenza> assenze = studentRepository.findStudentByUserCode(userCode)
				.orElseThrow(() -> new NotFoundStudentException("Studente non trovato"))
				.getAssenze();
		Iterator<AssenzaDto> iterAssenzaDto = assenzeDto.iterator();
	
		while(iterAssenzaDto.hasNext()) {
			AssenzaDto assenzaDto = iterAssenzaDto.next();
			
			Iterator<Assenza> iterAssenza = assenze.iterator();
			while(iterAssenza.hasNext()) {
				Assenza assenza = iterAssenza.next();
				
				if(assenza.getGiornataAssenza().compareTo(assenzaDto.getGiornataAssenza())==0) {
					if(!assenza.isGiustificata()) {
						assenza.setGiustificata(assenzaDto.isGiustificata());
						assenzaRepository.save(assenza);
					}
				}
			}
		}
		return "Assenze giustificate";
	}
	
	/* get registro delle materie e voti */
	//stampa i voti degli studenti
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
	/* metodi utilizzati da java */
	private List<ValutazioneDto> getList(List<ValutazioneDto> val, ValutazioneDto voto){
		val.add(voto);
		return val;
	}
	
	private StudenteDto conversioneStudente_StudenteDto(Studente studente) {
		StudenteDto studenteDto = new StudenteDto();
		
		studenteDto.setNome(studente.getNome());
		studenteDto.setCognome(studente.getCognome());
		studenteDto.setMail(studente.getMail());
		studenteDto.setPassword(studente.getPass());
		studenteDto.setUserCode(studente.getUserCode());
		studenteDto.setSezione(studente.getClasse().getSezione());
		
		return studenteDto;
	}
	
	private Studente conversioneStudenteDto_Studente(StudenteDto studenteDto) {
		Studente studente = new Studente();
		Classe classe = classRepository.findBySezione(studenteDto.getSezione())
				.orElseThrow(() -> new NotFoundSezioneException("Sezione non trovata"));

		studente.setId(studenteDto.getId());
		studente.setNome(studenteDto.getNome());
		studente.setCognome(studenteDto.getCognome());
		studente.setMail(studenteDto.getMail());
		studente.setPass(passwordEncoder.encode(studenteDto.getPassword()));
		studente.setUserCode(studenteDto.getUserCode());
		studente.setClasse(classe);
		return studente;
	}
}