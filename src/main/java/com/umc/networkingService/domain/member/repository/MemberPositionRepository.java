package com.umc.networkingService.domain.member.repository;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.MemberPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberPositionRepository extends JpaRepository<MemberPosition, UUID> {
    Optional<MemberPosition> findByIdAndDeletedAtIsNull(UUID id);
    List<MemberPosition> findAllByMember(Member member);
}
