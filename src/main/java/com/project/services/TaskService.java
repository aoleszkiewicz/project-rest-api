package com.project.services;

import com.project.model.TaskEntity;
import com.project.repositories.ITaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService implements ITaskService {
    private ITaskRepository taskRepository;

    @Autowired
    public TaskService(ITaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskEntity save(TaskEntity taskEntity) {
        return taskRepository.save(taskEntity);
    }

    @Override
    public Optional<TaskEntity> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        this.taskRepository.deleteById(id);
    }

    @Override
    public TaskEntity update(Long id, TaskEntity updatedTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setName(updatedTask.getName());
                    task.setDescription(updatedTask.getDescription());
                    task.setPriority(updatedTask.getPriority());
                    task.setCreatedAt(updatedTask.getCreatedAt());
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task with given ID not found:" + id));
    }

    @Override
    public Page<TaskEntity> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Page<TaskEntity> getTasksByProjectId(Long projectId, Pageable pageable) {
        return taskRepository.findProjectTasks(projectId, pageable);
    }
}
