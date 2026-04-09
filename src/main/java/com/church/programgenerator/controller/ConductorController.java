package com.church.programgenerator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.church.programgenerator.service.ConductorService;

@Controller
@RequestMapping("/conductors")
public class ConductorController {

    private final ConductorService conductorService;

    public ConductorController(ConductorService conductorService) {
        this.conductorService = conductorService;
    }

    @GetMapping
    public String manage(Model model) {
        model.addAttribute("conductors", conductorService.getAll());
        model.addAttribute("pageTitle", "Manage Conductors");
        return "conductor-admin";
    }

    @PostMapping("/add")
    public String add(@RequestParam String name, RedirectAttributes ra) {
        if (name != null && !name.isBlank()) {
            conductorService.add(name.trim());
            ra.addFlashAttribute("successMessage", "Conductor added successfully.");
        } else {
            ra.addFlashAttribute("errorMessage", "Name cannot be empty.");
        }
        return "redirect:/conductors";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        conductorService.delete(id);
        ra.addFlashAttribute("successMessage", "Conductor deleted.");
        return "redirect:/conductors";
    }
}
