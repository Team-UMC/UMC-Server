package com.umc.networkingService.domain.album.repository;

import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@RequiredArgsConstructor
public class AlbumRepositoryCustomImpl implements AlbumRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Page<Album> findAllAlbums(Member member, Pageable pageable) {
        
    }
}
