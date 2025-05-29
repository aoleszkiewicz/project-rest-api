package com.project.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.project.model.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByAlbumNumber(String albumNumber);

    Page<StudentEntity> findByAlbumNumberStartsWith(String albumNumber, Pageable pageable);

    Page<StudentEntity> findByLastNameStartsWithIgnoreCase(String lastname, Pageable pageable);
}
