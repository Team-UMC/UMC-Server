package com.umc.networkingService.domain.branch.repository;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.global.common.enums.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchUniversityRepository extends JpaRepository<BranchUniversity, UUID> {

    @Query("SELECT bu FROM BranchUniversity bu JOIN FETCH bu.branch WHERE bu.isActive = :isActive")
    List<BranchUniversity> findAllByIsActive(Boolean isActive);
    List<BranchUniversity> findAllByBranch(Branch branch);

    Boolean existsByBranchIdAndUniversityId(UUID branchId, UUID universityId);

    BranchUniversity findByBranchAndUniversity(Branch branch, University university);

    void deleteByBranchIdAndUniversityId(UUID branchId, UUID universityId);

    void deleteByBranch(Branch branch);
    Optional<BranchUniversity> findByUniversityAndBranch_Semester(University university, Semester branch_semester);
    Optional<BranchUniversity> findByUniversityAndIsActive(University university, boolean isActive);
}
