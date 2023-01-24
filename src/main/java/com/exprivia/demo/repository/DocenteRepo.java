package com.exprivia.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exprivia.demo.model.Docente;

public interface DocenteRepo extends JpaRepository <Docente, Long> {

}
