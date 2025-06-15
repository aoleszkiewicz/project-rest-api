package com.project.project_rest_api.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.project.project_rest_api.model.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByIndexNumber(String indexNumber);

    Page<StudentEntity> findByIndexNumberStartsWith(String indexNumber, Pageable pageable);

    Page<StudentEntity> findByLastNameStartsWithIgnoreCase(String lastname, Pageable pageable);
}
