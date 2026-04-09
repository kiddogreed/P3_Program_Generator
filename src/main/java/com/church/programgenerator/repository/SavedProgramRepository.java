package com.church.programgenerator.repository;

import com.church.programgenerator.model.SavedProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedProgramRepository extends JpaRepository<SavedProgram, Long> {

    List<SavedProgram> findAllByOrderByCreatedAtDesc();

    List<SavedProgram> findByMeetingTypeOrderByCreatedAtDesc(String meetingType);
}
