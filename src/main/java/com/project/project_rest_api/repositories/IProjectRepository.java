package com.project.project_rest_api.repositories;

import com.project.project_rest_api.model.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Page<ProjectEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<ProjectEntity> findByNameContainingIgnoreCase(String name);
}
