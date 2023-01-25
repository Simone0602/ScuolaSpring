package com.exprivia.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.exprivia.demo.model.Studente;

public interface StudentRepo extends JpaRepository <Studente, Long>{

	public Optional<Studente> findStudentByUserCode(String userCode);

	public boolean existsByUserCode(String userCode);

	@Query(value = "SELECT * FROM studenti WHERE email = :mail and password = :pas and user_code = :user_code", nativeQuery = true)
	public Studente findStudent(@Param("mail") String mail, @Param("pas") String pas, @Param("user_code") String userCode);
}
