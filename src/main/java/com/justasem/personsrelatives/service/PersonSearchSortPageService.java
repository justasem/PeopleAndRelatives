package com.justasem.personsrelatives.service;

import com.justasem.personsrelatives.model.Person;
import com.justasem.personsrelatives.repositories.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
public class PersonSearchSortPageService {
    private static final int PAGE_SIZE = 5;

    private PersonRepository repository;

    @Inject
    public PersonSearchSortPageService(PersonRepository repository) {
        this.repository = repository;
    }


    public Page<Person> getPersonPageSortedBy(Integer pageNumber, String sortProperty) {

        PageRequest pageRequest =
                new PageRequest(pageNumber - 1, PAGE_SIZE,
                        "birthDate".equals(sortProperty) ? Sort.Direction.DESC : Sort.Direction.ASC,
                        sortProperty);

        return repository.findAll(pageRequest);
    }
}
