package com.umc.networkingService.domain.album.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.QAlbum;
import com.umc.networkingService.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumRepositoryCustomImpl implements AlbumRepositoryCustom{

    private final JPAQueryFactory query;
    QAlbum album = QAlbum.album;

    @Override
    public Page<Album> findAllAlbums(Member member, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();

        List<Album> albums = query.selectFrom(album).where(predicate)
                .orderBy(album.isFixed.desc(), album.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(albums, pageable, query.selectFrom(album).where(predicate).fetch().size());
    }
}