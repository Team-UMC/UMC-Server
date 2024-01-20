package com.umc.networkingService.domain.member.repository;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.MemberPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberPointRepository extends JpaRepository<MemberPoint, UUID> {
    Page<MemberPoint> findAllByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);
}
