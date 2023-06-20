package com.exprivia.demo.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "docenti")
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Docente implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DROIT_SEQ")
	@SequenceGenerator(name = "DROIT_SEQ", sequenceName = "DROIT_ACCEES_SEQ", allocationSize = 1, initialValue = 1)
	@Column(name = "docente_id")
	@NonNull
	private long id;

	@NonNull
	@Column(name = "nome")
	private String nome;
	@NonNull
	@Column(name = "cognome")
	private String cognome;
	@NonNull
	@Column(name = "email")
	private String mail;
	@NonNull
	@Column(name = "password")
	private String pass;
	@NonNull
	@Column(name = "codice_fiscale", length = 16)
	private String codiceFiscale;

	@NonNull
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "docente_classe_table", 
				joinColumns = {
						@JoinColumn(name = "docente_id", referencedColumnName = "docente_id")
				},
				inverseJoinColumns = {
						@JoinColumn(name = "classe_id", referencedColumnName = "classe_id")
				}
			)
	@JsonManagedReference
	private List<Classe> classi;

	@NonNull
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "docente_materia_table", 
				joinColumns = {
						@JoinColumn(name = "docente_id", referencedColumnName = "docente_id")
				},
				inverseJoinColumns = {
						@JoinColumn(name = "materia_id", referencedColumnName = "materia_id")
				}
			)
	@JsonManagedReference
	private Set<Materia> materie;
	
	@OneToMany(mappedBy = "docente")
	@JsonManagedReference
	private List<Token> tokens;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return this.pass;
	}

	@Override
	public String getUsername() {
		return this.codiceFiscale;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
