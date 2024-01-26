package com.umc.networkingService.domain.album.repository;

import com.umc.networkingService.domain.album.entity.AlbumImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlbumImageRepository extends JpaRepository<AlbumImage, UUID> {
    Optional<AlbumImage> findById(UUID albumImageId);
}
