package com.umc.networkingService.domain.invite.repository;

import com.umc.networkingService.domain.invite.entity.Invite;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {

    Optional<Invite> findByMemberAndRole(Member member, Role role);
    Optional<Invite> findByCode(String code);
    boolean existsByCode(String code);
}
