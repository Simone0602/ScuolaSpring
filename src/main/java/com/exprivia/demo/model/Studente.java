package com.exprivia.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "studenti")
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Studente implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DROIT_SEQ")
	@SequenceGenerator(name = "DROIT_SEQ", sequenceName = "DROIT_ACCEES_SEQ", allocationSize = 1, initialValue = 1)
	@Column(name = "student_id")
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
	@Column(name = "user_code", length = 6)
	private String userCode;

	@NonNull
	@ManyToOne
	@JoinColumn(name = "classe_id")
	@JsonBackReference
	private Classe classe;
	
	@OneToMany(mappedBy = "studente")
	@JsonManagedReference
	private List<Token> tokens;
	
	@OneToMany(mappedBy = "studente")
	@JsonManagedReference
	private List<Valutazione> voti;
	
	@OneToMany(mappedBy = "studente")
	@JsonManagedReference
	private List<Assenza> assenze;

	@Override
	public String toString() {
		return "Studente [getId()=" + getId() + ", getNome()=" + getNome() + ", getCognome()=" + getCognome()
				+ ", getMail()=" + getMail() + ", getpassword()=" + getPass() + ", getUser()=" + getUserCode() + "]";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.userCode;
	}
	
	@Override
	public String getPassword() {
		return this.pass;
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
