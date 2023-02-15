package com.exprivia.demo.model;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "docenti")
public class Docente {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DROIT_SEQ")
	@SequenceGenerator(name = "DROIT_SEQ", sequenceName = "DROIT_ACCEES_SEQ", allocationSize = 1, initialValue = 1)
	@Column(name = "docente_id")
	private long id;

	@Column(name = "nome")
	private String nome;
	@Column(name = "cognome")
	private String cognome;
	@Column(name = "email")
	private String mail;
	@Column(name = "password")
	private String password;
	@Column(name = "codice_fiscale", length = 16)
	private String codiceFiscale;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "docente_classe_table", 
				joinColumns = {
						@JoinColumn(name = "docente_id", referencedColumnName = "docente_id")
				},
				inverseJoinColumns = {
						@JoinColumn(name = "classe_id", referencedColumnName = "classe_id")
				}
			)
	@JsonManagedReference
	private List<Classe> classi;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "docente_materia_table", 
				joinColumns = {
						@JoinColumn(name = "docente_id", referencedColumnName = "docente_id")
				},
				inverseJoinColumns = {
						@JoinColumn(name = "materia_id", referencedColumnName = "materia_id")
				}
			)
	@JsonManagedReference
	private Set<Materia> materie;

	public Docente() {
	}

	public Docente(long id, String nome, String cognome, String mail, String password, String codiceFiscale,
			List<Classe> classi, Set<Materia> materie) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.mail = mail;
		this.password = password;
		this.codiceFiscale = codiceFiscale;
		this.classi = classi;
		this.materie = materie;
	}
	
	public List<Classe> getClassi() {
		return classi;
	}

	public void setClassi(List<Classe> classi) {
		this.classi = classi;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public Set<Materia> getMaterie() {
		return materie;
	}

	public void setMaterie(Set<Materia> materie) {
		this.materie = materie;
	}
}
