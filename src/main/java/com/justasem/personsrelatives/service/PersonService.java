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
        String relativeType = "";

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
        return relativeType;
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
            }else {
                return "senelė";
            }
        }else {
            if(isMan(relative.getLastName())) {
                return "anūkas";
            }else {
                return "anūkė";
            }
        }
    }

    private String getRelativeTypeIfMediumAgeDifference(Person person, Person relative) {
        if(relativeIsOlder(person, relative)){
            if(isMan(relative.getLastName())) {
                return "tėvas";
            }else {
                return "motina";
            }
        }else {
            if(isMan(relative.getLastName())) {
                return "sūnus";
            }else {
                return "dukra";
            }
        }
    }

    private String getRelativeTypeIfLowAgeDifference(Person person, Person relative) {
        if(isMan(relative.getLastName())) {
            if(isMarriedWoman(person)){
                return "vyras";
            }else {
                return "brolis";
            }
        }else {
            if(isMarriedWoman(relative)){
                return "žmona";
            }else {
                return "sesuo";
            }
        }
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

    public Map<Person, String> getRelativesMappedWithType(Person person, List<Person> relatives) {
        Map<Person, String> relativesMapped = new TreeMap<>((person1, person2) -> {
            Period period = Period.between(person2.getBirthDate(), person1.getBirthDate());
            return period.getYears();
        });
        for (Person relative:relatives) {
            relativesMapped.put(relative, getRelativeType(person, relative));
        }
        return relativesMapped;
    }

}
