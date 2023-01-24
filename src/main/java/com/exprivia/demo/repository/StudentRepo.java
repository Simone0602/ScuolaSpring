package com.exprivia.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exprivia.demo.model.Studente;

public interface StudentRepo extends JpaRepository <Studente, Long>{

}
