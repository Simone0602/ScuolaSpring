package com.exprivia.demo.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.exprivia.demo.model.Docente;

public interface DocenteRepo extends JpaRepository <Docente, Long> {

	public Optional <Docente> findDocenteById(long id);

	public boolean existsByCodiceFiscale(String codiceFiscale);

	public Optional<Docente> findDocenteByCodiceFiscale(String codiceFiscale);

	@Transactional
    @Modifying
	@Query(value = "DELETE FROM docente_materia_table WHERE docente_id = :id", nativeQuery = true)
	public void deleteMateriaByDocenteId(@Param("id") long id);
	
	@Transactional
    @Modifying
	@Query(value = "DELETE FROM docente_classe_table WHERE docente_id = :id", nativeQuery = true)
	public void deleteClasseByDocenteId(@Param("id") long id);
}
