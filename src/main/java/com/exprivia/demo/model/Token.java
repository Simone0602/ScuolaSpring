package com.exprivia.demo.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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

	public Token() {}
	public Token(String token, Studente studente) {
		this.token = token;
		this.expiredDate = LocalDate.now().plusDays(7);
		this.studente = studente;
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
	@Override
	public String toString() {
		return "Token [getToken()=" + getToken() + ", getExpiredDate()=" + getExpiredDate() + ", getStudente()="
				+ getStudente() + "]";
	}
}
