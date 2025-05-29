package com.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "student", indexes = {
        @Index(name = "lastname_index", columnList = "lastname", unique = false),
        @Index(name = "index_of_index_number", columnList = "index_number", unique = true)
})
@Getter
@Setter
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_id")
    private Long Id;

    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "First name length must be between {min} and {max} characters")
    @Column(name = "firstname", nullable = false, length = 50)
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 3, max = 100, message = "Last name length must be between {min} and {max} characters")
    @Column(name = "lastname", nullable = false, length = 100)
    private String lastName;

    @NotNull(message = "Index number cannot be null")
    @Size(min = 1, max = 20, message = "Index number length must be between {min} and {max} characters")
    @Column(name = "index_number", unique = true, nullable = false, length = 20)
    private String indexNumber;

    @NotNull(message = "Email cannot be null")
    @Size(min = 3, max = 50, message = "Email's length must be between {min} and {max} characters")
    @Email(message = "Invalid email address")
    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @NotNull(message = "Is onsite property cannot be null")
    @Column(name = "is_onsite", nullable = false)
    private Boolean isOnsite;

    @ManyToMany(mappedBy = "students")
    private List<ProjectEntity> projects;

    public StudentEntity() {}

    public StudentEntity(String firstName, String lastName, String indexNumber, Boolean isOnsite) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.isOnsite = isOnsite;
    }

    public StudentEntity(String firstName, String lastName, String email, String indexNumber, Boolean isOnsite) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.indexNumber = indexNumber;
        this.isOnsite = isOnsite;
    }
}
