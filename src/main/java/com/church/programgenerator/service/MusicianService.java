package com.church.programgenerator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.church.programgenerator.model.Musician;
import com.church.programgenerator.repository.MusicianRepository;

import jakarta.annotation.PostConstruct;

@Service
public class MusicianService {

    private final MusicianRepository repository;

    public MusicianService(MusicianRepository repository) {
        this.repository = repository;
    }

    public List<Musician> getChoristers() {
        return repository.findByMusicianTypeOrderByDisplayOrderAscNameAsc("chorister");
    }

    public List<Musician> getPianists() {
        return repository.findByMusicianTypeOrderByDisplayOrderAscNameAsc("pianist");
    }

    public Musician add(String name, String musicianType) {
        Musician m = new Musician(name.trim(), musicianType);
        return repository.save(m);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Musician update(Long id, String name) {
        Musician m = repository.findById(id).orElseThrow();
        m.setName(name.trim());
        return repository.save(m);
    }

    /** Seed default musicians if table is empty on first start. */
    @PostConstruct
    public void initDefaults() {
        if (repository.count() == 0) {
            repository.save(new Musician("Sis. Kyle Domingo", "chorister"));
            repository.save(new Musician("Bro. Oscar Driz", "pianist"));
        }
    }
}
