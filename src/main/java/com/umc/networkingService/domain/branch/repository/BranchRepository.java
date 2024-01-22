package com.umc.networkingService.domain.branch.repository;

import com.umc.networkingService.domain.branch.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {
}
