package com.exprivia.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
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

	@Override
	public String toString() {
		return "Token [getToken()=" + getToken() + ", getExpiredDate()=" + getExpiredDate() + ", getStudente()="
				+ getStudente() + "]";
	}
}
