package com.project.project_rest_api.services;

import com.project.project_rest_api.model.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ITaskService {
    TaskEntity save(TaskEntity taskEntity);

    Optional<TaskEntity> findById(Long id);

    void deleteById(Long id);

    TaskEntity update(Long id, TaskEntity updatedTask);

    Page<TaskEntity> getAllTasks(Pageable pageable);

    Page<TaskEntity> getTasksByProjectId(Long projectId, Pageable pageable);
}
