package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.dto.request.AlbumCommentCreateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumCommentIdResponse;
import com.umc.networkingService.domain.member.entity.Member;

public interface AlbumCommentService {
    AlbumCommentIdResponse createAlbumComment(Member member, AlbumCommentCreateRequest request);
}
