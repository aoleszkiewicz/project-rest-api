package com.project.services;

import com.project.model.ProjectEntity;
import com.project.model.StudentEntity;
import com.project.repositories.IProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class ProjectService implements IProjectService {
    private final IProjectRepository projectRepository;

    public ProjectService(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Optional<ProjectEntity> findById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public Page<ProjectEntity> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    public Page<ProjectEntity> findAllByName(String name, Pageable pageable) {
        return projectRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    @Transactional
    public ProjectEntity save(ProjectEntity projectEntity) {
        return projectRepository.save(projectEntity);
    }

    @Transactional
    @Override
    public void update(Long id, ProjectEntity projectEntity) {
        Optional<ProjectEntity> project = this.findById(id);

        if (project.isEmpty()) {
            return;
        }

        projectRepository.save(projectEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<ProjectEntity> projectEntity = this.findById(id);

        if (projectEntity.isEmpty()) {
            return;
        }

        projectRepository.delete(projectEntity.get());
    }
}
