package com.umc.networkingService.domain.branch.repository;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.enums.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BranchUniversityRepository extends JpaRepository<BranchUniversity, UUID> {
    Optional<BranchUniversity> findByUniversityAndBranch_Semester(University university, Semester branch_semester);
    Optional<BranchUniversity> findByUniversityAndIsActive(University university, boolean isActive);
}
