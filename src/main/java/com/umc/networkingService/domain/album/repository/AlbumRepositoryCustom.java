package com.umc.networkingService.domain.album.repository;

import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.enums.Semester;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface AlbumRepositoryCustom {
    Page<Album> findAllAlbums(Member member, Pageable pageable);
}