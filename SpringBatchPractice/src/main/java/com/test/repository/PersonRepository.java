package com.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{

	
	
}
