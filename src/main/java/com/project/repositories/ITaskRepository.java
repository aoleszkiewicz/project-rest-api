package com.project.repositories;

import com.project.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskEntity, Long> { }
