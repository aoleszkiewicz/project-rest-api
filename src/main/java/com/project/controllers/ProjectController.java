package com.project.controllers;

import com.project.model.ProjectEntity;
import com.project.services.IProjectService;
import jakarta.validation.Valid;
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
    private final IProjectService projectService;

    @Autowired
    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping()
    public Page<ProjectEntity> getProjects(Pageable pageable) {
        return projectService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProjectById(@PathVariable("id") Long id) {
        return ResponseEntity.of(projectService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<ProjectEntity> saveProject(
            @Valid @RequestBody ProjectEntity projectEntity
    ) {
        ProjectEntity project = projectService.save(projectEntity);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(project.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProject(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProjectEntity projectEntity
    ) {
        projectService.update(id, projectEntity);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
