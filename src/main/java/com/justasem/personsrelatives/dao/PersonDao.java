package com.justasem.personsrelatives.dao;

import com.justasem.personsrelatives.model.Person;

import java.util.List;

public interface PersonDao {
    List<Person> findAll();
    Person findById(int id);
    void save(Person person);
}
