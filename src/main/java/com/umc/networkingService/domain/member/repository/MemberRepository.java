package com.umc.networkingService.domain.member.repository;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.university.entity.University;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    @Query(value = "select m from Member m where m.id = :memberId and m.deletedAt is null")
    Optional<Member> findById(@Param("memberId") UUID memberId);
    Optional<Member> findByClientIdAndSocialType(String clientId, SocialType socialType);
    List<Member> findAllByUniversityOrderByContributionPointDesc(University university);
    List<Member> findAllByNicknameAndName(String nickname, String name);
}
