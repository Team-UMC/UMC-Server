package com.umc.networkingService.domain.album.repository;

import com.umc.networkingService.domain.album.entity.AlbumHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlbumHeartRepository extends JpaRepository <AlbumHeart, UUID> {
    Optional<AlbumHeart> findById(UUID albumHeartId);
}
