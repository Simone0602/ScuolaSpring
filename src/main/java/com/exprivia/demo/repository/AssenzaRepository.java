package com.exprivia.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exprivia.demo.model.Assenza;

@Repository
public interface AssenzaRepository extends JpaRepository<Assenza, Long>{
	
	@Transactional
    @Modifying
    @Query(value = "DELETE FROM assenza WHERE student_id = :id", nativeQuery = true)
    public void deleteAssenzeByStudenteId(@Param("id") long id);
}
