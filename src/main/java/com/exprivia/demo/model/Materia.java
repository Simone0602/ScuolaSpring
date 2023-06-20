package com.exprivia.demo.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import lombok.*;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Materia {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DROIT_SEQ")
	@SequenceGenerator(name = "DROIT_SEQ", sequenceName = "DROIT_ACCEES_SEQ", allocationSize = 1, initialValue = 1)
	@Column(name = "materia_id")
	@NonNull
	private long id;

	@NonNull
	@Enumerated(EnumType.STRING)
	private Materie materia;
	
	@OneToMany(mappedBy = "materia")
	@JsonManagedReference
	private List<Valutazione> voti;
	
	@ManyToMany(mappedBy = "materie", fetch = FetchType.LAZY)
	@JsonBackReference
	private List<Docente> docenti;
	
}
