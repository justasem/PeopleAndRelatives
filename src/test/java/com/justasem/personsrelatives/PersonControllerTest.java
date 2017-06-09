package com.justasem.personsrelatives;

import com.justasem.personsrelatives.model.Person;
import com.justasem.personsrelatives.repositories.PersonRepository;
import com.justasem.personsrelatives.service.PersonSearchSortPageService;
import com.justasem.personsrelatives.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonControllerTest {

    private MockMvc mockMvc;
    private Person father, mother, son, daughter, grandfather, grandmother;
    private List<Person> persons;
    private Page<Person> personsPage;

    @Resource
    private WebApplicationContext context;

    @MockBean
    private PersonService serviceMock;

    @MockBean
    private PersonSearchSortPageService serviceSortingMock;

    @MockBean
    private PersonRepository repository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        father = new Person(1L,"Algirdas", "Paulauskas", LocalDate.of(1965,5,13));
        mother = new Person(2L,"Raminta", "Paulauskienė", LocalDate.of(1967,12,2));
        son = new Person(3L,"Matas", "Paulauskas", LocalDate.of(1988,4,14));
        daughter = new Person(4L,"Sandra", "Paulauskaitė", LocalDate.of(1989,2,13));
        grandfather = new Person(5L,"Bronius", "Paulauskas", LocalDate.of(1935,11,7));
        grandmother = new Person(6L,"Danutė", "Paulauskienė", LocalDate.of(1941,9,30));

        persons = Arrays.asList(father, mother, son, daughter, grandfather, grandmother);
        personsPage = new PageImpl<>(persons);
        when(serviceMock.getAllPersons()).thenReturn(persons);
        when(serviceSortingMock.getPersonPageSortedBy(1, "lastName")).thenReturn(personsPage);
        when(repository.findAll()).thenReturn(persons);
        when(repository.findAll(any(Pageable.class))).thenReturn(personsPage);

    }

    @Test
    public void whenHomePageLoads_thenRedirectToAllPersonsSortedByLastName() throws Exception {

        mockMvc.perform(get("/")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/index.jsp"))
                .andExpect(model().attribute("persons", hasSize(6)))
                .andExpect(model().attribute("persons", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("firstName", is("Algirdas")),
                                hasProperty("lastName", is("Paulauskas")),
                                hasProperty("birthDate", is(LocalDate.of(1965, 5, 13)))
                        )
                )))
                .andExpect(model().attribute("persons", hasItem(
                        allOf(
                                hasProperty("id", is(2L)),
                                hasProperty("firstName", is("Raminta")),
                                hasProperty("lastName", is("Paulauskienė")),
                                hasProperty("birthDate", is(LocalDate.of(1967, 12, 2))
                                )
                        ))));

        verify(serviceMock, times(1)).getAllPersons();
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void givenFindByIdIsCalled_whenPersonFound_thenRenderDetailView() throws Exception {

        when(serviceMock.findById(1L)).thenReturn(father);

        mockMvc.perform(get("/person/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("detail"))
                .andExpect(forwardedUrl("/detail.jsp"))
                .andExpect(model().attribute("person", hasProperty("id", is(1L))))
                .andExpect(model().attribute("person", hasProperty("firstName", is("Algirdas"))))
                .andExpect(model().attribute("person", hasProperty("lastName", is("Paulauskas"))))
                .andExpect(model().attribute("person", hasProperty("birthDate", is(LocalDate.of(1965, 5, 13)))));

        verify(serviceMock, times(1)).findById(1L);
    }

    @Test
    public void whenRoutingToPersonDetails_thenHisRelativesMappedWithTypeArePassedAsAttribute() throws Exception {
        List<Person> relatives = Arrays.asList(mother, son, daughter, grandmother, grandfather);
        List<Person> relativesWithType = new ArrayList<>();
        for (Person relative:relatives) {
            relative.setRelativeType(serviceMock.getRelativeType(father, relative));
            relativesWithType.add(relative);
        }
        when(serviceMock.findById(1L)).thenReturn(father);
        when(serviceMock.getAllRelatives(father)).thenReturn(relatives);
        when(serviceMock.getRelativesWithType(father, relatives)).thenReturn(relativesWithType);

        mockMvc.perform(get("/person/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("detail"))
                .andExpect(forwardedUrl("/detail.jsp"))
                .andExpect(model().attribute("person", father))
                .andExpect(model().attribute("relativesWithType", relativesWithType));

        verify(serviceMock, times(1)).findById(1L);
        verify(serviceMock,times(1)).getAllRelatives(father);
        //verifyNoMoreInteractions(serviceMock);

    }

    @Test
    public void whenRoutingToEditPerson_thenFormViewIsRenderedWithExistingPersonDetails() throws Exception {
        when(serviceMock.findById(1L)).thenReturn(father);

        mockMvc.perform(get("/person/edit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(forwardedUrl("/form.jsp"))
                .andExpect(model().attribute("person", father));

        verify(serviceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void whenRoutingToAddNewPerson_thenFormViewIsRenderedAndInstanceOfPersonIsPassed() throws Exception {
        mockMvc.perform(get("/person/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(forwardedUrl("/form.jsp"))
                .andExpect(model().attribute("person", instanceOf(Person.class)));
    }

    @Test
    public void whenSavingNewPerson_thenPassPersonAsParamAndRenderAllPersonsView() throws Exception {

        Person son = new Person(
                3L,
                "Matas",
                "Paulauskas",
                LocalDate.of(1988, 4, 14));

        mockMvc.perform(post("/person").sessionAttr("person", son))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(view().name("redirect:/"));

        verify(serviceMock, times(1)).savePerson(son);
        verifyNoMoreInteractions(serviceMock);

    }

    //TODO fix the ASC/DESC order tests
//    @Test
//    public void whenSortingPaging_thenReturnPageThatIsSortedInDesscendingOrderByUsingSortPropertyBirthDate() throws Exception {
//        when(repository.findAll(any(Pageable.class))).thenReturn(personsPage);
//        when(serviceSortingMock.getPersonPageSortedBy(1, "birthDate")).thenReturn(personsPage);
//        Page<Person> searchResultPage = serviceSortingMock.getPersonPageSortedBy(1, "birthDate");
//
//        mockMvc.perform(get("/birthDate/pages/1")).andExpect(status().isOk())
//                .andExpect(view().name("index"))
//                .andExpect(model().attribute("persons", persons))
//                .andExpect(model().attribute("page", personsPage));
//
//        assertThat(searchResultPage.getSort().getOrderFor("birthDate").getDirection())
//                .isEqualTo(Sort.Direction.DESC);
//    }
//
//    @Test
//    public void whenSortingPaging_thenReturnPageThatIsSortedInAscendingOrderByUsingSortPropertyLastName() throws Exception {
//
//        when(repository.findAll(any(Pageable.class))).thenReturn(personsPage);
//        Page<Person> searchResultPage = serviceSortingMock.getPersonPageSortedBy(1, "lastName");
//        assertThat(searchResultPage.getSort().getOrderFor("lastName").getDirection())
//                .isEqualTo(Sort.Direction.ASC);
//    }





}


