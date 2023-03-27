package com.exprivia.demo.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@SuppressWarnings("serial")
@Entity
@Table(name = "studenti")
public class Studente implements UserDetails{

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
	private String pass;
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
	
	@OneToMany(mappedBy = "studente")
	@JsonManagedReference
	private List<Assenza> assenze;

	public Studente() {
	}

	public Studente(String nome, String cognome, String mail, String pass, String userCode, Classe classe) {
		this.nome = nome;
		this.cognome = cognome;
		this.mail = mail;
		this.pass = pass;
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
	
	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String user) {
		this.userCode = user;
	}
	

	public List<Assenza> getAssenze() {
		return assenze;
	}

	public void setAssenze(List<Assenza> assenze) {
		this.assenze = assenze;
	}

	@Override
	public String toString() {
		return "Studente [getId()=" + getId() + ", getNome()=" + getNome() + ", getCognome()=" + getCognome()
				+ ", getMail()=" + getMail() + ", getpassword()=" + getPass() + ", getUser()=" + getUserCode() + "]";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.userCode;
	}
	
	@Override
	public String getPassword() {
		return this.pass;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
