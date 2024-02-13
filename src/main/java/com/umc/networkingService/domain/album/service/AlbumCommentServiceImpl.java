package com.umc.networkingService.domain.album.service;

import com.umc.networkingService.domain.album.dto.request.AlbumCommentCreateRequest;
import com.umc.networkingService.domain.album.dto.request.AlbumCommentUpdateRequest;
import com.umc.networkingService.domain.album.dto.response.AlbumCommentIdResponse;
import com.umc.networkingService.domain.album.entity.Album;
import com.umc.networkingService.domain.album.entity.AlbumComment;
import com.umc.networkingService.domain.album.mapper.AlbumCommentMapper;
import com.umc.networkingService.domain.album.repository.AlbumCommentRepository;
import com.umc.networkingService.domain.album.repository.AlbumRepository;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.global.common.exception.ErrorCode;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.AlbumCommentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumCommentServiceImpl implements AlbumCommentService{

    private final AlbumCommentRepository albumCommentRepository;
    private final AlbumRepository albumRepository;
    private final AlbumCommentMapper albumCommentMapper;
    private final AlbumService aLbumService;

    @Override
    @Transactional
    public AlbumCommentIdResponse createAlbumComment(Member member, AlbumCommentCreateRequest request) {
        Album album = aLbumService.loadEntity(request.getAlbumId());

        AlbumComment comment = albumCommentRepository.save(
                albumCommentMapper.createAlbumComment(member, album, request));

        album.increaseCommentCount();

        return new AlbumCommentIdResponse(comment.getId());
    }

    @Override
    @Transactional
    public AlbumCommentIdResponse updateAlbumComment(Member member, UUID commentId, AlbumCommentUpdateRequest request) {
        AlbumComment comment = loadEntity(commentId);

        if(!comment.getWriter().getId().equals(member.getId()))
            throw new RestApiException(AlbumCommentErrorCode.NO_AUTHORIZATION_ALBUM_COMMENT);

        comment.update(request);

        return new AlbumCommentIdResponse(comment.getId());
    }

    @Override
    @Transactional
    public AlbumCommentIdResponse deleteAlbumComment(Member member, UUID commentId) {
        AlbumComment comment = loadEntity(commentId);

        Album album = comment.getAlbum();

        if(!comment.getWriter().getId().equals(member.getId()))
            throw new RestApiException(AlbumCommentErrorCode.NO_AUTHORIZATION_ALBUM_COMMENT);

        album.decreaseCommentCount();
        comment.delete();

        return new AlbumCommentIdResponse(comment.getId());
    }

    @Override
    public AlbumComment loadEntity(UUID commentId) {
        AlbumComment comment = albumCommentRepository.findById(commentId).orElseThrow(() -> new RestApiException(
                AlbumCommentErrorCode.EMPTY_ALBUM_COMMENT));
        return comment;
    }
}
