package com.exprivia.demo.model;

import java.util.List;

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
	private String pas;
	@Column(name = "codice_fiscale", length = 16)
	private String codiceFiscale;
	@Column(name = "materia")
	private String materia;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "docente_classe_table", 
				joinColumns = {
						@JoinColumn(name = "docente_id", referencedColumnName = "docente_id")
				},
				inverseJoinColumns = {
						@JoinColumn(name = "classe_id", referencedColumnName = "classe_id")
				}
			)
	private List<Classe> classi;

	public Docente() {
	}

	public Docente(long id, String nome, String cognome, String mail, String pas, String codiceFiscale,
			String materia) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.mail = mail;
		this.pas = pas;
		this.codiceFiscale = codiceFiscale;
		this.materia = materia;
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

	public String getPas() {
		return pas;
	}

	public void setPas(String pas) {
		this.pas = pas;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	@Override
	public String toString() {
		return "Docente [nome=" + getNome() + ", cognome=" + getCognome() + ", mail=" + getMail() + ", pas=" + getPas()
				+ ", materia=" + getMateria() + "]";
	}
}
