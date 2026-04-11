package com.church.programgenerator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.church.programgenerator.service.AuxiliaryService;
import com.church.programgenerator.service.ConductorService;

@Controller

public class ConductorController {
    private final ConductorService conductorService;
    private final AuxiliaryService auxiliaryService;

    public ConductorController(ConductorService conductorService, AuxiliaryService auxiliaryService) {
        this.conductorService = conductorService;
        this.auxiliaryService = auxiliaryService;
    }

    @GetMapping({"/conductors", "/manage"})
    public String manage(Model model) {
        model.addAttribute("sacramentConductors", conductorService.getByType("sacrament"));
        model.addAttribute("bishopricConductors", conductorService.getByType("bishopric"));
        model.addAttribute("auxiliaries", auxiliaryService.getAll());
        return "conductor-admin";
    }

    @PostMapping("/conductors/add")
    public String add(@RequestParam String name, @RequestParam(defaultValue = "sacrament") String type,
                      RedirectAttributes ra) {
        if (name != null && !name.isBlank()) {
            conductorService.add(name.trim(), type);
            ra.addFlashAttribute("successMessage", "Conductor added.");
        } else {
            ra.addFlashAttribute("errorMessage", "Name cannot be empty.");
        }
        return "redirect:/manage";
    }

    @PostMapping("/conductors/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        conductorService.delete(id);
        ra.addFlashAttribute("successMessage", "Conductor deleted.");
        return "redirect:/manage";
    }

    @PostMapping("/conductors/edit/{id}")
    public String edit(@PathVariable Long id, @RequestParam String name, RedirectAttributes ra) {
        if (name != null && !name.isBlank()) {
            conductorService.update(id, name.trim());
            ra.addFlashAttribute("successMessage", "Conductor updated.");
        } else {
            ra.addFlashAttribute("errorMessage", "Name cannot be empty.");
        }
        return "redirect:/manage";
    }
}
