package com.umc.networkingService.domain.album.mapper;

import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumDetailResponse;
import com.umc.networkingService.domain.album.dto.response.AlbumInquiryFeaturedResponse;
import com.umc.networkingService.domain.album.dto.response.AlbumInquiryResponse;
import com.umc.networkingService.domain.album.dto.response.AlbumPagingResponse;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.converter.DataConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlbumMapper {

    public Album toAlbum(Member member, AlbumCreateRequest request) {
        return Album.builder()
                .writer(member)
                .semester(request.getSemester())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

    public AlbumInquiryResponse toAlbumInquiryResponse(Album album, String thumbnail, int imageCnt) {
        return AlbumInquiryResponse.builder()
                .albumId(album.getId())
                .writer(DataConverter.convertToWriter(album.getWriter()))
                .title(album.getTitle())
                .semester(album.getSemester())
                .thumbnail(thumbnail)
                .imageCnt(imageCnt)
                .hitCount(album.getHitCount())
                .heartCount(album.getHeartCount())
                .commentCount(album.getCommentCount())
                .createdAt(DataConverter.convertToRelativeTimeFormat(album.getCreatedAt()))
                .build();
    }

    public AlbumInquiryFeaturedResponse toAlbumPageFeaturedResponse(Album album, String thumbnail) {
        return AlbumInquiryFeaturedResponse.builder()
                .albumId(album.getId())
                .title(album.getTitle())
                .thumbnail(thumbnail)
                .createdAt(DataConverter.convertToRelativeTimeFormat(album.getCreatedAt()))
                .build();
    }

    public <T> AlbumPagingResponse<T> toAlbumPagingResponse(
            Page<Album> albums, List<T> albumPageResponses) {

        return AlbumPagingResponse.<T>builder()
                .albumPageResponses(albumPageResponses)
                .page(albums.getNumber())
                .totalPages(albums.getTotalPages())
                .totalElements((int) albums.getTotalElements())
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
