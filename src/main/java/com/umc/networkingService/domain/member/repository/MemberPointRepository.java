package com.umc.networkingService.domain.member.repository;

import com.umc.networkingService.domain.member.entity.MemberPoint;
import com.umc.networkingService.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MemberPointRepository extends JpaRepository<MemberPoint, UUID> {
}
