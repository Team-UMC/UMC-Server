package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.dto.request.AlbumCommentCreateRequest;
import com.umc.networkingService.domain.album.dto.request.AlbumCommentUpdateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumCommentIdResponse;
import com.umc.networkingService.domain.member.entity.Member;

import java.util.UUID;

public interface AlbumCommentService {
    AlbumCommentIdResponse createAlbumComment(Member member, AlbumCommentCreateRequest request);
    AlbumCommentIdResponse updateAlbumComment(Member member, UUID commentId, AlbumCommentUpdateRequest request);
    AlbumCommentIdResponse deleteAlbumComment(Member member, UUID commentId);
}
