package com.church.programgenerator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.church.programgenerator.model.Conductor;
import com.church.programgenerator.repository.ConductorRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ConductorService {

    private final ConductorRepository repository;

    public ConductorService(ConductorRepository repository) {
        this.repository = repository;
    }

    public List<Conductor> getAll() {
        return repository.findAllByOrderByDisplayOrderAscNameAsc();
    }

    public Conductor add(String name) {
        Conductor c = new Conductor(name.trim());
        return repository.save(c);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    /** Seed default conductors on first start if table is empty. */
    @PostConstruct
    public void initDefaults() {
        if (repository.count() == 0) {
            List.of(
                "Bishop Sherwin Tan",
                "(1st Co) Bro. John Moroni Mendoza",
                "(2nd Co) Bro. Joenice Gaco"
            ).forEach(name -> {
                Conductor c = new Conductor(name);
                repository.save(c);
            });
        }
    }
}
