package com.project.services;


import com.project.model.StudentEntity;
import com.project.repositories.IStudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class StudentService implements IStudentService {
    private final IStudentRepository studentRepository;

    public StudentService(IStudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Optional<StudentEntity> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Page<StudentEntity> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public StudentEntity save(StudentEntity studentEntity) {
        return studentRepository.save(studentEntity);
    }

    @Transactional
    @Override
    public void update(Long id, StudentEntity studentEntity) {
        Optional<StudentEntity> student = this.findById(id);

        if (student.isEmpty()) {
            return;
        }

        studentRepository.save(studentEntity);
    }

    @Transactional
    @Override
    public void delete(StudentEntity studentEntity) {
        studentRepository.delete(studentEntity);
    }
}

