package com.project.project_rest_api.services;

import com.project.project_rest_api.model.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IStudentService {
    Optional<StudentEntity> findById(Long id);
    Optional<StudentEntity> findByIndexNumber(String albumNumber);
    Page<StudentEntity> findAll(Pageable pageable);
    Page<StudentEntity> findAllByLastname(String lastname, Pageable pageable);
    StudentEntity save(StudentEntity studentEntity);
    StudentEntity update(Long id, StudentEntity updatedStudent);
    void deleteById(Long studentId);
    void assignProjectToStudent(Long studentId, Long projectId);
}
