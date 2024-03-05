package com.umc.networkingService.domain.album.mapper;

import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumDetailResponse;
import com.umc.networkingService.domain.album.dto.response.AlbumPageResponse;
import com.umc.networkingService.domain.album.dto.response.AlbumPagingResponse;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.service.AlbumImageService;
import com.umc.networkingService.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlbumMapper {

    private final AlbumImageService albumImageService;
    public Album toAlbumEntity(Member member, AlbumCreateRequest request) {
        return Album.builder()
                .writer(member)
                .semester(request.getSemester())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

    public AlbumPageResponse toAlbumPageResponse(Album album, int imageCnt) {
        return AlbumPageResponse.builder()
                .albumId(album.getId())
                .writer(album.getWriter().getNickname() + "/" + album.getWriter().getName())
                .profileImage(album.getWriter().getProfileImage())
                .title(album.getTitle())
                .semester(album.getSemester())
                .content(album.getContent())
                .thumbnail(albumImageService.findThumbnailImage(album))
                .imageCnt(imageCnt)
                .hitCount(album.getHitCount())
                .heartCount(album.getHeartCount())
                .commentCount(album.getCommentCount())
                .createdAt(album.getCreatedAt())
                .build();
    }

    public AlbumPagingResponse toAlbumPagingResponse(Page<Album> albums, List<AlbumPageResponse> albumPageResponses) {

        return AlbumPagingResponse.builder()
                .albumPageResponses(albumPageResponses)
                .page(albums.getNumber())
                .totalPages(albums.getNumber())
                .totalElements((int)albums.getTotalElements())
                .isFirst(albums.isFirst())
                .isLast(albums.isLast())
                .build();
    }

    public AlbumDetailResponse toAlbumDetailResponse(Album album, List<String> albumImages, boolean isLiked) {
        return AlbumDetailResponse.builder()
                .writer(album.getWriter().getNickname() + "/" + album.getWriter().getName())
                .profileImage(album.getWriter().getProfileImage())
                .semester(album.getSemester())
                .title(album.getTitle())
                .content(album.getContent())
                .hitCount(album.getHitCount())
                .heartCount(album.getHeartCount())
                .commentCount(album.getCommentCount())
                .albumImages(albumImages)
                .isLiked(isLiked)
                .createdAt(album.getCreatedAt())
                .build();
    }
}
