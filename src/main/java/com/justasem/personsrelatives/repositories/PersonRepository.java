package com.justasem.personsrelatives.repositories;

import com.justasem.personsrelatives.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {
}
