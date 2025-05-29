package com.project.services;


import com.project.model.ProjectEntity;
import com.project.model.StudentEntity;
import com.project.repositories.IProjectRepository;
import com.project.repositories.IStudentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService implements IStudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final IStudentRepository studentRepository;
    private final IProjectRepository projectRepository;

    @Autowired
    public StudentService(IStudentRepository studentRepository, IProjectRepository projectRepository) {
        this.studentRepository = studentRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Optional<StudentEntity> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Optional<StudentEntity> findByAlbumNumber(String albumNumber) {
        return studentRepository.findByAlbumNumber(albumNumber);
    }

    @Override
    public Page<StudentEntity> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public Page<StudentEntity> findAllByLastname(String lastname, Pageable pageable) {
        return studentRepository.findByLastNameStartsWithIgnoreCase(lastname, pageable);
    }

    @Transactional
    @Override
    public StudentEntity save(StudentEntity studentEntity) {
        logger.info("[StudentService] Saving student method called..");
        return studentRepository.save(studentEntity);
    }

    @Override
    public StudentEntity update(Long id, StudentEntity updatedStudent) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setFirstName(updatedStudent.getFirstName());
                    student.setLastName(updatedStudent.getLastName());
                    student.setIndexNumber(updatedStudent.getIndexNumber());
                    student.setEmail(updatedStudent.getEmail());
                    student.setIsOnsite(updatedStudent.getIsOnsite());
                    return studentRepository.save(student);
                })
                .orElseThrow(() -> new RuntimeException("Student not found with given ID: " + id));
    }

    @Transactional
    @Override
    public void deleteById(Long studentId) {
        studentRepository.deleteById(studentId);
    }

    @Override
    public void assignProjectToStudent(Long studentId, Long projectId) {
        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with given ID: " + studentId));
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with given ID:  " + projectId));

        student.getProjects().add(project);
        studentRepository.save(student);
    }
}

