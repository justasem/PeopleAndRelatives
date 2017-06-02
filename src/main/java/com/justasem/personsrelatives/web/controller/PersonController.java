package com.justasem.personsrelatives.web.controller;

import com.justasem.personsrelatives.model.Person;
import com.justasem.personsrelatives.service.PersonNotFoundException;
import com.justasem.personsrelatives.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("person")
public class PersonController {

    @Inject
    private PersonService personService;

    @RequestMapping("/")
    public String listPersons(Model model) {
        List<Person> persons = personService.getAllPersons();
        model.addAttribute("persons", persons);
        return "index";
    }

    //TODO Sort relatives
    @RequestMapping("person/{id}")
    public String personDetail(@PathVariable("id") Long id, Model model) throws PersonNotFoundException {
        Person person = personService.findById(id);
        List<Person> relatives = personService.getAllRelatives(person);
        Map<Person, String> relativesMapped = personService.getRelativesMappedWithType(person, relatives);
        model.addAttribute("person", person);
        model.addAttribute("relativesMapped", relativesMapped);
        return "detail";
    }

    @RequestMapping("person/add")
    public String addPerson(Model model) {
        model.addAttribute("person", new Person());
        return "form";
    }

    @RequestMapping("person/edit/{id}")
    public String editPerson(@PathVariable("id") Long id, Model model) {
        model.addAttribute("person", personService.findById(id));
        return "form";
    }

    @RequestMapping(value = "person", method = RequestMethod.POST)
    public String savePerson(@ModelAttribute("person") @Valid Person person) {
        personService.savePerson(person);
        return "redirect:/";
    }
}
