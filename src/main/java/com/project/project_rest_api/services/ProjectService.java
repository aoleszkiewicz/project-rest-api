package com.project.project_rest_api.services;

import com.project.project_rest_api.model.ProjectEntity;
import com.project.project_rest_api.model.TaskEntity;
import com.project.project_rest_api.repositories.IProjectRepository;
import com.project.project_rest_api.repositories.ITaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService implements IProjectService {
    private final IProjectRepository projectRepository;
    private final ITaskRepository taskRepository;

    @Autowired
    public ProjectService(IProjectRepository projectRepository, ITaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
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
    public Page<ProjectEntity> findByName(String name, Pageable pageable) {
        return projectRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    @Transactional
    public ProjectEntity save(ProjectEntity projectEntity) {
        return projectRepository.save(projectEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        for (TaskEntity taskEntity : taskRepository.findProjectTasks(id)) {
            taskRepository.delete(taskEntity);
        }
        projectRepository.deleteById(id);
    }
}
