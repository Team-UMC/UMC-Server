package com.umc.networkingService.domain.album.repository;

import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumHeart;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlbumHeartRepository extends JpaRepository <AlbumHeart, UUID> {
    Optional<AlbumHeart> findByMemberAndAlbum(Member member, Album album);
}
