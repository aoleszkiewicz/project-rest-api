package com.project.services;

import com.project.model.TaskEntity;
import com.project.repositories.ITaskRepository;

import java.util.Optional;

public class TaskService implements ITaskService {
    private ITaskRepository taskRepository;

    public TaskService(ITaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Optional<TaskEntity> findById(Long id) {
        return taskRepository.findById(id);
    }
}
