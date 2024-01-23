package com.umc.networkingService.domain.branch.repository;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BranchUniversityRepository extends JpaRepository<BranchUniversity, UUID> {

    Optional<BranchUniversity> findByUniversityAndIsActive(University university, Boolean isActive);
}
