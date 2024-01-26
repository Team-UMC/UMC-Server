package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.dto.request.AlbumCreateRequest;
import com.umc.networkingService.domain.album.dto.request.AlbumUpdateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumIdResponse;
import com.umc.networkingService.domain.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AlbumService {
    AlbumIdResponse createAlbum(Member member, AlbumCreateRequest request, List<MultipartFile> albumImages);

    AlbumIdResponse updateAlbum(Member member, UUID albumId, AlbumUpdateRequest request, List<MultipartFile> albumImages);
}
