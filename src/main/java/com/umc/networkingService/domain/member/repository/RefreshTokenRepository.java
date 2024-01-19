package com.umc.networkingService.domain.member.repository;

import com.umc.networkingService.domain.member.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String>
{
    Optional<RefreshToken> findByMemberId(UUID memberId);
}
