package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "project")
@Getter
@Setter
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private Long Id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 50, message = "Name's length must be between {min} and {max} characters")
    @Column(nullable = false, length = 50)
    private String name;

    @Size(max = 1000, message = "Description's length cannot have more than {max} characters")
    @Column(length = 1000)
    private String description;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({"project"})
    private List<TaskEntity> tasks;

    @ManyToMany
    @JoinTable(
        joinColumns = {
            @JoinColumn(name = "project_id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "student_id")
        }
    )
    private List<StudentEntity> students;
}
