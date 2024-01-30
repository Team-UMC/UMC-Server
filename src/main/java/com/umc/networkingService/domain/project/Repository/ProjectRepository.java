package com.umc.networkingService.domain.project.Repository;

import com.umc.networkingService.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findById(UUID projectId);
    Optional<Project> findByName(String projectName);
}
