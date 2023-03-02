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
public class Valutazione {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DROIT_SEQ")
	@SequenceGenerator(name = "DROIT_SEQ", sequenceName = "DROIT_ACCEES_SEQ", allocationSize = 1, initialValue = 1)
	@Column(name = "valutazione_id")
	private long id;
	private float voto;
	private LocalDate data;

	@ManyToOne
	@JoinColumn(name = "student_id")
	@JsonBackReference
	private Studente studente;
	
	@ManyToOne
	@JoinColumn(name = "materia_id")
	@JsonBackReference
	private Materia materia;

	public Valutazione() {}
	public Valutazione(long id, float voto, LocalDate data, Studente studente, Materia materia) {
		super();
		this.id = id;
		this.voto = voto;
		this.data = data;
		this.studente = studente;
		this.materia = materia;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getVoto() {
		return voto;
	}

	public void setVoto(float voto) {
		this.voto = voto;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Studente getStudente() {
		return studente;
	}

	public void setStudente(Studente studente) {
		this.studente = studente;
	}

	public Materia getMateria() {
		return materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}
}
