package com.justasem.personsrelatives.repositories;

import com.justasem.personsrelatives.model.Person;
import org.springframework.data.repository.CrudRepository;


public interface PersonRepository extends CrudRepository<Person, Long> {
}
