package com.umc.networkingService.domain.branch.repository;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.entity.BranchUniversity;
import com.umc.networkingService.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchUniversityRepository extends JpaRepository<BranchUniversity, Long> {
    List<BranchUniversity> findByBranch(Branch branch);
}
