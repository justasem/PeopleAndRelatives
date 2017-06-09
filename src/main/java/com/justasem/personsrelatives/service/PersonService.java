package com.justasem.personsrelatives.service;

import com.justasem.personsrelatives.model.Person;
import com.justasem.personsrelatives.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static com.justasem.personsrelatives.service.Relative.*;

@Service
public class PersonService {

    private static final int FIFTEEN = 15;
    private static final int FORTY = 40;
    private static final String UNKNOWNTYPE = "nežinomas giminystės ryšys";
    private static final String AITĖ = "aitė";
    private static final String IENĖ = "ienė";
    private static final String YTĖ = "ytė";
    private static final String ŪTĖ = "ūtė";
    private static final String UTĖ = "utė";
    private static final String IS = "is";
    private static final String AS = "as";
    private static final String US = "us";
    private static final String YS = "ys";

    private PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {

        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons() {
        return (List<Person>)personRepository.findAll();
    }

    public Person findById(Long id) {
        Person person = personRepository.findOne(id);
        if(person == null) {
            throw new PersonNotFoundException();
        }
        return person;
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public List<Person> savePersons(List<Person> persons) {
        return (List<Person>)personRepository.save(persons);
    }


    public String getRelativeType(Person person, Person relative) {
        int years = getYearsBetweenBirthDates(person.getBirthDate(), relative.getBirthDate());

        if (hasSameLastName(person, relative)) {
            if(years >= 0 && years <= FIFTEEN) {
                return getRelativeTypeIfLowAgeDifference(person, relative);
            }
            if(years > FIFTEEN && years <= FORTY) {
                return getRelativeTypeIfMediumAgeDifference(person, relative);
            }
            if(years > FORTY) {
                return getRelativeTypeIfHighAgeDifference(person, relative);
            }
        }
        return UNKNOWNTYPE;
    }

    public int getYearsBetweenBirthDates(LocalDate birthDate1, LocalDate birthDate2) {

        Optional.ofNullable(birthDate1).orElseThrow(IllegalArgumentException::new);
        Optional.ofNullable(birthDate2).orElseThrow(IllegalArgumentException::new);

        Period period = Period.between(birthDate1, birthDate2);

        return Math.abs(period.getYears());
    }

    public boolean hasSameLastName(Person personOne, Person personTwo) {

        return getLastNameRoot(personOne.getLastName()).equals(getLastNameRoot(personTwo.getLastName()));
    }

    public boolean isSamePerson(Person personOne, Person personTwo) {
        return personOne.getFirstName().equals(personTwo.getFirstName()) &&
                personOne.getLastName().equals(personTwo.getLastName()) &&
                personOne.getBirthDate().equals(personTwo.getBirthDate());
    }

    public String getLastNameRoot(String lastName) {

        String lastNameRoot;

        if (lastName.endsWith(AITĖ) || lastName.endsWith(IENĖ)){
            lastNameRoot = lastName.substring(lastName.lastIndexOf("-") + 1, lastName.length() - 4);
        } else if (lastName.endsWith(YTĖ) || lastName.endsWith(ŪTĖ) || lastName.endsWith(UTĖ)) {
            lastNameRoot = lastName.substring(lastName.lastIndexOf("-") + 1, lastName.length() - 3);
        } else {
            lastNameRoot = lastName.substring(lastName.lastIndexOf("-") + 1, lastName.length() - 2);
        }
        return lastNameRoot;
    }

    public boolean isMan(String lastName) {

        return lastName.endsWith(IS) || lastName.endsWith(AS) ||
                lastName.endsWith(US) || lastName.endsWith(YS);
    }

    public boolean isWoman(String lastName) {

       return lastName.endsWith(YTĖ) || lastName.endsWith(ŪTĖ) ||
               lastName.endsWith(AITĖ) || lastName.endsWith(UTĖ) ||
               lastName.endsWith(IENĖ);
    }

    private String getRelativeTypeIfHighAgeDifference(Person person, Person relative) {
        if(relativeIsOlder(person, relative)){
            if(isMan(relative.getLastName())) {
                return GRANDFATHER.getRelativeType();
            }
            if(isWoman(relative.getLastName())){
                return GRANDMOTHER.getRelativeType();
            }
        }
        else {
            if(isMan(relative.getLastName())) {
                return GRANDSON.getRelativeType();
            }
            if (isWoman(relative.getLastName())) {
                return GRANDDAUGHTER.getRelativeType();
            }
        }
        return UNKNOWNTYPE;
    }

    private String getRelativeTypeIfMediumAgeDifference(Person person, Person relative) {
        if(relativeIsOlder(person, relative)){
            if(isMan(relative.getLastName())) {
                return FATHER.getRelativeType();
            }
            if(isWoman(relative.getLastName())){
                return MOTHER.getRelativeType();
            }
        }
        else {
            if(isMan(relative.getLastName())) {
                return SON.getRelativeType();
            }
            if (isWoman(relative.getLastName())) {
                return DAUGHTER.getRelativeType();
            }
        }
        return UNKNOWNTYPE;
    }

    private String getRelativeTypeIfLowAgeDifference(Person person, Person relative) {
        if(isMan(relative.getLastName())) {
            if(isMarriedWoman(person)){
                return HUSBAND.getRelativeType();
            }else {
                return BROTHER.getRelativeType();
            }
        }
        if(isWoman(relative.getLastName())) {
            if (isMarriedWoman(relative)) {
                return WIFE.getRelativeType();
            } else {
                return SISTER.getRelativeType();
            }
        }
        return UNKNOWNTYPE;
    }

    private boolean relativeIsOlder(Person person, Person relative) {
        return relative.getBirthDate().isBefore(person.getBirthDate());
    }

    private boolean isMarriedWoman(Person person) {
        return person.getLastName().endsWith(IENĖ);
    }

    public List<Person> getAllRelatives(Person person) {
        List<Person> relatives = new ArrayList<>();

        getAllPersons().forEach(person1 -> {
               if(hasSameLastName(person, person1) && !isSamePerson(person, person1)) {
                    relatives.add(person1);
               }
        });
        return relatives;
    }

    public List<Person> getRelativesWithType(Person person, List<Person> relatives) {
        List<Person> relativesWithType = new ArrayList<>();

        for (Person relative:relatives) {
            relative.setRelativeType(getRelativeType(person, relative));
            relativesWithType.add(relative);
        }
        return relativesWithType;
    }
}
