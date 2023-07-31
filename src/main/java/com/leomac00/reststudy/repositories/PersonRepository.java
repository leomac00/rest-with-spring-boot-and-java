package com.leomac00.reststudy.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leomac00.reststudy.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
