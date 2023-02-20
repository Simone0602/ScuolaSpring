package com.exprivia.demo.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.exprivia.demo.enums.Materie;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	
	@ManyToMany(mappedBy = "materie", fetch = FetchType.LAZY)
	@JsonBackReference
	private List<Docente> docenti;
	
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
