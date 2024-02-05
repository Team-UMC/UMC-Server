package com.umc.networkingService.domain.branch.repository;

import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.global.common.enums.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID>{
    //기수 별로 지부 조회
    List<Branch> findAllBySemester(Semester semester);

    boolean existsByName(String name);
}
