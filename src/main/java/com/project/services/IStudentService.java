package com.project.services;

import com.project.model.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IStudentService {
    Optional<StudentEntity> findById(Long id);
    Page<StudentEntity> findAll(Pageable pageable);
    StudentEntity save(StudentEntity studentEntity);
    void update(Long id, StudentEntity studentEntity);
    void delete(StudentEntity studentEntity);
}
