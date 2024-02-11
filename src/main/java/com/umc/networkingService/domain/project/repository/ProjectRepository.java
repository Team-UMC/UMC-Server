package com.umc.networkingService.domain.project.repository;

import com.umc.networkingService.domain.project.entity.Project;
import com.umc.networkingService.domain.project.entity.ProjectType;
import com.umc.networkingService.global.common.enums.Semester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByName(String projectName);
    Page<Project> findAllBySemester(Semester semester, Pageable pageable);
    Page<Project> findAllByProjectTypeContains(ProjectType projectType, Pageable pageable);
    Page<Project> findAllBySemesterAndProjectTypeContains(Semester semester, ProjectType projectType, Pageable pageable);
    boolean existsByName(String name);
}
