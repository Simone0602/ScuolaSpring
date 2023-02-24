package com.exprivia.demo.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.ClasseDto;
import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.enums.Materie;
import com.exprivia.demo.exception.IllegalMailException;
import com.exprivia.demo.exception.NotFoundDocenteException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.exception.NotFoundTokenException;
import com.exprivia.demo.mail.SendMailService;
import com.exprivia.demo.model.Docente;
import com.exprivia.demo.model.Materia;
import com.exprivia.demo.model.Token;
import com.exprivia.demo.repository.DocenteRepo;
import com.exprivia.demo.repository.MateriaRepository;
import com.exprivia.demo.repository.TokenRepository;

@Service
public class DocenteService {

	private final DocenteRepo docenteRepository;
	private final MateriaRepository materiaRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final SendMailService mailService;

	public DocenteService(DocenteRepo docenteRepository, 
			MateriaRepository materiaRepository,
			TokenRepository tokenRepository, 
			PasswordEncoder passwordEncoder,
			SendMailService mailService) {
		this.docenteRepository = docenteRepository;
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

	// SERVE ALLO STUDENTE
	// TROVA UN DOCENTE TRAMITE CODICE FISCALE
	public List<ClasseDto> findAllClassByDocente(String codiceFiscale) {
		return docenteRepository.findDocenteByCodiceFiscale(codiceFiscale).get().getClassi().stream()
				.map(x -> new ClasseDto(x.getId(), x.getSezione(), x.getCordinatore(), x.getAula()))
				.collect(Collectors.toList());
	}

	// SERVE ALLA SEGRETERIA
	// AGGIUNGE UN DOCENTE
	public String addDocente(DocenteDto docenteDto) {
		if (!docenteRepository.existsByCodiceFiscale(docenteDto.getCodiceFiscale())) {
			docenteRepository.save(conversioneDocenteDto_Docente(docenteDto));
			return "Docente salvato";
		}
		return "Docente già presente";
	}

	/* Get docente */
	public DocenteDto getDocente(String codiceFiscale) {
		Docente docente = docenteRepository.findDocenteByCodiceFiscale(codiceFiscale)
				.orElseThrow(() -> new NotFoundDocenteException("Docente non trovato"));

		return conversioneDocente_DocenteDto(docente);
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

	// SERVE ALLA SEGRETERIA
	// AGGIORNAMENTO DATI DOCENTE
	public DocenteDto updateDocente(DocenteDto docenteDto) {
		DocenteDto newDocenteDto = new DocenteDto();
		Docente docente = docenteRepository.findDocenteByCodiceFiscale(docenteDto.getCodiceFiscale())
				.orElseThrow(() -> new NotFoundDocenteException("Docente non esistente"));

		docente = conversioneDocenteDto_Docente(docenteDto);
		docente.setId(docente.getId());

		docenteRepository.save(docente);

		newDocenteDto.setNome(docente.getNome());
		newDocenteDto.setCognome(docente.getCognome());
		newDocenteDto.setMail(docente.getMail());
		newDocenteDto.setPassword(docente.getPassword());
		newDocenteDto.setMaterie(getStringMaterie(docente));
		newDocenteDto.setCodiceFiscale(docente.getCodiceFiscale());

		return newDocenteDto;
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
	private List<String> getStringMaterie(Docente docente) {
		List<String> materie = docente.getMaterie().stream().map(x -> x.getMateria().name())
				.collect(Collectors.toList());
		return materie;
	}

	private Set<Materia> getSetMaterie(DocenteDto docenteDto) {
		Set<Materia> materie = docenteDto.getMaterie().stream()
				.map(x -> this.materiaRepository.findByMateria(Materie.valueOf(x))).filter(x -> x != null)
				.collect(Collectors.toSet());
		return materie;
	}

	private DocenteDto conversioneDocente_DocenteDto(Docente docente) {
		DocenteDto docenteDto = new DocenteDto();

		docenteDto.setNome(docente.getNome());
		docenteDto.setCognome(docente.getCognome());
		docenteDto.setMail(docente.getMail());
		docenteDto.setPassword(docente.getPass());
		docenteDto.setCodiceFiscale(docente.getCodiceFiscale());
		docenteDto.setMaterie(getStringMaterie(docente));
		return docenteDto;
	}

	private Docente conversioneDocenteDto_Docente(DocenteDto docenteDto) {
		Docente docente = new Docente();

		docente.setNome(docenteDto.getNome());
		docente.setCognome(docenteDto.getCognome());
		docente.setMail(docenteDto.getMail());
		docente.setPass(docenteDto.getPassword());
		docente.setMaterie(getSetMaterie(docenteDto));
		docente.setCodiceFiscale(docenteDto.getCodiceFiscale());
		return docente;
	}
}
