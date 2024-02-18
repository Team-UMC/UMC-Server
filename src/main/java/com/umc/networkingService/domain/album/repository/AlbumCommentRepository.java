package com.umc.networkingService.domain.album.repository;

import com.umc.networkingService.domain.album.entity.AlbumComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlbumCommentRepository extends JpaRepository<AlbumComment, UUID> {
    Optional<AlbumComment> findById(UUID albumCommentId);
}
