package com.exprivia.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

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
