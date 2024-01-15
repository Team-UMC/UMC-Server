package com.umc.networkingService.domain.member.repository;

import com.umc.networkingService.domain.member.entity.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select m from Member m where m.id = :memberId and m.deletedAt = null")
    Optional<Member> findById(@Param("memberId") UUID memberId);
    Boolean existsByNickname(String nickname);

    Member findByNickname(String nickname);
}
