package com.church.programgenerator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.church.programgenerator.model.Auxiliary;
import com.church.programgenerator.service.AuxiliaryService;

@Controller
@RequestMapping("/auxiliaries")
public class AuxiliaryAdminController {

    private final AuxiliaryService auxiliaryService;

    public AuxiliaryAdminController(AuxiliaryService auxiliaryService) {
        this.auxiliaryService = auxiliaryService;
    }

    @GetMapping
    public String listAuxiliaries(Model model) {
        model.addAttribute("auxiliaries", auxiliaryService.getAll());
        return "auxiliary-admin";
    }

    @PostMapping("/add")
    public String addAuxiliary(@RequestParam String name) {
        if (!auxiliaryService.existsByName(name)) {
            auxiliaryService.save(new Auxiliary(name));
        }
        return "redirect:/manage";
    }

    @PostMapping("/delete/{id}")
    public String deleteAuxiliary(@PathVariable Long id) {
        auxiliaryService.delete(id);
        return "redirect:/manage";
    }
}
