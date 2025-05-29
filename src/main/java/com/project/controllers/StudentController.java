package com.project.controllers;

import com.project.model.StudentEntity;
import com.project.services.IStudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final IStudentService studentService;

    @Autowired
    public StudentController(IStudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping()
    public Page<StudentEntity> getStudents(Pageable pageable) {
        return studentService.findAll(pageable);
    }

    @GetMapping()
    public Page<StudentEntity> getStudentsByName(
            @RequestParam String name,
            Pageable pageable
    ) {
        return studentService.findAllByLastname(name, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentEntity> getStudentById(@PathVariable("id") Long id) {
        return ResponseEntity.of(studentService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<StudentEntity> saveStudent(
            @Valid @RequestBody StudentEntity studentEntity
    ) {
        StudentEntity student = studentService.save(studentEntity);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(student.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable("id") Long id, @Valid @RequestBody StudentEntity student) {
        return studentService.findById(id)
                .map(std -> {
                    studentService.save(student);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        Optional<StudentEntity> studentOptional = studentService.findById(id);
        if (studentOptional.isPresent()) {
            studentService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}
