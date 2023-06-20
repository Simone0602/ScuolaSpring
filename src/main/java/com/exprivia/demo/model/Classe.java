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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

@Entity
@Table(name = "classi")
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Classe {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DROIT_SEQ")
	@SequenceGenerator(name = "DROIT_SEQ", sequenceName = "DROIT_ACCEES_SEQ", allocationSize = 1, initialValue = 1)
	@Column(name = "classe_id")
	private Long id;

	@NonNull
	@Column(name = "sezione")
	private String sezione;
	@NonNull
	@Column(name = "cordinatore")
	private String cordinatore;
	@NonNull
	@Column(name = "aula")
	private String aula;
	
	@OneToMany(mappedBy = "classe")
	@JsonManagedReference
	private List<Studente> studenti;
	
	@ManyToMany(mappedBy = "classi", fetch = FetchType.LAZY)
	@JsonBackReference
	private List<Docente> docenti;

	@Override
	public String toString() {
		return "Classe [getId()=" + getId() + ", getSezione()=" + getSezione() + ", getCordinatore()="
				+ getCordinatore() + ", getAula()=" + getAula() + "]";
	}

}
