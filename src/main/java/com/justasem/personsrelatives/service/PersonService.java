package com.justasem.personsrelatives.service;

import com.justasem.personsrelatives.model.Person;

import java.util.List;

public interface PersonService {
    List<Person> findAll();
    Person findById(int id);
    void save(Person person);

}

