package com.exprivia.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exprivia.demo.model.Studente;

public interface StudentRepo extends JpaRepository <Studente, Long>{

	public Optional<Studente> findStudentByUserCode(String userCode);
	
	public Optional<Studente> findStudentByMail(String mail);

	public boolean existsByUserCode(String userCode);

	public boolean existsByMail(String mail);
}
