package com.project.controllers;

import com.project.model.ProjectEntity;
import com.project.services.IProjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final IProjectService projectService;

    @Autowired
    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping()
    public Page<ProjectEntity> getProjects(Pageable pageable) {
        return projectService.findAll(pageable);
    }

    @GetMapping(params = "name")
    public Page<ProjectEntity> getProjectsByName(@RequestParam(name = "name") String name, Pageable pageable) {
        return projectService.findByName(name, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProjectById(@PathVariable("id") Long id) {
        return ResponseEntity.of(projectService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<ProjectEntity> saveProject(
            @Valid @RequestBody ProjectEntity projectEntity
    ) {
        logger.info("[ProjectController] Received request to create project: {}", projectEntity);

        try {
            ProjectEntity project = projectService.save(projectEntity);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(project.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            logger.error("Failed to create project: {}. Error {}", projectEntity, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProject(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProjectEntity projectEntity
    ) {
        return projectService.findById(id).map(project -> {
            projectService.save(projectEntity);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        return projectService.findById(id).map(project -> {
            projectService.delete(id);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
