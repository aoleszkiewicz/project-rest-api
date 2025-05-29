package com.project.services;

import com.project.model.ProjectEntity;
import com.project.model.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProjectService {
    Optional<ProjectEntity> findById(Long id);
    Page<ProjectEntity> findAll(Pageable pageable);
    Page<ProjectEntity> findByName(String name, Pageable pageable);
    ProjectEntity save(ProjectEntity project);
    void delete(Long id);
}
