package com.church.programgenerator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.church.programgenerator.service.MusicianService;

@Controller
public class MusicianController {

    private final MusicianService musicianService;

    public MusicianController(MusicianService musicianService) {
        this.musicianService = musicianService;
    }

    @PostMapping("/musicians/add")
    public String add(@RequestParam String name,
                      @RequestParam String musicianType,
                      RedirectAttributes ra) {
        if (name != null && !name.isBlank()) {
            musicianService.add(name.trim(), musicianType);
            ra.addFlashAttribute("successMessage",
                    ("chorister".equals(musicianType) ? "Chorister" : "Pianist") + " added.");
        } else {
            ra.addFlashAttribute("errorMessage", "Name cannot be empty.");
        }
        return "redirect:/manage";
    }

    @PostMapping("/musicians/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        musicianService.delete(id);
        ra.addFlashAttribute("successMessage", "Musician deleted.");
        return "redirect:/manage";
    }

    @PostMapping("/musicians/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam String name,
                       RedirectAttributes ra) {
        if (name != null && !name.isBlank()) {
            musicianService.update(id, name.trim());
            ra.addFlashAttribute("successMessage", "Musician updated.");
        } else {
            ra.addFlashAttribute("errorMessage", "Name cannot be empty.");
        }
        return "redirect:/manage";
    }
}
