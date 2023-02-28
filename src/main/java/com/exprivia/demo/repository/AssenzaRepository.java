package com.exprivia.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exprivia.demo.model.Assenza;

@Repository
public interface AssenzaRepository extends JpaRepository<Assenza, Long>{

}
