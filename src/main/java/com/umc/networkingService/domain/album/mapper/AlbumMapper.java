package com.umc.networkingService.domain.album.mapper;

import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumPageResponse;
import com.umc.networkingService.domain.album.dto.response.AlbumPagingResponse;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlbumMapper {

    public Album createAlbum(Member member, AlbumCreateRequest request) {
        return Album.builder()
                .writer(member)
                .semester(request.getSemester())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

    public AlbumPageResponse toAlbumPageResponse(Album album) {
        return AlbumPageResponse.builder()
                .albumId(album.getId())
                .writer(album.getWriter().getNickname() + "/" + album.getWriter().getName())
                .profileImage(album.getWriter().getProfileImage())
                .title(album.getTitle())
                .content(album.getContent())
                .hitCount(album.getHitCount())
                .heartCount(album.getHeartCount())
                .commentCount(album.getCommentCount())
                .createdAt(album.getCreatedAt())
                .build();
    }

    public AlbumPagingResponse toAlbumPagingResponse(Page<Album> albums) {
        List<AlbumPageResponse> AlbumPageResponses = albums.map(this::toAlbumPageResponse)
                .stream().toList();

        return AlbumPagingResponse.builder()
                .albumCommentPageResponses(AlbumPageResponses)
                .page(albums.getNumber())
                .totalPages(albums.getNumber())
                .totalElements(albums.getTotalPages())
                .isFirst(albums.isFirst())
                .isLast(albums.isLast())
                .build();
    }
}
