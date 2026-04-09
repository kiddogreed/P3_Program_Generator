package com.church.programgenerator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.church.programgenerator.model.Conductor;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Long> {
    List<Conductor> findAllByOrderByDisplayOrderAscNameAsc();
}
