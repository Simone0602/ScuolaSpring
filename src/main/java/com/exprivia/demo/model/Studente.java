package com.exprivia.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "studenti")
public class Studente {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DROIT_SEQ")
	@SequenceGenerator(name = "DROIT_SEQ", sequenceName = "DROIT_ACCEES_SEQ", allocationSize = 1, initialValue = 1)
	@Column(name = "student_id")
	private long id;

	@Column(name = "nome")
	private String nome;
	@Column(name = "cognome")
	private String cognome;
	@Column(name = "email")
	private String mail;
	@Column(name = "password")
	private String password;
	@Column(name = "user_code", length = 6)
	private String userCode;

	@ManyToOne
	@JoinColumn(name = "classe_id")
	@JsonBackReference
	private Classe classe;
	
	@OneToMany(mappedBy = "studente")
	@JsonManagedReference
	private List<Token> tokens;
	
	@OneToMany(mappedBy = "studente")
	@JsonManagedReference
	private List<Valutazione> voti;

	public Studente() {
	}

	public Studente(String nome, String cognome, String mail, String password, String userCode, Classe classe) {
		this.nome = nome;
		this.cognome = cognome;
		this.mail = mail;
		this.password = password;
		this.userCode = userCode;
		this.classe = classe;
	}

	public List<Valutazione> getVoti() {
		return voti;
	}

	public void setVoti(List<Valutazione> voti) {
		this.voti = voti;
	}
	
	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
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

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String user) {
		this.userCode = user;
	}

	@Override
	public String toString() {
		return "Studente [getId()=" + getId() + ", getNome()=" + getNome() + ", getCognome()=" + getCognome()
				+ ", getMail()=" + getMail() + ", getpassword()=" + getPassword() + ", getUser()=" + getUserCode() + "]";
	}

}
