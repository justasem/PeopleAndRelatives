package com.justasem.personsrelatives;

import com.justasem.personsrelatives.model.Person;
import com.justasem.personsrelatives.repositories.PersonRepository;
import com.justasem.personsrelatives.service.PersonNotFoundException;
import com.justasem.personsrelatives.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import sun.dc.path.PathError;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;


public class PersonServiceTest {

    private PersonService service;
    private List<Person> personsDummy;
    private Person father, mother, son, daughter, grandfather, grandmother;

    @Mock
    private PersonRepository repository;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);

        father = new Person(1L,"Algirdas", "Paulauskas", LocalDate.of(1965,5,13));
        mother = new Person(2L,"Raminta", "Paulauskienė", LocalDate.of(1967,12,2));
        son = new Person(3L,"Matas", "Paulauskas", LocalDate.of(1988,4,14));
        daughter = new Person(4L,"Sandra", "Paulauskaitė", LocalDate.of(1989,2,13));
        grandfather = new Person(5L,"Bronius", "Paulauskas", LocalDate.of(1935,11,7));
        grandmother = new Person(6L,"Danutė", "Paulauskienė", LocalDate.of(1941,9,30));

        personsDummy = Arrays.asList(father, mother, son, daughter, grandfather, grandmother);
        when(repository.findAll()).thenReturn(personsDummy);
        service = new PersonService(repository);
    }

    @Test
    public void whenGettingAllPersons_thenReturnList() throws Exception {
        List<Person> personsAll = service.getAllPersons();
        assertThat(personsAll).isNotNull();
        assertThat(personsAll).containsAll(personsDummy);
    }

    @Test
    public void whenGettingAllPersons_thenReturnSix() throws Exception {
        assertEquals("findAll should return six persons", 6, service.getAllPersons().size());
        verify(repository).findAll();
    }

    @Test
    public void whenGettingOnePersonById_thenReturnOne() throws Exception {
        when(repository.findOne(1L)).thenReturn(new Person());
        assertThat(service.findById(1L)).isInstanceOf(Person.class);
    }

    @Test(expected = PersonNotFoundException.class)
    public void givenPersonIsNull_whenGettingPersonById_thenThrowPersonNotFoundException() throws Exception {
        when(repository.findOne(1L)).thenReturn(null);
        service.findById(1L);
        verify(repository).findOne(1L);
    }

    //------------------------------------------
    //Getting number of years between birthDates
    //------------------------------------------

    @Test
    public void givenTwoBirhtdatesFirstLaterSecondEarlier_whenGettingYearsBetweenBirthdates_thenReturnYears()
            throws Exception {
        //given
        LocalDate birthDate1 = LocalDate.of(1988, 4, 14);
        LocalDate birthDate2 = LocalDate.of(1964, 6, 22);
        //when
        int yearsBetweenBirthDates = service.getYearsBetweenBirthDates(birthDate1, birthDate2);
        //then
        assertThat(yearsBetweenBirthDates).isEqualTo(23);
    }

    @Test
    public void givenTwoBirhtdatesFirstEarlierSecondLater_whenGettingYearsBetweenBirthdates_thenReturnYears()
            throws Exception {
        //given
        LocalDate birthDate1 = LocalDate.of(1964, 6, 22);
        LocalDate birthDate2 = LocalDate.of(1988, 4, 14);
        //when
        int yearsBetweenBirthDates = service.getYearsBetweenBirthDates(birthDate1, birthDate2);
        //then
        assertThat(yearsBetweenBirthDates).isEqualTo(23);
    }

    @Test
    public void givenTwoIdenticalBirhtdates_whenGettingYearsBetweenBirthdates_thenReturnZero()
            throws Exception {
        //given
        LocalDate birthDate1 = LocalDate.of(1988, 4, 14);
        LocalDate birthDate2 = LocalDate.of(1988, 4, 14);
        //when
        int yearsBetweenBirthDates = service.getYearsBetweenBirthDates(birthDate1, birthDate2);
        //then
        assertThat(yearsBetweenBirthDates).isEqualTo(0);
    }

    @Test
    public void givenNullDates_whenGettingYearsBetweenBirthdates_thenReturnIllegalArgumentException()
            throws Exception {
        //given
        //when
        Throwable t = catchThrowable(()-> service.getYearsBetweenBirthDates(null, null));
        //then
        assertThat(t).isInstanceOf(IllegalArgumentException.class);
    }

    //----------------------
    //Dealing with lastNames
    //----------------------
    //
    //Checking if Man or Woman

    @Test
    public void givenALastNameManEnding_whenCheckingIfMan_thenReturnTrue()
            throws Exception {
        //given
        String lastName = "Paulauskas";
        //when
        boolean isMan = service.isMan(lastName);
        //then
        assertThat(isMan).isTrue();
    }

    @Test
    public void givenALastNameFeminineEnding1_whenCheckingIfWoman_thenReturnTrue()
            throws Exception {
        //given
        String lastName = "Paulauskaitė";
        //when
        boolean isWoman = service.isWoman(lastName);
        //then
        assertThat(isWoman).isTrue();
    }

    @Test
    public void givenALastNameFeminineEnding2_whenCheckingIfWoman_thenReturnTrue()
            throws Exception {
        //given
        String lastName = "Genytė";
        //when
        boolean isWoman = service.isWoman(lastName);
        //then
        assertThat(isWoman).isTrue();
    }

    @Test
    public void givenALastNameFeminineEnding3_whenCheckingIfWoman_thenReturnTrue()
            throws Exception {
        //given
        String lastName = "Antanavičiūtė";
        //when
        boolean isWoman = service.isWoman(lastName);
        //then
        assertThat(isWoman).isTrue();
    }

    @Test
    public void givenALastNameFeminineEnding4_whenCheckingIfWoman_thenReturnTrue()
            throws Exception {
        //given
        String lastName = "Stankutė";
        //when
        boolean isWoman = service.isWoman(lastName);
        //then
        assertThat(isWoman).isTrue();
    }

    @Test
    public void givenALastNameFeminineEnding5_whenCheckingIfWoman_thenReturnTrue()
            throws Exception {
        //given
        String lastName = "Paulauskienė";
        //when
        boolean isWoman = service.isWoman(lastName);
        //then
        assertThat(isWoman).isTrue();
    }

    //
    //Getting lastName Root
    //

    @Test
    public void givenLastNameEndingWithTwoChars_whenGettingRoot_thenReturnAppendedLastName()
            throws Exception {
        //given
        String lastName = "Paulauskas";
        //when
        String appendedLastName = service.getLastNameRoot(lastName);
        //then
        assertThat(appendedLastName).contains("Paulausk");
    }

    @Test
    public void givenLastNameEndingWithThreeChars_whenGettingRoot_thenReturnAppendedLastName()
            throws Exception {
        //given
        String lastName = "Genytė";
        //when
        String appendedLastName = service.getLastNameRoot(lastName);
        //then
        assertThat(appendedLastName).contains("Gen");
    }

    @Test
    public void givenLastNameEndingWithFourChars_whenGettingRoot_thenReturnAppendedLastName()
            throws Exception {
        //given
        String lastName = "Paulauskaitė";
        //when
        String appendedLastName = service.getLastNameRoot(lastName);
        //then
        assertThat(appendedLastName).contains("Paulausk");
    }

    @Test
    public void givenTwoPersonsWithSameLastNameRoot_whenCheckingIfSameLastName_thenReturnTrue()
            throws Exception {
        //given
        Person person1 = father;
        Person person2 = daughter;
        //when
        boolean isRelatives = service.hasSameLastName(person1, person2);
        //then
        assertThat(isRelatives).isTrue();
    }

    //
    //Getting All Relatives
    //
    @Test
    public void givenAPerson_whenGettingAllRelatives_thenReturnList()
        throws Exception {
        //given
        Person person = father;
        //when
        List<Person> relatives = service.getAllRelatives(person);
        //then
        assertThat(relatives).contains(mother, son, daughter, grandfather, grandmother);
    }

    //
    //Getting Relative Type
    //

    @Test
    public void givenSisterAndBrother_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = daughter;
        Person person2 = son;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("brolis");
    }

    @Test
    public void givenBrotherAndBrother_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = son;
        Person person2 = son;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("brolis");
    }

    @Test
    public void givenBrotherAndSister_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = son;
        Person person2 = daughter;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("sesuo");
    }

    @Test
    public void givenSisterAndSister_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = daughter;
        Person person2 = daughter;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("sesuo");
    }

    @Test
    public void givenHusbandAndWife_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = father;
        Person person2 = mother;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("žmona");
    }

    @Test
    public void givenWifeAndHusband_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = mother;
        Person person2 = father;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("vyras");
    }

    @Test
    public void givenSonAndFather_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = son;
        Person person2 = father;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("tėvas");
    }

    @Test
    public void givenDaughterAndFather_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = daughter;
        Person person2 = father;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("tėvas");
    }

    @Test
    public void givenSonAndMother_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = son;
        Person person2 = mother;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("motina");
    }

    @Test
    public void givenDaughterAndMother_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = daughter;
        Person person2 = mother;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("motina");
    }

    @Test
    public void givenFatherAndSon_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = father;
        Person person2 = son;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("sūnus");
    }

    @Test
    public void givenMotherAndSon_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = mother;
        Person person2 = son;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("sūnus");
    }

    @Test
    public void givenFatherAndDaughter_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = father;
        Person person2 = daughter;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("dukra");
    }

    @Test
    public void givenMotherAndDaughter_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = mother;
        Person person2 = daughter;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("dukra");
    }

    @Test
    public void givenSonAndGrandfather_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = son;
        Person person2 = grandfather;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("senelis");
    }

    @Test
    public void givenDaughterAndGrandfather_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = daughter;
        Person person2 = grandfather;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("senelis");
    }

    @Test
    public void givenSonAndGrandmother_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = son;
        Person person2 = grandmother;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("senelė");
    }

    @Test
    public void givenDaughterAndGrandmother_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = daughter;
        Person person2 = grandmother;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("senelė");
    }

    @Test
    public void givenGrandfatherAndSon_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = grandfather;
        Person person2 = son;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("anūkas");
    }

    @Test
    public void givenGrandmotherAndSon_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = grandmother;
        Person person2 = son;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("anūkas");
    }

    @Test
    public void givenGrandfatherAndDaughter_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = grandfather;
        Person person2 = daughter;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("anūkė");
    }

    @Test
    public void givenGrandmotherAndDaughter_whenGettingRelativeKind_thenReturnKindInString()
            throws Exception {
        //given
        Person person1 = grandmother;
        Person person2 = daughter;
        //when
        String relativeType = service.getRelativeType(person1, person2);
        //then
        assertThat(relativeType).contains("anūkė");
    }

    //Getting a list of relatives of one person

    @Test
    public void givenOnePerson_whenGettingHisRelatives_thenReturnListOfPersons()
        throws Exception {
        //given
        Person person = father;
        //when
        List<Person> allRelatives = service.getAllRelatives(person);
        //then
        assertThat(allRelatives).contains(mother, son, daughter, grandfather, grandmother);
    }

    //Getting a list of relatives with type of one person

    @Test
    public void givenOnePersonAndHisRelatives_whenGettingRelativesWithType_thenSetRelativeTypePropertyAndReturnList()
        throws Exception {
        //given
        Person person = father;
        List<Person> relatives = service.getAllRelatives(father);
        //when
        List<Person> relativesWithType = service.getRelativesWithType(person, relatives);
        //then
        assertThat(relativesWithType).contains(mother, son, daughter, grandfather, grandmother);
        assertThat(mother.getRelativeType()).contains("žmona");
        assertThat(son.getRelativeType()).contains("sūnus");
        assertThat(daughter.getRelativeType()).contains("dukra");
        assertThat(grandfather.getRelativeType()).contains("tėvas");
        assertThat(grandmother.getRelativeType()).contains("motina");

    }






}
