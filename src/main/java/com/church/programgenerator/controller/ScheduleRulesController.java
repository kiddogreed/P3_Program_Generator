package com.church.programgenerator.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.church.programgenerator.model.Conductor;
import com.church.programgenerator.model.WardConfig;
import com.church.programgenerator.service.ConductorService;
import com.church.programgenerator.service.MusicianService;
import com.church.programgenerator.service.WardConfigService;
import com.church.programgenerator.service.WardConfigService.SundayPreview;

@Controller
@RequestMapping("/rules")
public class ScheduleRulesController {

    private final WardConfigService wardConfigService;
    private final ConductorService conductorService;
    private final MusicianService musicianService;

    public ScheduleRulesController(WardConfigService wardConfigService,
                                   ConductorService conductorService,
                                   MusicianService musicianService) {
        this.wardConfigService = wardConfigService;
        this.conductorService = conductorService;
        this.musicianService = musicianService;
    }

    @GetMapping
    public String rulesPage(Model model) {
        WardConfig config = wardConfigService.getConfig();
        List<Conductor> sacramentConductors = conductorService.getByType("sacrament");
        List<Conductor> bishopricConductors = conductorService.getByType("bishopric");

        // Next meeting suggestions
        model.addAttribute("nextSacramentDate", wardConfigService.nextSacramentDate());
        model.addAttribute("nextBishopricDate", wardConfigService.nextBishopricDate());
        model.addAttribute("nextWardCouncilDate", wardConfigService.nextWardCouncilDate());

        // Speaker type for next sacrament
        model.addAttribute("nextSpeakerType",
                wardConfigService.getSpeakerTypeLabel(wardConfigService.nextSacramentDate()));

        // Preview of upcoming 6 Sundays
        List<SundayPreview> upcoming = wardConfigService.getUpcomingSundayPreviews(6);
        model.addAttribute("upcomingSundays", upcoming);

        // Suggested conductors
        Conductor suggestedSacrament = wardConfigService.getSuggestedConductor(
                sacramentConductors, config.getLastSacramentConductorId());
        Conductor suggestedBishopric = wardConfigService.getSuggestedConductor(
                bishopricConductors, config.getLastBishopricConductorId());
        model.addAttribute("suggestedSacramentConductor", suggestedSacrament);
        model.addAttribute("suggestedBishopricConductor", suggestedBishopric);

        model.addAttribute("choristers", musicianService.getChoristers());
        model.addAttribute("pianists", musicianService.getPianists());

        model.addAttribute("wardConfig", config);
        model.addAttribute("pageTitle", "Scheduling Rules");
        return "rules";
    }

    @PostMapping("/save")
    public String saveRules(@ModelAttribute WardConfig wardConfig,
                            RedirectAttributes redirectAttrs) {
        wardConfigService.save(wardConfig);
        redirectAttrs.addFlashAttribute("successMessage", "Scheduling rules saved.");
        return "redirect:/rules";
    }
}
