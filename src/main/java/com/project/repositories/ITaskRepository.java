package com.project.repositories;

import com.project.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITaskRepository extends JpaRepository<TaskEntity, Long> {
    @Query("SELECT t FROM TaskEntity t WHERE t.project.Id = :projectId")
    Page<TaskEntity> findProjectTasks(@Param("projectId") Long projectId, Pageable pageable);

    @Query("SELECT t FROM TaskEntity t WHERE t.project.Id = :projectId")
    List<TaskEntity> findProjectTasks(@Param("projectId") Long projectId);
}
