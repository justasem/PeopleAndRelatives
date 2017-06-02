package com.justasem.personsrelatives;

import com.justasem.personsrelatives.model.Person;
import com.justasem.personsrelatives.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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

    @Resource
    private WebApplicationContext context;

    @MockBean
    private PersonService serviceMock;

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
        when(serviceMock.getAllPersons()).thenReturn(persons);
    }

    @Test
    public void whenHomePageLoads_thenDisplayAllPersons() throws Exception {

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
        Map<Person, String> relativesMapped = new HashMap<>();
        relativesMapped.put(mother, "žmona"); relativesMapped.put(son, "sūnus");
        relativesMapped.put(daughter, "dukra"); relativesMapped.put(grandmother, "motina");
        relativesMapped.put(grandfather, "tėvas");
        when(serviceMock.findById(1L)).thenReturn(father);
        when(serviceMock.getAllRelatives(father)).thenReturn(relatives);
        when(serviceMock.getRelativesMappedWithType(father, relatives)).thenReturn(relativesMapped);

        mockMvc.perform(get("/person/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("detail"))
                .andExpect(forwardedUrl("/detail.jsp"))
                .andExpect(model().attribute("person", father))
                .andExpect(model().attribute("relativesMapped", relativesMapped));

        verify(serviceMock, times(1)).findById(1L);
        verify(serviceMock,times(1)).getAllRelatives(father);
        //verifyNoMoreInteractions(serviceMock);

    }

    //TODO Check if Person details are correct;
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
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/index.jsp"));

        verify(serviceMock, times(1)).savePerson(son);
        verifyNoMoreInteractions(serviceMock);

    }



}


