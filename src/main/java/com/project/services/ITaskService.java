package com.project.services;

import com.project.model.TaskEntity;
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
