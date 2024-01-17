package com.umc.networkingService.domain.branch.repository;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BranchUniversityRepository extends JpaRepository<BranchUniversity, UUID> {
    List<BranchUniversity> findByBranch(Branch branch);

    Boolean existsByBranchIdAndUniversityId(UUID branchId, UUID universityId);

    List<BranchUniversity> findByUniversity(University university);

}
