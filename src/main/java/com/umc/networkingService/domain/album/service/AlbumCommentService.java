package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.dto.request.AlbumCommentCreateRequest;
import com.umc.networkingService.domain.album.dto.request.AlbumCommentUpdateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumCommentResponse;
import com.umc.networkingService.domain.album.entity.AlbumComment;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.base.EntityLoader;

import java.util.UUID;

public interface AlbumCommentService extends EntityLoader<AlbumComment, UUID> {
    AlbumCommentResponse createAlbumComment(Member member, AlbumCommentCreateRequest request);
    AlbumCommentResponse updateAlbumComment(Member member, UUID commentId, AlbumCommentUpdateRequest request);
    AlbumCommentResponse deleteAlbumComment(Member member, UUID commentId);
}
