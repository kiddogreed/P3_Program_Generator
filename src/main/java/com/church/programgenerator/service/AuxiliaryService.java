package com.church.programgenerator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.church.programgenerator.model.Auxiliary;
import com.church.programgenerator.repository.AuxiliaryRepository;

@Service
public class AuxiliaryService {
    @Autowired
    private AuxiliaryRepository auxiliaryRepository;

    public List<Auxiliary> getAll() {
        return auxiliaryRepository.findAll();
    }

    public Auxiliary save(Auxiliary auxiliary) {
        return auxiliaryRepository.save(auxiliary);
    }

    public void delete(Long id) {
        auxiliaryRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return auxiliaryRepository.existsByName(name);
    }
}
