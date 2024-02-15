package com.umc.networkingService.domain.project.repository;

import com.umc.networkingService.domain.project.entity.ProjectHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectHeartRepository extends JpaRepository<ProjectHeart, UUID> {
    boolean existsByMemberIdAndProjectId(UUID memberId, UUID projectId);
    void deleteByMemberIdAndProjectId(UUID memberId, UUID projectId);
    int countByProjectId(UUID projectId);
}
