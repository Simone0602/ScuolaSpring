package com.exprivia.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exprivia.demo.model.Classe;

@Repository
public interface ClassRepository extends JpaRepository<Classe, Long>{

	public Optional <Classe> findClasseById(Long id);

}
