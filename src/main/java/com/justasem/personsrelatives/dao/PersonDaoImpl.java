package com.justasem.personsrelatives.dao;

import com.justasem.personsrelatives.model.Person;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonDaoImpl implements PersonDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Person> findAll() {
        Session session = sessionFactory.openSession();
        List<Person> persons = session.createCriteria(Person.class).list();
        session.close();
        return persons;
    }

    @Override
    public Person findById(int id) {
        Session session = sessionFactory.openSession();
        Person person = session.get(Person.class, id);
        session.close();
        return person;
    }

    @Override
    public void save(Person person) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(person);
        session.getTransaction().commit();
        session.close();

    }

}
