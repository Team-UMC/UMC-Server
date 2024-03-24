package com.umc.networkingService.domain.project.repository;

import com.umc.networkingService.domain.project.entity.Project;
import com.umc.networkingService.domain.project.entity.ProjectType;
import com.umc.networkingService.global.common.enums.Semester;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Page<Project> findAllBySemester(Semester semester, Pageable pageable);
    Page<Project> findAllByTypesContains(ProjectType type, Pageable pageable);
    Page<Project> findAllBySemesterAndTypesContains(Semester semester, ProjectType type, Pageable pageable);
    @Query("SELECT DISTINCT p FROM Project p JOIN p.tags t WHERE p.name LIKE %:keyword% OR t LIKE %:keyword%")
    Page<Project> findByNameContainsOrTagContains(@Param("keyword") String keyword, Pageable pageable);
    @Query("SELECT p FROM Project p ORDER BY (p.hitCount * 1 + p.heartCount * 3) DESC")
    Page<Project> findHotProjects(Pageable pageable);
    boolean existsByName(String name);
}
