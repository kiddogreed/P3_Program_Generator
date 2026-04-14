package com.church.programgenerator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.church.programgenerator.model.Musician;

@Repository
public interface MusicianRepository extends JpaRepository<Musician, Long> {
    List<Musician> findByMusicianTypeOrderByDisplayOrderAscNameAsc(String musicianType);
}
