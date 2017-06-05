package com.justasem.personsrelatives.service;

import com.justasem.personsrelatives.model.Person;
import com.justasem.personsrelatives.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class PersonService {

    private static final int FIFTEEN = 15;
    private static final int FORTY = 40;

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
        return "nežinomas giminystės ryšys";
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

        if (lastName.endsWith("aitė") || lastName.endsWith("ienė")){
            lastNameRoot = lastName.substring(lastName.lastIndexOf("-") + 1, lastName.length() - 4);
        } else if (lastName.endsWith("ytė") || lastName.endsWith("ūtė") || lastName.endsWith("utė")) {
            lastNameRoot = lastName.substring(lastName.lastIndexOf("-") + 1, lastName.length() - 3);
        } else {
            lastNameRoot = lastName.substring(lastName.lastIndexOf("-") + 1, lastName.length() - 2);
        }
        return lastNameRoot;
    }

    public boolean isMan(String lastName) {

        return lastName.endsWith("is") || lastName.endsWith("as") ||
                lastName.endsWith("us") || lastName.endsWith("ys");
    }

    public boolean isWoman(String lastName) {

       return lastName.endsWith("ytė") || lastName.endsWith("ūtė") ||
               lastName.endsWith("aitė") || lastName.endsWith("utė") ||
               lastName.endsWith("ienė");
    }

    private String getRelativeTypeIfHighAgeDifference(Person person, Person relative) {
        if(relativeIsOlder(person, relative)){
            if(isMan(relative.getLastName())) {
                return "senelis";
            }
            if(isWoman(relative.getLastName())){
                return "senelė";
            }
        }
        else {
            if(isMan(relative.getLastName())) {
                return "anūkas";
            }
            if (isWoman(relative.getLastName())) {
                return "anūkė";
            }
        }
        return "nežinomas giminystės ryšys";
    }

    private String getRelativeTypeIfMediumAgeDifference(Person person, Person relative) {
        if(relativeIsOlder(person, relative)){
            if(isMan(relative.getLastName())) {
                return "tėvas";
            }
            if(isWoman(relative.getLastName())){
                return "motina";
            }
        }
        else {
            if(isMan(relative.getLastName())) {
                return "sūnus";
            }
            if (isWoman(relative.getLastName())) {
                return "dukra";
            }
        }
        return "nežinomas giminystės ryšys";
    }

    private String getRelativeTypeIfLowAgeDifference(Person person, Person relative) {
        if(isMan(relative.getLastName())) {
            if(isMarriedWoman(person)){
                return "vyras";
            }else {
                return "brolis";
            }
        }
        if(isWoman(relative.getLastName())) {
            if (isMarriedWoman(relative)) {
                return "žmona";
            } else {
                return "sesuo";
            }
        }
        return "nežinomas giminystės ryšys";
    }

    private boolean relativeIsOlder(Person person, Person relative) {
        return relative.getBirthDate().isBefore(person.getBirthDate());
    }

    private boolean isMarriedWoman(Person person) {
        return person.getLastName().endsWith("ienė");
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
