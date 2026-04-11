package com.church.programgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.church.programgenerator.model.WardConfig;

@Repository
public interface WardConfigRepository extends JpaRepository<WardConfig, Long> {
}
