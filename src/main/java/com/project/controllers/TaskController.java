package com.project.controllers;

import com.project.model.TaskEntity;
import com.project.services.ITaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final ITaskService taskService;

    @Autowired
    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable Long id) {
        return ResponseEntity.of(taskService.findById(id));
    }

    @GetMapping
    public Page<TaskEntity> getTasks(Pageable pageable) {
        return taskService.getAllTasks(pageable);
    }

    @GetMapping(params = "id")
    public Page<TaskEntity> getTasksByProjectId(@RequestParam Long id, Pageable pageable) {
        return taskService.getTasksByProjectId(id, pageable);
    }

    @PostMapping
    public ResponseEntity<Void> createTask(@Valid @RequestBody TaskEntity task) {
        TaskEntity newTask = taskService.save(task);
        URI location = URI.create(String.format("/api/tasks/%d", newTask.getId()));
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable Long id, @Valid @RequestBody TaskEntity task) {
        try {
            taskService.update(id, task);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
