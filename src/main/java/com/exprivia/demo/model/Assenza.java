package com.exprivia.demo.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Assenza {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DROIT_SEQ")
	@SequenceGenerator(name = "DROIT_SEQ", sequenceName = "DROIT_ACCEES_SEQ", allocationSize = 1, initialValue = 1)
	@Column(name = "assenze_id")
	private long id;
	
	@Column(name = "giornata_assenza")
	private LocalDate giornataAssenza;
	@Column(name = "giustificata")
	private boolean isGiustificata;
	
	@ManyToOne()
	@JoinColumn(name = "student_id")
	@JsonBackReference
	private Studente studente;
	
	public Assenza() {};

	public Assenza(long id, LocalDate giornataAssenza, Studente studente) {
		super();
		this.id = id;
		this.giornataAssenza = giornataAssenza;
		this.isGiustificata = false;
		this.studente = studente;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getGiornataAssenza() {
		return giornataAssenza;
	}

	public void setGiornataAssenza(LocalDate giornataAssenza) {
		this.giornataAssenza = giornataAssenza;
	}

	public boolean isGiustificata() {
		return isGiustificata;
	}

	public void setGiustificata(boolean isGiustificata) {
		this.isGiustificata = isGiustificata;
	}

	public Studente getStudente() {
		return studente;
	}

	public void setStudente(Studente studente) {
		this.studente = studente;
	}
}
