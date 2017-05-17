package com.justasem.personsrelatives.service;

import com.justasem.personsrelatives.dao.PersonDao;
import com.justasem.personsrelatives.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonDao personDao;

    @Override
    public List<Person> findAll() {
        return personDao.findAll();
    }

    @Override
    public Person findById(int id) {
        return personDao.findById(id);
    }

    @Override
    public void save(Person person) {
        personDao.save(person);

    }
}
