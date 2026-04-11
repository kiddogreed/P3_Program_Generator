package com.church.programgenerator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.church.programgenerator.model.Auxiliary;
import com.church.programgenerator.service.AuxiliaryService;

@RestController
@RequestMapping("/api/auxiliaries")
public class AuxiliaryController {
    @Autowired
    private AuxiliaryService auxiliaryService;

    @GetMapping
    public List<Auxiliary> getAll() {
        return auxiliaryService.getAll();
    }

    @PostMapping
    public Auxiliary create(@RequestBody Auxiliary auxiliary) {
        return auxiliaryService.save(auxiliary);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        auxiliaryService.delete(id);
    }
}
