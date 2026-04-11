package com.church.programgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.church.programgenerator.model.Auxiliary;

public interface AuxiliaryRepository extends JpaRepository<Auxiliary, Long> {
    boolean existsByName(String name);
}
