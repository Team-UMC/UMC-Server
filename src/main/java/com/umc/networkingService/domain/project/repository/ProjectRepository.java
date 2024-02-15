package com.umc.networkingService.domain.project.repository;

import com.umc.networkingService.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    boolean existsByName(String name);
}
