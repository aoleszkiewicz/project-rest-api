package com.project.services;

import com.project.model.TaskEntity;

import java.util.Optional;

public interface ITaskService {
    Optional<TaskEntity> findById(Long id);
}
