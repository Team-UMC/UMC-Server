package com.umc.networkingService.domain.album.mapper;

import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.dto.response.*;
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

    public AlbumInquiryResponse toAlbumInquiryResponse(Album album, WriterInfo writer, String thumbnail, int imageCnt) {
        return AlbumInquiryResponse.builder()
                .albumId(album.getId())
                .writer(writer)
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

    public AlbumDetailResponse toAlbumDetailResponse(
            Album album, List<String> albumImages, WriterInfo writer, boolean isLiked, boolean isMine) {
        return AlbumDetailResponse.builder()
                .writer(writer)
                .title(album.getTitle())
                .content(album.getContent())
                .hitCount(album.getHitCount())
                .heartCount(album.getHeartCount())
                .commentCount(album.getCommentCount())
                .albumImages(albumImages)
                .isLiked(isLiked)
                .isMine(isMine)
                .createdAt(DataConverter.convertToRelativeTimeFormat(album.getCreatedAt()))
                .build();
    }

    public WriterInfo toWriterInfo(Member member, String position) {
        return WriterInfo.builder()
                .writer(DataConverter.convertToWriter(member))
                .profileImage(member.getProfileImage())
                .position(position)
                .build();
    }
}
