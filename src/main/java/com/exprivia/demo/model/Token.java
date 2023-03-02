package com.exprivia.demo.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Token {
	@Id
	@Column(name = "token", nullable = false, updatable = false)
	private String token;
	@Column(name = "expired_date", nullable = false, updatable = false)
	private LocalDate expiredDate;
	
	@ManyToOne
	@JoinColumn(name = "studente_id")
	@JsonBackReference
	private Studente studente;
	
	@ManyToOne
	@JoinColumn(name = "docente_id")
	@JsonBackReference
	private Docente docente;

	public Token() {}
	public Token(String token, Studente studente) {
		this.token = token;
		this.expiredDate = LocalDate.now().plusDays(7);
		this.studente = studente;
	}
	public Token(String token, Docente docente) {
		this.token = token;
		this.expiredDate = LocalDate.now().plusDays(7);
		this.docente = docente;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LocalDate getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(LocalDate expiredDate) {
		this.expiredDate = expiredDate;
	}
	public Studente getStudente() {
		return studente;
	}
	public void setStudente(Studente studente) {
		this.studente = studente;
	}
	public Docente getDocente() {
		return docente;
	}
	public void setDocente(Docente docente) {
		this.docente = docente;
	}
	@Override
	public String toString() {
		return "Token [getToken()=" + getToken() + ", getExpiredDate()=" + getExpiredDate() + ", getStudente()="
				+ getStudente() + "]";
	}
}
