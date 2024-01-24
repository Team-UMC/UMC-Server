package com.umc.networkingService.domain.member.repository;

import com.umc.networkingService.domain.member.entity.SemesterPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SemesterPartRepository extends JpaRepository<SemesterPart, UUID> {
}
