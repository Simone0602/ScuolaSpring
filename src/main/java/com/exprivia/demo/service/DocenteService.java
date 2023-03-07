package com.exprivia.demo.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.ClasseDto;
import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.enums.Materie;
import com.exprivia.demo.exception.IllegalDatiException;
import com.exprivia.demo.exception.IllegalMailException;
import com.exprivia.demo.exception.NotFoundClasseException;
import com.exprivia.demo.exception.NotFoundDocenteException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.exception.NotFoundTokenException;
import com.exprivia.demo.mail.SendMailService;
import com.exprivia.demo.model.Classe;
import com.exprivia.demo.model.Docente;
import com.exprivia.demo.model.Materia;
import com.exprivia.demo.model.Token;
import com.exprivia.demo.repository.ClassRepository;
import com.exprivia.demo.repository.DocenteRepo;
import com.exprivia.demo.repository.MateriaRepository;
import com.exprivia.demo.repository.TokenRepository;

@Service
public class DocenteService {

	private final ClassRepository classRepository;
	private final DocenteRepo docenteRepository;
	private final MateriaRepository materiaRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final SendMailService mailService;

	public DocenteService(DocenteRepo docenteRepository,
			ClassRepository classRepository,
			MateriaRepository materiaRepository,
			TokenRepository tokenRepository, 
			PasswordEncoder passwordEncoder,
			SendMailService mailService) {
		this.docenteRepository = docenteRepository;
		this.classRepository = classRepository;
		this.materiaRepository = materiaRepository;
		this.tokenRepository = tokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
	}

	// SERVE ALLO STUDENTE
	// TROVA TUTTI I DOCENTI
	public List<DocenteDto> findAllDocenti() {
		return docenteRepository.findAll().stream().map(x -> conversioneDocente_DocenteDto(x))
				.collect(Collectors.toList());
	}
	
	public List<ClasseDto> findClassByDocente(String codiceFiscale){
		return docenteRepository.findDocenteByCodiceFiscale(codiceFiscale)
				.orElseThrow(() -> new NotFoundDocenteException("Docente non trovato"))
				.getClassi()
				.stream()
				.map(x -> new ClasseDto(x.getSezione(), x.getCordinatore(), x.getAula()))
				.collect(Collectors.toList());
	}

	/* Get docente */
	public DocenteDto getDocente(String codiceFiscale) {
		Docente docente = docenteRepository.findDocenteByCodiceFiscale(codiceFiscale)
				.orElseThrow(() -> new NotFoundDocenteException("Docente non trovato"));

		return conversioneDocente_DocenteDto(docente);
	}
	
	/* update studente tramite dati anagrafici */
	//aggiorna studente
	public String updateDocente(DocenteDto docenteDto) {
		Docente docente = docenteRepository.findDocenteByCodiceFiscale(docenteDto.getCodiceFiscale())
				.orElseThrow(() -> new NotFoundDocenteException("Docente non trovato"));
		
		if(docente.getPass().equals(docenteDto.getPassword()) && docente.getMail().equals(docenteDto.getMail())) {
			throw new IllegalDatiException("I dati sono uguali a quelli già presenti.");
		}
		
		docenteDto.setId(docente.getId());
		Docente newDocente = conversioneDocenteDto_Docente(docenteDto);
		
		if(docenteDto.getPassword().equals("****************")){
			newDocente.setPass(docente.getPass());
		}
		docenteRepository.save(newDocente);
		
		return "Docente aggiornato";
	}

	/* Metodi utilizzo send mail e update password */
	// reset della password
	public String resetPassword(DocenteDto docenteDto) throws UnsupportedEncodingException {
		Docente docente = docenteRepository.findDocenteByCodiceFiscale(docenteDto.getCodiceFiscale())
				.orElseThrow(() -> new NotFoundStudentException("Docente non trovato"));

		if (!docente.getMail().equals(docenteDto.getMail())) {
			throw new IllegalMailException("Email errata");
		}

		UUID uuid = UUID.randomUUID();
		String resetToken = uuid.toString();

		Token token = new Token(resetToken, docente);
		tokenRepository.save(token);

		return mailService.sendEmail(docente.getMail(), resetToken, "docente");
	}

	// aggiornamento password
	public String updatePassword(String password, String token) {
		Token newToken = tokenRepository.findByIdAndDate(token, LocalDate.now())
				.orElseThrow(() -> new NotFoundTokenException("Token non trovato o scaduto"));
		Docente docente = docenteRepository.findById(newToken.getDocente().getId())
				.orElseThrow(() -> new NotFoundStudentException("Docente non trovato"));

		docente.setPass(passwordEncoder.encode(password));

		docenteRepository.save(docente);
		tokenRepository.delete(newToken);
		return "Password aggiornata";
	}
	
	private DocenteDto conversioneDocente_DocenteDto(Docente docente) {
		DocenteDto docenteDto = new DocenteDto();

		docenteDto.setNome(docente.getNome());
		docenteDto.setCognome(docente.getCognome());
		docenteDto.setMail(docente.getMail());
		docenteDto.setPassword(docente.getPass());
		docenteDto.setCodiceFiscale(docente.getCodiceFiscale());
		docenteDto.setMaterie(getStringMaterie(docente));
		docenteDto.setSezioni(getListaSezioni(docente.getClassi()));
		return docenteDto;
	}

	private Docente conversioneDocenteDto_Docente(DocenteDto docenteDto) {
		Docente docente = new Docente();

		docente.setId(docenteDto.getId());
		docente.setNome(docenteDto.getNome());
		docente.setCognome(docenteDto.getCognome());
		docente.setMail(docenteDto.getMail());
		docente.setPass(passwordEncoder.encode(docenteDto.getPassword()));
		docente.setMaterie(getSetMaterie(docenteDto));
		docente.setCodiceFiscale(docenteDto.getCodiceFiscale());
		docente.setClassi(getListaClassi(docenteDto.getSezioni()));
		return docente;
	}

	// STAMPA LE MATERIE DI UN DOCENTE
	private List<String> getStringMaterie(Docente docente) {
		return docente.getMaterie().stream().map(x -> x.getMateria().name())
				.collect(Collectors.toList());
	}

	private Set<Materia> getSetMaterie(DocenteDto docenteDto) {
		return docenteDto.getMaterie().stream()
				.map(x -> this.materiaRepository.findByMateria(Materie.valueOf(x))).filter(x -> x != null)
				.collect(Collectors.toSet());
	}
	
	private List<String> getListaSezioni(List<Classe> classi){
		return classi.stream()
				.map(x -> {
					return x.getSezione();
				})
				.collect(Collectors.toList());
	}
	
	private List<Classe> getListaClassi(List<String> sezioni){
		return sezioni.stream()
				.map(x -> {
					return classRepository.findBySezione(x)
							.orElseThrow(() -> new NotFoundClasseException("Classe non trovata"));
				})
				.collect(Collectors.toList());
	}
}
