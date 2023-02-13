package com.exprivia.demo.model;

import java.util.List;

import com.exprivia.demo.enums.Materie;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Materia {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DROIT_SEQ")
	@SequenceGenerator(name = "DROIT_SEQ", sequenceName = "DROIT_ACCEES_SEQ", allocationSize = 1, initialValue = 1)
	@Column(name = "materia_id")
	private long id;
	private Materie materia;
	
	@OneToMany(mappedBy = "materia")
	@JsonManagedReference
	private List<Valutazione> voti;
	
	public Materia() {}
	public Materia(long id, Materie materia) {
		super();
		this.id = id;
		this.materia = materia;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Materie getMateria() {
		return materia;
	}
	public void setMateria(Materie materia) {
		this.materia = materia;
	}
	public List<Valutazione> getVoti() {
		return voti;
	}
	public void setVoti(List<Valutazione> voti) {
		this.voti = voti;
	}
	
}
