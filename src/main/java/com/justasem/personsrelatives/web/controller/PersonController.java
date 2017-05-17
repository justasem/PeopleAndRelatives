package com.justasem.personsrelatives.web.controller;

import com.justasem.personsrelatives.model.Person;
import com.justasem.personsrelatives.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PersonController {

   @Autowired
    private PersonService personService;

    @SuppressWarnings("unchecked")
    @RequestMapping("/persons")
    public String listPersons (Model model) {
       List<Person> persons = personService.findAll();

       model.addAttribute("persons", persons);
       return "person/index";
   }

   @RequestMapping("/persons/{personId}")
    public String person(@PathVariable int personId, Model model) {
        Person person = null;

        model.addAttribute("person", person);
        return "person/details";

   }

   @RequestMapping("persons/add")
    public String formNewPerson(Model model) {
        if(!model.containsAttribute("person")) {
            model.addAttribute("person", new Person());
        }

        return "person/form";
   }

   @RequestMapping("persons/{personId}/edit")
    public String formEditPerson(@PathVariable int personId, Model model) {
        if(!model.containsAttribute("person")) {
            model.addAttribute("person", personService.findById(personId));
        }
        return "person/form";
   }

   @RequestMapping(value = "/persons/{personId}", method = RequestMethod.POST)
    public String updatePerson(@Valid Person person, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return String.format("redirect:/persons/%s/edit", person.getId());
        }
        personService.save(person);

        return "redirect:/persons";
   }

   @RequestMapping(value = "/persons", method = RequestMethod.POST)
    public String addPerson(@Valid Person person, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "redirect:/persons/add";
        }

        personService.save(person);

        return "redirect:/persons";
   }



}
