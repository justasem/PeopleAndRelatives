package com.justasem.personsrelatives;

import com.justasem.personsrelatives.model.Person;
import com.justasem.personsrelatives.repositories.PersonRepository;
import com.justasem.personsrelatives.service.PersonSearchSortPageService;
import org.h2.store.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonSearchSortPageServiceTest {

    private final int PAGE_NUMBER = 1;
    private final int PAGE_SIZE = 5;
    private final String SORT_PROPERTY_LASTNAME = "lastName";
    private final String SORT_PROPERTY_BIRTHDATE = "birthDate";
    private Person father, mother;

    private Pageable pageRequest;
    private Sort sort;
    private Page<Person> emptyPage, resultPage;

    private PersonRepository repository;

    private PersonSearchSortPageService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        repository = mock(PersonRepository.class);
        service = new PersonSearchSortPageService(repository);

        father = new Person(1L,"Algirdas", "Paulauskas", LocalDate.of(1965,5,13));
        mother = new Person(2L,"Raminta", "Paulauskienė", LocalDate.of(1967,12,2));

        sort = new Sort(Sort.Direction.ASC, SORT_PROPERTY_LASTNAME);
        pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE, sort);

        emptyPage = new PageImpl<>(new ArrayList<>(), pageRequest, 0);
        resultPage = new PageImpl<>(Arrays.asList(father, mother));
    }

    @Test
    public void whenSortingPaging_thenReturnPageWithRequestedPageNumberAndSize() throws Exception {

        when(repository.findAll(any(Pageable.class))).thenReturn(emptyPage);
        Page<Person> searchResultPage = service.getPersonPageSortedBy(PAGE_NUMBER, SORT_PROPERTY_LASTNAME);
        assertThat(searchResultPage.getTotalPages()).isEqualTo(0);
        assertThat(searchResultPage.getNumber()).isEqualTo(PAGE_NUMBER);
    }

    @Test
    public void whenNoPersonsAreFound_thenReturnEmptyPageWithZeroElements() throws Exception {

        when(repository.findAll(any(Pageable.class))).thenReturn(emptyPage);
        Page<Person> searchResultPage = service.getPersonPageSortedBy(PAGE_NUMBER, SORT_PROPERTY_LASTNAME);
        assertThat(searchResultPage).isEmpty();
        assertThat(searchResultPage.getTotalElements()).isEqualTo(0);
    }

    @Test

    public void whenTwoPersonsAreFound_thenReturnPageThatHasTwoEntriesWithCorrectInfo() throws Exception {

        when(repository.findAll(any(Pageable.class))).thenReturn(resultPage);
        Page<Person> searchResultPage = service.getPersonPageSortedBy(PAGE_NUMBER, SORT_PROPERTY_LASTNAME);
        assertThat(searchResultPage.getContent().size()).isEqualTo(2);
        assertThat(searchResultPage.getContent()).contains(father, mother);
    }








}
