package com.church.programgenerator.controller;

import com.church.programgenerator.model.BishopricProgram;
import com.church.programgenerator.model.SacramentProgram;
import com.church.programgenerator.model.WardCouncilProgram;
import com.church.programgenerator.service.ProgramStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/history")
public class HistoryController {

    private final ProgramStorageService storageService;

    public HistoryController(ProgramStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public String listHistory(
            @RequestParam(required = false) String type,
            Model model) {
        if (type != null && !type.isBlank()) {
            model.addAttribute("programs", storageService.getProgramsByType(type.toUpperCase()));
            model.addAttribute("activeFilter", type.toUpperCase());
        } else {
            model.addAttribute("programs", storageService.getAllPrograms());
            model.addAttribute("activeFilter", "ALL");
        }
        model.addAttribute("pageTitle", "Saved Programs");
        return "history";
    }

    @GetMapping("/load/sacrament/{id}")
    public String loadSacrament(@PathVariable Long id, Model model) {
        SacramentProgram program = storageService.loadSacramentProgram(id);
        model.addAttribute("pageTitle", "Sacrament Meeting Program");
        model.addAttribute("sacramentProgram", program);
        model.addAttribute("successMessage", "Program loaded from history.");
        return "sacrament";
    }

    @GetMapping("/load/bishopric/{id}")
    public String loadBishopric(@PathVariable Long id, Model model) {
        BishopricProgram program = storageService.loadBishopricProgram(id);
        model.addAttribute("pageTitle", "Bishopric Meeting");
        model.addAttribute("bishopricProgram", program);
        model.addAttribute("successMessage", "Program loaded from history.");
        return "bishopric";
    }

    @GetMapping("/load/ward-council/{id}")
    public String loadWardCouncil(@PathVariable Long id, Model model) {
        WardCouncilProgram program = storageService.loadWardCouncilProgram(id);
        model.addAttribute("pageTitle", "Ward Council Meeting");
        model.addAttribute("wardCouncilProgram", program);
        model.addAttribute("successMessage", "Program loaded from history.");
        return "ward-council";
    }

    @PostMapping("/delete/{id}")
    public String deleteProgram(
            @PathVariable Long id,
            RedirectAttributes redirectAttrs) {
        storageService.deleteProgram(id);
        redirectAttrs.addFlashAttribute("successMessage", "Program deleted successfully.");
        return "redirect:/history";
    }
}
