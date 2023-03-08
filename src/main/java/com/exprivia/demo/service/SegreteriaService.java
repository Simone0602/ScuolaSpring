package com.exprivia.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exprivia.demo.dto.DocenteDto;
import com.exprivia.demo.dto.MateriaDto;
import com.exprivia.demo.dto.StudenteDto;
import com.exprivia.demo.enums.Materie;
import com.exprivia.demo.exception.NotFoundClasseException;
import com.exprivia.demo.exception.NotFoundDocenteException;
import com.exprivia.demo.exception.NotFoundSezioneException;
import com.exprivia.demo.exception.NotFoundStudentException;
import com.exprivia.demo.model.Classe;
import com.exprivia.demo.model.Docente;
import com.exprivia.demo.model.Materia;
import com.exprivia.demo.model.Studente;
import com.exprivia.demo.repository.AssenzaRepository;
import com.exprivia.demo.repository.ClassRepository;
import com.exprivia.demo.repository.DocenteRepo;
import com.exprivia.demo.repository.MateriaRepository;
import com.exprivia.demo.repository.StudentRepo;
import com.exprivia.demo.repository.TokenRepository;
import com.exprivia.demo.repository.ValutazioneRepository;

@Service
public class SegreteriaService {
	private final StudentRepo studentRepository;
	private final TokenRepository tokenRepository;
	private final ValutazioneRepository valutazioneRepository;
	private final AssenzaRepository assenzaRepository;
	private final DocenteRepo docenteRepository;
	private final ClassRepository classRepository;
	private final MateriaRepository materiaRepository;
	private final PasswordEncoder passwordEncoder;
	
	public SegreteriaService(StudentRepo studentRepository, 
			TokenRepository tokenRepository,
			ValutazioneRepository valutazioneRepository,
			AssenzaRepository assenzaRepository,
			DocenteRepo docenteRepository, 
			ClassRepository classRepository,
			MateriaRepository materiaRepository,
			PasswordEncoder passwordEncoder) {
		this.studentRepository = studentRepository;
		this.tokenRepository = tokenRepository;
		this.valutazioneRepository = valutazioneRepository;
		this.assenzaRepository = assenzaRepository;
		this.docenteRepository = docenteRepository;
		this.classRepository = classRepository;
		this.materiaRepository = materiaRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public List<MateriaDto> findAllMaterie(){
		return materiaRepository.findAll()
				.stream()
				.map(x -> new MateriaDto(x.getMateria().name().toLowerCase()))
				.collect(Collectors.toList());
	}
	
	public List<StudenteDto> findAllStudent(){
		return studentRepository.findAll()
				.stream()
				.map(x -> conversioneStudente_StudenteDto(x))
				.collect(Collectors.toList());
	}
	
	public List<DocenteDto> findAllDocenti(){
		return docenteRepository.findAll()
				.stream()
				.map(x -> conversioneDocente_DocenteDto(x))
				.collect(Collectors.toList());
	}
	
	public String saveStudent(StudenteDto studenteDto) {
		Optional<Studente> studente = studentRepository.findStudentByUserCode(studenteDto.getUserCode());
		if(studente.isPresent()) {
			throw new NotFoundStudentException("Studente già presente nel database");
		}else {
			Studente newStudente = conversioneStudenteDto_Studente(studenteDto);
			studentRepository.save(newStudente);
			return "Studente aggiunto con successso";
		}
	}
	
	public String saveDocente(DocenteDto docenteDto) {
		Optional<Docente> docente = docenteRepository.findDocenteByCodiceFiscale(docenteDto.getCodiceFiscale());
		if(docente.isPresent()) {
			throw new NotFoundDocenteException("Docente già presente nel database");
		}else {
			Docente newDocente = conversioneDocenteDto_Docente(docenteDto);
			docenteRepository.save(newDocente);
			return "Docente aggiunto con successso";
		}
	}
	
	public String deleteStudent(String userCode) {
		Optional<Studente> studente = studentRepository.findStudentByUserCode(userCode);
		if(studente.isEmpty()) {
			throw new NotFoundStudentException("Studente non presente nel database");
		}else {
			tokenRepository.deleteTokenByStudenteId(studente.get().getId());
			valutazioneRepository.deleteValutazioneByStudenteId(studente.get().getId());
			assenzaRepository.deleteAssenzeByStudenteId(studente.get().getId());
			studentRepository.deleteById(studente.get().getId());
			return "Studente eliminato";
		}
	}

	public String deleteDocente(String codiceFsicale) {
		Optional<Docente> docente = docenteRepository.findDocenteByCodiceFiscale(codiceFsicale);
		if(docente.isEmpty()) {
			throw new NotFoundStudentException("Studente non presente nel database");
		} else {
			docenteRepository.deleteMateriaByDocenteId(docente.get().getId());
			docenteRepository.deleteClasseByDocenteId(docente.get().getId());
			tokenRepository.deleteTokenByDocenteId(docente.get().getId());
			docenteRepository.deleteById(docente.get().getId());
			return "Docente eliminato";
		}
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

		studente.setNome(studenteDto.getNome());
		studente.setCognome(studenteDto.getCognome());
		studente.setMail(studenteDto.getMail());
		studente.setPass(passwordEncoder.encode(studenteDto.getPassword()));
		studente.setUserCode(studenteDto.getUserCode());
		studente.setClasse(classe);
		return studente;
	}
	
	private DocenteDto conversioneDocente_DocenteDto(Docente docente) {
		DocenteDto docenteDto = new DocenteDto();

		docenteDto.setNome(docente.getNome());
		docenteDto.setCognome(docente.getCognome());
		docenteDto.setMail(docente.getMail());
		docenteDto.setPassword(docente.getPass());
		docenteDto.setCodiceFiscale(docente.getCodiceFiscale());
		docenteDto.setMaterie(getStringMaterie(docente));
		docenteDto.setSezioni(getListSezioniDocente(docente.getClassi()));
		return docenteDto;
	}
	
	private Docente conversioneDocenteDto_Docente(DocenteDto docenteDto) {
		Docente docente = new Docente();

		docente.setNome(docenteDto.getNome());
		docente.setCognome(docenteDto.getCognome());
		docente.setMail(docenteDto.getMail());
		docente.setPass(passwordEncoder.encode(docenteDto.getPassword()));
		docente.setMaterie(getSetMaterie(docenteDto));
		docente.setCodiceFiscale(docenteDto.getCodiceFiscale());
		docente.setClassi(getListaClassiDocente(docenteDto.getSezioni()));
		return docente;
	}
	
	private List<Classe> getListaClassiDocente(List<String> sezioni){
		return sezioni.stream()
				.map(x -> {
					Classe classe = classRepository.findBySezione(x)
							.orElseThrow(() -> new NotFoundClasseException("Classe non trovata"));
					return classe;
				})
				.collect(Collectors.toList());
	}
	
	private List<String> getListSezioniDocente(List<Classe> classi){
		return classi.stream()
				.map(x -> x.getSezione())
				.collect(Collectors.toList());
	}
	
	private List<String> getStringMaterie(Docente docente) {
		List<String> materie = docente.getMaterie().stream().map(x -> x.getMateria().name())
				.collect(Collectors.toList());
		return materie;
	}
	
	private Set<Materia> getSetMaterie(DocenteDto docenteDto) {
		Set<Materia> materie = docenteDto.getMaterie().stream()
				.map(x -> this.materiaRepository.findByMateria(Materie.valueOf(x.toUpperCase())))
				.filter(x -> x != null)
				.collect(Collectors.toSet());
		return materie;
	}
}
