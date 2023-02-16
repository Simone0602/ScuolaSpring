package com.exprivia.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exprivia.demo.enums.Materie;
import com.exprivia.demo.model.Materia;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long>{

	public Materia findByMateria(Materie valueOf);

}
