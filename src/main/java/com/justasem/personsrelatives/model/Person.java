package com.justasem.personsrelatives.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Size(min=2, max=50)
    @Pattern(regexp = "([A-ZĄČĘĖĮŠŲŪŽ][a-ząčęėįšųūž]+)\\s?([A-ZĄČĘĖĮŠŲŪŽ][a-ząčęėįšųūž]+)?")
    private String firstName;

    @NotEmpty
    @Size(min=2, max=50)
    @Pattern(regexp = "([A-ZĄČĘĖĮŠŲŪŽ][a-ząčęėįšųūž]+)\\-?([A-ZĄČĘĖĮŠŲŪŽ][a-ząčęėįšųūž]+)?")
    private String lastName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Transient
    private String relativeType;

    public String getRelativeType() {
        return relativeType;
    }

    public void setRelativeType(String relativeType) {
        this.relativeType = relativeType;
    }

    public Person() {}
    public Person(Long id, String firstName, String lastName, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

}
