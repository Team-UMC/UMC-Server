package com.umc.networkingService.domain.project.repository;

import com.umc.networkingService.domain.project.entity.Project;
import com.umc.networkingService.domain.project.entity.ProjectType;
import com.umc.networkingService.global.common.enums.Semester;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByName(String projectName);
    Page<Project> findAllBySemester(Semester semester, Pageable pageable);
    Page<Project> findAllByProjectTypeContains(ProjectType projectType, Pageable pageable);
    Page<Project> findAllBySemesterAndProjectTypeContains(Semester semester, ProjectType projectType, Pageable pageable);
    @Query("SELECT DISTINCT p FROM Project p JOIN p.tags t WHERE p.name LIKE %:keyword% OR t LIKE %:keyword%")
    Page<Project> findByNameContainsOrTagContains(@Param("keyword") String keyword, Pageable pageable);
    boolean existsByName(String name);
}
