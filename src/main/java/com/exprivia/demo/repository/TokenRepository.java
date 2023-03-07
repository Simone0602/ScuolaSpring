package com.exprivia.demo.repository;

import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exprivia.demo.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, String>{

	@Query(value = "SELECT * FROM token WHERE token = :token and expired_date >= :date", nativeQuery = true)
	public Optional<Token> findByIdAndDate(@Param("token") String token, @Param("date") LocalDate date);

	@Transactional
    @Modifying
    @Query(value = "DELETE FROM token WHERE studente_id = :id", nativeQuery = true)
    public void deleteTokenByStudenteId(@Param("id") long id);
	
	@Transactional
    @Modifying
    @Query(value = "DELETE FROM token WHERE docente_id = :id", nativeQuery = true)
    public void deleteTokenByDocenteId(@Param("id") long id);

}
