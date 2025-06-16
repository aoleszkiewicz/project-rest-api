package com.project.project_rest_api.model;

import jakarta.validation.constraints.Size;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="task_id")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 50, message = "Name's length must be between {min} and {max} characters")
    @Column(nullable = false, length = 50)
    private String name;

    @Column
    private Integer priority;

    @Column(length = 1000)
    @Size(max = 1000, message = "Description's length cannot have more than {max} characters")
    private String description;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
