package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.dto.request.AlbumUpdateRequest;
import com.umc.networkingService.domain.album.dto.response.*;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.EntityLoader;
import com.umc.networkingService.global.common.enums.Semester;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AlbumService extends EntityLoader<Album, UUID> {
    AlbumIdResponse createAlbum(Member member, AlbumCreateRequest request, List<MultipartFile> albumImages);
    AlbumIdResponse updateAlbum(Member member, UUID albumId, AlbumUpdateRequest request, List<MultipartFile> albumImages);
    AlbumIdResponse deleteAlbum(Member member, UUID albumId);
    AlbumPagingResponse<AlbumInquiryResponse> inquiryAlbums(Member member, Semester semester, Pageable pageable);
    AlbumPagingResponse<AlbumInquiryResponse> searchAlbums(Member member, String keyword, Pageable pageable);
    AlbumPagingResponse<AlbumInquiryFeaturedResponse> inquiryAlbumsFeatured(Member member, Pageable pageable);
    AlbumDetailResponse inquiryAlbumDetail(Member member, UUID albumId);
    AlbumHeartResponse toggleAlbumLike(Member member, UUID albumId);
}