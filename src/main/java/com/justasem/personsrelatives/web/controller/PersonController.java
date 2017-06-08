package com.justasem.personsrelatives.web.controller;

import com.justasem.personsrelatives.model.Person;
import com.justasem.personsrelatives.service.PersonNotFoundException;
import com.justasem.personsrelatives.service.PersonSearchSortPageService;
import com.justasem.personsrelatives.service.PersonService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    @Inject
    private PersonSearchSortPageService searchService;

    @RequestMapping("/")
    public String listPersons(Model model) {
        List<Person> persons = personService.getAllPersons();
        model.addAttribute("persons", persons);
        return "index";
    }

    @RequestMapping("/{sortProperty}/pages/{pageNumber}")
    public String sortPersons(@PathVariable String sortProperty, @PathVariable Integer pageNumber, Model model) {
        Page<Person> page = searchService.getPersonPageSortedBy(pageNumber, sortProperty);
        List<Person> persons = page.getContent();

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 2);
        int end = Math.min(begin + 2, page.getTotalPages());

        model.addAttribute("persons", persons);
        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "index";
    }

    //TODO Sort relatives
    @RequestMapping("person/{id}")
    public String personDetail(@PathVariable("id") Long id, Model model) throws PersonNotFoundException {
        Person person = personService.findById(id);
        List<Person> relatives = personService.getAllRelatives(person);
        List<Person> relativesWithType = personService.getRelativesWithType(person, relatives);
        model.addAttribute("person", person);
        model.addAttribute("relativesWithType", relativesWithType);
        return "detail";
    }

    @RequestMapping("person/add")
    public String addPerson(Model model) {
        model.addAttribute("person", new Person());
        return "form";
    }

    @RequestMapping("person/edit/{id}")
    public String editPerson(@PathVariable("id") Long id, Model model) throws PersonNotFoundException {
        model.addAttribute("person", personService.findById(id));
        return "form";
    }

    @RequestMapping(value = "person", method = RequestMethod.POST)
    public String savePerson(@ModelAttribute("person") @Valid Person person, BindingResult result) {
        if(result.hasErrors()) {
            return "form";
        } else {
            personService.savePerson(person);
        }
        return "redirect:/";
    }


}
