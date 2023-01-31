package com.exprivia.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exprivia.demo.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, String>{

}
