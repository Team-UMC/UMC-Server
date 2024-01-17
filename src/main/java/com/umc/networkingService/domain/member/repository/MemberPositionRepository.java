package com.umc.networkingService.domain.member.repository;

import com.umc.networkingService.domain.member.entity.MemberPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberPositionRepository extends JpaRepository<MemberPosition, UUID> {
}
