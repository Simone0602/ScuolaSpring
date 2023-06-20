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
import lombok.*;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Assenza {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DROIT_SEQ")
	@SequenceGenerator(name = "DROIT_SEQ", sequenceName = "DROIT_ACCEES_SEQ", allocationSize = 1, initialValue = 1)
	@NonNull
	@Column(name = "assenze_id")
	private long id;

	@NonNull
	@Column(name = "giornata_assenza")
	private LocalDate giornataAssenza;

	@Column(name = "giustificata")
	private boolean isGiustificata;

	@NonNull
	@ManyToOne()
	@JoinColumn(name = "student_id")
	@JsonBackReference
	private Studente studente;
}
