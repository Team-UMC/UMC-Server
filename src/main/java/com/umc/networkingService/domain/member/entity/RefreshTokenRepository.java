package com.umc.networkingService.domain.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String>
{
    Optional<RefreshToken> findByAccessToken(String accessToken);
}
