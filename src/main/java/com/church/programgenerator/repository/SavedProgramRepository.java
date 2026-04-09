package com.church.programgenerator.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.church.programgenerator.model.SavedProgram;

@Repository
public interface SavedProgramRepository extends JpaRepository<SavedProgram, Long> {

    List<SavedProgram> findAllByOrderByCreatedAtDesc();

    List<SavedProgram> findByMeetingTypeOrderByCreatedAtDesc(String meetingType);

    Page<SavedProgram> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<SavedProgram> findByMeetingTypeOrderByCreatedAtDesc(String meetingType, Pageable pageable);
}
