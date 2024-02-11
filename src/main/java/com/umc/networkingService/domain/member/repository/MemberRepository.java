package com.umc.networkingService.domain.member.repository;

import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SocialType;
import com.umc.networkingService.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByClientIdAndSocialType(String clientId, SocialType socialType);
    List<Member> findAllByUniversityOrderByContributionPointDesc(University university);
    List<Member> findAllByNicknameAndName(String nickname, String name);
    boolean existsByGitNickname(String gitNickname);
}
