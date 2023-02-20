package com.exprivia.demo.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "classi")
public class Classe {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DROIT_SEQ")
	@SequenceGenerator(name = "DROIT_SEQ", sequenceName = "DROIT_ACCEES_SEQ", allocationSize = 1, initialValue = 1)
	@Column(name = "classe_id")
	private Long id;
	
	@Column(name = "sezione")
	private String sezione;
	@Column(name = "cordinatore")
	private String cordinatore;
	@Column(name = "aula")
	private String aula;
	
	@OneToMany(mappedBy = "classe")
	@JsonManagedReference
	private List<Studente> studenti;
	
	@ManyToMany(mappedBy = "classi", fetch = FetchType.LAZY)
	@JsonBackReference
	private List<Docente> docenti;

	public Classe() {
	}

	public Classe(String sezione, String cordinatore, String aula) {
		super();
		this.sezione = sezione;
		this.cordinatore = cordinatore;
		this.aula = aula;
	}

	
	public List<Studente> getStudenti() {
		return studenti;
	}

	public void setStudenti(List<Studente> studenti) {
		this.studenti = studenti;
	}

	public List<Docente> getDocenti() {
		return docenti;
	}

	public void setDocenti(List<Docente> docenti) {
		this.docenti = docenti;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getCordinatore() {
		return cordinatore;
	}

	public void setCordinatore(String cordinatore) {
		this.cordinatore = cordinatore;
	}

	public String getAula() {
		return aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	@Override
	public String toString() {
		return "Classe [getId()=" + getId() + ", getSezione()=" + getSezione() + ", getCordinatore()="
				+ getCordinatore() + ", getAula()=" + getAula() + "]";
	}

}
